package pl.edu.agh.rabbitmq.task4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.rabbitmq.util.ExchangeType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Runner {
    private static final Random RANDOM = new Random();
    private static final String EXCHANGE_NAME = "task4Exchange";
    private static final int ITERATIONS = 4;

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareDurableDirectExchangeIn(channel);

        Sender senderRed = Sender.with(channel, EXCHANGE_NAME, RoutingKey.RED);
        Sender senderGreen = Sender.with(channel, EXCHANGE_NAME, RoutingKey.GREEN);
        Sender senderBlue = Sender.with(channel, EXCHANGE_NAME, RoutingKey.BLUE);
        List<Sender> senders = Arrays.asList(senderRed, senderGreen, senderBlue);

        System.out.println("Before publishing messages");
        publishAll(senders, ITERATIONS);

        System.out.println("Messages published");

        channel.close();
        connection.close();
    }

    private static void declareDurableDirectExchangeIn(Channel channel) throws IOException {
        final Boolean durable = true;

        //noinspection ConstantConditions
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT, durable);
    }

    private static void publishAll(List<Sender> senders, int iterations) throws IOException {
        int counter = 1;
        for (int i = 0; i < iterations; ++i) {
            for (Sender sender : senders) {
                if (RANDOM.nextBoolean()) {
                    System.out.printf("  publishing %d: %s message\n", counter, sender.getRoutingKey());
                    sender.publishMessage("message " + counter + " ");
                    ++counter;
                }
            }
        }
    }
}
