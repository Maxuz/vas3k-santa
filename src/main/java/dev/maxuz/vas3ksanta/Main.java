package dev.maxuz.vas3ksanta;

import dev.maxuz.vas3ksanta.bot.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class Main implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private final ApplicationContext applicationContext;

    public Main(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        log.info("Starting telegram bot - elf");
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = applicationContext.getBean(Bot.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Error", e);
        }
    }
}