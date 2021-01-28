package ua.isolutions.bot.telegram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.isolutions.bot.telegram.bots.CustomTelegramWebhookBot;
import ua.isolutions.bot.telegram.service.TelegramBotService;

@RestController
@ConditionalOnProperty(name = {"telegram.bot.webhook-url", "telegram.bot.token"})
@RequestMapping("/telegram")
@Log4j2
@RequiredArgsConstructor
public class TelegramBotController {

	private final TelegramBotService telegramBotService;
	private final CustomTelegramWebhookBot telegramBot;

	@PostMapping("/${telegram.bot.token}")
	public boolean processUpdate(@RequestBody Update update) {
		return telegramBotService.processTelegramUpdate(update, telegramBot);
	}
}
