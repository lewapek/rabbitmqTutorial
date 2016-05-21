package pl.edu.agh.rabbitmq.util;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author lewap
 * @since 22.05.16.
 */
public class Utils {
    public static final Random RANDOM = new Random();

    public static void sleepForSeconds(int seconds, int maxAdditionalSeconds) throws InterruptedException {
        int additionalSeconds = RANDOM.nextInt(maxAdditionalSeconds);
        int totalSeconds = seconds + additionalSeconds;

        System.out.printf("  Sleeping for %d seconds\n", totalSeconds);
        for (int secondsLeft = totalSeconds; secondsLeft >= 0; --secondsLeft) {
            System.out.printf("\r    seconds left: %02d", secondsLeft);
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println();
    }
}
