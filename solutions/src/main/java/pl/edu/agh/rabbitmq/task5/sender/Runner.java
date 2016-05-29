package pl.edu.agh.rabbitmq.task5.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.rabbitmq.util.ExchangeType;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Runner {
    private static final String EXCHANGE_NAME = "task5Exchange";
    private static final Map<String, Integer> messagesWithQuantities = new LinkedHashMap<>();
    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    static {
        messagesWithQuantities.put("red.green.blue", 3);
        messagesWithQuantities.put("red.green.red", 2);
        messagesWithQuantities.put("red.blue.green", 2);
        messagesWithQuantities.put("red", 1);
        messagesWithQuantities.put("green.blue", 1);
        messagesWithQuantities.put("green.blue.blue", 1);
        messagesWithQuantities.put("blue.green", 3);
        messagesWithQuantities.put("blue", 1);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareDurableTopicExchangeIn(channel);

        System.out.println("Before publishing messages");
        messagesWithQuantities.forEach((bindingKey, quantity) -> {
            final Sender sender = Sender.with(channel, EXCHANGE_NAME, bindingKey);
            System.out.printf("  Publishing %d %s messages\n", quantity, bindingKey);
            sender.publishMessages(quantity);
        });
        System.out.println("Messages published");

        channel.close();
        connection.close();
    }

    private static void declareDurableTopicExchangeIn(Channel channel) throws IOException {
        final Boolean durable = true;

        //noinspection ConstantConditions
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.TOPIC, durable);
    }
}
