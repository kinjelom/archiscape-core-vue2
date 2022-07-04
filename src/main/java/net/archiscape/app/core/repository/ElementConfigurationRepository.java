package net.archiscape.app.core.repository;

import net.archiscape.app.core.domain.ElementConfiguration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ElementConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElementConfigurationRepository extends JpaRepository<ElementConfiguration, String> {}
