package net.archiscape.app.core.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import net.archiscape.app.core.IntegrationTest;
import net.archiscape.app.core.domain.ElementConfiguration;
import net.archiscape.app.core.domain.Team;
import net.archiscape.app.core.repository.ElementConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ElementConfigurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ElementConfigurationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTATION = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTATION = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNOLOGY = "AAAAAAAAAA";
    private static final String UPDATED_TECHNOLOGY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EOL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EOL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/element-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ElementConfigurationRepository elementConfigurationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restElementConfigurationMockMvc;

    private ElementConfiguration elementConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ElementConfiguration createEntity(EntityManager em) {
        ElementConfiguration elementConfiguration = new ElementConfiguration()
            .name(DEFAULT_NAME)
            .documentation(DEFAULT_DOCUMENTATION)
            .technology(DEFAULT_TECHNOLOGY)
            .eolDate(DEFAULT_EOL_DATE);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        elementConfiguration.setTeam(team);
        return elementConfiguration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ElementConfiguration createUpdatedEntity(EntityManager em) {
        ElementConfiguration elementConfiguration = new ElementConfiguration()
            .name(UPDATED_NAME)
            .documentation(UPDATED_DOCUMENTATION)
            .technology(UPDATED_TECHNOLOGY)
            .eolDate(UPDATED_EOL_DATE);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        elementConfiguration.setTeam(team);
        return elementConfiguration;
    }

    @BeforeEach
    public void initTest() {
        elementConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    void createElementConfiguration() throws Exception {
        int databaseSizeBeforeCreate = elementConfigurationRepository.findAll().size();
        // Create the ElementConfiguration
        restElementConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(elementConfiguration))
            )
            .andExpect(status().isCreated());

        // Validate the ElementConfiguration in the database
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        ElementConfiguration testElementConfiguration = elementConfigurationList.get(elementConfigurationList.size() - 1);
        assertThat(testElementConfiguration.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testElementConfiguration.getDocumentation()).isEqualTo(DEFAULT_DOCUMENTATION);
        assertThat(testElementConfiguration.getTechnology()).isEqualTo(DEFAULT_TECHNOLOGY);
        assertThat(testElementConfiguration.getEolDate()).isEqualTo(DEFAULT_EOL_DATE);
    }

    @Test
    @Transactional
    void createElementConfigurationWithExistingId() throws Exception {
        // Create the ElementConfiguration with an existing ID
        elementConfiguration.setId("existing_id");

        int databaseSizeBeforeCreate = elementConfigurationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restElementConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(elementConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElementConfiguration in the database
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = elementConfigurationRepository.findAll().size();
        // set the field null
        elementConfiguration.setName(null);

        // Create the ElementConfiguration, which fails.

        restElementConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(elementConfiguration))
            )
            .andExpect(status().isBadRequest());

        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentationIsRequired() throws Exception {
        int databaseSizeBeforeTest = elementConfigurationRepository.findAll().size();
        // set the field null
        elementConfiguration.setDocumentation(null);

        // Create the ElementConfiguration, which fails.

        restElementConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(elementConfiguration))
            )
            .andExpect(status().isBadRequest());

        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTechnologyIsRequired() throws Exception {
        int databaseSizeBeforeTest = elementConfigurationRepository.findAll().size();
        // set the field null
        elementConfiguration.setTechnology(null);

        // Create the ElementConfiguration, which fails.

        restElementConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(elementConfiguration))
            )
            .andExpect(status().isBadRequest());

        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllElementConfigurations() throws Exception {
        // Initialize the database
        elementConfiguration.setId(UUID.randomUUID().toString());
        elementConfigurationRepository.saveAndFlush(elementConfiguration);

        // Get all the elementConfigurationList
        restElementConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(elementConfiguration.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].documentation").value(hasItem(DEFAULT_DOCUMENTATION)))
            .andExpect(jsonPath("$.[*].technology").value(hasItem(DEFAULT_TECHNOLOGY)))
            .andExpect(jsonPath("$.[*].eolDate").value(hasItem(DEFAULT_EOL_DATE.toString())));
    }

    @Test
    @Transactional
    void getElementConfiguration() throws Exception {
        // Initialize the database
        elementConfiguration.setId(UUID.randomUUID().toString());
        elementConfigurationRepository.saveAndFlush(elementConfiguration);

        // Get the elementConfiguration
        restElementConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, elementConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(elementConfiguration.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.documentation").value(DEFAULT_DOCUMENTATION))
            .andExpect(jsonPath("$.technology").value(DEFAULT_TECHNOLOGY))
            .andExpect(jsonPath("$.eolDate").value(DEFAULT_EOL_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingElementConfiguration() throws Exception {
        // Get the elementConfiguration
        restElementConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewElementConfiguration() throws Exception {
        // Initialize the database
        elementConfiguration.setId(UUID.randomUUID().toString());
        elementConfigurationRepository.saveAndFlush(elementConfiguration);

        int databaseSizeBeforeUpdate = elementConfigurationRepository.findAll().size();

        // Update the elementConfiguration
        ElementConfiguration updatedElementConfiguration = elementConfigurationRepository.findById(elementConfiguration.getId()).get();
        // Disconnect from session so that the updates on updatedElementConfiguration are not directly saved in db
        em.detach(updatedElementConfiguration);
        updatedElementConfiguration
            .name(UPDATED_NAME)
            .documentation(UPDATED_DOCUMENTATION)
            .technology(UPDATED_TECHNOLOGY)
            .eolDate(UPDATED_EOL_DATE);

        restElementConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedElementConfiguration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedElementConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the ElementConfiguration in the database
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeUpdate);
        ElementConfiguration testElementConfiguration = elementConfigurationList.get(elementConfigurationList.size() - 1);
        assertThat(testElementConfiguration.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testElementConfiguration.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
        assertThat(testElementConfiguration.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testElementConfiguration.getEolDate()).isEqualTo(UPDATED_EOL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingElementConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = elementConfigurationRepository.findAll().size();
        elementConfiguration.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElementConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, elementConfiguration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(elementConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElementConfiguration in the database
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchElementConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = elementConfigurationRepository.findAll().size();
        elementConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(elementConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElementConfiguration in the database
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamElementConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = elementConfigurationRepository.findAll().size();
        elementConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(elementConfiguration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ElementConfiguration in the database
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateElementConfigurationWithPatch() throws Exception {
        // Initialize the database
        elementConfiguration.setId(UUID.randomUUID().toString());
        elementConfigurationRepository.saveAndFlush(elementConfiguration);

        int databaseSizeBeforeUpdate = elementConfigurationRepository.findAll().size();

        // Update the elementConfiguration using partial update
        ElementConfiguration partialUpdatedElementConfiguration = new ElementConfiguration();
        partialUpdatedElementConfiguration.setId(elementConfiguration.getId());

        partialUpdatedElementConfiguration
            .name(UPDATED_NAME)
            .documentation(UPDATED_DOCUMENTATION)
            .technology(UPDATED_TECHNOLOGY)
            .eolDate(UPDATED_EOL_DATE);

        restElementConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElementConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedElementConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the ElementConfiguration in the database
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeUpdate);
        ElementConfiguration testElementConfiguration = elementConfigurationList.get(elementConfigurationList.size() - 1);
        assertThat(testElementConfiguration.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testElementConfiguration.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
        assertThat(testElementConfiguration.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testElementConfiguration.getEolDate()).isEqualTo(UPDATED_EOL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateElementConfigurationWithPatch() throws Exception {
        // Initialize the database
        elementConfiguration.setId(UUID.randomUUID().toString());
        elementConfigurationRepository.saveAndFlush(elementConfiguration);

        int databaseSizeBeforeUpdate = elementConfigurationRepository.findAll().size();

        // Update the elementConfiguration using partial update
        ElementConfiguration partialUpdatedElementConfiguration = new ElementConfiguration();
        partialUpdatedElementConfiguration.setId(elementConfiguration.getId());

        partialUpdatedElementConfiguration
            .name(UPDATED_NAME)
            .documentation(UPDATED_DOCUMENTATION)
            .technology(UPDATED_TECHNOLOGY)
            .eolDate(UPDATED_EOL_DATE);

        restElementConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElementConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedElementConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the ElementConfiguration in the database
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeUpdate);
        ElementConfiguration testElementConfiguration = elementConfigurationList.get(elementConfigurationList.size() - 1);
        assertThat(testElementConfiguration.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testElementConfiguration.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
        assertThat(testElementConfiguration.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testElementConfiguration.getEolDate()).isEqualTo(UPDATED_EOL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingElementConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = elementConfigurationRepository.findAll().size();
        elementConfiguration.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElementConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, elementConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(elementConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElementConfiguration in the database
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchElementConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = elementConfigurationRepository.findAll().size();
        elementConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(elementConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElementConfiguration in the database
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamElementConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = elementConfigurationRepository.findAll().size();
        elementConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(elementConfiguration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ElementConfiguration in the database
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteElementConfiguration() throws Exception {
        // Initialize the database
        elementConfiguration.setId(UUID.randomUUID().toString());
        elementConfigurationRepository.saveAndFlush(elementConfiguration);

        int databaseSizeBeforeDelete = elementConfigurationRepository.findAll().size();

        // Delete the elementConfiguration
        restElementConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, elementConfiguration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ElementConfiguration> elementConfigurationList = elementConfigurationRepository.findAll();
        assertThat(elementConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
