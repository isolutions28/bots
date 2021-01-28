package ua.isolutions.bot.viber.retranslator.utils.client.impl;

import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.viber.retranslator.dto.SendMessagesRequest;
import ua.isolutions.bot.viber.retranslator.utils.client.RetranslatorClient;
import ua.innovations.commons.http.BaseSerializer;
import ua.innovations.commons.http.impl.AbstractHttpClient;

import java.util.Collection;
import java.util.Collections;

@Component
@ConditionalOnProperty(name = "enable.viber.retranslator", havingValue = "true")
public class RetranslatorClientImpl extends AbstractHttpClient implements RetranslatorClient {
	@Value("${retranslator.bot.url}")
	private String retranslatorUrl;

	public RetranslatorClientImpl(BaseSerializer serializer) {
		super(serializer);
	}

	@Override
	public void sendMessage(SendMessagesRequest sendMessagesRequest) {
		post(retranslatorUrl + "/vbot/send", sendMessagesRequest);
	}

	@Override
	protected Collection<? extends Header> addHeaders() {
		return Collections.emptyList();
	}
}
