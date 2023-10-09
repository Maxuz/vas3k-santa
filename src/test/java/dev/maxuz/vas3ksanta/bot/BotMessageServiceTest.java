package dev.maxuz.vas3ksanta.bot;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BotMessageServiceTest {
    private static final String chatId = "1234";
    private final AbsSender absSender = mock(AbsSender.class);

    private final BotMessageService service = new BotMessageService(absSender);

    @Test
    void sendPlainText() throws Exception {
        service.sendPlainText(chatId, "Simple message");

        ArgumentCaptor<SendMessage> msgArgCap = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(msgArgCap.capture());

        var message = msgArgCap.getValue();
        assertNotNull(message);
        assertEquals("Simple message", message.getText());
        assertEquals(chatId, message.getChatId());
    }

    @Test
    void sendPlainText_SenderThrowsException_Rethrow() throws Exception {
        TelegramApiException exc = new TelegramApiException();
        doThrow(exc).when(absSender).execute(any(SendMessage.class));

        RuntimeException expectedExc = assertThrows(RuntimeException.class, () -> service.sendPlainText(chatId, "Simple message"));
        assertEquals("Executing send message error", expectedExc.getMessage());
        assertEquals(exc, expectedExc.getCause());
    }

    @Test
    void sendMarkdown() throws Exception {
        var text = "Пройдите авторизацию в Вастрик\\.Клуб по [ссылке](http://test?tid=1234)";
        service.sendMarkdown(chatId, text);

        ArgumentCaptor<SendMessage> msgArgCap = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(msgArgCap.capture());

        var message = msgArgCap.getValue();
        assertNotNull(message);
        assertEquals(text, message.getText());
        assertEquals(chatId, message.getChatId());
    }

    @Test
    void sendInternalErrorMessage() throws Exception {
        service.sendInternalErrorMessage(chatId);

        ArgumentCaptor<SendMessage> msgArgCap = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(msgArgCap.capture());

        var message = msgArgCap.getValue();
        assertNotNull(message);
        assertEquals("Упс, что-то пошло не так ☹️. Попробуйте еще раз, если ошибка повторится, напишите администратору", message.getText());
        assertEquals(chatId, message.getChatId());
    }
}