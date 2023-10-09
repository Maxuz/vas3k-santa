package dev.maxuz.vas3ksanta.bot;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class BotMessageService {
    private final AbsSender absSender;

    public BotMessageService(@Lazy AbsSender absSender) {
        this.absSender = absSender;
    }

    private void execute(BotApiMethod<?> method) {
        try {
            absSender.execute(method);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Executing send message error", e);
        }
    }

    public void sendPlainText(String chatId, String message) {
        SendMessage sm = SendMessage.builder()
            .chatId(chatId)
            .text(message).build();
        execute(sm);
    }

    public void sendMarkdown(String chatId, String message) {
        SendMessage sm = SendMessage.builder()
            .chatId(chatId)
            .parseMode(ParseMode.MARKDOWNV2)
            .text(message).build();
        execute(sm);
    }

    public void sendInternalErrorMessage(String chatId) {
        sendPlainText(chatId, "Упс, что-то пошло не так ☹️. Попробуйте еще раз, если ошибка повторится, напишите администратору");
    }
}
