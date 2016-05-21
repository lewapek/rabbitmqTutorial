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
    private static final int QOS_PREFETCH_COUNT = 1;

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareDurableQueueIn(channel);
        channel.basicQos(QOS_PREFETCH_COUNT);

        System.out.println("Before receiving");
        Receiver receiver = Receiver.with(channel, QUEUE_NAME);
        receiver.startReceiving();
    }

    private static void declareDurableQueueIn(Channel channel) throws IOException {
        final Boolean durable = true;
        final Boolean exclusive = false;
        final Boolean autoDelete = false;
        final Map<String, Object> arguments = null;

        //noinspection ConstantConditions
        channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, arguments);
    }
}
