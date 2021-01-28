package ua.isolutions.bot.commons.text.dao;


import lombok.Data;
import ua.isolutions.bot.commons.text.exception.NoSuchTextException;

import javax.persistence.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "texts")
@Data
public class Text  {

	@Id
	@GeneratedValue
	private int id;
	@Column(unique = true)
	private String name;

	@MapKeyEnumerated(EnumType.ORDINAL)
	@ElementCollection(fetch = FetchType.EAGER)
	@Column(columnDefinition = "TEXT")
	private Map<Locale, String> textLocales = new ConcurrentHashMap<>(5);

	public String getByLocale(Locale locale) {
		return ofNullable(getTextLocales()
				.get(locale))
				.orElseThrow(() -> new NoSuchTextException("Locale: " + locale + " does not exist for text: " + name))
				.replaceAll("\\\\n", "\n");
	}
}

