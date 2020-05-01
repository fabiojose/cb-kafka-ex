package github.fabiojose.kafka;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import github.fabiojose.http.SomeReq;
import github.fabiojose.http.SomeRestEndpointClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fabiojose
 */
@Component
@Slf4j
public class CircuitBreaker extends HystrixEventNotifier {
    
    @Autowired
    KafkaListenerEndpointRegistry registry;

    @Autowired
    SomeRestEndpointClient some;

    @Autowired
    TaskScheduler scheduler;

    ScheduledFuture<?> scheduled;

    public void markEvent(HystrixEventType type, HystrixCommandKey key) {

        log.debug(" > > > type: {} key: {}", type, key.name());

        if("SomeRestEndpointClient#post(SomeReq)"
            .equals(key.name())) {
            
            if(HystrixEventType.FAILURE.equals(type) ){

                registry.getListenerContainer("consumer-1")
                    .pause();
                
                log.info("Consumo *pausado*");

                //TODO Iniciar task para manter fluxo no circuito
                if(null== scheduled){
                    scheduled = 
                        scheduler.scheduleWithFixedDelay(() -> {
                            SomeReq req = new SomeReq();
                            try{
                                some.post(req);
                            }catch(Exception e){}
                        },
                        Duration.ofSeconds(10));
                }

            } else if(HystrixEventType.SUCCESS.equals(type)){

                if(null!= scheduled){
                    registry.getListenerContainer("consumer-1")
                        .resume();

                    log.info("Consumo *retomado*");

                    scheduled.cancel(Boolean.TRUE);
                    scheduled = null;
                }
            }
        }

    }

}