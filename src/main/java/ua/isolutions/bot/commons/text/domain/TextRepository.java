package ua.isolutions.bot.commons.text.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.isolutions.bot.commons.text.dao.Text;

import java.util.Optional;

@Repository
public interface TextRepository extends JpaRepository<Text, Integer> {
	Optional<Text> findOneByName(String name);
}

