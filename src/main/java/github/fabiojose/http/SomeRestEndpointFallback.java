package github.fabiojose.http;

import java.time.Duration;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fabiojose
 */
@Component
@Slf4j
public class SomeRestEndpointFallback implements SomeRestEndpointClient {

    @Autowired
    KafkaListenerEndpointRegistry registry;

    @Autowired
    SomeRestEndpointClient some;

    @Autowired
    TaskScheduler scheduler;

    @Autowired
    AtomicReference<TaskHolder> holder;

    @Value("${app.cb.refresh.ms}")
    long refresh;

    @Override
    public void post(@Valid final SomeReq request) {

        // Nenhuma tarefa iniciada?
        if(holder.get() == null){

            log.info("Atualização do circuíto a cada {}ms", refresh);

            MessageListenerContainer container =
                registry.getListenerContainer("to-rest");
            
            // Pausar o Kafka Consumer
            container.pause();

            log.info(" >> >> Kafka Consumer **pausado**");

            // Iniciar tarefa para atualização do circuíto
            Future<?> scheduled = 
                scheduler.scheduleWithFixedDelay(() -> {

                    /*
                    * Envia requisição até passar com sucesso
                    * e fechar novamente o circuíto.
                    */
                    try{
                        some.post(request);
                    }catch(Exception ignored){};

                }, Duration.ofMillis(refresh));
            
            // Guardar future no holder para uso em outro local
            holder.set(new TaskHolder(scheduled, container));

        } else {
            log.info("Já existe uma tarefa para atualização do circuíto");
        }
    }
    
}