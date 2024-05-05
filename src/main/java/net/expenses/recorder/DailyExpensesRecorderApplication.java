package net.expenses.recorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Kazi Tanvir Azad
 */
@SpringBootApplication
public class DailyExpensesRecorderApplication {
    public static void main(String[] args) {
        // JVM argument takes precedence over the environment variable TZ
        // Setting the JVM argument -Duser.timezone="UTC" to change the time zone in the
        // JVM to UTC for non-prod instance
        SpringApplication.run(DailyExpensesRecorderApplication.class, args);
    }
}
