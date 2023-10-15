package dev.maxuz.vas3ksanta.bot.handler;

import dev.maxuz.vas3ksanta.bot.BotMessageService;
import dev.maxuz.vas3ksanta.bot.BotUtils;
import dev.maxuz.vas3ksanta.db.GrandchildRepository;
import dev.maxuz.vas3ksanta.db.RegStageRepository;
import dev.maxuz.vas3ksanta.model.GrandchildEntity;
import dev.maxuz.vas3ksanta.model.RegStageEntity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BotExceptionHandlerTest {
    private final BotUtils botUtils = mock(BotUtils.class);
    private final BotMessageService messageService = mock(BotMessageService.class);
    private final GrandchildRepository grandchildRepository = mock(GrandchildRepository.class);
    private final RegStageRepository regStageRepository = mock(RegStageRepository.class);

    private final BotExceptionHandler handler = new BotExceptionHandler(botUtils, messageService, grandchildRepository, regStageRepository);

    @Test
    void handle_NoFromId_NothingHappened() {
        var update = mock(Update.class);
        var e = mock(Exception.class);

        when(botUtils.getFromId(any(Update.class))).thenReturn(null);

        handler.handle(update, e);

        verify(messageService, never()).sendInternalErrorMessage(any());
        verify(regStageRepository, never()).save(any());
    }

    @Test
    void handle_NoGrandchildFound_SendMessage() {
        var update = mock(Update.class);
        var e = mock(Exception.class);

        when(grandchildRepository.findByTelegramId(any())).thenReturn(Optional.empty());
        when(botUtils.getFromId(any(Update.class))).thenReturn("1234");

        handler.handle(update, e);

        verify(messageService).sendInternalErrorMessage("1234");
        verify(regStageRepository, never()).save(any());
    }

    @Test
    void handle_GrandchildExists_UpdateRegStageAndSendMessage() {
        var update = mock(Update.class);
        var exception = mock(Exception.class);
        var grandchild = mock(GrandchildEntity.class);

        when(botUtils.getFromId(any(Update.class))).thenReturn("1234");
        when(grandchildRepository.findByTelegramId(any())).thenReturn(Optional.of(grandchild));

        handler.handle(update, exception);

        verify(messageService).sendInternalErrorMessage("1234");
        ArgumentCaptor<RegStageEntity> regStageArgCap = ArgumentCaptor.forClass(RegStageEntity.class);
        verify(regStageRepository).save(regStageArgCap.capture());

        RegStageEntity regStage = regStageArgCap.getValue();
        assertNotNull(regStage);
        assertEquals(RegStageEntity.Stage.ERROR, regStage.getStage());
    }
}