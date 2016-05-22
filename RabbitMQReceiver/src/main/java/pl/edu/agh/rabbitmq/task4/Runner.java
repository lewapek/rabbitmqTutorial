package pl.edu.agh.rabbitmq.task4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.rabbitmq.util.ExchangeType;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class Runner {
    private static final String EXCHANGE_NAME = "task4Exchange";
    private static final String QUEUE_NAME_PREFIX = "task4Queue.";
    private static final int QOS_PREFETCH_COUNT = 1;

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) throws IOException, TimeoutException {
        if (args.length != 1) {
            System.err.println("Wrong number of arguments. Provide routing keys.");
            System.exit(1);
        }
        final String keysAsString = args[0];
        final String queueName = QUEUE_NAME_PREFIX + keysAsString;

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareDurableExchangeIn(channel);
        declareDurableQueueIn(channel, queueName);

        Set<String> routingKeys = getRoutingKeysFrom(keysAsString);
        for (String routingKey : routingKeys) {
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        }

        channel.basicQos(QOS_PREFETCH_COUNT);

        System.out.println("Before receiving");
        Receiver receiver = Receiver.with(channel, queueName);
        receiver.startReceiving();
    }

    private static void declareDurableExchangeIn(Channel channel) throws IOException {
        final Boolean durable = true;

        //noinspection ConstantConditions
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT, durable);
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

    private static Set<String> getRoutingKeysFrom(String string) {
        final Set<String> result = new HashSet<>();

        final char[] characters = string.toCharArray();
        for (char character : characters) {
            switch (character) {
                case 'r':
                    result.add(RoutingKey.RED);
                    break;
                case 'g':
                    result.add(RoutingKey.GREEN);
                    break;
                case 'b':
                    result.add(RoutingKey.BLUE);
                    break;
                default: // do nothing
            }
        }

        return result;
    }
}
