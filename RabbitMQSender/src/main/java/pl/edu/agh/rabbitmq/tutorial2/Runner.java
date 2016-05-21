package pl.edu.agh.rabbitmq.tutorial2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author lewap
 * @since 21.05.16.
 */
public class Runner {
    private static final String QUEUE_NAME = "tutorial2Queue";
    private static final int MESSAGES_QUANTITY = 20;

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareDurableQueueIn(channel);

        System.out.printf("Before publishing %d messages\n", MESSAGES_QUANTITY);

        Sender producer = Sender.with(channel, QUEUE_NAME);
        producer.publishMessages(MESSAGES_QUANTITY);

        System.out.println("Messages published");

        channel.close();
        connection.close();
    }

    private static void declareDurableQueueIn(Channel channel) throws IOException {
        Boolean durable = true;
        Boolean exclusive = false;
        Boolean autoDelete = false;
        Map<String, Object> arguments = null;

        // noinspection ConstantConditions
        channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, arguments);
    }
}
