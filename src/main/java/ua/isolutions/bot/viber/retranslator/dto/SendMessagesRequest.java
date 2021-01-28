package ua.isolutions.bot.viber.retranslator.dto;

import com.viber.bot.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessagesRequest {
	private String userId;
	private Collection<Message> messages;
}
