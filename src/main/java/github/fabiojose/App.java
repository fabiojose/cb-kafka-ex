package github.fabiojose;

import javax.annotation.PostConstruct;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

import github.fabiojose.kafka.CircuitBreaker;

/**
 * @author fabiojose
 */
@SpringBootApplication
public class App implements ApplicationRunner {

    @Autowired
    KafkaListenerEndpointRegistry registry;

    @Autowired
    CircuitBreaker cb;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
    /**
     * To changet the {@link HystrixEventNotifier}
     */
    @PostConstruct
    public void init() {
        System.out.println(" $$$$$$ >>> " + registry);

        HystrixPlugins hystrix = 
            HystrixPlugins.getInstance();
            
        HystrixCommandExecutionHook ceh =
            hystrix.getCommandExecutionHook();

        HystrixConcurrencyStrategy cs = 
            hystrix.getConcurrencyStrategy();

        HystrixMetricsPublisher mp = 
            hystrix.getMetricsPublisher();

        HystrixPropertiesStrategy ps = 
            hystrix.getPropertiesStrategy();

        // reset Hystrix
        HystrixPlugins.reset();

        hystrix.registerCommandExecutionHook(ceh);
        hystrix.registerConcurrencyStrategy(cs);
        hystrix.registerMetricsPublisher(mp);
        hystrix.registerPropertiesStrategy(ps);

        hystrix.registerEventNotifier(cb);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // TODO Auto-generated method stub

    }
}
