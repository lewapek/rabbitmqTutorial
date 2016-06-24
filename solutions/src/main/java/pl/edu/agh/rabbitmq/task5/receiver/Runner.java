package pl.edu.agh.rabbitmq.task5.receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.rabbitmq.util.ConfigurationProperties;
import pl.edu.agh.rabbitmq.util.ExchangeType;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Runner {
    private static final String EXCHANGE_NAME = "task5Exchange";
    private static final String QUEUE_NAME_PREFIX = "task5Queue.";
    private static final int QOS_PREFETCH_COUNT = 1;

    private static final ArgsParser parser = ArgsParser.with(QUEUE_NAME_PREFIX);
    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) throws IOException, TimeoutException {
        if (args.length < 1) {
            System.err.println("Wrong number of arguments. Provide 1 or more binding keys (separated by space).");
            System.err.println("Example binding key:\n\t\"red.*.blue\"");
            System.exit(1);
        }

        final String queueName = parser.composeQueueNameFrom(args);

        ConfigurationProperties configurationProperties = new ConfigurationProperties();
        connectionFactory.setHost(configurationProperties.getRabbitHost());
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareDurableTopicExchangeIn(channel);
        declareDurableQueueIn(channel, queueName);

        for (String routingKey : args) {
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        }

        channel.basicQos(QOS_PREFETCH_COUNT);

        System.out.println("Before receiving");
        Receiver receiver = Receiver.with(channel, queueName);
        receiver.startReceiving();
    }

    private static void declareDurableTopicExchangeIn(Channel channel) throws IOException {
        final Boolean durable = true;

        //noinspection ConstantConditions
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.TOPIC, durable);
    }

    @SuppressWarnings("Duplicates")
    private static void declareDurableQueueIn(Channel channel, String queueName) throws IOException {
        final Boolean durable = true;
        final Boolean exclusive = false;
        final Boolean autoDelete = true;
        final Map<String, Object> arguments = null;

        //noinspection ConstantConditions
        channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);
    }
}
