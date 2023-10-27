package dev.maxuz.vas3ksanta.bot;

import dev.maxuz.vas3ksanta.bot.handler.BotExceptionHandler;
import dev.maxuz.vas3ksanta.bot.handler.BotUpdateHandler;
import dev.maxuz.vas3ksanta.exception.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class Bot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    private final BotProps properties;
    private final List<BotUpdateHandler> handlers;
    private final BotMessageService messageService;
    private final BotExceptionHandler botExceptionHandler;
    private final BotUtils botUtils;

    public Bot(BotProps properties,
               List<BotUpdateHandler> handlers,
               BotMessageService messageService,
               BotExceptionHandler botExceptionHandler,
               BotUtils botUtils) {
        super(properties.token());
        this.properties = properties;
        this.handlers = handlers;
        this.messageService = messageService;
        this.botExceptionHandler = botExceptionHandler;
        this.botUtils = botUtils;
    }

    @Override
    public String getBotUsername() {
        return properties.name();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Incoming request: {}", update);
        String telegramId = botUtils.getFromId(update);
        MDC.put("tid", telegramId);

        try {
            boolean handled = false;
            for (BotUpdateHandler handler : handlers) {
                if (handler.canHandle(update)) {
                    handler.handle(update);
                    handled = true;
                    break;
                }
            }
            if (!handled) {
                messageService.sendPlainText(telegramId, "Неизвестный запрос. Проверьте правильность сообщения и если все хорошо обратитесь к администратору");
            }

        } catch (UserException e) {
            botExceptionHandler.handle(update, e);
        } catch (Exception e) {
            botExceptionHandler.handle(update, e);
        } finally {
            MDC.clear();
        }
    }
}
