package community.n1ce.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import community.n1ce.blog.IntegrationTest;
import community.n1ce.blog.domain.BlogPost;
import community.n1ce.blog.repository.BlogPostRepository;
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
 * Integration tests for the {@link BlogPostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BlogPostResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_SNIPPET = "AAAAAAAAAA";
    private static final String UPDATED_SNIPPET = "BBBBBBBBBB";

    private static final String DEFAULT_EXPERT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXPERT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FEATURED_IMG = "AAAAAAAAAA";
    private static final String UPDATED_FEATURED_IMG = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLISHING_TIME = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHING_TIME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/blog-posts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private MockMvc restBlogPostMockMvc;

    private BlogPost blogPost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlogPost createEntity() {
        BlogPost blogPost = new BlogPost()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .snippet(DEFAULT_SNIPPET)
            .expertId(DEFAULT_EXPERT_ID)
            .featuredImg(DEFAULT_FEATURED_IMG)
            .category(DEFAULT_CATEGORY)
            .publishingTime(DEFAULT_PUBLISHING_TIME);
        return blogPost;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlogPost createUpdatedEntity() {
        BlogPost blogPost = new BlogPost()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .snippet(UPDATED_SNIPPET)
            .expertId(UPDATED_EXPERT_ID)
            .featuredImg(UPDATED_FEATURED_IMG)
            .category(UPDATED_CATEGORY)
            .publishingTime(UPDATED_PUBLISHING_TIME);
        return blogPost;
    }

    @BeforeEach
    public void initTest() {
        blogPostRepository.deleteAll();
        blogPost = createEntity();
    }

    @Test
    void createBlogPost() throws Exception {
        int databaseSizeBeforeCreate = blogPostRepository.findAll().size();
        // Create the BlogPost
        restBlogPostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blogPost)))
            .andExpect(status().isCreated());

        // Validate the BlogPost in the database
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeCreate + 1);
        BlogPost testBlogPost = blogPostList.get(blogPostList.size() - 1);
        assertThat(testBlogPost.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBlogPost.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBlogPost.getSnippet()).isEqualTo(DEFAULT_SNIPPET);
        assertThat(testBlogPost.getExpertId()).isEqualTo(DEFAULT_EXPERT_ID);
        assertThat(testBlogPost.getFeaturedImg()).isEqualTo(DEFAULT_FEATURED_IMG);
        assertThat(testBlogPost.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testBlogPost.getPublishingTime()).isEqualTo(DEFAULT_PUBLISHING_TIME);
    }

    @Test
    void createBlogPostWithExistingId() throws Exception {
        // Create the BlogPost with an existing ID
        blogPost.setId("existing_id");

        int databaseSizeBeforeCreate = blogPostRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlogPostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blogPost)))
            .andExpect(status().isBadRequest());

        // Validate the BlogPost in the database
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBlogPosts() throws Exception {
        // Initialize the database
        blogPostRepository.save(blogPost);

        // Get all the blogPostList
        restBlogPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blogPost.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].snippet").value(hasItem(DEFAULT_SNIPPET)))
            .andExpect(jsonPath("$.[*].expertId").value(hasItem(DEFAULT_EXPERT_ID)))
            .andExpect(jsonPath("$.[*].featuredImg").value(hasItem(DEFAULT_FEATURED_IMG)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].publishingTime").value(hasItem(DEFAULT_PUBLISHING_TIME)));
    }

    @Test
    void getBlogPost() throws Exception {
        // Initialize the database
        blogPostRepository.save(blogPost);

        // Get the blogPost
        restBlogPostMockMvc
            .perform(get(ENTITY_API_URL_ID, blogPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(blogPost.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.snippet").value(DEFAULT_SNIPPET))
            .andExpect(jsonPath("$.expertId").value(DEFAULT_EXPERT_ID))
            .andExpect(jsonPath("$.featuredImg").value(DEFAULT_FEATURED_IMG))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.publishingTime").value(DEFAULT_PUBLISHING_TIME));
    }

    @Test
    void getNonExistingBlogPost() throws Exception {
        // Get the blogPost
        restBlogPostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewBlogPost() throws Exception {
        // Initialize the database
        blogPostRepository.save(blogPost);

        int databaseSizeBeforeUpdate = blogPostRepository.findAll().size();

        // Update the blogPost
        BlogPost updatedBlogPost = blogPostRepository.findById(blogPost.getId()).get();
        updatedBlogPost
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .snippet(UPDATED_SNIPPET)
            .expertId(UPDATED_EXPERT_ID)
            .featuredImg(UPDATED_FEATURED_IMG)
            .category(UPDATED_CATEGORY)
            .publishingTime(UPDATED_PUBLISHING_TIME);

        restBlogPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBlogPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBlogPost))
            )
            .andExpect(status().isOk());

        // Validate the BlogPost in the database
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeUpdate);
        BlogPost testBlogPost = blogPostList.get(blogPostList.size() - 1);
        assertThat(testBlogPost.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlogPost.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBlogPost.getSnippet()).isEqualTo(UPDATED_SNIPPET);
        assertThat(testBlogPost.getExpertId()).isEqualTo(UPDATED_EXPERT_ID);
        assertThat(testBlogPost.getFeaturedImg()).isEqualTo(UPDATED_FEATURED_IMG);
        assertThat(testBlogPost.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testBlogPost.getPublishingTime()).isEqualTo(UPDATED_PUBLISHING_TIME);
    }

    @Test
    void putNonExistingBlogPost() throws Exception {
        int databaseSizeBeforeUpdate = blogPostRepository.findAll().size();
        blogPost.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blogPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blogPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogPost in the database
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBlogPost() throws Exception {
        int databaseSizeBeforeUpdate = blogPostRepository.findAll().size();
        blogPost.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(blogPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogPost in the database
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBlogPost() throws Exception {
        int databaseSizeBeforeUpdate = blogPostRepository.findAll().size();
        blogPost.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(blogPost)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlogPost in the database
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBlogPostWithPatch() throws Exception {
        // Initialize the database
        blogPostRepository.save(blogPost);

        int databaseSizeBeforeUpdate = blogPostRepository.findAll().size();

        // Update the blogPost using partial update
        BlogPost partialUpdatedBlogPost = new BlogPost();
        partialUpdatedBlogPost.setId(blogPost.getId());

        partialUpdatedBlogPost.title(UPDATED_TITLE).content(UPDATED_CONTENT).expertId(UPDATED_EXPERT_ID).category(UPDATED_CATEGORY);

        restBlogPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlogPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlogPost))
            )
            .andExpect(status().isOk());

        // Validate the BlogPost in the database
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeUpdate);
        BlogPost testBlogPost = blogPostList.get(blogPostList.size() - 1);
        assertThat(testBlogPost.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlogPost.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBlogPost.getSnippet()).isEqualTo(DEFAULT_SNIPPET);
        assertThat(testBlogPost.getExpertId()).isEqualTo(UPDATED_EXPERT_ID);
        assertThat(testBlogPost.getFeaturedImg()).isEqualTo(DEFAULT_FEATURED_IMG);
        assertThat(testBlogPost.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testBlogPost.getPublishingTime()).isEqualTo(DEFAULT_PUBLISHING_TIME);
    }

    @Test
    void fullUpdateBlogPostWithPatch() throws Exception {
        // Initialize the database
        blogPostRepository.save(blogPost);

        int databaseSizeBeforeUpdate = blogPostRepository.findAll().size();

        // Update the blogPost using partial update
        BlogPost partialUpdatedBlogPost = new BlogPost();
        partialUpdatedBlogPost.setId(blogPost.getId());

        partialUpdatedBlogPost
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .snippet(UPDATED_SNIPPET)
            .expertId(UPDATED_EXPERT_ID)
            .featuredImg(UPDATED_FEATURED_IMG)
            .category(UPDATED_CATEGORY)
            .publishingTime(UPDATED_PUBLISHING_TIME);

        restBlogPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlogPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlogPost))
            )
            .andExpect(status().isOk());

        // Validate the BlogPost in the database
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeUpdate);
        BlogPost testBlogPost = blogPostList.get(blogPostList.size() - 1);
        assertThat(testBlogPost.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlogPost.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBlogPost.getSnippet()).isEqualTo(UPDATED_SNIPPET);
        assertThat(testBlogPost.getExpertId()).isEqualTo(UPDATED_EXPERT_ID);
        assertThat(testBlogPost.getFeaturedImg()).isEqualTo(UPDATED_FEATURED_IMG);
        assertThat(testBlogPost.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testBlogPost.getPublishingTime()).isEqualTo(UPDATED_PUBLISHING_TIME);
    }

    @Test
    void patchNonExistingBlogPost() throws Exception {
        int databaseSizeBeforeUpdate = blogPostRepository.findAll().size();
        blogPost.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, blogPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blogPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogPost in the database
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBlogPost() throws Exception {
        int databaseSizeBeforeUpdate = blogPostRepository.findAll().size();
        blogPost.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(blogPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogPost in the database
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBlogPost() throws Exception {
        int databaseSizeBeforeUpdate = blogPostRepository.findAll().size();
        blogPost.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(blogPost)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlogPost in the database
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBlogPost() throws Exception {
        // Initialize the database
        blogPostRepository.save(blogPost);

        int databaseSizeBeforeDelete = blogPostRepository.findAll().size();

        // Delete the blogPost
        restBlogPostMockMvc
            .perform(delete(ENTITY_API_URL_ID, blogPost.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        assertThat(blogPostList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
