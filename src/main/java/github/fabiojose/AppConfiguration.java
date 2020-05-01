package github.fabiojose;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author fabiojose
 */
@Configuration
public class AppConfiguration {

    @Autowired
    BuildProperties buildProperties;

    @PostConstruct
    public void init() {
        System.setProperty("APP_NAME", buildProperties.getName());
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){

        ThreadPoolTaskScheduler threadPoolTaskScheduler
            = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix(
          "ThreadPoolTaskScheduler");

        return threadPoolTaskScheduler;
    }
}
