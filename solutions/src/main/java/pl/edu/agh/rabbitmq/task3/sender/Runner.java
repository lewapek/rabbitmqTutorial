package pl.edu.agh.rabbitmq.task3.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.rabbitmq.util.ExchangeType;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Runner {
    private static final String EXCHANGE_NAME = "task3Exchange";
    private static final int MESSAGES_QUANTITY = 20;

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareDurableFanoutExchangeIn(channel);

        System.out.printf("Before publishing %d messages\n", MESSAGES_QUANTITY);

        Sender sender = Sender.with(channel, EXCHANGE_NAME);
        sender.publishMessages(MESSAGES_QUANTITY);

        System.out.println("Messages published");

        channel.close();
        connection.close();
    }

    private static void declareDurableFanoutExchangeIn(Channel channel) throws IOException {
        final Boolean durable = true;

        //noinspection ConstantConditions
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.FANOUT, durable);
    }
}
