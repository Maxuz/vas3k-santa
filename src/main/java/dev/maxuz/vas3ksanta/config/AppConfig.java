package dev.maxuz.vas3ksanta.config;

import dev.maxuz.vas3ksanta.bot.BotProps;
import dev.maxuz.vas3ksanta.utils.URIUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private static final String loginUrlPattern = "http://%s:%s/auth";
    private static final String defaultLoginPort = "80";

    @Bean
    public BotProps botProps(@Value("${app.login.host:}") String host,
                             @Value("${app.login.port:}") String port,
                             @Value("${app.bot.token}") String token,
                             @Value("${app.bot.username}") String name) {
        String loginUrl = getLoginUrl(host, port);
        return new BotProps(name, token, loginUrl);
    }

    private static String getLoginUrl(String host, String port) {
        String loginUrl;
        if (StringUtils.isBlank(host) || StringUtils.isBlank(port)) {
            loginUrl = loginUrlPattern.formatted(URIUtils.getPublicIp(), defaultLoginPort);
        } else {
            loginUrl = loginUrlPattern.formatted(host, port);
        }
        return loginUrl;
    }
}
