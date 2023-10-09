package dev.maxuz.vas3ksanta.bot;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BotUtilsTest {
    private static final Long testId = 1234L;

    private final BotUtils utils = new BotUtils();

    private static Message getMessage() {
        var msg = mock(Message.class);
        var user = getUser();
        when(msg.getFrom()).thenReturn(user);
        return msg;
    }

    private static CallbackQuery getCallbackQuery() {
        var query = mock(CallbackQuery.class);
        var user = getUser();
        when(query.getFrom()).thenReturn(user);
        return query;
    }

    private static User getUser() {
        var user = mock(User.class);
        when(user.getId()).thenReturn(testId);
        return user;
    }

    @Test
    void getFromId_EmptyUpdate_Null() {
        var update = mock(Update.class);
        assertNull(utils.getFromId(update));
    }

    @Test
    void getFromId_WithMessageWithoutCallbackQuery() {
        var update = mock(Update.class);
        var message = getMessage();
        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        when(update.hasCallbackQuery()).thenReturn(false);

        assertEquals(testId.toString(), utils.getFromId(update));
    }

    @Test
    void getFromId_WithoutMessageWithCallbackQuery() {
        var update = mock(Update.class);
        when(update.hasMessage()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(true);
        var callbackQuery = getCallbackQuery();
        when(update.getCallbackQuery()).thenReturn(callbackQuery);

        assertEquals(testId.toString(), utils.getFromId(update));
    }

    @Test
    void getFromId_WithMessageWithCallbackQuery() {
        var update = mock(Update.class);
        when(update.hasCallbackQuery()).thenReturn(true);
        var callbackQuery = getCallbackQuery();
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(update.hasMessage()).thenReturn(true);
        var message = getMessage();
        when(update.getMessage()).thenReturn(message);

        assertEquals(testId.toString(), utils.getFromId(update));
    }

    @Test
    void getFromIdFromMessage() {
        var message = getMessage();

        assertEquals(testId.toString(), utils.getFromId(message));
    }

    @Test
    void getFromIdFromCallbackQuery() {
        var query = getCallbackQuery();

        assertEquals(testId.toString(), utils.getFromId(query));
    }
}