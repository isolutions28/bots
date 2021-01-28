package ua.isolutions.bot.commons.actions;

import lombok.extern.log4j.Log4j2;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.response.MessageWrapper;

@Log4j2
public abstract class DataAction<T> extends ResponseAction {

    @Override
    public MessageWrapper process(ActionMessage message) {
		return getDomainSendMessageWrapper(message);
	}

	protected MessageWrapper getDomainSendMessageWrapper(ActionMessage message) {
		try {
			T data = getData(message);
			T formattedData = formattedData(data, message);
			if (isCorrectData(formattedData, message)) {
				addData(formattedData, message);
				return delegateProcessing(message);
			}
			throw new IllegalArgumentException("Data " + data + " hasn't passed check in: " + getClass().getSimpleName());
		} catch (Exception e) {
			log.debug("Bad data from user. " + message, e);
			return createWrongDataResponse(message);
		}
	}

	protected MessageWrapper delegateProcessing(ActionMessage message) {
		return processByNext(message);
	}

	protected void addData(T formattedData, ActionMessage message) {
		saveData(message.getChatIdMessenger(), dataName(), formattedData);
	}

	protected abstract T getData(ActionMessage message);

	protected T formattedData(T data, ActionMessage message) {
        return data;
    }

    protected boolean isCorrectData(T formattedData, ActionMessage message) { return true; }
    protected abstract String dataName();

}
