package ua.isolutions.bot.commons.command.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.isolutions.bot.commons.command.domain.CommandEntity;
import ua.isolutions.bot.commons.command.dto.CommandLabelCommandDto;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CommandRepository extends JpaRepository<CommandEntity, Integer> {
    Optional<CommandEntity> findOneByLabel(String label);

	@Query("SELECT new ua.innovations.bot.commons.command.dto.CommandLabelCommandDto(ce.command, ce.label, ce.position) " +
			"FROM CommandEntity ce " +
			"WHERE ce.enabled = true " +
			"AND ce.topCommand = true " +
			"ORDER BY ce.position")
	Collection<CommandLabelCommandDto> getActiveCommands();
}
