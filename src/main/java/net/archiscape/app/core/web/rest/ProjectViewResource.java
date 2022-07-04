package net.archiscape.app.core.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import net.archiscape.app.core.domain.ProjectView;
import net.archiscape.app.core.repository.ProjectViewRepository;
import net.archiscape.app.core.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link net.archiscape.app.core.domain.ProjectView}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProjectViewResource {

    private final Logger log = LoggerFactory.getLogger(ProjectViewResource.class);

    private static final String ENTITY_NAME = "projectView";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectViewRepository projectViewRepository;

    public ProjectViewResource(ProjectViewRepository projectViewRepository) {
        this.projectViewRepository = projectViewRepository;
    }

    /**
     * {@code POST  /project-views} : Create a new projectView.
     *
     * @param projectView the projectView to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projectView, or with status {@code 400 (Bad Request)} if the projectView has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/project-views")
    public ResponseEntity<ProjectView> createProjectView(@Valid @RequestBody ProjectView projectView) throws URISyntaxException {
        log.debug("REST request to save ProjectView : {}", projectView);
        if (projectView.getId() != null) {
            throw new BadRequestAlertException("A new projectView cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectView result = projectViewRepository.save(projectView);
        return ResponseEntity
            .created(new URI("/api/project-views/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /project-views/:id} : Updates an existing projectView.
     *
     * @param id the id of the projectView to save.
     * @param projectView the projectView to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectView,
     * or with status {@code 400 (Bad Request)} if the projectView is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projectView couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/project-views/{id}")
    public ResponseEntity<ProjectView> updateProjectView(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProjectView projectView
    ) throws URISyntaxException {
        log.debug("REST request to update ProjectView : {}, {}", id, projectView);
        if (projectView.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectView.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectViewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProjectView result = projectViewRepository.save(projectView);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectView.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /project-views/:id} : Partial updates given fields of an existing projectView, field will ignore if it is null
     *
     * @param id the id of the projectView to save.
     * @param projectView the projectView to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectView,
     * or with status {@code 400 (Bad Request)} if the projectView is not valid,
     * or with status {@code 404 (Not Found)} if the projectView is not found,
     * or with status {@code 500 (Internal Server Error)} if the projectView couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/project-views/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProjectView> partialUpdateProjectView(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProjectView projectView
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProjectView partially : {}, {}", id, projectView);
        if (projectView.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectView.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectViewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProjectView> result = projectViewRepository
            .findById(projectView.getId())
            .map(existingProjectView -> {
                if (projectView.getName() != null) {
                    existingProjectView.setName(projectView.getName());
                }
                if (projectView.getDocumentation() != null) {
                    existingProjectView.setDocumentation(projectView.getDocumentation());
                }
                if (projectView.getc4level() != null) {
                    existingProjectView.setc4level(projectView.getc4level());
                }
                if (projectView.getExtElementId() != null) {
                    existingProjectView.setExtElementId(projectView.getExtElementId());
                }
                if (projectView.getImage() != null) {
                    existingProjectView.setImage(projectView.getImage());
                }
                if (projectView.getImageContentType() != null) {
                    existingProjectView.setImageContentType(projectView.getImageContentType());
                }

                return existingProjectView;
            })
            .map(projectViewRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectView.getId().toString())
        );
    }

    /**
     * {@code GET  /project-views} : get all the projectViews.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projectViews in body.
     */
    @GetMapping("/project-views")
    public ResponseEntity<List<ProjectView>> getAllProjectViews(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ProjectViews");
        Page<ProjectView> page = projectViewRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /project-views/:id} : get the "id" projectView.
     *
     * @param id the id of the projectView to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projectView, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/project-views/{id}")
    public ResponseEntity<ProjectView> getProjectView(@PathVariable Long id) {
        log.debug("REST request to get ProjectView : {}", id);
        Optional<ProjectView> projectView = projectViewRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(projectView);
    }

    /**
     * {@code DELETE  /project-views/:id} : delete the "id" projectView.
     *
     * @param id the id of the projectView to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/project-views/{id}")
    public ResponseEntity<Void> deleteProjectView(@PathVariable Long id) {
        log.debug("REST request to delete ProjectView : {}", id);
        projectViewRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
