package dev.maxuz.vas3ksanta.bot;

import dev.maxuz.vas3ksanta.bot.handler.BotExceptionHandler;
import dev.maxuz.vas3ksanta.bot.handler.BotUpdateHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BotTest {
    private final BotProps props = mock(BotProps.class);
    private final BotUtils botUtils = mock(BotUtils.class);

    @BeforeEach
    void setUp() {
        when(props.name()).thenReturn("test-name");
        when(botUtils.getFromId(any(Update.class))).thenReturn("test-tid");
    }

    @Test
    void getBotUsername() {
        Bot bot = new Bot(props, null, null, null, botUtils);
        assertEquals("test-name", bot.getBotUsername());
    }

    @Test
    void onUpdateReceived_Positive() {
        BotUpdateHandler handler = mock(BotUpdateHandler.class);
        when(handler.canHandle(any())).thenReturn(true);
        List<BotUpdateHandler> handlers = List.of(handler);

        Update update = mock(Update.class);

        Bot bot = new Bot(props, handlers, null, null, botUtils);
        bot.onUpdateReceived(update);

        verify(handler).handle(update);
    }

    @Test
    void onUpdateReceived_MultipleHandlerCanHandle_OnlyFirstIsCalled() {
        BotUpdateHandler firstHandler = mock(BotUpdateHandler.class);
        BotUpdateHandler secondHandler = mock(BotUpdateHandler.class);
        when(firstHandler.canHandle(any())).thenReturn(true);
        when(secondHandler.canHandle(any())).thenReturn(true);

        List<BotUpdateHandler> handlers = List.of(firstHandler, secondHandler);

        Update update = mock(Update.class);

        Bot bot = new Bot(props, handlers, null, null, botUtils);
        bot.onUpdateReceived(update);

        verify(firstHandler).handle(update);
        verify(secondHandler, never()).handle(update);
    }

    @Test
    void onUpdateReceived_NoHandler_SendMessageToUser() {
        List<BotUpdateHandler> handlers = List.of();
        BotMessageService messageService = mock(BotMessageService.class);

        Update update = mock(Update.class);

        Bot bot = new Bot(props, handlers, messageService, null, botUtils);
        bot.onUpdateReceived(update);

        verify(messageService).sendPlainText("test-tid", "Неизвестный запрос. Проверьте правильность сообщения и если все хорошо обратитесь к администратору");
    }

    @Test
    void onUpdateReceived_HandlerThrowsException_ExceptionHandlerCalled() {
        RuntimeException exc = new RuntimeException("test");

        BotUpdateHandler handler = mock(BotUpdateHandler.class);
        when(handler.canHandle(any())).thenReturn(true);
        doThrow(exc).when(handler).handle(any());
        List<BotUpdateHandler> handlers = List.of(handler);

        BotExceptionHandler exceptionHandler = mock(BotExceptionHandler.class);

        Update update = mock(Update.class);

        Bot bot = new Bot(props, handlers, null, exceptionHandler, botUtils);
        bot.onUpdateReceived(update);

        verify(exceptionHandler).handle(update, exc);
    }

}