package community.n1ce.blog.web.rest;

import community.n1ce.blog.domain.BlogPost;
import community.n1ce.blog.repository.BlogPostRepository;
import community.n1ce.blog.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link community.n1ce.blog.domain.BlogPost}.
 */
@RestController
@RequestMapping("/api")
public class BlogPostResource {

    private final Logger log = LoggerFactory.getLogger(BlogPostResource.class);

    private static final String ENTITY_NAME = "blogPost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlogPostRepository blogPostRepository;

    public BlogPostResource(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    /**
     * {@code POST  /blog-posts} : Create a new blogPost.
     *
     * @param blogPost the blogPost to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blogPost, or with status {@code 400 (Bad Request)} if the blogPost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/blog-posts")
    public ResponseEntity<BlogPost> createBlogPost(@RequestBody BlogPost blogPost) throws URISyntaxException {
        log.debug("REST request to save BlogPost : {}", blogPost);
        if (blogPost.getId() != null) {
            throw new BadRequestAlertException("A new blogPost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlogPost result = blogPostRepository.save(blogPost);
        return ResponseEntity
            .created(new URI("/api/blog-posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /blog-posts/:id} : Updates an existing blogPost.
     *
     * @param id the id of the blogPost to save.
     * @param blogPost the blogPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blogPost,
     * or with status {@code 400 (Bad Request)} if the blogPost is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blogPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/blog-posts/{id}")
    public ResponseEntity<BlogPost> updateBlogPost(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody BlogPost blogPost
    ) throws URISyntaxException {
        log.debug("REST request to update BlogPost : {}, {}", id, blogPost);
        if (blogPost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blogPost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blogPostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BlogPost result = blogPostRepository.save(blogPost);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blogPost.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /blog-posts/:id} : Partial updates given fields of an existing blogPost, field will ignore if it is null
     *
     * @param id the id of the blogPost to save.
     * @param blogPost the blogPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blogPost,
     * or with status {@code 400 (Bad Request)} if the blogPost is not valid,
     * or with status {@code 404 (Not Found)} if the blogPost is not found,
     * or with status {@code 500 (Internal Server Error)} if the blogPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/blog-posts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BlogPost> partialUpdateBlogPost(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody BlogPost blogPost
    ) throws URISyntaxException {
        log.debug("REST request to partial update BlogPost partially : {}, {}", id, blogPost);
        if (blogPost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blogPost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blogPostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BlogPost> result = blogPostRepository
            .findById(blogPost.getId())
            .map(
                existingBlogPost -> {
                    if (blogPost.getTitle() != null) {
                        existingBlogPost.setTitle(blogPost.getTitle());
                    }
                    if (blogPost.getContent() != null) {
                        existingBlogPost.setContent(blogPost.getContent());
                    }
                    if (blogPost.getSnippet() != null) {
                        existingBlogPost.setSnippet(blogPost.getSnippet());
                    }
                    if (blogPost.getExpertId() != null) {
                        existingBlogPost.setExpertId(blogPost.getExpertId());
                    }
                    if (blogPost.getFeaturedImg() != null) {
                        existingBlogPost.setFeaturedImg(blogPost.getFeaturedImg());
                    }
                    if (blogPost.getCategory() != null) {
                        existingBlogPost.setCategory(blogPost.getCategory());
                    }
                    if (blogPost.getPublishingTime() != null) {
                        existingBlogPost.setPublishingTime(blogPost.getPublishingTime());
                    }

                    return existingBlogPost;
                }
            )
            .map(blogPostRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blogPost.getId())
        );
    }

    /**
     * {@code GET  /blog-posts} : get all the blogPosts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blogPosts in body.
     */
    @GetMapping("/blog-posts")
    public ResponseEntity<List<BlogPost>> getAllBlogPosts(Pageable pageable) {
        log.debug("REST request to get a page of BlogPosts");
        Page<BlogPost> page = blogPostRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /blog-posts/:id} : get the "id" blogPost.
     *
     * @param id the id of the blogPost to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blogPost, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/blog-posts/{id}")
    public ResponseEntity<BlogPost> getBlogPost(@PathVariable String id) {
        log.debug("REST request to get BlogPost : {}", id);
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(blogPost);
    }

    /**
     * {@code DELETE  /blog-posts/:id} : delete the "id" blogPost.
     *
     * @param id the id of the blogPost to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/blog-posts/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable String id) {
        log.debug("REST request to delete BlogPost : {}", id);
        blogPostRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
