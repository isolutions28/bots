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
public abstract class SkipablePageableButtonAction<T> extends AbstractAction implements SkipableAction {

	private static final String NEXT_BUTTON_TEXT = "next.button.text";
	private static final String PREVIOUS_BUTTON_TEXT = "previous.button.text";
	public static final String DIVIDER = "__";
	private KeyboardFactory keyboardFactory;

	@Override
	protected MessageWrapper process(ActionMessage message) {
		if (skip(message) && !message.getMessage().contains(getNavigationButtonCallback())) {
			return processByNext(message);
		}
		return internalProcessing(message);
	}

	private MessageWrapper internalProcessing(ActionMessage message) {
		int page = getPage(message.getMessage());
		Collection<T> data = getData(page);
		return createResponse(data, page, message);
	}

	private MessageWrapper createResponse(Collection<T> data, int page, ActionMessage message) {
		if (data.size() == 0) {
			return wrap(sendMessage(text(getEmptyCollectionTextIdentifier(), message)));
		}
		String text = text(getTopText(), message);
		KeyboardWrapper keyboard = createKeyboard(data, page, message);
		return wrap(sendMessage(text, keyboard), this);
	}

	private KeyboardWrapper createKeyboard(Collection<T> data, int page, ActionMessage message) {
		Map<String, String> keyboardButtons = new LinkedHashMap<>(getElementsPerPage() + 2);
		for (T element : data) {
			keyboardButtons.put(getElementButtonText(element, message), getElementButtonCallback(element, message));
		}

		addPreviousButton(page, keyboardButtons, message);
		addNextButton(page, keyboardButtons, message);

		return keyboardFactory.createKeyboard(keyboardButtons, getButtonsPerRow());
	}


	private void addNextButton(int page, Map<String, String> keyboardButtons, ActionMessage message) {
		int transitionalPage = page + 1;
		if (transitionalPage < getTotalPages()) {
			keyboardButtons.put(text(NEXT_BUTTON_TEXT, message), getNavigationButtonCallback()
					+ DIVIDER
					+ (page + 1)
			);
		}
	}

	private void addPreviousButton(int page, Map<String, String> keyboardButtons, ActionMessage message) {
		int transitionalPage = page + 1;
		if (transitionalPage > 1) {
			keyboardButtons.put(text(PREVIOUS_BUTTON_TEXT, message), getNavigationButtonCallback()
					+ DIVIDER
					+ (page - 1)
			);
		}
	}

	protected abstract int getButtonsPerRow();
	protected abstract String getEmptyCollectionTextIdentifier();
	protected abstract String getElementButtonText(T element, ActionMessage message);
	protected abstract String getElementButtonCallback(T element, ActionMessage message);
	protected abstract String getNavigationButtonCallback();
	protected abstract int getTotalElements();
	protected abstract int getElementsPerPage();
	protected abstract Collection<T> getData(int page);
	protected abstract String getTopText();

	private int getTotalPages() {
		return new BigDecimal(getTotalElements())
				.divide(new BigDecimal(getElementsPerPage()), RoundingMode.CEILING)
				.intValue();
	}

	@Override
	public boolean skip(ActionMessage message) {
		return false;
	}

	private int getPage(String text) {
		return text.split(DIVIDER).length >= 2 && text.contains(getNavigationButtonCallback())
				? Integer.parseInt(text.split(DIVIDER)[1])
				: 0;
	}

	@Autowired
	public void setKeyboardFactory(KeyboardFactory keyboardFactory) {
		this.keyboardFactory = keyboardFactory;
	}
}
