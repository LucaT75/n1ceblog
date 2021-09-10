package community.n1ce.blog.web.rest;

import static community.n1ce.blog.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import community.n1ce.blog.IntegrationTest;
import community.n1ce.blog.domain.ExpertPlatformService;
import community.n1ce.blog.repository.ExpertPlatformServiceRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link ExpertPlatformServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpertPlatformServiceResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_EXPERT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXPERT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FEATURED_IMG = "AAAAAAAAAA";
    private static final String UPDATED_FEATURED_IMG = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_STARTING_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_STARTING_PRICE = new BigDecimal(2);

    private static final String DEFAULT_PUBLISHING_TIME = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHING_TIME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/expert-platform-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ExpertPlatformServiceRepository expertPlatformServiceRepository;

    @Autowired
    private MockMvc restExpertPlatformServiceMockMvc;

    private ExpertPlatformService expertPlatformService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpertPlatformService createEntity() {
        ExpertPlatformService expertPlatformService = new ExpertPlatformService()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .expertId(DEFAULT_EXPERT_ID)
            .featuredImg(DEFAULT_FEATURED_IMG)
            .category(DEFAULT_CATEGORY)
            .startingPrice(DEFAULT_STARTING_PRICE)
            .publishingTime(DEFAULT_PUBLISHING_TIME);
        return expertPlatformService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpertPlatformService createUpdatedEntity() {
        ExpertPlatformService expertPlatformService = new ExpertPlatformService()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .expertId(UPDATED_EXPERT_ID)
            .featuredImg(UPDATED_FEATURED_IMG)
            .category(UPDATED_CATEGORY)
            .startingPrice(UPDATED_STARTING_PRICE)
            .publishingTime(UPDATED_PUBLISHING_TIME);
        return expertPlatformService;
    }

    @BeforeEach
    public void initTest() {
        expertPlatformServiceRepository.deleteAll();
        expertPlatformService = createEntity();
    }

    @Test
    void createExpertPlatformService() throws Exception {
        int databaseSizeBeforeCreate = expertPlatformServiceRepository.findAll().size();
        // Create the ExpertPlatformService
        restExpertPlatformServiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expertPlatformService))
            )
            .andExpect(status().isCreated());

        // Validate the ExpertPlatformService in the database
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeCreate + 1);
        ExpertPlatformService testExpertPlatformService = expertPlatformServiceList.get(expertPlatformServiceList.size() - 1);
        assertThat(testExpertPlatformService.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testExpertPlatformService.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testExpertPlatformService.getExpertId()).isEqualTo(DEFAULT_EXPERT_ID);
        assertThat(testExpertPlatformService.getFeaturedImg()).isEqualTo(DEFAULT_FEATURED_IMG);
        assertThat(testExpertPlatformService.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testExpertPlatformService.getStartingPrice()).isEqualByComparingTo(DEFAULT_STARTING_PRICE);
        assertThat(testExpertPlatformService.getPublishingTime()).isEqualTo(DEFAULT_PUBLISHING_TIME);
    }

    @Test
    void createExpertPlatformServiceWithExistingId() throws Exception {
        // Create the ExpertPlatformService with an existing ID
        expertPlatformService.setId("existing_id");

        int databaseSizeBeforeCreate = expertPlatformServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpertPlatformServiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expertPlatformService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpertPlatformService in the database
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllExpertPlatformServices() throws Exception {
        // Initialize the database
        expertPlatformServiceRepository.save(expertPlatformService);

        // Get all the expertPlatformServiceList
        restExpertPlatformServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expertPlatformService.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].expertId").value(hasItem(DEFAULT_EXPERT_ID)))
            .andExpect(jsonPath("$.[*].featuredImg").value(hasItem(DEFAULT_FEATURED_IMG)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].startingPrice").value(hasItem(sameNumber(DEFAULT_STARTING_PRICE))))
            .andExpect(jsonPath("$.[*].publishingTime").value(hasItem(DEFAULT_PUBLISHING_TIME)));
    }

    @Test
    void getExpertPlatformService() throws Exception {
        // Initialize the database
        expertPlatformServiceRepository.save(expertPlatformService);

        // Get the expertPlatformService
        restExpertPlatformServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, expertPlatformService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expertPlatformService.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.expertId").value(DEFAULT_EXPERT_ID))
            .andExpect(jsonPath("$.featuredImg").value(DEFAULT_FEATURED_IMG))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.startingPrice").value(sameNumber(DEFAULT_STARTING_PRICE)))
            .andExpect(jsonPath("$.publishingTime").value(DEFAULT_PUBLISHING_TIME));
    }

    @Test
    void getNonExistingExpertPlatformService() throws Exception {
        // Get the expertPlatformService
        restExpertPlatformServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewExpertPlatformService() throws Exception {
        // Initialize the database
        expertPlatformServiceRepository.save(expertPlatformService);

        int databaseSizeBeforeUpdate = expertPlatformServiceRepository.findAll().size();

        // Update the expertPlatformService
        ExpertPlatformService updatedExpertPlatformService = expertPlatformServiceRepository.findById(expertPlatformService.getId()).get();
        updatedExpertPlatformService
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .expertId(UPDATED_EXPERT_ID)
            .featuredImg(UPDATED_FEATURED_IMG)
            .category(UPDATED_CATEGORY)
            .startingPrice(UPDATED_STARTING_PRICE)
            .publishingTime(UPDATED_PUBLISHING_TIME);

        restExpertPlatformServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpertPlatformService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExpertPlatformService))
            )
            .andExpect(status().isOk());

        // Validate the ExpertPlatformService in the database
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeUpdate);
        ExpertPlatformService testExpertPlatformService = expertPlatformServiceList.get(expertPlatformServiceList.size() - 1);
        assertThat(testExpertPlatformService.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testExpertPlatformService.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testExpertPlatformService.getExpertId()).isEqualTo(UPDATED_EXPERT_ID);
        assertThat(testExpertPlatformService.getFeaturedImg()).isEqualTo(UPDATED_FEATURED_IMG);
        assertThat(testExpertPlatformService.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testExpertPlatformService.getStartingPrice()).isEqualTo(UPDATED_STARTING_PRICE);
        assertThat(testExpertPlatformService.getPublishingTime()).isEqualTo(UPDATED_PUBLISHING_TIME);
    }

    @Test
    void putNonExistingExpertPlatformService() throws Exception {
        int databaseSizeBeforeUpdate = expertPlatformServiceRepository.findAll().size();
        expertPlatformService.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpertPlatformServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expertPlatformService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expertPlatformService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpertPlatformService in the database
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchExpertPlatformService() throws Exception {
        int databaseSizeBeforeUpdate = expertPlatformServiceRepository.findAll().size();
        expertPlatformService.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertPlatformServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expertPlatformService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpertPlatformService in the database
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamExpertPlatformService() throws Exception {
        int databaseSizeBeforeUpdate = expertPlatformServiceRepository.findAll().size();
        expertPlatformService.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertPlatformServiceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expertPlatformService))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpertPlatformService in the database
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateExpertPlatformServiceWithPatch() throws Exception {
        // Initialize the database
        expertPlatformServiceRepository.save(expertPlatformService);

        int databaseSizeBeforeUpdate = expertPlatformServiceRepository.findAll().size();

        // Update the expertPlatformService using partial update
        ExpertPlatformService partialUpdatedExpertPlatformService = new ExpertPlatformService();
        partialUpdatedExpertPlatformService.setId(expertPlatformService.getId());

        partialUpdatedExpertPlatformService.startingPrice(UPDATED_STARTING_PRICE);

        restExpertPlatformServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpertPlatformService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpertPlatformService))
            )
            .andExpect(status().isOk());

        // Validate the ExpertPlatformService in the database
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeUpdate);
        ExpertPlatformService testExpertPlatformService = expertPlatformServiceList.get(expertPlatformServiceList.size() - 1);
        assertThat(testExpertPlatformService.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testExpertPlatformService.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testExpertPlatformService.getExpertId()).isEqualTo(DEFAULT_EXPERT_ID);
        assertThat(testExpertPlatformService.getFeaturedImg()).isEqualTo(DEFAULT_FEATURED_IMG);
        assertThat(testExpertPlatformService.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testExpertPlatformService.getStartingPrice()).isEqualByComparingTo(UPDATED_STARTING_PRICE);
        assertThat(testExpertPlatformService.getPublishingTime()).isEqualTo(DEFAULT_PUBLISHING_TIME);
    }

    @Test
    void fullUpdateExpertPlatformServiceWithPatch() throws Exception {
        // Initialize the database
        expertPlatformServiceRepository.save(expertPlatformService);

        int databaseSizeBeforeUpdate = expertPlatformServiceRepository.findAll().size();

        // Update the expertPlatformService using partial update
        ExpertPlatformService partialUpdatedExpertPlatformService = new ExpertPlatformService();
        partialUpdatedExpertPlatformService.setId(expertPlatformService.getId());

        partialUpdatedExpertPlatformService
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .expertId(UPDATED_EXPERT_ID)
            .featuredImg(UPDATED_FEATURED_IMG)
            .category(UPDATED_CATEGORY)
            .startingPrice(UPDATED_STARTING_PRICE)
            .publishingTime(UPDATED_PUBLISHING_TIME);

        restExpertPlatformServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpertPlatformService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpertPlatformService))
            )
            .andExpect(status().isOk());

        // Validate the ExpertPlatformService in the database
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeUpdate);
        ExpertPlatformService testExpertPlatformService = expertPlatformServiceList.get(expertPlatformServiceList.size() - 1);
        assertThat(testExpertPlatformService.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testExpertPlatformService.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testExpertPlatformService.getExpertId()).isEqualTo(UPDATED_EXPERT_ID);
        assertThat(testExpertPlatformService.getFeaturedImg()).isEqualTo(UPDATED_FEATURED_IMG);
        assertThat(testExpertPlatformService.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testExpertPlatformService.getStartingPrice()).isEqualByComparingTo(UPDATED_STARTING_PRICE);
        assertThat(testExpertPlatformService.getPublishingTime()).isEqualTo(UPDATED_PUBLISHING_TIME);
    }

    @Test
    void patchNonExistingExpertPlatformService() throws Exception {
        int databaseSizeBeforeUpdate = expertPlatformServiceRepository.findAll().size();
        expertPlatformService.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpertPlatformServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expertPlatformService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expertPlatformService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpertPlatformService in the database
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchExpertPlatformService() throws Exception {
        int databaseSizeBeforeUpdate = expertPlatformServiceRepository.findAll().size();
        expertPlatformService.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertPlatformServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expertPlatformService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpertPlatformService in the database
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamExpertPlatformService() throws Exception {
        int databaseSizeBeforeUpdate = expertPlatformServiceRepository.findAll().size();
        expertPlatformService.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertPlatformServiceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expertPlatformService))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpertPlatformService in the database
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteExpertPlatformService() throws Exception {
        // Initialize the database
        expertPlatformServiceRepository.save(expertPlatformService);

        int databaseSizeBeforeDelete = expertPlatformServiceRepository.findAll().size();

        // Delete the expertPlatformService
        restExpertPlatformServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, expertPlatformService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpertPlatformService> expertPlatformServiceList = expertPlatformServiceRepository.findAll();
        assertThat(expertPlatformServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
