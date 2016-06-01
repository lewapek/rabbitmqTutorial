package pl.edu.agh.rabbitmq.util;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static final Random RANDOM = new Random();

    public static void sleepForSeconds(int seconds, int maxAdditionalSeconds) throws InterruptedException {
        int additionalSeconds = RANDOM.nextInt(maxAdditionalSeconds);
        int totalSeconds = seconds + additionalSeconds;

        System.out.printf("  Sleeping for %d seconds\n", totalSeconds);
        System.out.print("    seconds left");
        for (int secondsLeft = totalSeconds; secondsLeft >= 0; --secondsLeft) {
            System.out.printf(" -> %02d", secondsLeft);
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println();
    }
}
