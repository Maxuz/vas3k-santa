package dev.maxuz.vas3ksanta.bot;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class BotUtils {

    @Nullable
    public String getFromId(Update update) {
        if (update.hasCallbackQuery()) {
            return getFromId(update.getCallbackQuery());
        }
        if (update.hasMessage()) {
            return getFromId(update.getMessage());
        }
        return null;
    }

    @Nullable
    public String getFromId(CallbackQuery callbackQuery) {
        var from = callbackQuery.getFrom();
        return from == null ? null : from.getId().toString();
    }

    @Nullable
    public String getFromId(Message message) {
        var from = message.getFrom();
        return from == null ? null : from.getId().toString();
    }
}
