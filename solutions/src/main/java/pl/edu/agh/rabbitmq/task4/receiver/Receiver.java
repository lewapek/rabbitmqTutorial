package pl.edu.agh.rabbitmq.task4.receiver;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import pl.edu.agh.rabbitmq.util.Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class Receiver {
    private static final int SLEEP_TIME_BASE_SECONDS = 2;
    private static final int SLEEP_TIME_MAX_ADDITIONAL_SECONDS = 10;

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

        Boolean autoAck = false;
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
                simulateComputationUsing(channel, envelope);
            }
        };
    }

    @SuppressWarnings("Duplicates")
    private void simulateComputationUsing(Channel channel, Envelope envelope) throws IOException {
        try {
            Utils.sleepForSeconds(SLEEP_TIME_BASE_SECONDS, SLEEP_TIME_MAX_ADDITIONAL_SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Problem with sleeping: " + e.getMessage());
        } finally {
            final Boolean multiple = false;
            //noinspection ConstantConditions, ThrowFromFinallyBlock
            channel.basicAck(envelope.getDeliveryTag(), multiple);
        }
    }
}
