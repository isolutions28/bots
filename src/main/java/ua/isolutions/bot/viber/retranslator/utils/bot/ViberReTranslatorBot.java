package ua.isolutions.bot.viber.retranslator.utils.bot;

import com.viber.bot.Request;
import com.viber.bot.api.ApiResponse;
import com.viber.bot.api.ViberBot;
import com.viber.bot.event.Event;
import com.viber.bot.message.Message;
import com.viber.bot.profile.BotProfile;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import ua.isolutions.bot.viber.retranslator.dto.SendMessagesRequest;
import ua.isolutions.bot.viber.retranslator.utils.client.RetranslatorClient;
import ua.innovations.commons.http.BaseSerializer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;


@Log4j2
@ConditionalOnProperty(name = "enable.viber.retranslator", havingValue = "true")
public class ViberReTranslatorBot extends ViberBot {

	private final RetranslatorClient retranslatorClient;
	@Value("${retranslate.user.id}")
	private String userId;

	public ViberReTranslatorBot(BotProfile botProfile, String authToken, BaseSerializer baseSerializer, RetranslatorClient retranslatorClient, ExecutorService incomingProcessor, ExecutorService outgoingProcessor) {
		super(botProfile, authToken, baseSerializer, incomingProcessor, outgoingProcessor);
		this.retranslatorClient = retranslatorClient;
	}

	@Override
	public String incoming(Request request) {
		return super.incoming(request);
	}

	@Override
	@SneakyThrows
	public ApiResponse setWebhook(String url) {
		return successResponse();
	}

	@Override
	@SneakyThrows
	public ApiResponse setWebhook(String url, List<Event> registerToEvents) {
		return successResponse();
	}

	@Override
	public Collection<String> sendMessage(String to, Collection<Message> messages) {
		retranslatorClient.sendMessage(new SendMessagesRequest(getUserId(), messages));
		return null;
	}

	@Override
	public Collection<String> sendMessage(String to, Message... messages) {
		retranslatorClient.sendMessage(new SendMessagesRequest(getUserId(), Arrays.asList(messages)));
		return null;
	}

	private String getUserId() {
		return userId;
	}

	private ApiResponse successResponse() {
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setStatus(0);
		return apiResponse;
	}

}
