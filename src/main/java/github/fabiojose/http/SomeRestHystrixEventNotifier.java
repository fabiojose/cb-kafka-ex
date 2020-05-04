package github.fabiojose.http;

import java.util.concurrent.atomic.AtomicReference;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fabiojose
 */
@Component
@Slf4j
public class SomeRestHystrixEventNotifier extends HystrixEventNotifier {

    private static final String KEY = "SomeRestEndpointClient#post(SomeReq)";
    private static final TaskHolder NO_HOLDER = null;
    
    @Autowired
    AtomicReference<TaskHolder> holder;

    public void markEvent(HystrixEventType type, HystrixCommandKey key) {

        log.debug(" > > > type: {} key: {}", type, key.name());

        if(KEY.equals(key.name())) {
            
            if(HystrixEventType.SUCCESS.equals(type)){

                if(null!= holder.get()) {

                    log.info(" >> >> Kafka Consumer **retomado**");

                    holder.get().cancel();
                    holder.set(NO_HOLDER);

                }
            }
        }

    }

}