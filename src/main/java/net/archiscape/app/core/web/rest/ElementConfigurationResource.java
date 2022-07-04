package net.archiscape.app.core.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import net.archiscape.app.core.domain.ElementConfiguration;
import net.archiscape.app.core.repository.ElementConfigurationRepository;
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
 * REST controller for managing {@link net.archiscape.app.core.domain.ElementConfiguration}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ElementConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(ElementConfigurationResource.class);

    private static final String ENTITY_NAME = "elementConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ElementConfigurationRepository elementConfigurationRepository;

    public ElementConfigurationResource(ElementConfigurationRepository elementConfigurationRepository) {
        this.elementConfigurationRepository = elementConfigurationRepository;
    }

    /**
     * {@code POST  /element-configurations} : Create a new elementConfiguration.
     *
     * @param elementConfiguration the elementConfiguration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new elementConfiguration, or with status {@code 400 (Bad Request)} if the elementConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/element-configurations")
    public ResponseEntity<ElementConfiguration> createElementConfiguration(@Valid @RequestBody ElementConfiguration elementConfiguration)
        throws URISyntaxException {
        log.debug("REST request to save ElementConfiguration : {}", elementConfiguration);
        if (elementConfiguration.getId() != null) {
            throw new BadRequestAlertException("A new elementConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ElementConfiguration result = elementConfigurationRepository.save(elementConfiguration);
        return ResponseEntity
            .created(new URI("/api/element-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /element-configurations/:id} : Updates an existing elementConfiguration.
     *
     * @param id the id of the elementConfiguration to save.
     * @param elementConfiguration the elementConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated elementConfiguration,
     * or with status {@code 400 (Bad Request)} if the elementConfiguration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the elementConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/element-configurations/{id}")
    public ResponseEntity<ElementConfiguration> updateElementConfiguration(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ElementConfiguration elementConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to update ElementConfiguration : {}, {}", id, elementConfiguration);
        if (elementConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, elementConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!elementConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        elementConfiguration.setIsPersisted();
        ElementConfiguration result = elementConfigurationRepository.save(elementConfiguration);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, elementConfiguration.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /element-configurations/:id} : Partial updates given fields of an existing elementConfiguration, field will ignore if it is null
     *
     * @param id the id of the elementConfiguration to save.
     * @param elementConfiguration the elementConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated elementConfiguration,
     * or with status {@code 400 (Bad Request)} if the elementConfiguration is not valid,
     * or with status {@code 404 (Not Found)} if the elementConfiguration is not found,
     * or with status {@code 500 (Internal Server Error)} if the elementConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/element-configurations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ElementConfiguration> partialUpdateElementConfiguration(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ElementConfiguration elementConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to partial update ElementConfiguration partially : {}, {}", id, elementConfiguration);
        if (elementConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, elementConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!elementConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ElementConfiguration> result = elementConfigurationRepository
            .findById(elementConfiguration.getId())
            .map(existingElementConfiguration -> {
                if (elementConfiguration.getName() != null) {
                    existingElementConfiguration.setName(elementConfiguration.getName());
                }
                if (elementConfiguration.getDocumentation() != null) {
                    existingElementConfiguration.setDocumentation(elementConfiguration.getDocumentation());
                }
                if (elementConfiguration.getTechnology() != null) {
                    existingElementConfiguration.setTechnology(elementConfiguration.getTechnology());
                }
                if (elementConfiguration.getEolDate() != null) {
                    existingElementConfiguration.setEolDate(elementConfiguration.getEolDate());
                }

                return existingElementConfiguration;
            })
            .map(elementConfigurationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, elementConfiguration.getId())
        );
    }

    /**
     * {@code GET  /element-configurations} : get all the elementConfigurations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of elementConfigurations in body.
     */
    @GetMapping("/element-configurations")
    public ResponseEntity<List<ElementConfiguration>> getAllElementConfigurations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ElementConfigurations");
        Page<ElementConfiguration> page = elementConfigurationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /element-configurations/:id} : get the "id" elementConfiguration.
     *
     * @param id the id of the elementConfiguration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the elementConfiguration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/element-configurations/{id}")
    public ResponseEntity<ElementConfiguration> getElementConfiguration(@PathVariable String id) {
        log.debug("REST request to get ElementConfiguration : {}", id);
        Optional<ElementConfiguration> elementConfiguration = elementConfigurationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(elementConfiguration);
    }

    /**
     * {@code DELETE  /element-configurations/:id} : delete the "id" elementConfiguration.
     *
     * @param id the id of the elementConfiguration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/element-configurations/{id}")
    public ResponseEntity<Void> deleteElementConfiguration(@PathVariable String id) {
        log.debug("REST request to delete ElementConfiguration : {}", id);
        elementConfigurationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
