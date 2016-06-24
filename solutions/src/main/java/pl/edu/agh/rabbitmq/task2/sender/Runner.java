package pl.edu.agh.rabbitmq.task2.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.rabbitmq.util.ConfigurationProperties;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Runner {
    private static final String QUEUE_NAME = "task2Queue";
    private static final int MESSAGES_QUANTITY = 20;

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) throws IOException, TimeoutException {
        ConfigurationProperties configurationProperties = new ConfigurationProperties();
        connectionFactory.setHost(configurationProperties.getRabbitHost());
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareDurableQueueIn(channel);

        System.out.printf("Before publishing %d messages\n", MESSAGES_QUANTITY);

        Sender sender = Sender.with(channel, QUEUE_NAME);
        sender.publishMessages(MESSAGES_QUANTITY);

        System.out.println("Messages published");

        channel.close();
        connection.close();
    }

    private static void declareDurableQueueIn(Channel channel) throws IOException {
        final Boolean durable = true;
        final Boolean exclusive = false;
        final Boolean autoDelete = false;
        final Map<String, Object> arguments = null;

        // noinspection ConstantConditions
        channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, arguments);
    }
}
