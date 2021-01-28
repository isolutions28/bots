package ua.isolutions.bot.viber.controller;

import com.google.common.base.Preconditions;
import com.viber.bot.Request;
import com.viber.bot.ViberSignatureValidator;
import com.viber.bot.api.ViberBot;
import com.viber.bot.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.*;
import ua.isolutions.bot.viber.config.ViberConfig;

@RestController
@ConditionalOnBean(ViberConfig.class)
@RequestMapping("/vbot")
@RequiredArgsConstructor
@Log4j2
public class ViberBotController {
	private final ViberSignatureValidator signatureValidator;
	private final ViberBot bot;

	@PostMapping(value = "/in", produces = "application/json")
	public String incoming(@RequestBody String json,
						   @RequestHeader("X-Viber-Content-Signature") String serverSideSignature) {
		Preconditions.checkState(signatureValidator.isSignatureValid(serverSideSignature, json), "invalid signature");
		Request request = Request.fromJsonString(json);
		if (request.getEvent().getEvent() != Event.MESSAGE_RECEIVED && request.getEvent().getEvent() != Event.WEBHOOK && request.getEvent().getEvent() != Event.CONVERSATION_STARTED) {
			return null;
		}
		log.info("Received: {}", json);
		String response = bot.incoming(request);
		log.info("Sending: {}", response);
		return response;
	}
}