package net.archiscape.app.core.repository;

import net.archiscape.app.core.domain.ProjectView;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProjectView entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectViewRepository extends JpaRepository<ProjectView, Long> {}
