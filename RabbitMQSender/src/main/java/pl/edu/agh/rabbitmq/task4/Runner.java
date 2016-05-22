package pl.edu.agh.rabbitmq.task4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.rabbitmq.util.ExchangeType;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Runner {
    private static final String EXCHANGE_NAME = "task4Exchange";
    private static final int RED_MESSAGES_QUANTITY = 3;
    private static final int GREEN_MESSAGES_QUANTITY = 5;
    private static final int BLUE_MESSAGES_QUANTITY = 10;

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareDurableExchangeIn(channel);

        Sender producerRed = Sender.with(channel, EXCHANGE_NAME, RoutingKey.RED);
        Sender producerGreen = Sender.with(channel, EXCHANGE_NAME, RoutingKey.GREEN);
        Sender producerBlue = Sender.with(channel, EXCHANGE_NAME, RoutingKey.BLUE);

        System.out.printf("Before publishing %d messages\n", RED_MESSAGES_QUANTITY);

        producerRed.publishMessages(RED_MESSAGES_QUANTITY);
        producerGreen.publishMessages(GREEN_MESSAGES_QUANTITY);
        producerBlue.publishMessages(BLUE_MESSAGES_QUANTITY);

        System.out.println("Messages published");

        channel.close();
        connection.close();
    }

    private static void declareDurableExchangeIn(Channel channel) throws IOException {
        final Boolean durable = true;

        //noinspection ConstantConditions
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT, durable);
    }
}
