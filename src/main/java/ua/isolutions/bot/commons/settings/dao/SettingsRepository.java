package ua.isolutions.bot.commons.settings.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.isolutions.bot.commons.settings.domain.Settings;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, String> {
}
