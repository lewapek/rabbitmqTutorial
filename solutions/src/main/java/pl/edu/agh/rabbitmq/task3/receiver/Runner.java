package pl.edu.agh.rabbitmq.task3.receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.rabbitmq.util.ConfigurationProperties;
import pl.edu.agh.rabbitmq.util.ExchangeType;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Runner {
    private static final String UNNECESSARY_ROUTING_KEY = "";
    private static final String EXCHANGE_NAME = "task3Exchange";
    private static final int QOS_PREFETCH_COUNT = 1;

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void main(String[] args) throws IOException, TimeoutException {
        ConfigurationProperties configurationProperties = new ConfigurationProperties();
        connectionFactory.setHost(configurationProperties.getRabbitHost());
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        declareDurableFanoutExchangeIn(channel);
        final String tempUniqueQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(tempUniqueQueueName, EXCHANGE_NAME, UNNECESSARY_ROUTING_KEY);

        channel.basicQos(QOS_PREFETCH_COUNT);

        System.out.println("Before receiving");
        Receiver receiver = Receiver.with(channel, tempUniqueQueueName);
        receiver.startReceiving();
    }

    private static void declareDurableFanoutExchangeIn(Channel channel) throws IOException {
        final Boolean durable = true;

        //noinspection ConstantConditions
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.FANOUT, durable);
    }
}
