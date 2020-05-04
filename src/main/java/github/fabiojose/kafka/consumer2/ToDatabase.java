package github.fabiojose.kafka.consumer2;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fabiojoise
 */
@Component
@Slf4j
public class ToDatabase {
    
    @KafkaListener(
        id = "to-database",
        groupId = "database", // group.id
        topics = "topico"
    )
    public void consume(ConsumerRecord<String, String> r, Acknowledgment ack) {

        // TODO Sua persistência do registro

        log.info(" ** TO DATABASE ** persistência no banco de dados offset={}",
            r.offset());

        // Commmit manual, que também será síncrono
        ack.acknowledge();

    }
}