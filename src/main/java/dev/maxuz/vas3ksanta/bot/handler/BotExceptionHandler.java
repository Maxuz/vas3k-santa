package dev.maxuz.vas3ksanta.bot.handler;

import dev.maxuz.vas3ksanta.bot.BotMessageService;
import dev.maxuz.vas3ksanta.bot.BotUtils;
import dev.maxuz.vas3ksanta.exception.UserException;
import dev.maxuz.vas3ksanta.model.RegStageEntity;
import dev.maxuz.vas3ksanta.service.RegStageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class BotExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(BotExceptionHandler.class);

    private final BotUtils botUtils;
    private final BotMessageService messageService;
    private final RegStageService regStageService;

    public BotExceptionHandler(BotUtils botUtils,
                               BotMessageService messageService,
                               RegStageService regStageService) {
        this.botUtils = botUtils;
        this.messageService = messageService;
        this.regStageService = regStageService;
    }

    public void handle(Update update, UserException e) {
        log.info("User exception", e);
        String fromId = botUtils.getFromId(update);
        if (fromId != null) {
            messageService.sendPlainText(fromId, e.getUserMessage());
            regStageService.updateStageByTelegramId(fromId, RegStageEntity.Stage.ERROR);
        }
    }

    public void handle(Update update, Exception e) {
        log.error("Internal error", e);

        String fromId = botUtils.getFromId(update);
        if (fromId != null) {
            messageService.sendInternalErrorMessage(fromId);
            regStageService.updateStageByTelegramId(fromId, RegStageEntity.Stage.ERROR);
        }
    }
}
