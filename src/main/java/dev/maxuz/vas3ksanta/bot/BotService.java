package dev.maxuz.vas3ksanta.bot;

import dev.maxuz.vas3ksanta.vas3k.Vas3kApiService;
import dev.maxuz.vas3ksanta.vas3k.dto.Vas3kUser;
import org.springframework.stereotype.Service;

@Service
public class BotService {
    private final Vas3kApiService vas3kApiService;
    private final BotMessageService messageService;

    public BotService(Vas3kApiService vas3kApiService, BotMessageService messageService) {
        this.vas3kApiService = vas3kApiService;
        this.messageService = messageService;
    }

    public void onSuccessAuth(String accessToken, String telegramId) {
        Vas3kUser user = vas3kApiService.getUser(accessToken);

        // todo
        // request user data
        // save data into database
        // send message to the user
        messageService.sendPlainText(telegramId, "Данные от Vas3k.Club получены. Можем продолжать. Твой slug: " + user.getSlug());
    }

    public void onError(String telegramId) {
        messageService.sendInternalErrorMessage(telegramId);
    }
}
