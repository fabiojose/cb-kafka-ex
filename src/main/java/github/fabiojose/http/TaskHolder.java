package github.fabiojose.http;

import java.util.concurrent.Future;

import org.springframework.kafka.listener.MessageListenerContainer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Para armazenar os objetos utilizados na tarefa e ela própria.
 * 
 * @author fabiojose
 */
@Getter
@AllArgsConstructor
public class TaskHolder {

    final Future<?> task;
    final MessageListenerContainer container;

    /**
     * Cancela a tarefa a retoma o consumo
     */
    public void cancel() {
        this.task.cancel(Boolean.TRUE);
        this.container.resume();
    }

}