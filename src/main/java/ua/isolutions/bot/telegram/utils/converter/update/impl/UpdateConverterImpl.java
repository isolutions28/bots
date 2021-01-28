package ua.isolutions.bot.telegram.utils.converter.update.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import ua.isolutions.bot.commons.dto.ChatIdMessenger;
import ua.isolutions.bot.commons.messages.message.incoming.ActionMessage;
import ua.isolutions.bot.commons.messages.message.incoming.LocationWrapper;
import ua.isolutions.bot.commons.messages.message.incoming.MessengerFileWrapper;
import ua.isolutions.bot.commons.messages.message.response.MessengerType;
import ua.isolutions.bot.commons.utils.PhoneFormatter;
import ua.isolutions.bot.telegram.clients.TelegramClient;
import ua.isolutions.bot.telegram.clients.dto.FileDetails;
import ua.isolutions.bot.telegram.config.TelegramBotConfig;
import ua.isolutions.bot.telegram.utils.converter.update.UpdateConverter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ua.isolutions.bot.commons.messages.message.incoming.ActionMessage.MessageType.*;

@Component
@RequiredArgsConstructor
@ConditionalOnBean(TelegramBotConfig.class)
public class UpdateConverterImpl implements UpdateConverter {

	private final TelegramClient telegramClient;

	@Override
	public ActionMessage convert(Update update) {
		if (isNull(update)) {
			throw new IllegalArgumentException("Update is null");
		}
		ActionMessage am = new ActionMessage(new ChatIdMessenger(getChatId(update), MessengerType.TELEGRAM));
		addMessageText(update, am);
		addFile(update, am);
		addLocation(update, am);
		addMessageId(update, am);
		return am;
	}

	private void addMessageText(Update update, ActionMessage am) {

		if (nonNull(getMessage(update))) {
			if (nonNull(getMessage(update).getContact())) {
				am.setMessage(PhoneFormatter.getPhone(getMessage(update).getContact().getPhoneNumber()));
				am.setType(CONTACT);
				return;
			}

			if (nonNull(getMessage(update).getDice())) {
				am.setMessage(String.valueOf(getMessage(update).getDice().getValue()));
				am.setType(DICE);
				return;
			}

			am.setMessage(getMessage(update).getText());
			am.setType(TEXT);
		}

		if (nonNull(getCallBackQuery(update))) {
			am.setMessage(getCallBackQuery(update).getData());
			am.setType(TEXT);
		}
	}

	private String getChatId(Update update) {
		if (nonNull(getMessage(update))) {
			return String.valueOf(getMessage(update).getChatId());
		} else {
			return String.valueOf(update.getCallbackQuery().getFrom().getId());
		}
	}

	private Message getMessage(Update update) {
		return update.getMessage();
	}

	private CallbackQuery getCallBackQuery(Update update) {
		return update.getCallbackQuery();
	}

	private void addLocation(Update update, ActionMessage am) {
		if (isNull(getMessage(update)) || isNull(getMessage(update).getLocation())) {
			return;
		}

		Location location = getMessage(update).getLocation();
		am.setLocation(new LocationWrapper(location.getLongitude(), location.getLatitude()));
		am.setType(LOCATION);
	}

	private void addMessageId(Update update, ActionMessage am) {
		if (nonNull(getMessage(update))) {
			am.setMessageId(getMessage(update).getMessageId());
		}

		if (nonNull(update.getCallbackQuery())) {
			am.setMessageId(getCallBackQuery(update).getMessage().getMessageId());
		}
	}

	private void addFile(Update update, ActionMessage am) {
		if (isNull(getMessage(update))) {
			return;
		}

		if (nonNull(getMessage(update).getPhoto()) && !getMessage(update).getPhoto().isEmpty()) {

			PhotoSize largestPhoto = getMessage(update).getPhoto().get(0);
			for (PhotoSize photo : getMessage(update).getPhoto()) {
				if (photo.getFileSize() > largestPhoto.getFileSize()) {
					largestPhoto = photo;
				}
			}

			am.setFile(getFileByPath(largestPhoto.getFilePath(), largestPhoto.getFileUniqueId()));
			am.setType(PICTURE);
		}

		if (nonNull(getMessage(update).getDocument())) {
			Document document = getMessage(update).getDocument();
			am.setFile(getFileById(document.getFileId(), document.getFileName()));
			am.setType(FILE);
		}


		if (nonNull(getMessage(update).getAudio())) {
			Audio audio = getMessage(update).getAudio();
			am.setFile(getFileById(audio.getFileId(), audio.getFileName()));
			am.setType(AUDIO);
		}

		if (nonNull(getMessage(update).getVideo())) {
			Video video = getMessage(update).getVideo();
			am.setFile(getFileById(video.getFileId(), video.getFileName()));
			am.setType(VIDEO);
		}

		if (nonNull(getMessage(update).getSticker())) {
			Sticker sticker = getMessage(update).getSticker();
			String emoji = sticker.getEmoji();
			am.setMessage(emoji);
			am.setType(STICKER);
		}
	}

	private MessengerFileWrapper getFileByPath(String filePath, String fileName) {
		return new MessengerFileWrapper(fileName, getFileInputStream(filePath));
	}

	private InputStream getFileInputStream(String filePath) {
		return new ByteArrayInputStream(telegramClient.getImageBase64(filePath).getBytes());
	}

	private MessengerFileWrapper getFileById(String fileId, String fileName) {
		FileDetails imageDetails = telegramClient.getImageDetails(fileId);
		return new MessengerFileWrapper(fileName, getFileInputStream(imageDetails.getResult().getFilePath()));
	}
}
