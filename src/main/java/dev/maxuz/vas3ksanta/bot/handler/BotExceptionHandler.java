package dev.maxuz.vas3ksanta.bot.handler;

import dev.maxuz.vas3ksanta.bot.BotUtils;
import dev.maxuz.vas3ksanta.bot.BotMessageService;
import dev.maxuz.vas3ksanta.db.GrandchildRepository;
import dev.maxuz.vas3ksanta.db.RegStageRepository;
import dev.maxuz.vas3ksanta.model.Grandchild;
import dev.maxuz.vas3ksanta.model.RegStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class BotExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(BotExceptionHandler.class);

    private final BotUtils botUtils;
    private final BotMessageService messageService;
    private final GrandchildRepository grandchildRepository;
    private final RegStageRepository regStageRepository;

    public BotExceptionHandler(BotUtils botUtils,
                               BotMessageService messageService,
                               GrandchildRepository grandchildRepository,
                               RegStageRepository regStageRepository) {
        this.botUtils = botUtils;
        this.messageService = messageService;
        this.grandchildRepository = grandchildRepository;
        this.regStageRepository = regStageRepository;
    }

    public void handle(Update update, Exception e) {
        log.error("Internal error", e);

        String fromId = botUtils.getFromId(update);
        if (fromId != null) {
            messageService.sendInternalErrorMessage(fromId);
            Optional<Grandchild> opGrandchild = grandchildRepository.findByTelegramId(fromId);
            if (opGrandchild.isPresent()) {
                RegStage regStage = regStageRepository.findByGrandchild(opGrandchild.get())
                    .orElse(new RegStage(RegStage.Stage.ERROR, opGrandchild.get()));
                regStageRepository.save(regStage);
            }
        }
    }
}
