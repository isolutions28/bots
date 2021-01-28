package ua.isolutions.bot.viber.utils.messages.impl;

import com.viber.bot.message.PictureMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ua.isolutions.bot.commons.file.dto.FileDto;
import ua.isolutions.bot.commons.messages.message.response.impl.ImageMessage;
import ua.isolutions.bot.viber.config.ViberConfig;
import ua.isolutions.bot.viber.utils.messages.ViberResponseMessageConverter;

import static java.util.Objects.isNull;

@Component
@ConditionalOnBean(ViberConfig.class)
public class ImageMessageViberConverter extends AbstractFileViberConverter implements ViberResponseMessageConverter<ImageMessage, PictureMessage> {

	@Override
	public PictureMessage convert(ImageMessage message) {
		FileDto fileDto = saveFile(message);
		PictureMessage pictureMessage = new PictureMessage();
		pictureMessage.setUrl(fileDto.getDownloadUrl());
 		pictureMessage.setThumbnail(isNull(message.getFileName()) ? "" : message.getFileName());
		pictureMessage.setKeyboard(createKeyboard(message));
		return pictureMessage;
	}

	@Override
	public Class<ImageMessage> getConvertedClass() {
		return ImageMessage.class;
	}
}
