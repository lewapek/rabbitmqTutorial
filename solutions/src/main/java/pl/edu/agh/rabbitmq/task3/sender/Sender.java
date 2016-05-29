package pl.edu.agh.rabbitmq.task3.sender;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;

class Sender {
    private static final String UNNECESSARY_ROUTING_KEY = "";
    private final Channel channel;
    private final String exchangeName;

    private Sender(Channel channel, String exchangeName) {
        this.channel = channel;
        this.exchangeName = exchangeName;
    }

    static Sender with(Channel channel, String exchangeName) {
        return new Sender(channel, exchangeName);
    }

    void publishMessages(int quantity) throws IOException {
        final AMQP.BasicProperties properties = MessageProperties.PERSISTENT_TEXT_PLAIN;

        for (int i = 1; i <= quantity; ++i) {
            String currentMessage = "message " + i;
            channel.basicPublish(exchangeName, UNNECESSARY_ROUTING_KEY, properties, currentMessage.getBytes());
        }
    }
}
