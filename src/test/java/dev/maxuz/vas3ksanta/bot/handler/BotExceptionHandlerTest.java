package dev.maxuz.vas3ksanta.bot.handler;

import dev.maxuz.vas3ksanta.bot.BotMessageService;
import dev.maxuz.vas3ksanta.bot.BotUtils;
import dev.maxuz.vas3ksanta.exception.UserException;
import dev.maxuz.vas3ksanta.model.RegStageEntity;
import dev.maxuz.vas3ksanta.service.RegStageService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BotExceptionHandlerTest {
    private final BotUtils botUtils = mock(BotUtils.class);
    private final BotMessageService messageService = mock(BotMessageService.class);
    private final RegStageService regStageService = mock(RegStageService.class);

    private final BotExceptionHandler handler = new BotExceptionHandler(botUtils, messageService, regStageService);

    @Test
    void handle_NoFromId_NothingHappened() {
        var update = mock(Update.class);
        var e = mock(Exception.class);

        when(botUtils.getFromId(any(Update.class))).thenReturn(null);

        handler.handle(update, e);

        verify(messageService, never()).sendInternalErrorMessage(any());
        verify(regStageService, never()).updateStageByTelegramId(any(), any());
    }

    @Test
    void handle_UserExists_UpdateRegStageAndSendMessage() {
        var update = mock(Update.class);
        var exception = mock(Exception.class);

        when(botUtils.getFromId(any(Update.class))).thenReturn("1234");

        handler.handle(update, exception);

        verify(messageService).sendInternalErrorMessage("1234");
        ArgumentCaptor<RegStageEntity.Stage> regStageArgCap = ArgumentCaptor.forClass(RegStageEntity.Stage.class);
        verify(regStageService).updateStageByTelegramId(eq("1234"), regStageArgCap.capture());

        RegStageEntity.Stage regStage = regStageArgCap.getValue();
        assertNotNull(regStage);
        assertEquals(RegStageEntity.Stage.ERROR, regStage);
    }

    @Test
    void handleUserException_UserExists_UpdateRegStageAndSendMessage() {
        var update = mock(Update.class);
        var exception = new UserException("error text");

        when(botUtils.getFromId(any(Update.class))).thenReturn("1234");

        handler.handle(update, exception);

        verify(messageService).sendPlainText("1234", "error text");
        ArgumentCaptor<RegStageEntity.Stage> regStageArgCap = ArgumentCaptor.forClass(RegStageEntity.Stage.class);
        verify(regStageService).updateStageByTelegramId(eq("1234"), regStageArgCap.capture());

        RegStageEntity.Stage regStage = regStageArgCap.getValue();
        assertNotNull(regStage);
        assertEquals(RegStageEntity.Stage.ERROR, regStage);
    }
}