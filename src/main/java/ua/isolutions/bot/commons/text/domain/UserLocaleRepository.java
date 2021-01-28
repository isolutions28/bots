package ua.isolutions.bot.commons.text.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.isolutions.bot.commons.messages.message.response.MessengerType;
import ua.isolutions.bot.commons.text.dao.Locale;
import ua.isolutions.bot.commons.text.dao.UserLocale;

import java.util.Optional;

public interface UserLocaleRepository extends JpaRepository<UserLocale, String> {

	@Query(value = "select u.locale from UserLocale u where u.chatId = :chatId and u.messengerType = :messengerType")
	Optional<Locale> findLocaleByUserIdAndMessengerType(@Param("chatId") String userIdentifier, @Param("messengerType") MessengerType messengerType);

	@Query(value = "select u from UserLocale u where u.chatId = :chatId and u.messengerType = :messengerType")
	Optional<UserLocale> findUserLocaleByIdAndMessengerType(@Param("chatId") String userIdentifier, @Param("messengerType") MessengerType messengerType);

}
