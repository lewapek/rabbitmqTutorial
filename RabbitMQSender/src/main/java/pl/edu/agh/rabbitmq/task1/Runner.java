package pl.edu.agh.rabbitmq.task1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Runner {
    private static final String QUEUE_NAME = "task1Queue";
    private static final int MESSAGES_QUANTITY = 100;

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareQueueIn(channel);

        System.out.printf("Before publishing %d messages\n", MESSAGES_QUANTITY);

        Sender producer = Sender.with(channel, QUEUE_NAME);
        producer.publishMessages(MESSAGES_QUANTITY);

        System.out.println("Messages published");

        channel.close();
        connection.close();
    }

    private static void declareQueueIn(Channel channel) throws IOException {
        final Boolean durable = false;
        final Boolean exclusive = false;
        final Boolean autoDelete = false;
        final Map<String, Object> arguments = null;

        //noinspection ConstantConditions
        channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, arguments);
    }
}
