package github.fabiojose.http;

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

    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   return builder.build();
    }
    
}