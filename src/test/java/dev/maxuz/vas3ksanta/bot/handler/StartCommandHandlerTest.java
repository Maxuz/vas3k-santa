package dev.maxuz.vas3ksanta.bot.handler;

import dev.maxuz.vas3ksanta.bot.BotMessageService;
import dev.maxuz.vas3ksanta.bot.BotProps;
import dev.maxuz.vas3ksanta.bot.BotUtils;
import dev.maxuz.vas3ksanta.db.UserRepository;
import dev.maxuz.vas3ksanta.db.RegStageRepository;
import dev.maxuz.vas3ksanta.model.UserEntity;
import dev.maxuz.vas3ksanta.model.RegStageEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StartCommandHandlerTest {
    private static final String telegramId = "1234";

    private final UserRepository userRepository = mock(UserRepository.class);
    private final RegStageRepository regStageRepository = mock(RegStageRepository.class);
    private final BotProps properties = mock(BotProps.class);
    private final BotMessageService messageService = mock(BotMessageService.class);
    private final BotUtils botUtils = mock(BotUtils.class);

    private final StartCommandHandler handler = new StartCommandHandler(userRepository, regStageRepository, properties, messageService, botUtils);

    @BeforeEach
    void setUp() {
        when(properties.loginUrl()).thenReturn("https://test");
        when(botUtils.getFromId(any(Message.class))).thenReturn(telegramId);
    }

    @Test
    void canHandle_UpdateMessageIsNull_False() {
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(null);
        assertFalse(handler.canHandle(update));
    }

    @Test
    void canHandle_UpdateMessageTextIsNull_False() {
        var update = mock(Update.class);
        var message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn(null);
        assertFalse(handler.canHandle(update));
    }

    @Test
    void canHandle_UpdateMessageTextIsEmpty_False() {
        var update = mock(Update.class);
        var message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("");

        assertFalse(handler.canHandle(update));
    }

    private static Stream<Arguments> canHandleSource() {
        return Stream.of(
            Arguments.of("/start", true, true),
            Arguments.of("/start something", true, true),
            Arguments.of("/start", false, false),
            Arguments.of("/random", true, false),
            Arguments.of("/random", false, false),
            Arguments.of(" ", false, false),
            Arguments.of("", false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("canHandleSource")
    void canHandle(String text, Boolean isCommand, boolean expected) {
        var update = mock(Update.class);
        var message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn(text);
        when(message.isCommand()).thenReturn(isCommand);

        assertEquals(expected, handler.canHandle(update));
    }

    private static Update getUpdate() {
        var message = mock(Message.class);
        when(message.isCommand()).thenReturn(true);
        when(message.getText()).thenReturn("/start");

        var update = mock(Update.class);
        when(update.getMessage()).thenReturn(message);

        return update;
    }

    private static RegStageEntity getRegStage(RegStageEntity.Stage stage, UserEntity user) {
        RegStageEntity regStage = mock(RegStageEntity.class);
        when(regStage.getStage()).thenReturn(stage);
        when(regStage.getUser()).thenReturn(user);
        return regStage;
    }

    /*
    1. New user
    2. User exist
    2.1 User is already registered
    2.2 There was an error during registration
     */

    @Test
    void handle_UserIsNewStageIsNull_MessageWithLink() {
        when(userRepository.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        when(regStageRepository.findByUser(any())).thenReturn(Optional.empty());

        Update update = getUpdate();
        handler.handle(update);

        verify(messageService).sendMarkdown(telegramId, "Пройдите авторизацию в Вастрик\\.Клуб по [ссылке](https://test?tid=1234)");
        ArgumentCaptor<UserEntity> userArgCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userArgCaptor.capture());

        UserEntity actualUser = userArgCaptor.getValue();
        assertNotNull(actualUser);
        assertEquals(telegramId, actualUser.getTelegramId());

        ArgumentCaptor<RegStageEntity> regStageArgCaptor = ArgumentCaptor.forClass(RegStageEntity.class);
        verify(regStageRepository).save(regStageArgCaptor.capture());
        RegStageEntity regStage = regStageArgCaptor.getValue();
        assertNotNull(regStage);
        assertEquals(actualUser, regStage.getUser());
        assertEquals(RegStageEntity.Stage.STARTED, regStage.getStage());
    }

    @Test
    void handle_UserExistsStageIsNull_MessageWithLink() {
        UserEntity user = mock(UserEntity.class);
        when(userRepository.findByTelegramId(telegramId)).thenReturn(Optional.of(user));
        when(regStageRepository.findByUser(any())).thenReturn(Optional.empty());

        Update update = getUpdate();
        handler.handle(update);

        verify(messageService).sendMarkdown(telegramId, "Пройдите авторизацию в Вастрик\\.Клуб по [ссылке](https://test?tid=1234)");
        ArgumentCaptor<UserEntity> userArgCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userArgCaptor.capture());

        UserEntity actualUser = userArgCaptor.getValue();
        assertNotNull(actualUser);
        assertEquals(user, actualUser);

        ArgumentCaptor<RegStageEntity> regStageArgCaptor = ArgumentCaptor.forClass(RegStageEntity.class);
        verify(regStageRepository).save(regStageArgCaptor.capture());
        RegStageEntity regStage = regStageArgCaptor.getValue();
        assertNotNull(regStage);
        assertEquals(actualUser, regStage.getUser());
        assertEquals(RegStageEntity.Stage.STARTED, regStage.getStage());
    }

    @Test
    void handle_UserExistsStageIsStarted_MessageWithLink() {
        UserEntity user = mock(UserEntity.class);
        when(userRepository.findByTelegramId(telegramId)).thenReturn(Optional.of(user));
        RegStageEntity regStage = getRegStage(RegStageEntity.Stage.STARTED, user);
        when(regStageRepository.findByUser(any())).thenReturn(Optional.of(regStage));

        Update update = getUpdate();
        handler.handle(update);

        verify(messageService).sendMarkdown(telegramId, "Пройдите авторизацию в Вастрик\\.Клуб по [ссылке](https://test?tid=1234)");

        ArgumentCaptor<UserEntity> userArgCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userArgCaptor.capture());

        UserEntity actualUser = userArgCaptor.getValue();
        assertNotNull(actualUser);
        assertEquals(user, actualUser);

        ArgumentCaptor<RegStageEntity> regStageArgCaptor = ArgumentCaptor.forClass(RegStageEntity.class);
        verify(regStageRepository).save(regStageArgCaptor.capture());
        RegStageEntity actualStage = regStageArgCaptor.getValue();
        assertNotNull(actualStage);
        assertEquals(actualUser, actualStage.getUser());
        assertEquals(RegStageEntity.Stage.STARTED, actualStage.getStage());
    }

    @Test
    void handle_UserExistsStageIsFinal_MessageWithLink() {
        UserEntity user = mock(UserEntity.class);
        when(userRepository.findByTelegramId(telegramId)).thenReturn(Optional.of(user));

        RegStageEntity regStage = getRegStage(RegStageEntity.Stage.FINAL, user);
        when(regStageRepository.findByUser(any())).thenReturn(Optional.of(regStage));

        Update update = getUpdate();
        handler.handle(update);

        verify(messageService).sendMarkdown(telegramId, "Пройдите авторизацию в Вастрик\\.Клуб по [ссылке](https://test?tid=1234)");
        verify(messageService).sendPlainText(telegramId, "⚠️ Warning ⚠️ Мы нашли существующую регистрацию. Продолжение операции удалит текущую запись. Навеки вечные.");

        ArgumentCaptor<UserEntity> userArgCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userArgCaptor.capture());

        UserEntity actualUser = userArgCaptor.getValue();
        assertNotNull(actualUser);
        assertEquals(user, actualUser);
        verify(regStageRepository, never()).save(any());
    }
}