package ua.isolutions.bot.commons.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.messages.keyboard.dto.KeyboardWrapper;
import ua.isolutions.bot.commons.messages.keyboard.factory.KeyboardFactory;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.MessageWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class PageableAction<T> extends AbstractAction {

	private static final String NEXT_BUTTON_TEXT = "next.button.text";
	private static final String PREVIOUS_BUTTON_TEXT = "previous.button.text";
	private static final String DIVIDER = "__";
	private KeyboardFactory keyboardFactory;

	@Override
	protected MessageWrapper process(ActionMessage message) {
		int page = getPage(message.getMessage());
		Collection<T> data = getData(page);
		return createResponse(data, page, message);
	}

	private MessageWrapper createResponse(Collection<T> data, int page, ActionMessage message) {
		if (data.size() == 0) {
			return wrap(sendMessage(text(getEmptyCollectionText(), message)));
		}


		String messageText = createMessageText(data, message);
		KeyboardWrapper keyboard = createKeyboard(page, message);
		return wrap(sendMessage(messageText, keyboard));
	}

	private KeyboardWrapper createKeyboard(int page, ActionMessage message) {
		int totalPages = totalPages();
		if (totalPages < 1) {
			return null;
		}
		Map<String, String> keyboardButtons = new LinkedHashMap<>(2);

		addPreviousButton(page, keyboardButtons, message);
		addNextButton(page, keyboardButtons, message);
		return keyboardFactory.createKeyboard(keyboardButtons, getButtonsPerRow());
	}

	private void addNextButton(int page, Map<String, String> keyboardButtons, ActionMessage message) {
		String commandText = getCommandText(message.getMessage());
		int transitionalPage = page + 1;
		if (transitionalPage < totalPages()) {
			keyboardButtons.put(text(NEXT_BUTTON_TEXT, message), getPageButtonText()
					+ DIVIDER
					+ (page + 1)
					+ DIVIDER
					+ commandText
			);
		}
	}

	private void addPreviousButton(int page, Map<String, String> keyboardButtons, ActionMessage message) {
		String commandText = getCommandText(message.getMessage());
		int transitionalPage = page + 1;
		if (transitionalPage > 1) {
			keyboardButtons.put(text(PREVIOUS_BUTTON_TEXT, message), getPageButtonText()
					+ DIVIDER
					+ (page - 1)
					+ DIVIDER
					+ commandText
			);
		}
	}

	private int totalPages() {
		return new BigDecimal(getTotalElements())
				.divide(new BigDecimal(getItemsPerPageAmount()), RoundingMode.CEILING)
				.intValue();
	}

	protected String createMessageText(Collection<T> dataList, ActionMessage message) {
		StringBuilder messageText = new StringBuilder();
		for (T element : dataList) {
			messageText.append(convertElementToText(element, message));
			messageText.append("\n");
		}
		return messageText.toString();
	}

	private int getPage(String text) {
		return text.split(DIVIDER).length >= 2
				? Integer.parseInt(text.split(DIVIDER)[1])
				: 0;
	}


	private String getCommandText(String text) {
		return text.split(DIVIDER).length >= 3
				? text.split(DIVIDER)[2]
				: getPageButtonText();
	}

	protected abstract String convertElementToText(T element, ActionMessage message);
	protected abstract Collection<T> getData(int page);
	protected abstract String getEmptyCollectionText();
	protected abstract String getPageButtonText();
	protected abstract String getItemButtonText();
	protected abstract long getTotalElements();
	protected abstract long getItemsPerPageAmount();
	protected int getButtonsPerRow() {
		return 2;
	}

	@Autowired
	public void setKeyboardFactory(KeyboardFactory keyboardFactory) {
		this.keyboardFactory = keyboardFactory;
	}
}
