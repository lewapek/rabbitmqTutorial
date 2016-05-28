package pl.edu.agh.rabbitmq.task1;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;

class Sender {
    private static final String NAMELESS_EXCHANGE = "";
    private final Channel channel;
    private final String queueName;

    private Sender(Channel channel, String queueName) {
        this.channel = channel;
        this.queueName = queueName;
    }

    static Sender with(Channel channel, String queueName) {
        return new Sender(channel, queueName);
    }

    void publishMessages(int quantity) throws IOException {
        final AMQP.BasicProperties properties = null;

        for (int i = 1; i <= quantity; ++i) {
            String currentMessage = "message " + i;
            channel.basicPublish(NAMELESS_EXCHANGE, queueName, properties, currentMessage.getBytes());
        }
    }
}
