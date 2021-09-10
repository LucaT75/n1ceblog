package community.n1ce.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import community.n1ce.blog.IntegrationTest;
import community.n1ce.blog.domain.ServiceReview;
import community.n1ce.blog.repository.ServiceReviewRepository;
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
 * Integration tests for the {@link ServiceReviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceReviewResourceIT {

    private static final String DEFAULT_SERVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLISHING_TIME = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHING_TIME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ServiceReviewRepository serviceReviewRepository;

    @Autowired
    private MockMvc restServiceReviewMockMvc;

    private ServiceReview serviceReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceReview createEntity() {
        ServiceReview serviceReview = new ServiceReview()
            .serviceId(DEFAULT_SERVICE_ID)
            .userId(DEFAULT_USER_ID)
            .rating(DEFAULT_RATING)
            .comment(DEFAULT_COMMENT)
            .publishingTime(DEFAULT_PUBLISHING_TIME);
        return serviceReview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceReview createUpdatedEntity() {
        ServiceReview serviceReview = new ServiceReview()
            .serviceId(UPDATED_SERVICE_ID)
            .userId(UPDATED_USER_ID)
            .rating(UPDATED_RATING)
            .comment(UPDATED_COMMENT)
            .publishingTime(UPDATED_PUBLISHING_TIME);
        return serviceReview;
    }

    @BeforeEach
    public void initTest() {
        serviceReviewRepository.deleteAll();
        serviceReview = createEntity();
    }

    @Test
    void createServiceReview() throws Exception {
        int databaseSizeBeforeCreate = serviceReviewRepository.findAll().size();
        // Create the ServiceReview
        restServiceReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceReview)))
            .andExpect(status().isCreated());

        // Validate the ServiceReview in the database
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceReview testServiceReview = serviceReviewList.get(serviceReviewList.size() - 1);
        assertThat(testServiceReview.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testServiceReview.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testServiceReview.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testServiceReview.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testServiceReview.getPublishingTime()).isEqualTo(DEFAULT_PUBLISHING_TIME);
    }

    @Test
    void createServiceReviewWithExistingId() throws Exception {
        // Create the ServiceReview with an existing ID
        serviceReview.setId("existing_id");

        int databaseSizeBeforeCreate = serviceReviewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceReview)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceReview in the database
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllServiceReviews() throws Exception {
        // Initialize the database
        serviceReviewRepository.save(serviceReview);

        // Get all the serviceReviewList
        restServiceReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceReview.getId())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].publishingTime").value(hasItem(DEFAULT_PUBLISHING_TIME)));
    }

    @Test
    void getServiceReview() throws Exception {
        // Initialize the database
        serviceReviewRepository.save(serviceReview);

        // Get the serviceReview
        restServiceReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceReview.getId()))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.publishingTime").value(DEFAULT_PUBLISHING_TIME));
    }

    @Test
    void getNonExistingServiceReview() throws Exception {
        // Get the serviceReview
        restServiceReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewServiceReview() throws Exception {
        // Initialize the database
        serviceReviewRepository.save(serviceReview);

        int databaseSizeBeforeUpdate = serviceReviewRepository.findAll().size();

        // Update the serviceReview
        ServiceReview updatedServiceReview = serviceReviewRepository.findById(serviceReview.getId()).get();
        updatedServiceReview
            .serviceId(UPDATED_SERVICE_ID)
            .userId(UPDATED_USER_ID)
            .rating(UPDATED_RATING)
            .comment(UPDATED_COMMENT)
            .publishingTime(UPDATED_PUBLISHING_TIME);

        restServiceReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceReview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedServiceReview))
            )
            .andExpect(status().isOk());

        // Validate the ServiceReview in the database
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeUpdate);
        ServiceReview testServiceReview = serviceReviewList.get(serviceReviewList.size() - 1);
        assertThat(testServiceReview.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testServiceReview.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testServiceReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testServiceReview.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testServiceReview.getPublishingTime()).isEqualTo(UPDATED_PUBLISHING_TIME);
    }

    @Test
    void putNonExistingServiceReview() throws Exception {
        int databaseSizeBeforeUpdate = serviceReviewRepository.findAll().size();
        serviceReview.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceReview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceReview in the database
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchServiceReview() throws Exception {
        int databaseSizeBeforeUpdate = serviceReviewRepository.findAll().size();
        serviceReview.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceReview in the database
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamServiceReview() throws Exception {
        int databaseSizeBeforeUpdate = serviceReviewRepository.findAll().size();
        serviceReview.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceReview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceReview in the database
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateServiceReviewWithPatch() throws Exception {
        // Initialize the database
        serviceReviewRepository.save(serviceReview);

        int databaseSizeBeforeUpdate = serviceReviewRepository.findAll().size();

        // Update the serviceReview using partial update
        ServiceReview partialUpdatedServiceReview = new ServiceReview();
        partialUpdatedServiceReview.setId(serviceReview.getId());

        partialUpdatedServiceReview
            .serviceId(UPDATED_SERVICE_ID)
            .rating(UPDATED_RATING)
            .comment(UPDATED_COMMENT)
            .publishingTime(UPDATED_PUBLISHING_TIME);

        restServiceReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceReview))
            )
            .andExpect(status().isOk());

        // Validate the ServiceReview in the database
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeUpdate);
        ServiceReview testServiceReview = serviceReviewList.get(serviceReviewList.size() - 1);
        assertThat(testServiceReview.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testServiceReview.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testServiceReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testServiceReview.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testServiceReview.getPublishingTime()).isEqualTo(UPDATED_PUBLISHING_TIME);
    }

    @Test
    void fullUpdateServiceReviewWithPatch() throws Exception {
        // Initialize the database
        serviceReviewRepository.save(serviceReview);

        int databaseSizeBeforeUpdate = serviceReviewRepository.findAll().size();

        // Update the serviceReview using partial update
        ServiceReview partialUpdatedServiceReview = new ServiceReview();
        partialUpdatedServiceReview.setId(serviceReview.getId());

        partialUpdatedServiceReview
            .serviceId(UPDATED_SERVICE_ID)
            .userId(UPDATED_USER_ID)
            .rating(UPDATED_RATING)
            .comment(UPDATED_COMMENT)
            .publishingTime(UPDATED_PUBLISHING_TIME);

        restServiceReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceReview))
            )
            .andExpect(status().isOk());

        // Validate the ServiceReview in the database
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeUpdate);
        ServiceReview testServiceReview = serviceReviewList.get(serviceReviewList.size() - 1);
        assertThat(testServiceReview.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testServiceReview.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testServiceReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testServiceReview.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testServiceReview.getPublishingTime()).isEqualTo(UPDATED_PUBLISHING_TIME);
    }

    @Test
    void patchNonExistingServiceReview() throws Exception {
        int databaseSizeBeforeUpdate = serviceReviewRepository.findAll().size();
        serviceReview.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceReview in the database
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchServiceReview() throws Exception {
        int databaseSizeBeforeUpdate = serviceReviewRepository.findAll().size();
        serviceReview.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceReview in the database
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamServiceReview() throws Exception {
        int databaseSizeBeforeUpdate = serviceReviewRepository.findAll().size();
        serviceReview.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceReviewMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(serviceReview))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceReview in the database
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteServiceReview() throws Exception {
        // Initialize the database
        serviceReviewRepository.save(serviceReview);

        int databaseSizeBeforeDelete = serviceReviewRepository.findAll().size();

        // Delete the serviceReview
        restServiceReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceReview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceReview> serviceReviewList = serviceReviewRepository.findAll();
        assertThat(serviceReviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
