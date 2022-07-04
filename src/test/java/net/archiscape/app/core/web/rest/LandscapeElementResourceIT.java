package net.archiscape.app.core.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import net.archiscape.app.core.IntegrationTest;
import net.archiscape.app.core.domain.Landscape;
import net.archiscape.app.core.domain.LandscapeElement;
import net.archiscape.app.core.domain.enumeration.C4ElementType;
import net.archiscape.app.core.repository.LandscapeElementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LandscapeElementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LandscapeElementResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTATION = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTATION = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNOLOGY = "AAAAAAAAAA";
    private static final String UPDATED_TECHNOLOGY = "BBBBBBBBBB";

    private static final C4ElementType DEFAULT_C_4_TYPE = C4ElementType.PERSON;
    private static final C4ElementType UPDATED_C_4_TYPE = C4ElementType.SOFTWARE_SYSTEM;

    private static final String ENTITY_API_URL = "/api/landscape-elements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private LandscapeElementRepository landscapeElementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLandscapeElementMockMvc;

    private LandscapeElement landscapeElement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LandscapeElement createEntity(EntityManager em) {
        LandscapeElement landscapeElement = new LandscapeElement()
            .name(DEFAULT_NAME)
            .documentation(DEFAULT_DOCUMENTATION)
            .technology(DEFAULT_TECHNOLOGY)
            .c4type(DEFAULT_C_4_TYPE);
        // Add required entity
        Landscape landscape;
        if (TestUtil.findAll(em, Landscape.class).isEmpty()) {
            landscape = LandscapeResourceIT.createEntity(em);
            em.persist(landscape);
            em.flush();
        } else {
            landscape = TestUtil.findAll(em, Landscape.class).get(0);
        }
        landscapeElement.setLandscape(landscape);
        return landscapeElement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LandscapeElement createUpdatedEntity(EntityManager em) {
        LandscapeElement landscapeElement = new LandscapeElement()
            .name(UPDATED_NAME)
            .documentation(UPDATED_DOCUMENTATION)
            .technology(UPDATED_TECHNOLOGY)
            .c4type(UPDATED_C_4_TYPE);
        // Add required entity
        Landscape landscape;
        if (TestUtil.findAll(em, Landscape.class).isEmpty()) {
            landscape = LandscapeResourceIT.createUpdatedEntity(em);
            em.persist(landscape);
            em.flush();
        } else {
            landscape = TestUtil.findAll(em, Landscape.class).get(0);
        }
        landscapeElement.setLandscape(landscape);
        return landscapeElement;
    }

    @BeforeEach
    public void initTest() {
        landscapeElement = createEntity(em);
    }

    @Test
    @Transactional
    void createLandscapeElement() throws Exception {
        int databaseSizeBeforeCreate = landscapeElementRepository.findAll().size();
        // Create the LandscapeElement
        restLandscapeElementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(landscapeElement))
            )
            .andExpect(status().isCreated());

        // Validate the LandscapeElement in the database
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeCreate + 1);
        LandscapeElement testLandscapeElement = landscapeElementList.get(landscapeElementList.size() - 1);
        assertThat(testLandscapeElement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLandscapeElement.getDocumentation()).isEqualTo(DEFAULT_DOCUMENTATION);
        assertThat(testLandscapeElement.getTechnology()).isEqualTo(DEFAULT_TECHNOLOGY);
        assertThat(testLandscapeElement.getc4type()).isEqualTo(DEFAULT_C_4_TYPE);
    }

    @Test
    @Transactional
    void createLandscapeElementWithExistingId() throws Exception {
        // Create the LandscapeElement with an existing ID
        landscapeElement.setId("existing_id");

        int databaseSizeBeforeCreate = landscapeElementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLandscapeElementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(landscapeElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandscapeElement in the database
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = landscapeElementRepository.findAll().size();
        // set the field null
        landscapeElement.setName(null);

        // Create the LandscapeElement, which fails.

        restLandscapeElementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(landscapeElement))
            )
            .andExpect(status().isBadRequest());

        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentationIsRequired() throws Exception {
        int databaseSizeBeforeTest = landscapeElementRepository.findAll().size();
        // set the field null
        landscapeElement.setDocumentation(null);

        // Create the LandscapeElement, which fails.

        restLandscapeElementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(landscapeElement))
            )
            .andExpect(status().isBadRequest());

        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkc4typeIsRequired() throws Exception {
        int databaseSizeBeforeTest = landscapeElementRepository.findAll().size();
        // set the field null
        landscapeElement.setc4type(null);

        // Create the LandscapeElement, which fails.

        restLandscapeElementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(landscapeElement))
            )
            .andExpect(status().isBadRequest());

        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLandscapeElements() throws Exception {
        // Initialize the database
        landscapeElement.setId(UUID.randomUUID().toString());
        landscapeElementRepository.saveAndFlush(landscapeElement);

        // Get all the landscapeElementList
        restLandscapeElementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landscapeElement.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].documentation").value(hasItem(DEFAULT_DOCUMENTATION)))
            .andExpect(jsonPath("$.[*].technology").value(hasItem(DEFAULT_TECHNOLOGY)))
            .andExpect(jsonPath("$.[*].c4type").value(hasItem(DEFAULT_C_4_TYPE.toString())));
    }

    @Test
    @Transactional
    void getLandscapeElement() throws Exception {
        // Initialize the database
        landscapeElement.setId(UUID.randomUUID().toString());
        landscapeElementRepository.saveAndFlush(landscapeElement);

        // Get the landscapeElement
        restLandscapeElementMockMvc
            .perform(get(ENTITY_API_URL_ID, landscapeElement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(landscapeElement.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.documentation").value(DEFAULT_DOCUMENTATION))
            .andExpect(jsonPath("$.technology").value(DEFAULT_TECHNOLOGY))
            .andExpect(jsonPath("$.c4type").value(DEFAULT_C_4_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLandscapeElement() throws Exception {
        // Get the landscapeElement
        restLandscapeElementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLandscapeElement() throws Exception {
        // Initialize the database
        landscapeElement.setId(UUID.randomUUID().toString());
        landscapeElementRepository.saveAndFlush(landscapeElement);

        int databaseSizeBeforeUpdate = landscapeElementRepository.findAll().size();

        // Update the landscapeElement
        LandscapeElement updatedLandscapeElement = landscapeElementRepository.findById(landscapeElement.getId()).get();
        // Disconnect from session so that the updates on updatedLandscapeElement are not directly saved in db
        em.detach(updatedLandscapeElement);
        updatedLandscapeElement
            .name(UPDATED_NAME)
            .documentation(UPDATED_DOCUMENTATION)
            .technology(UPDATED_TECHNOLOGY)
            .c4type(UPDATED_C_4_TYPE);

        restLandscapeElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLandscapeElement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLandscapeElement))
            )
            .andExpect(status().isOk());

        // Validate the LandscapeElement in the database
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeUpdate);
        LandscapeElement testLandscapeElement = landscapeElementList.get(landscapeElementList.size() - 1);
        assertThat(testLandscapeElement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLandscapeElement.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
        assertThat(testLandscapeElement.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testLandscapeElement.getc4type()).isEqualTo(UPDATED_C_4_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingLandscapeElement() throws Exception {
        int databaseSizeBeforeUpdate = landscapeElementRepository.findAll().size();
        landscapeElement.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandscapeElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, landscapeElement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landscapeElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandscapeElement in the database
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLandscapeElement() throws Exception {
        int databaseSizeBeforeUpdate = landscapeElementRepository.findAll().size();
        landscapeElement.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandscapeElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(landscapeElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandscapeElement in the database
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLandscapeElement() throws Exception {
        int databaseSizeBeforeUpdate = landscapeElementRepository.findAll().size();
        landscapeElement.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandscapeElementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(landscapeElement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LandscapeElement in the database
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLandscapeElementWithPatch() throws Exception {
        // Initialize the database
        landscapeElement.setId(UUID.randomUUID().toString());
        landscapeElementRepository.saveAndFlush(landscapeElement);

        int databaseSizeBeforeUpdate = landscapeElementRepository.findAll().size();

        // Update the landscapeElement using partial update
        LandscapeElement partialUpdatedLandscapeElement = new LandscapeElement();
        partialUpdatedLandscapeElement.setId(landscapeElement.getId());

        partialUpdatedLandscapeElement.name(UPDATED_NAME);

        restLandscapeElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLandscapeElement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLandscapeElement))
            )
            .andExpect(status().isOk());

        // Validate the LandscapeElement in the database
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeUpdate);
        LandscapeElement testLandscapeElement = landscapeElementList.get(landscapeElementList.size() - 1);
        assertThat(testLandscapeElement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLandscapeElement.getDocumentation()).isEqualTo(DEFAULT_DOCUMENTATION);
        assertThat(testLandscapeElement.getTechnology()).isEqualTo(DEFAULT_TECHNOLOGY);
        assertThat(testLandscapeElement.getc4type()).isEqualTo(DEFAULT_C_4_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateLandscapeElementWithPatch() throws Exception {
        // Initialize the database
        landscapeElement.setId(UUID.randomUUID().toString());
        landscapeElementRepository.saveAndFlush(landscapeElement);

        int databaseSizeBeforeUpdate = landscapeElementRepository.findAll().size();

        // Update the landscapeElement using partial update
        LandscapeElement partialUpdatedLandscapeElement = new LandscapeElement();
        partialUpdatedLandscapeElement.setId(landscapeElement.getId());

        partialUpdatedLandscapeElement
            .name(UPDATED_NAME)
            .documentation(UPDATED_DOCUMENTATION)
            .technology(UPDATED_TECHNOLOGY)
            .c4type(UPDATED_C_4_TYPE);

        restLandscapeElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLandscapeElement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLandscapeElement))
            )
            .andExpect(status().isOk());

        // Validate the LandscapeElement in the database
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeUpdate);
        LandscapeElement testLandscapeElement = landscapeElementList.get(landscapeElementList.size() - 1);
        assertThat(testLandscapeElement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLandscapeElement.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
        assertThat(testLandscapeElement.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testLandscapeElement.getc4type()).isEqualTo(UPDATED_C_4_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingLandscapeElement() throws Exception {
        int databaseSizeBeforeUpdate = landscapeElementRepository.findAll().size();
        landscapeElement.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandscapeElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, landscapeElement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landscapeElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandscapeElement in the database
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLandscapeElement() throws Exception {
        int databaseSizeBeforeUpdate = landscapeElementRepository.findAll().size();
        landscapeElement.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandscapeElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landscapeElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the LandscapeElement in the database
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLandscapeElement() throws Exception {
        int databaseSizeBeforeUpdate = landscapeElementRepository.findAll().size();
        landscapeElement.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLandscapeElementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(landscapeElement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LandscapeElement in the database
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLandscapeElement() throws Exception {
        // Initialize the database
        landscapeElement.setId(UUID.randomUUID().toString());
        landscapeElementRepository.saveAndFlush(landscapeElement);

        int databaseSizeBeforeDelete = landscapeElementRepository.findAll().size();

        // Delete the landscapeElement
        restLandscapeElementMockMvc
            .perform(delete(ENTITY_API_URL_ID, landscapeElement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LandscapeElement> landscapeElementList = landscapeElementRepository.findAll();
        assertThat(landscapeElementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
