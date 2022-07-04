package net.archiscape.app.core.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import net.archiscape.app.core.IntegrationTest;
import net.archiscape.app.core.domain.Project;
import net.archiscape.app.core.domain.ProjectElement;
import net.archiscape.app.core.domain.ProjectView;
import net.archiscape.app.core.domain.enumeration.C4ElementType;
import net.archiscape.app.core.repository.ProjectElementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProjectElementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjectElementResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTATION = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTATION = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNOLOGY = "AAAAAAAAAA";
    private static final String UPDATED_TECHNOLOGY = "BBBBBBBBBB";

    private static final C4ElementType DEFAULT_C_4_TYPE = C4ElementType.PERSON;
    private static final C4ElementType UPDATED_C_4_TYPE = C4ElementType.SOFTWARE_SYSTEM;

    private static final String DEFAULT_LANDSCAPE_ELEMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_LANDSCAPE_ELEMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EXT_ELEMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXT_ELEMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EXT_SOURCE_ELEMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXT_SOURCE_ELEMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EXT_TARGET_ELEMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXT_TARGET_ELEMENT_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/project-elements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectElementRepository projectElementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectElementMockMvc;

    private ProjectElement projectElement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectElement createEntity(EntityManager em) {
        ProjectElement projectElement = new ProjectElement()
            .name(DEFAULT_NAME)
            .documentation(DEFAULT_DOCUMENTATION)
            .technology(DEFAULT_TECHNOLOGY)
            .c4type(DEFAULT_C_4_TYPE)
            .landscapeElementId(DEFAULT_LANDSCAPE_ELEMENT_ID)
            .extElementId(DEFAULT_EXT_ELEMENT_ID)
            .extSourceElementId(DEFAULT_EXT_SOURCE_ELEMENT_ID)
            .extTargetElementId(DEFAULT_EXT_TARGET_ELEMENT_ID);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        projectElement.setProject(project);
        // Add required entity
        ProjectView projectView;
        if (TestUtil.findAll(em, ProjectView.class).isEmpty()) {
            projectView = ProjectViewResourceIT.createEntity(em);
            em.persist(projectView);
            em.flush();
        } else {
            projectView = TestUtil.findAll(em, ProjectView.class).get(0);
        }
        projectElement.getProjectViews().add(projectView);
        return projectElement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectElement createUpdatedEntity(EntityManager em) {
        ProjectElement projectElement = new ProjectElement()
            .name(UPDATED_NAME)
            .documentation(UPDATED_DOCUMENTATION)
            .technology(UPDATED_TECHNOLOGY)
            .c4type(UPDATED_C_4_TYPE)
            .landscapeElementId(UPDATED_LANDSCAPE_ELEMENT_ID)
            .extElementId(UPDATED_EXT_ELEMENT_ID)
            .extSourceElementId(UPDATED_EXT_SOURCE_ELEMENT_ID)
            .extTargetElementId(UPDATED_EXT_TARGET_ELEMENT_ID);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createUpdatedEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        projectElement.setProject(project);
        // Add required entity
        ProjectView projectView;
        if (TestUtil.findAll(em, ProjectView.class).isEmpty()) {
            projectView = ProjectViewResourceIT.createUpdatedEntity(em);
            em.persist(projectView);
            em.flush();
        } else {
            projectView = TestUtil.findAll(em, ProjectView.class).get(0);
        }
        projectElement.getProjectViews().add(projectView);
        return projectElement;
    }

    @BeforeEach
    public void initTest() {
        projectElement = createEntity(em);
    }

    @Test
    @Transactional
    void createProjectElement() throws Exception {
        int databaseSizeBeforeCreate = projectElementRepository.findAll().size();
        // Create the ProjectElement
        restProjectElementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectElement))
            )
            .andExpect(status().isCreated());

        // Validate the ProjectElement in the database
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectElement testProjectElement = projectElementList.get(projectElementList.size() - 1);
        assertThat(testProjectElement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjectElement.getDocumentation()).isEqualTo(DEFAULT_DOCUMENTATION);
        assertThat(testProjectElement.getTechnology()).isEqualTo(DEFAULT_TECHNOLOGY);
        assertThat(testProjectElement.getc4type()).isEqualTo(DEFAULT_C_4_TYPE);
        assertThat(testProjectElement.getLandscapeElementId()).isEqualTo(DEFAULT_LANDSCAPE_ELEMENT_ID);
        assertThat(testProjectElement.getExtElementId()).isEqualTo(DEFAULT_EXT_ELEMENT_ID);
        assertThat(testProjectElement.getExtSourceElementId()).isEqualTo(DEFAULT_EXT_SOURCE_ELEMENT_ID);
        assertThat(testProjectElement.getExtTargetElementId()).isEqualTo(DEFAULT_EXT_TARGET_ELEMENT_ID);
    }

    @Test
    @Transactional
    void createProjectElementWithExistingId() throws Exception {
        // Create the ProjectElement with an existing ID
        projectElement.setId(1L);

        int databaseSizeBeforeCreate = projectElementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectElementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectElement in the database
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkc4typeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectElementRepository.findAll().size();
        // set the field null
        projectElement.setc4type(null);

        // Create the ProjectElement, which fails.

        restProjectElementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectElement))
            )
            .andExpect(status().isBadRequest());

        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjectElements() throws Exception {
        // Initialize the database
        projectElementRepository.saveAndFlush(projectElement);

        // Get all the projectElementList
        restProjectElementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectElement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].documentation").value(hasItem(DEFAULT_DOCUMENTATION)))
            .andExpect(jsonPath("$.[*].technology").value(hasItem(DEFAULT_TECHNOLOGY)))
            .andExpect(jsonPath("$.[*].c4type").value(hasItem(DEFAULT_C_4_TYPE.toString())))
            .andExpect(jsonPath("$.[*].landscapeElementId").value(hasItem(DEFAULT_LANDSCAPE_ELEMENT_ID)))
            .andExpect(jsonPath("$.[*].extElementId").value(hasItem(DEFAULT_EXT_ELEMENT_ID)))
            .andExpect(jsonPath("$.[*].extSourceElementId").value(hasItem(DEFAULT_EXT_SOURCE_ELEMENT_ID)))
            .andExpect(jsonPath("$.[*].extTargetElementId").value(hasItem(DEFAULT_EXT_TARGET_ELEMENT_ID)));
    }

    @Test
    @Transactional
    void getProjectElement() throws Exception {
        // Initialize the database
        projectElementRepository.saveAndFlush(projectElement);

        // Get the projectElement
        restProjectElementMockMvc
            .perform(get(ENTITY_API_URL_ID, projectElement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projectElement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.documentation").value(DEFAULT_DOCUMENTATION))
            .andExpect(jsonPath("$.technology").value(DEFAULT_TECHNOLOGY))
            .andExpect(jsonPath("$.c4type").value(DEFAULT_C_4_TYPE.toString()))
            .andExpect(jsonPath("$.landscapeElementId").value(DEFAULT_LANDSCAPE_ELEMENT_ID))
            .andExpect(jsonPath("$.extElementId").value(DEFAULT_EXT_ELEMENT_ID))
            .andExpect(jsonPath("$.extSourceElementId").value(DEFAULT_EXT_SOURCE_ELEMENT_ID))
            .andExpect(jsonPath("$.extTargetElementId").value(DEFAULT_EXT_TARGET_ELEMENT_ID));
    }

    @Test
    @Transactional
    void getNonExistingProjectElement() throws Exception {
        // Get the projectElement
        restProjectElementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProjectElement() throws Exception {
        // Initialize the database
        projectElementRepository.saveAndFlush(projectElement);

        int databaseSizeBeforeUpdate = projectElementRepository.findAll().size();

        // Update the projectElement
        ProjectElement updatedProjectElement = projectElementRepository.findById(projectElement.getId()).get();
        // Disconnect from session so that the updates on updatedProjectElement are not directly saved in db
        em.detach(updatedProjectElement);
        updatedProjectElement
            .name(UPDATED_NAME)
            .documentation(UPDATED_DOCUMENTATION)
            .technology(UPDATED_TECHNOLOGY)
            .c4type(UPDATED_C_4_TYPE)
            .landscapeElementId(UPDATED_LANDSCAPE_ELEMENT_ID)
            .extElementId(UPDATED_EXT_ELEMENT_ID)
            .extSourceElementId(UPDATED_EXT_SOURCE_ELEMENT_ID)
            .extTargetElementId(UPDATED_EXT_TARGET_ELEMENT_ID);

        restProjectElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProjectElement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProjectElement))
            )
            .andExpect(status().isOk());

        // Validate the ProjectElement in the database
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeUpdate);
        ProjectElement testProjectElement = projectElementList.get(projectElementList.size() - 1);
        assertThat(testProjectElement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectElement.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
        assertThat(testProjectElement.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testProjectElement.getc4type()).isEqualTo(UPDATED_C_4_TYPE);
        assertThat(testProjectElement.getLandscapeElementId()).isEqualTo(UPDATED_LANDSCAPE_ELEMENT_ID);
        assertThat(testProjectElement.getExtElementId()).isEqualTo(UPDATED_EXT_ELEMENT_ID);
        assertThat(testProjectElement.getExtSourceElementId()).isEqualTo(UPDATED_EXT_SOURCE_ELEMENT_ID);
        assertThat(testProjectElement.getExtTargetElementId()).isEqualTo(UPDATED_EXT_TARGET_ELEMENT_ID);
    }

    @Test
    @Transactional
    void putNonExistingProjectElement() throws Exception {
        int databaseSizeBeforeUpdate = projectElementRepository.findAll().size();
        projectElement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectElement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectElement in the database
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjectElement() throws Exception {
        int databaseSizeBeforeUpdate = projectElementRepository.findAll().size();
        projectElement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectElementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectElement in the database
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjectElement() throws Exception {
        int databaseSizeBeforeUpdate = projectElementRepository.findAll().size();
        projectElement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectElementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectElement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectElement in the database
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectElementWithPatch() throws Exception {
        // Initialize the database
        projectElementRepository.saveAndFlush(projectElement);

        int databaseSizeBeforeUpdate = projectElementRepository.findAll().size();

        // Update the projectElement using partial update
        ProjectElement partialUpdatedProjectElement = new ProjectElement();
        partialUpdatedProjectElement.setId(projectElement.getId());

        partialUpdatedProjectElement
            .name(UPDATED_NAME)
            .documentation(UPDATED_DOCUMENTATION)
            .c4type(UPDATED_C_4_TYPE)
            .landscapeElementId(UPDATED_LANDSCAPE_ELEMENT_ID)
            .extSourceElementId(UPDATED_EXT_SOURCE_ELEMENT_ID)
            .extTargetElementId(UPDATED_EXT_TARGET_ELEMENT_ID);

        restProjectElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectElement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectElement))
            )
            .andExpect(status().isOk());

        // Validate the ProjectElement in the database
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeUpdate);
        ProjectElement testProjectElement = projectElementList.get(projectElementList.size() - 1);
        assertThat(testProjectElement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectElement.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
        assertThat(testProjectElement.getTechnology()).isEqualTo(DEFAULT_TECHNOLOGY);
        assertThat(testProjectElement.getc4type()).isEqualTo(UPDATED_C_4_TYPE);
        assertThat(testProjectElement.getLandscapeElementId()).isEqualTo(UPDATED_LANDSCAPE_ELEMENT_ID);
        assertThat(testProjectElement.getExtElementId()).isEqualTo(DEFAULT_EXT_ELEMENT_ID);
        assertThat(testProjectElement.getExtSourceElementId()).isEqualTo(UPDATED_EXT_SOURCE_ELEMENT_ID);
        assertThat(testProjectElement.getExtTargetElementId()).isEqualTo(UPDATED_EXT_TARGET_ELEMENT_ID);
    }

    @Test
    @Transactional
    void fullUpdateProjectElementWithPatch() throws Exception {
        // Initialize the database
        projectElementRepository.saveAndFlush(projectElement);

        int databaseSizeBeforeUpdate = projectElementRepository.findAll().size();

        // Update the projectElement using partial update
        ProjectElement partialUpdatedProjectElement = new ProjectElement();
        partialUpdatedProjectElement.setId(projectElement.getId());

        partialUpdatedProjectElement
            .name(UPDATED_NAME)
            .documentation(UPDATED_DOCUMENTATION)
            .technology(UPDATED_TECHNOLOGY)
            .c4type(UPDATED_C_4_TYPE)
            .landscapeElementId(UPDATED_LANDSCAPE_ELEMENT_ID)
            .extElementId(UPDATED_EXT_ELEMENT_ID)
            .extSourceElementId(UPDATED_EXT_SOURCE_ELEMENT_ID)
            .extTargetElementId(UPDATED_EXT_TARGET_ELEMENT_ID);

        restProjectElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectElement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectElement))
            )
            .andExpect(status().isOk());

        // Validate the ProjectElement in the database
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeUpdate);
        ProjectElement testProjectElement = projectElementList.get(projectElementList.size() - 1);
        assertThat(testProjectElement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectElement.getDocumentation()).isEqualTo(UPDATED_DOCUMENTATION);
        assertThat(testProjectElement.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testProjectElement.getc4type()).isEqualTo(UPDATED_C_4_TYPE);
        assertThat(testProjectElement.getLandscapeElementId()).isEqualTo(UPDATED_LANDSCAPE_ELEMENT_ID);
        assertThat(testProjectElement.getExtElementId()).isEqualTo(UPDATED_EXT_ELEMENT_ID);
        assertThat(testProjectElement.getExtSourceElementId()).isEqualTo(UPDATED_EXT_SOURCE_ELEMENT_ID);
        assertThat(testProjectElement.getExtTargetElementId()).isEqualTo(UPDATED_EXT_TARGET_ELEMENT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingProjectElement() throws Exception {
        int databaseSizeBeforeUpdate = projectElementRepository.findAll().size();
        projectElement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectElement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectElement in the database
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjectElement() throws Exception {
        int databaseSizeBeforeUpdate = projectElementRepository.findAll().size();
        projectElement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectElementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectElement))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectElement in the database
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjectElement() throws Exception {
        int databaseSizeBeforeUpdate = projectElementRepository.findAll().size();
        projectElement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectElementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(projectElement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectElement in the database
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjectElement() throws Exception {
        // Initialize the database
        projectElementRepository.saveAndFlush(projectElement);

        int databaseSizeBeforeDelete = projectElementRepository.findAll().size();

        // Delete the projectElement
        restProjectElementMockMvc
            .perform(delete(ENTITY_API_URL_ID, projectElement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProjectElement> projectElementList = projectElementRepository.findAll();
        assertThat(projectElementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
