package github.fabiojose.kafka.consumer1;

import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import github.fabiojose.http.SomeReq;
import github.fabiojose.http.SomeRestEndpointClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fabiojose
 */
@Component
@Slf4j
public class ToRestEndpoint {

    @Autowired
    SomeRestEndpointClient some;

    @KafkaListener(
        id = "to-rest",
        groupId = "rest", // group.id
        topics = "topico"
    )
    public void consume(ConsumerRecord<String, String> r, Acknowledgment ack) {

        SomeReq req = new SomeReq();
        req.setId(UUID.randomUUID());
        req.setName(r.value());

        // Post to Some API
        some.post(req);

        log.info(" ** TO REST ** enviado para o endpoint offset={} ",
            r.offset());

        // Commmit manual, que também será síncrono
        ack.acknowledge();

    }
}