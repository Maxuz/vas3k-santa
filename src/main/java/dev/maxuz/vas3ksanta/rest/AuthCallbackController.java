package dev.maxuz.vas3ksanta.rest;

import dev.maxuz.vas3ksanta.bot.BotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthCallbackController {
    private static final Logger log = LoggerFactory.getLogger(AuthCallbackController.class);

    private final BotService botService;
    private final SpringSecurityService securityService;

    public AuthCallbackController(BotService botService, SpringSecurityService securityService) {
        this.botService = botService;
        this.securityService = securityService;
    }

    @GetMapping("auth")
    public String auth(@RequestParam("tid") String telegramId, OAuth2AuthenticationToken authentication) {
        MDC.put("tid", telegramId);
        log.info("Success auth for telegram user: {}", telegramId);
        try {
            String accessToken = securityService.getAccessToken(authentication);
            botService.onSuccessAuth(accessToken, telegramId);
            return "Вы успешно залогинились! Наш бот скоро расскажет что делать дальше.\n";
        } catch (Exception e) {
            log.error("Internal error", e);
            botService.onError(telegramId);

            return "Что-то пошло не так.";
        } finally {
            MDC.clear();
        }
    }
}
