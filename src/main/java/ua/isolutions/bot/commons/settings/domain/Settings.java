package ua.isolutions.bot.commons.settings.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(name = "ui_settings_name", columnNames = "name"),
		indexes = {@Index(columnList = "name", name = "in_settings_name")})
public class Settings {
	@Id
	private String name;
	private String value;
}
