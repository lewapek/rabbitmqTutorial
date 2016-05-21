package pl.edu.agh.rabbitmq.tutorial1;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author lewap
 * @since 21.05.16.
 */
class Receiver {
    private final Channel channel;
    private final String queueName;

    private Receiver(Channel channel, String queueName) {
        this.channel = channel;
        this.queueName = queueName;
    }

    static Receiver with(Channel channel, String queueName) {
        return new Receiver(channel, queueName);
    }

    void startReceiving() throws IOException {
        Consumer consumer = createRabbitConsumer();

        Boolean autoAck = true;
        // noinspection ConstantConditions
        channel.basicConsume(queueName, autoAck, consumer);
    }

    private Consumer createRabbitConsumer() {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String receivedMessage = new String(body, StandardCharsets.UTF_8);
                System.out.printf("Received '%s' with consumer tag '%s'\n", receivedMessage, consumerTag);
            }
        };
    }
}
