package ua.isolutions.bot.commons.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandLabelCommandDto {
	private String command;
	private String label;
	private int position;
}
