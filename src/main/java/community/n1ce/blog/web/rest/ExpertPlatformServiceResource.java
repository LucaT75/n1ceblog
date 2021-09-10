package community.n1ce.blog.web.rest;

import community.n1ce.blog.domain.ExpertPlatformService;
import community.n1ce.blog.repository.ExpertPlatformServiceRepository;
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
 * REST controller for managing {@link community.n1ce.blog.domain.ExpertPlatformService}.
 */
@RestController
@RequestMapping("/api")
public class ExpertPlatformServiceResource {

    private final Logger log = LoggerFactory.getLogger(ExpertPlatformServiceResource.class);

    private static final String ENTITY_NAME = "expertPlatformService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpertPlatformServiceRepository expertPlatformServiceRepository;

    public ExpertPlatformServiceResource(ExpertPlatformServiceRepository expertPlatformServiceRepository) {
        this.expertPlatformServiceRepository = expertPlatformServiceRepository;
    }

    /**
     * {@code POST  /expert-platform-services} : Create a new expertPlatformService.
     *
     * @param expertPlatformService the expertPlatformService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expertPlatformService, or with status {@code 400 (Bad Request)} if the expertPlatformService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expert-platform-services")
    public ResponseEntity<ExpertPlatformService> createExpertPlatformService(@RequestBody ExpertPlatformService expertPlatformService)
        throws URISyntaxException {
        log.debug("REST request to save ExpertPlatformService : {}", expertPlatformService);
        if (expertPlatformService.getId() != null) {
            throw new BadRequestAlertException("A new expertPlatformService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpertPlatformService result = expertPlatformServiceRepository.save(expertPlatformService);
        return ResponseEntity
            .created(new URI("/api/expert-platform-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /expert-platform-services/:id} : Updates an existing expertPlatformService.
     *
     * @param id the id of the expertPlatformService to save.
     * @param expertPlatformService the expertPlatformService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expertPlatformService,
     * or with status {@code 400 (Bad Request)} if the expertPlatformService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expertPlatformService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expert-platform-services/{id}")
    public ResponseEntity<ExpertPlatformService> updateExpertPlatformService(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ExpertPlatformService expertPlatformService
    ) throws URISyntaxException {
        log.debug("REST request to update ExpertPlatformService : {}, {}", id, expertPlatformService);
        if (expertPlatformService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expertPlatformService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expertPlatformServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExpertPlatformService result = expertPlatformServiceRepository.save(expertPlatformService);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expertPlatformService.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /expert-platform-services/:id} : Partial updates given fields of an existing expertPlatformService, field will ignore if it is null
     *
     * @param id the id of the expertPlatformService to save.
     * @param expertPlatformService the expertPlatformService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expertPlatformService,
     * or with status {@code 400 (Bad Request)} if the expertPlatformService is not valid,
     * or with status {@code 404 (Not Found)} if the expertPlatformService is not found,
     * or with status {@code 500 (Internal Server Error)} if the expertPlatformService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/expert-platform-services/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExpertPlatformService> partialUpdateExpertPlatformService(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ExpertPlatformService expertPlatformService
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExpertPlatformService partially : {}, {}", id, expertPlatformService);
        if (expertPlatformService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expertPlatformService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expertPlatformServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpertPlatformService> result = expertPlatformServiceRepository
            .findById(expertPlatformService.getId())
            .map(
                existingExpertPlatformService -> {
                    if (expertPlatformService.getTitle() != null) {
                        existingExpertPlatformService.setTitle(expertPlatformService.getTitle());
                    }
                    if (expertPlatformService.getContent() != null) {
                        existingExpertPlatformService.setContent(expertPlatformService.getContent());
                    }
                    if (expertPlatformService.getExpertId() != null) {
                        existingExpertPlatformService.setExpertId(expertPlatformService.getExpertId());
                    }
                    if (expertPlatformService.getFeaturedImg() != null) {
                        existingExpertPlatformService.setFeaturedImg(expertPlatformService.getFeaturedImg());
                    }
                    if (expertPlatformService.getCategory() != null) {
                        existingExpertPlatformService.setCategory(expertPlatformService.getCategory());
                    }
                    if (expertPlatformService.getStartingPrice() != null) {
                        existingExpertPlatformService.setStartingPrice(expertPlatformService.getStartingPrice());
                    }
                    if (expertPlatformService.getPublishingTime() != null) {
                        existingExpertPlatformService.setPublishingTime(expertPlatformService.getPublishingTime());
                    }

                    return existingExpertPlatformService;
                }
            )
            .map(expertPlatformServiceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expertPlatformService.getId())
        );
    }

    /**
     * {@code GET  /expert-platform-services} : get all the expertPlatformServices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expertPlatformServices in body.
     */
    @GetMapping("/expert-platform-services")
    public ResponseEntity<List<ExpertPlatformService>> getAllExpertPlatformServices(Pageable pageable) {
        log.debug("REST request to get a page of ExpertPlatformServices");
        Page<ExpertPlatformService> page = expertPlatformServiceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /expert-platform-services/:id} : get the "id" expertPlatformService.
     *
     * @param id the id of the expertPlatformService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expertPlatformService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expert-platform-services/{id}")
    public ResponseEntity<ExpertPlatformService> getExpertPlatformService(@PathVariable String id) {
        log.debug("REST request to get ExpertPlatformService : {}", id);
        Optional<ExpertPlatformService> expertPlatformService = expertPlatformServiceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(expertPlatformService);
    }

    /**
     * {@code DELETE  /expert-platform-services/:id} : delete the "id" expertPlatformService.
     *
     * @param id the id of the expertPlatformService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expert-platform-services/{id}")
    public ResponseEntity<Void> deleteExpertPlatformService(@PathVariable String id) {
        log.debug("REST request to delete ExpertPlatformService : {}", id);
        expertPlatformServiceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
