package community.n1ce.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import community.n1ce.blog.IntegrationTest;
import community.n1ce.blog.domain.BlogCategory;
import community.n1ce.blog.repository.BlogCategoryRepository;
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
 * Integration tests for the {@link BlogCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BlogCategoryResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ARTICLES = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLES = "BBBBBBBBBB";

    private static final Integer DEFAULT_ARTCILES_PER_ROW = 1;
    private static final Integer UPDATED_ARTCILES_PER_ROW = 2;

    private static final String ENTITY_API_URL = "/api/blog-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    @Autowired
    private MockMvc restBlogCategoryMockMvc;

    private BlogCategory blogCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlogCategory createEntity() {
        BlogCategory blogCategory = new BlogCategory()
            .title(DEFAULT_TITLE)
            .articles(DEFAULT_ARTICLES)
            .artcilesPerRow(DEFAULT_ARTCILES_PER_ROW);
        return blogCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlogCategory createUpdatedEntity() {
        BlogCategory blogCategory = new BlogCategory()
            .title(UPDATED_TITLE)
            .articles(UPDATED_ARTICLES)
            .artcilesPerRow(UPDATED_ARTCILES_PER_ROW);
        return blogCategory;
    }

    @BeforeEach
    public void initTest() {
        blogCategoryRepository.deleteAll();
        blogCategory = createEntity();
    }

    @Test
    void createBlogCategory() throws Exception {
        int databaseSizeBeforeCreate = blogCategoryRepository.findAll().size();
        // Create the BlogCategory
        restBlogCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blogCategory)))
            .andExpect(status().isCreated());

        // Validate the BlogCategory in the database
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        BlogCategory testBlogCategory = blogCategoryList.get(blogCategoryList.size() - 1);
        assertThat(testBlogCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBlogCategory.getArticles()).isEqualTo(DEFAULT_ARTICLES);
        assertThat(testBlogCategory.getArtcilesPerRow()).isEqualTo(DEFAULT_ARTCILES_PER_ROW);
    }

    @Test
    void createBlogCategoryWithExistingId() throws Exception {
        // Create the BlogCategory with an existing ID
        blogCategory.setId("existing_id");

        int databaseSizeBeforeCreate = blogCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlogCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blogCategory)))
            .andExpect(status().isBadRequest());

        // Validate the BlogCategory in the database
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBlogCategories() throws Exception {
        // Initialize the database
        blogCategoryRepository.save(blogCategory);

        // Get all the blogCategoryList
        restBlogCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blogCategory.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].articles").value(hasItem(DEFAULT_ARTICLES)))
            .andExpect(jsonPath("$.[*].artcilesPerRow").value(hasItem(DEFAULT_ARTCILES_PER_ROW)));
    }

    @Test
    void getBlogCategory() throws Exception {
        // Initialize the database
        blogCategoryRepository.save(blogCategory);

        // Get the blogCategory
        restBlogCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, blogCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(blogCategory.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.articles").value(DEFAULT_ARTICLES))
            .andExpect(jsonPath("$.artcilesPerRow").value(DEFAULT_ARTCILES_PER_ROW));
    }

    @Test
    void getNonExistingBlogCategory() throws Exception {
        // Get the blogCategory
        restBlogCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewBlogCategory() throws Exception {
        // Initialize the database
        blogCategoryRepository.save(blogCategory);

        int databaseSizeBeforeUpdate = blogCategoryRepository.findAll().size();

        // Update the blogCategory
        BlogCategory updatedBlogCategory = blogCategoryRepository.findById(blogCategory.getId()).get();
        updatedBlogCategory.title(UPDATED_TITLE).articles(UPDATED_ARTICLES).artcilesPerRow(UPDATED_ARTCILES_PER_ROW);

        restBlogCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBlogCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBlogCategory))
            )
            .andExpect(status().isOk());

        // Validate the BlogCategory in the database
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeUpdate);
        BlogCategory testBlogCategory = blogCategoryList.get(blogCategoryList.size() - 1);
        assertThat(testBlogCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlogCategory.getArticles()).isEqualTo(UPDATED_ARTICLES);
        assertThat(testBlogCategory.getArtcilesPerRow()).isEqualTo(UPDATED_ARTCILES_PER_ROW);
    }

    @Test
    void putNonExistingBlogCategory() throws Exception {
        int databaseSizeBeforeUpdate = blogCategoryRepository.findAll().size();
        blogCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blogCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blogCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogCategory in the database
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBlogCategory() throws Exception {
        int databaseSizeBeforeUpdate = blogCategoryRepository.findAll().size();
        blogCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blogCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogCategory in the database
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBlogCategory() throws Exception {
        int databaseSizeBeforeUpdate = blogCategoryRepository.findAll().size();
        blogCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blogCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlogCategory in the database
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBlogCategoryWithPatch() throws Exception {
        // Initialize the database
        blogCategoryRepository.save(blogCategory);

        int databaseSizeBeforeUpdate = blogCategoryRepository.findAll().size();

        // Update the blogCategory using partial update
        BlogCategory partialUpdatedBlogCategory = new BlogCategory();
        partialUpdatedBlogCategory.setId(blogCategory.getId());

        partialUpdatedBlogCategory.artcilesPerRow(UPDATED_ARTCILES_PER_ROW);

        restBlogCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlogCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlogCategory))
            )
            .andExpect(status().isOk());

        // Validate the BlogCategory in the database
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeUpdate);
        BlogCategory testBlogCategory = blogCategoryList.get(blogCategoryList.size() - 1);
        assertThat(testBlogCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBlogCategory.getArticles()).isEqualTo(DEFAULT_ARTICLES);
        assertThat(testBlogCategory.getArtcilesPerRow()).isEqualTo(UPDATED_ARTCILES_PER_ROW);
    }

    @Test
    void fullUpdateBlogCategoryWithPatch() throws Exception {
        // Initialize the database
        blogCategoryRepository.save(blogCategory);

        int databaseSizeBeforeUpdate = blogCategoryRepository.findAll().size();

        // Update the blogCategory using partial update
        BlogCategory partialUpdatedBlogCategory = new BlogCategory();
        partialUpdatedBlogCategory.setId(blogCategory.getId());

        partialUpdatedBlogCategory.title(UPDATED_TITLE).articles(UPDATED_ARTICLES).artcilesPerRow(UPDATED_ARTCILES_PER_ROW);

        restBlogCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlogCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlogCategory))
            )
            .andExpect(status().isOk());

        // Validate the BlogCategory in the database
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeUpdate);
        BlogCategory testBlogCategory = blogCategoryList.get(blogCategoryList.size() - 1);
        assertThat(testBlogCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlogCategory.getArticles()).isEqualTo(UPDATED_ARTICLES);
        assertThat(testBlogCategory.getArtcilesPerRow()).isEqualTo(UPDATED_ARTCILES_PER_ROW);
    }

    @Test
    void patchNonExistingBlogCategory() throws Exception {
        int databaseSizeBeforeUpdate = blogCategoryRepository.findAll().size();
        blogCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, blogCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blogCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogCategory in the database
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBlogCategory() throws Exception {
        int databaseSizeBeforeUpdate = blogCategoryRepository.findAll().size();
        blogCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blogCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogCategory in the database
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBlogCategory() throws Exception {
        int databaseSizeBeforeUpdate = blogCategoryRepository.findAll().size();
        blogCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(blogCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlogCategory in the database
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBlogCategory() throws Exception {
        // Initialize the database
        blogCategoryRepository.save(blogCategory);

        int databaseSizeBeforeDelete = blogCategoryRepository.findAll().size();

        // Delete the blogCategory
        restBlogCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, blogCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BlogCategory> blogCategoryList = blogCategoryRepository.findAll();
        assertThat(blogCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
