package ua.isolutions.bot.commons.actions;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.action_cache.service.ActionCacheService;
import ua.isolutions.bot.commons.dto.ChatIdMessenger;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.innovations.bot.commons.messages.message.response.*;
import ua.isolutions.bot.commons.messages.message.response.Message;
import ua.isolutions.bot.commons.messages.message.response.MessageWrapper;
import ua.isolutions.bot.commons.messages.message.response.impl.FileMessage;
import ua.isolutions.bot.commons.messages.message.response.impl.KeyboardMessage;
import ua.isolutions.bot.commons.messages.message.response.impl.TextMessage;
import ua.isolutions.bot.commons.text.dao.Locale;
import ua.isolutions.bot.commons.text.service.LocaleService;
import ua.isolutions.bot.commons.text.service.TextService;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Log4j2
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class AbstractAction implements Action {
	private TextService textService;
	private LocaleService localeService;
	private ActionCacheService cache;

	private Action next;
	private Action previous;

	@Override
	public MessageWrapper processMessage(ActionMessage message) {
		logProcessing(message);
		return process(message);
	}

	private void logProcessing(ActionMessage message) {
		if (log.isInfoEnabled()) {
			log.info(toString() + " processing: " + message);
		}
	}

	protected abstract MessageWrapper process(ActionMessage message);

	protected <T> T getData(ChatIdMessenger userId, String key) {
		return cache.get(userId, key);
	}

	protected void saveData(ChatIdMessenger userId, String key, Object object) {
		cache.add(userId, key, object);
	}

	protected void removeData(ChatIdMessenger userId, String key) {
		cache.remove(userId, key);
	}

	protected MessageWrapper processByNext(ActionMessage message) {
		checkActionPresence(next, "next", message);
		return next.processMessage(message);
	}

	private void checkActionPresence(Action action, String position, ActionMessage message) {
		if (isNull(action)) {
			throw new RuntimeException("No " + position + " action set for action: " + this.getClass().getName()
					+ ". Message: " + message);
		}
	}

	protected MessageWrapper processByPrevious(ActionMessage message) {
		checkActionPresence(previous, "previous", message);
		return previous.processMessage(message);
	}

	protected Collection<Message> sendMessage(String text) {
		return Collections.singletonList(createTextMessage(text));
	}

	private Message createTextMessage(String text) {
		return new TextMessage(text);
	}

	protected Collection<Message> sendMessage(String text, KeyboardWrapper keyboard) {
		if (isNull(keyboard)) {
			return sendMessage(text);
		}
		return Collections.singletonList(new TextMessage(text, keyboard));
	}

	private Message createKeyboardMessage(KeyboardWrapper keyboard) {
		return new KeyboardMessage(keyboard);
	}

	protected Collection<Message> sendMessage(KeyboardWrapper keyboard) {
		return Collections.singletonList(createKeyboardMessage(keyboard));
	}

	protected Collection<Message> sendMessage(String fileName, InputStream file) {
		return Collections.singletonList(new FileMessage(file, fileName));
	}

	protected <T extends Message> MessageWrapper wrap(T sendMessage, Action lastAction) {
		return wrap(Collections.singletonList(sendMessage), lastAction, lastAction == null);
	}

	protected <T extends Message> MessageWrapper wrap(T sendMessage) {
		return wrap(Collections.singletonList(sendMessage), null, true);
	}

	protected <T extends Message> MessageWrapper wrap(Collection<T> sendMessage, Action lastAction) {
		return wrap(sendMessage, lastAction, lastAction == null);
	}

	protected <T extends Message> MessageWrapper wrap(Collection<T> sendMessage) {
		return wrap(sendMessage, null, true);
	}

	private <T extends Message>  MessageWrapper wrap(Collection<T> sendMessage, Action lastAction, boolean unregister) {
		return new MessageWrapper(sendMessage, lastAction, unregister, absentMessages(sendMessage) || anyMessageHasKeyboard(sendMessage, unregister));
	}

	private <T extends Message> boolean anyMessageHasKeyboard(Collection<T> sendMessage, boolean unregister) {
		return unregister && sendMessage.stream().noneMatch(m -> nonNull(m.getKeyboard()));
	}

	private <T extends Message> boolean absentMessages(Collection<T> sendMessage) {
		return isNull(sendMessage);
	}

	protected MessageWrapper createMessage(ActionMessage message, String textIdentifier) {
		return wrap(sendMessage(text(textIdentifier, message)));
	}

	protected MessageWrapper createMessage(ActionMessage message, String textIdentifier, Action lastAction) {
		return wrap(sendMessage(text(textIdentifier, message)), lastAction);
	}

	protected Action nextAction() {
		return next;
	}

	protected Action currentAction() {
		return this;
	}

	protected String text(String identifier, ActionMessage message) {
		return textService.getText(identifier, message.getChatIdMessenger());
	}

	protected String text(String identifier, Locale locale) {
		return textService.getText(identifier, locale);
	}

	protected Locale locale(ActionMessage message) {
		return localeService.getUserLocale(message.getChatIdMessenger());
	}

	@Override
	public void setNext(Action action) {
		this.next = action;
	}

	@Override
	public void setPrevious(Action action) {
		this.previous = action;
	}

	@Override
	public Action getNext() {
		return next;
	}

	@Autowired
	public void setTextService(TextService textService) {
		this.textService = textService;
	}

	@Autowired
	public void setLocaleService(LocaleService localeService) {
		this.localeService = localeService;
	}
	@Autowired
	public void setCache(ActionCacheService cache) {
		this.cache = cache;
	}

	@Override
	public String toString() {
		return getClass().getName();
	}
}
