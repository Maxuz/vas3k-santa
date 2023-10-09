package dev.maxuz.vas3ksanta.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotUpdateHandler {
    boolean canHandle(Update update);

    void handle(Update update);
}
