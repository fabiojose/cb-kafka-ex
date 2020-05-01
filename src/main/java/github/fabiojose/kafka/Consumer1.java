package github.fabiojose.kafka;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import github.fabiojose.http.SomeReq;
import github.fabiojose.http.SomeRestEndpointClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fabiojose
 */
@Component
@Slf4j
public class SpringKafkaListener {

    @Autowired
    SomeRestEndpointClient some;

    @KafkaListener(
        id = "consumer-1",
        topics = "topico"
    )
    public void consume(@Payload String valor, Acknowledgment ack) {

        log.info("registro processado {} ", valor);

        // TODO Processar valor do registro
        // ...

        SomeReq req = new SomeReq();
        req.setId(UUID.randomUUID());
        req.setName(valor);

        // Post to Some API
        some.post(req);

        // Commmit manual, que também será síncrono
        ack.acknowledge();

    }
}