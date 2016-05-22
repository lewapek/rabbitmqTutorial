package pl.edu.agh.rabbitmq.task4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.rabbitmq.util.ExchangeType;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class Runner {
    private static final String EXCHANGE_NAME = "task4Exchange";
    private static final int QOS_PREFETCH_COUNT = 1;

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) throws IOException, TimeoutException {
        if (args.length != 1) {
            System.err.println("Wrong number of arguments. Provide routing keys.");
            System.exit(1);
        }
        final String keysAsString = args[0];

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareDurableExchangeIn(channel);
        final String tempUniqueQueueName = channel.queueDeclare().getQueue();

        Set<String> routingKeys = getRoutingKeysFrom(keysAsString);
        for (String routingKey : routingKeys) {
            channel.queueBind(tempUniqueQueueName, EXCHANGE_NAME, routingKey);
        }

        channel.basicQos(QOS_PREFETCH_COUNT);

        System.out.println("Before receiving");
        Receiver receiver = Receiver.with(channel, tempUniqueQueueName);
        receiver.startReceiving();
    }

    private static void declareDurableExchangeIn(Channel channel) throws IOException {
        final Boolean durable = true;

        //noinspection ConstantConditions
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT, durable);
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
