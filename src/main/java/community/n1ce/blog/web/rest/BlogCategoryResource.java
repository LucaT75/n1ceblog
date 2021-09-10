package community.n1ce.blog.web.rest;

import community.n1ce.blog.domain.BlogCategory;
import community.n1ce.blog.repository.BlogCategoryRepository;
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
 * REST controller for managing {@link community.n1ce.blog.domain.BlogCategory}.
 */
@RestController
@RequestMapping("/api")
public class BlogCategoryResource {

    private final Logger log = LoggerFactory.getLogger(BlogCategoryResource.class);

    private static final String ENTITY_NAME = "blogCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlogCategoryRepository blogCategoryRepository;

    public BlogCategoryResource(BlogCategoryRepository blogCategoryRepository) {
        this.blogCategoryRepository = blogCategoryRepository;
    }

    /**
     * {@code POST  /blog-categories} : Create a new blogCategory.
     *
     * @param blogCategory the blogCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blogCategory, or with status {@code 400 (Bad Request)} if the blogCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/blog-categories")
    public ResponseEntity<BlogCategory> createBlogCategory(@RequestBody BlogCategory blogCategory) throws URISyntaxException {
        log.debug("REST request to save BlogCategory : {}", blogCategory);
        if (blogCategory.getId() != null) {
            throw new BadRequestAlertException("A new blogCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlogCategory result = blogCategoryRepository.save(blogCategory);
        return ResponseEntity
            .created(new URI("/api/blog-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /blog-categories/:id} : Updates an existing blogCategory.
     *
     * @param id the id of the blogCategory to save.
     * @param blogCategory the blogCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blogCategory,
     * or with status {@code 400 (Bad Request)} if the blogCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blogCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/blog-categories/{id}")
    public ResponseEntity<BlogCategory> updateBlogCategory(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody BlogCategory blogCategory
    ) throws URISyntaxException {
        log.debug("REST request to update BlogCategory : {}, {}", id, blogCategory);
        if (blogCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blogCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blogCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BlogCategory result = blogCategoryRepository.save(blogCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blogCategory.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /blog-categories/:id} : Partial updates given fields of an existing blogCategory, field will ignore if it is null
     *
     * @param id the id of the blogCategory to save.
     * @param blogCategory the blogCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blogCategory,
     * or with status {@code 400 (Bad Request)} if the blogCategory is not valid,
     * or with status {@code 404 (Not Found)} if the blogCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the blogCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/blog-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BlogCategory> partialUpdateBlogCategory(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody BlogCategory blogCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update BlogCategory partially : {}, {}", id, blogCategory);
        if (blogCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blogCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blogCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BlogCategory> result = blogCategoryRepository
            .findById(blogCategory.getId())
            .map(
                existingBlogCategory -> {
                    if (blogCategory.getTitle() != null) {
                        existingBlogCategory.setTitle(blogCategory.getTitle());
                    }
                    if (blogCategory.getArticles() != null) {
                        existingBlogCategory.setArticles(blogCategory.getArticles());
                    }
                    if (blogCategory.getArtcilesPerRow() != null) {
                        existingBlogCategory.setArtcilesPerRow(blogCategory.getArtcilesPerRow());
                    }

                    return existingBlogCategory;
                }
            )
            .map(blogCategoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blogCategory.getId())
        );
    }

    /**
     * {@code GET  /blog-categories} : get all the blogCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blogCategories in body.
     */
    @GetMapping("/blog-categories")
    public ResponseEntity<List<BlogCategory>> getAllBlogCategories(Pageable pageable) {
        log.debug("REST request to get a page of BlogCategories");
        Page<BlogCategory> page = blogCategoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /blog-categories/:id} : get the "id" blogCategory.
     *
     * @param id the id of the blogCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blogCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/blog-categories/{id}")
    public ResponseEntity<BlogCategory> getBlogCategory(@PathVariable String id) {
        log.debug("REST request to get BlogCategory : {}", id);
        Optional<BlogCategory> blogCategory = blogCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(blogCategory);
    }

    /**
     * {@code DELETE  /blog-categories/:id} : delete the "id" blogCategory.
     *
     * @param id the id of the blogCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/blog-categories/{id}")
    public ResponseEntity<Void> deleteBlogCategory(@PathVariable String id) {
        log.debug("REST request to delete BlogCategory : {}", id);
        blogCategoryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
