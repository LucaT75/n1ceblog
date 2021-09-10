package community.n1ce.blog.web.rest;

import static community.n1ce.blog.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import community.n1ce.blog.IntegrationTest;
import community.n1ce.blog.domain.Expert;
import community.n1ce.blog.repository.ExpertRepository;
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
 * Integration tests for the {@link ExpertResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpertResourceIT {

    private static final String DEFAULT_SHORT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_BIO = "BBBBBBBBBB";

    private static final String DEFAULT_EXPERTISE = "AAAAAAAAAA";
    private static final String UPDATED_EXPERTISE = "BBBBBBBBBB";

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;

    private static final Double DEFAULT_REVIEWS = 1D;
    private static final Double UPDATED_REVIEWS = 2D;

    private static final Double DEFAULT_CANDIDATURE_VOTES = 1D;
    private static final Double UPDATED_CANDIDATURE_VOTES = 2D;

    private static final String DEFAULT_CANDIDATURE_END_TIME = "AAAAAAAAAA";
    private static final String UPDATED_CANDIDATURE_END_TIME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CANDIDATURE_STAKED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CANDIDATURE_STAKED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/experts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private MockMvc restExpertMockMvc;

    private Expert expert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expert createEntity() {
        Expert expert = new Expert()
            .shortBio(DEFAULT_SHORT_BIO)
            .expertise(DEFAULT_EXPERTISE)
            .rating(DEFAULT_RATING)
            .reviews(DEFAULT_REVIEWS)
            .candidatureVotes(DEFAULT_CANDIDATURE_VOTES)
            .candidatureEndTime(DEFAULT_CANDIDATURE_END_TIME)
            .candidatureStakedAmount(DEFAULT_CANDIDATURE_STAKED_AMOUNT);
        return expert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expert createUpdatedEntity() {
        Expert expert = new Expert()
            .shortBio(UPDATED_SHORT_BIO)
            .expertise(UPDATED_EXPERTISE)
            .rating(UPDATED_RATING)
            .reviews(UPDATED_REVIEWS)
            .candidatureVotes(UPDATED_CANDIDATURE_VOTES)
            .candidatureEndTime(UPDATED_CANDIDATURE_END_TIME)
            .candidatureStakedAmount(UPDATED_CANDIDATURE_STAKED_AMOUNT);
        return expert;
    }

    @BeforeEach
    public void initTest() {
        expertRepository.deleteAll();
        expert = createEntity();
    }

    @Test
    void createExpert() throws Exception {
        int databaseSizeBeforeCreate = expertRepository.findAll().size();
        // Create the Expert
        restExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expert)))
            .andExpect(status().isCreated());

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeCreate + 1);
        Expert testExpert = expertList.get(expertList.size() - 1);
        assertThat(testExpert.getShortBio()).isEqualTo(DEFAULT_SHORT_BIO);
        assertThat(testExpert.getExpertise()).isEqualTo(DEFAULT_EXPERTISE);
        assertThat(testExpert.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testExpert.getReviews()).isEqualTo(DEFAULT_REVIEWS);
        assertThat(testExpert.getCandidatureVotes()).isEqualTo(DEFAULT_CANDIDATURE_VOTES);
        assertThat(testExpert.getCandidatureEndTime()).isEqualTo(DEFAULT_CANDIDATURE_END_TIME);
        assertThat(testExpert.getCandidatureStakedAmount()).isEqualByComparingTo(DEFAULT_CANDIDATURE_STAKED_AMOUNT);
    }

    @Test
    void createExpertWithExistingId() throws Exception {
        // Create the Expert with an existing ID
        expert.setId("existing_id");

        int databaseSizeBeforeCreate = expertRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expert)))
            .andExpect(status().isBadRequest());

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllExperts() throws Exception {
        // Initialize the database
        expertRepository.save(expert);

        // Get all the expertList
        restExpertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expert.getId())))
            .andExpect(jsonPath("$.[*].shortBio").value(hasItem(DEFAULT_SHORT_BIO)))
            .andExpect(jsonPath("$.[*].expertise").value(hasItem(DEFAULT_EXPERTISE)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].reviews").value(hasItem(DEFAULT_REVIEWS.doubleValue())))
            .andExpect(jsonPath("$.[*].candidatureVotes").value(hasItem(DEFAULT_CANDIDATURE_VOTES.doubleValue())))
            .andExpect(jsonPath("$.[*].candidatureEndTime").value(hasItem(DEFAULT_CANDIDATURE_END_TIME)))
            .andExpect(jsonPath("$.[*].candidatureStakedAmount").value(hasItem(sameNumber(DEFAULT_CANDIDATURE_STAKED_AMOUNT))));
    }

    @Test
    void getExpert() throws Exception {
        // Initialize the database
        expertRepository.save(expert);

        // Get the expert
        restExpertMockMvc
            .perform(get(ENTITY_API_URL_ID, expert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expert.getId()))
            .andExpect(jsonPath("$.shortBio").value(DEFAULT_SHORT_BIO))
            .andExpect(jsonPath("$.expertise").value(DEFAULT_EXPERTISE))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.reviews").value(DEFAULT_REVIEWS.doubleValue()))
            .andExpect(jsonPath("$.candidatureVotes").value(DEFAULT_CANDIDATURE_VOTES.doubleValue()))
            .andExpect(jsonPath("$.candidatureEndTime").value(DEFAULT_CANDIDATURE_END_TIME))
            .andExpect(jsonPath("$.candidatureStakedAmount").value(sameNumber(DEFAULT_CANDIDATURE_STAKED_AMOUNT)));
    }

    @Test
    void getNonExistingExpert() throws Exception {
        // Get the expert
        restExpertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewExpert() throws Exception {
        // Initialize the database
        expertRepository.save(expert);

        int databaseSizeBeforeUpdate = expertRepository.findAll().size();

        // Update the expert
        Expert updatedExpert = expertRepository.findById(expert.getId()).get();
        updatedExpert
            .shortBio(UPDATED_SHORT_BIO)
            .expertise(UPDATED_EXPERTISE)
            .rating(UPDATED_RATING)
            .reviews(UPDATED_REVIEWS)
            .candidatureVotes(UPDATED_CANDIDATURE_VOTES)
            .candidatureEndTime(UPDATED_CANDIDATURE_END_TIME)
            .candidatureStakedAmount(UPDATED_CANDIDATURE_STAKED_AMOUNT);

        restExpertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpert.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExpert))
            )
            .andExpect(status().isOk());

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
        Expert testExpert = expertList.get(expertList.size() - 1);
        assertThat(testExpert.getShortBio()).isEqualTo(UPDATED_SHORT_BIO);
        assertThat(testExpert.getExpertise()).isEqualTo(UPDATED_EXPERTISE);
        assertThat(testExpert.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testExpert.getReviews()).isEqualTo(UPDATED_REVIEWS);
        assertThat(testExpert.getCandidatureVotes()).isEqualTo(UPDATED_CANDIDATURE_VOTES);
        assertThat(testExpert.getCandidatureEndTime()).isEqualTo(UPDATED_CANDIDATURE_END_TIME);
        assertThat(testExpert.getCandidatureStakedAmount()).isEqualTo(UPDATED_CANDIDATURE_STAKED_AMOUNT);
    }

    @Test
    void putNonExistingExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().size();
        expert.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expert.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expert))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().size();
        expert.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expert))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().size();
        expert.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(expert)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateExpertWithPatch() throws Exception {
        // Initialize the database
        expertRepository.save(expert);

        int databaseSizeBeforeUpdate = expertRepository.findAll().size();

        // Update the expert using partial update
        Expert partialUpdatedExpert = new Expert();
        partialUpdatedExpert.setId(expert.getId());

        partialUpdatedExpert.expertise(UPDATED_EXPERTISE).rating(UPDATED_RATING);

        restExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpert.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpert))
            )
            .andExpect(status().isOk());

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
        Expert testExpert = expertList.get(expertList.size() - 1);
        assertThat(testExpert.getShortBio()).isEqualTo(DEFAULT_SHORT_BIO);
        assertThat(testExpert.getExpertise()).isEqualTo(UPDATED_EXPERTISE);
        assertThat(testExpert.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testExpert.getReviews()).isEqualTo(DEFAULT_REVIEWS);
        assertThat(testExpert.getCandidatureVotes()).isEqualTo(DEFAULT_CANDIDATURE_VOTES);
        assertThat(testExpert.getCandidatureEndTime()).isEqualTo(DEFAULT_CANDIDATURE_END_TIME);
        assertThat(testExpert.getCandidatureStakedAmount()).isEqualByComparingTo(DEFAULT_CANDIDATURE_STAKED_AMOUNT);
    }

    @Test
    void fullUpdateExpertWithPatch() throws Exception {
        // Initialize the database
        expertRepository.save(expert);

        int databaseSizeBeforeUpdate = expertRepository.findAll().size();

        // Update the expert using partial update
        Expert partialUpdatedExpert = new Expert();
        partialUpdatedExpert.setId(expert.getId());

        partialUpdatedExpert
            .shortBio(UPDATED_SHORT_BIO)
            .expertise(UPDATED_EXPERTISE)
            .rating(UPDATED_RATING)
            .reviews(UPDATED_REVIEWS)
            .candidatureVotes(UPDATED_CANDIDATURE_VOTES)
            .candidatureEndTime(UPDATED_CANDIDATURE_END_TIME)
            .candidatureStakedAmount(UPDATED_CANDIDATURE_STAKED_AMOUNT);

        restExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpert.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExpert))
            )
            .andExpect(status().isOk());

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
        Expert testExpert = expertList.get(expertList.size() - 1);
        assertThat(testExpert.getShortBio()).isEqualTo(UPDATED_SHORT_BIO);
        assertThat(testExpert.getExpertise()).isEqualTo(UPDATED_EXPERTISE);
        assertThat(testExpert.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testExpert.getReviews()).isEqualTo(UPDATED_REVIEWS);
        assertThat(testExpert.getCandidatureVotes()).isEqualTo(UPDATED_CANDIDATURE_VOTES);
        assertThat(testExpert.getCandidatureEndTime()).isEqualTo(UPDATED_CANDIDATURE_END_TIME);
        assertThat(testExpert.getCandidatureStakedAmount()).isEqualByComparingTo(UPDATED_CANDIDATURE_STAKED_AMOUNT);
    }

    @Test
    void patchNonExistingExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().size();
        expert.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expert.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expert))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().size();
        expert.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expert))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().size();
        expert.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(expert)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteExpert() throws Exception {
        // Initialize the database
        expertRepository.save(expert);

        int databaseSizeBeforeDelete = expertRepository.findAll().size();

        // Delete the expert
        restExpertMockMvc
            .perform(delete(ENTITY_API_URL_ID, expert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Expert> expertList = expertRepository.findAll();
        assertThat(expertList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
