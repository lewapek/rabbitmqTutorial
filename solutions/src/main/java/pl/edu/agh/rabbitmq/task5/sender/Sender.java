package pl.edu.agh.rabbitmq.task5.sender;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;

class Sender {
    private static final AMQP.BasicProperties properties = MessageProperties.PERSISTENT_TEXT_PLAIN;
    private final Channel channel;
    private final String exchangeName;
    private final String routingKey;

    private Sender(Channel channel, String exchangeName, String routingKey) {
        this.channel = channel;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    static Sender with(Channel channel, String exchangeName, String routingKey) {
        return new Sender(channel, exchangeName, routingKey);
    }

    void publishMessages(int quantity) {
        try {
            for (int i = 1; i <= quantity; ++i) {
                String currentMessage = "message [" + routingKey + "]" + i;
                channel.basicPublish(exchangeName, routingKey, properties, currentMessage.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
