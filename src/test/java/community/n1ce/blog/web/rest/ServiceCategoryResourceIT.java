package community.n1ce.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import community.n1ce.blog.IntegrationTest;
import community.n1ce.blog.domain.ServiceCategory;
import community.n1ce.blog.repository.ServiceCategoryRepository;
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
 * Integration tests for the {@link ServiceCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceCategoryResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICES = "AAAAAAAAAA";
    private static final String UPDATED_SERVICES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    @Autowired
    private MockMvc restServiceCategoryMockMvc;

    private ServiceCategory serviceCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceCategory createEntity() {
        ServiceCategory serviceCategory = new ServiceCategory().title(DEFAULT_TITLE).icon(DEFAULT_ICON).services(DEFAULT_SERVICES);
        return serviceCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceCategory createUpdatedEntity() {
        ServiceCategory serviceCategory = new ServiceCategory().title(UPDATED_TITLE).icon(UPDATED_ICON).services(UPDATED_SERVICES);
        return serviceCategory;
    }

    @BeforeEach
    public void initTest() {
        serviceCategoryRepository.deleteAll();
        serviceCategory = createEntity();
    }

    @Test
    void createServiceCategory() throws Exception {
        int databaseSizeBeforeCreate = serviceCategoryRepository.findAll().size();
        // Create the ServiceCategory
        restServiceCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isCreated());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceCategory testServiceCategory = serviceCategoryList.get(serviceCategoryList.size() - 1);
        assertThat(testServiceCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testServiceCategory.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testServiceCategory.getServices()).isEqualTo(DEFAULT_SERVICES);
    }

    @Test
    void createServiceCategoryWithExistingId() throws Exception {
        // Create the ServiceCategory with an existing ID
        serviceCategory.setId("existing_id");

        int databaseSizeBeforeCreate = serviceCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllServiceCategories() throws Exception {
        // Initialize the database
        serviceCategoryRepository.save(serviceCategory);

        // Get all the serviceCategoryList
        restServiceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceCategory.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].services").value(hasItem(DEFAULT_SERVICES)));
    }

    @Test
    void getServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.save(serviceCategory);

        // Get the serviceCategory
        restServiceCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceCategory.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.services").value(DEFAULT_SERVICES));
    }

    @Test
    void getNonExistingServiceCategory() throws Exception {
        // Get the serviceCategory
        restServiceCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.save(serviceCategory);

        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();

        // Update the serviceCategory
        ServiceCategory updatedServiceCategory = serviceCategoryRepository.findById(serviceCategory.getId()).get();
        updatedServiceCategory.title(UPDATED_TITLE).icon(UPDATED_ICON).services(UPDATED_SERVICES);

        restServiceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedServiceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
        ServiceCategory testServiceCategory = serviceCategoryList.get(serviceCategoryList.size() - 1);
        assertThat(testServiceCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testServiceCategory.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testServiceCategory.getServices()).isEqualTo(UPDATED_SERVICES);
    }

    @Test
    void putNonExistingServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateServiceCategoryWithPatch() throws Exception {
        // Initialize the database
        serviceCategoryRepository.save(serviceCategory);

        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();

        // Update the serviceCategory using partial update
        ServiceCategory partialUpdatedServiceCategory = new ServiceCategory();
        partialUpdatedServiceCategory.setId(serviceCategory.getId());

        partialUpdatedServiceCategory.services(UPDATED_SERVICES);

        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
        ServiceCategory testServiceCategory = serviceCategoryList.get(serviceCategoryList.size() - 1);
        assertThat(testServiceCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testServiceCategory.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testServiceCategory.getServices()).isEqualTo(UPDATED_SERVICES);
    }

    @Test
    void fullUpdateServiceCategoryWithPatch() throws Exception {
        // Initialize the database
        serviceCategoryRepository.save(serviceCategory);

        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();

        // Update the serviceCategory using partial update
        ServiceCategory partialUpdatedServiceCategory = new ServiceCategory();
        partialUpdatedServiceCategory.setId(serviceCategory.getId());

        partialUpdatedServiceCategory.title(UPDATED_TITLE).icon(UPDATED_ICON).services(UPDATED_SERVICES);

        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
        ServiceCategory testServiceCategory = serviceCategoryList.get(serviceCategoryList.size() - 1);
        assertThat(testServiceCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testServiceCategory.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testServiceCategory.getServices()).isEqualTo(UPDATED_SERVICES);
    }

    @Test
    void patchNonExistingServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.save(serviceCategory);

        int databaseSizeBeforeDelete = serviceCategoryRepository.findAll().size();

        // Delete the serviceCategory
        restServiceCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
