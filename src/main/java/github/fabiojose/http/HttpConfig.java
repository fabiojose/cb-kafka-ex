package github.fabiojose.http;

import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author fabiojose
 */
@Configuration
@EnableFeignClients
@EnableCircuitBreaker
public class HttpConfig {
    
    @Autowired
    SomeRestHystrixEventNotifier cb;

    /**
     * To changet the {@link HystrixEventNotifier}
     */
    @PostConstruct
    public void init() {
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

        // Custom event notifier
        hystrix.registerEventNotifier(cb);

    }
    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   return builder.build();
    }

    /**
     * Para armazenar a tarefa responsável por atualizar o circuíto.
     * @return
     */
    @Bean
    public static AtomicReference<TaskHolder> taskHolder() {
        return new AtomicReference<>();
    }
}
