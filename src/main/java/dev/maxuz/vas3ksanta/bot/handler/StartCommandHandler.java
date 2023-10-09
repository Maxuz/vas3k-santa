package dev.maxuz.vas3ksanta.bot.handler;

import dev.maxuz.vas3ksanta.bot.BotMessageService;
import dev.maxuz.vas3ksanta.bot.BotProps;
import dev.maxuz.vas3ksanta.bot.BotUtils;
import dev.maxuz.vas3ksanta.db.GrandchildRepository;
import dev.maxuz.vas3ksanta.db.RegStageRepository;
import dev.maxuz.vas3ksanta.model.Grandchild;
import dev.maxuz.vas3ksanta.model.RegStage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class StartCommandHandler implements BotUpdateHandler {
    private final GrandchildRepository grandchildRepository;
    private final RegStageRepository regStageRepository;
    private final BotProps properties;
    private final BotMessageService messageService;
    private final BotUtils botUtils;

    public StartCommandHandler(GrandchildRepository grandchildRepository,
                               RegStageRepository regStageRepository,
                               BotProps properties,
                               BotMessageService messageService,
                               BotUtils botUtils) {
        this.grandchildRepository = grandchildRepository;
        this.regStageRepository = regStageRepository;
        this.properties = properties;
        this.messageService = messageService;
        this.botUtils = botUtils;
    }


    @Override
    public boolean canHandle(Update update) {
        var msg = update.getMessage();
        return msg != null
            && msg.isCommand()
            && StringUtils.isNotBlank(msg.getText())
            && msg.getText().startsWith("/start");
    }

    @Override
    public void handle(Update update) {
        var telegramId = botUtils.getFromId(update.getMessage());

        Optional<Grandchild> opGrandchild = grandchildRepository.findByTelegramId(telegramId);
        Grandchild grandchild = opGrandchild.orElse(new Grandchild(telegramId));
        grandchildRepository.save(grandchild);


        RegStage regStage = regStageRepository.findByGrandchild(grandchild).orElse(new RegStage(RegStage.Stage.STARTED, grandchild));
        if (regStage.getStage() == RegStage.Stage.FINAL) {
            messageService.sendPlainText(telegramId, "⚠️ Warning ⚠️ Мы нашли существующую регистрацию. Продолжение операции удалит текущую запись. Навеки вечные.");
        } else {
            regStageRepository.save(regStage);
        }

        String message = "Пройдите авторизацию в Вастрик\\.Клуб по [ссылке](%s?tid=%s)".formatted(properties.loginUrl(), telegramId);
        messageService.sendMarkdown(telegramId, message);
    }
}
