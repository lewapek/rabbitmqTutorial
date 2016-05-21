package pl.edu.agh.rabbitmq.tutorial2;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author lewap
 * @since 21.05.16.
 */
class Receiver {
    private static final Random RANDOM = new Random();
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
                try {
                    sleepForSeconds(SLEEP_TIME_BASE_SECONDS);
                } catch (InterruptedException e) {
                    System.err.println("Problem with sleeping: " + e.getMessage());
                }
            }
        };
    }

    private void sleepForSeconds(int seconds) throws InterruptedException {
        int additionalSeconds = RANDOM.nextInt(SLEEP_TIME_MAX_ADDITIONAL_SECONDS);
        int totalSeconds = seconds + additionalSeconds;

        System.out.printf("  Sleeping for %d seconds\n", totalSeconds);
        for (int secondsLeft = totalSeconds; secondsLeft >= 0; --secondsLeft) {
            System.out.printf("\r    seconds left: %02d", secondsLeft);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
