package community.n1ce.blog.web.rest;

import community.n1ce.blog.domain.ServiceReview;
import community.n1ce.blog.repository.ServiceReviewRepository;
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
 * REST controller for managing {@link community.n1ce.blog.domain.ServiceReview}.
 */
@RestController
@RequestMapping("/api")
public class ServiceReviewResource {

    private final Logger log = LoggerFactory.getLogger(ServiceReviewResource.class);

    private static final String ENTITY_NAME = "serviceReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceReviewRepository serviceReviewRepository;

    public ServiceReviewResource(ServiceReviewRepository serviceReviewRepository) {
        this.serviceReviewRepository = serviceReviewRepository;
    }

    /**
     * {@code POST  /service-reviews} : Create a new serviceReview.
     *
     * @param serviceReview the serviceReview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceReview, or with status {@code 400 (Bad Request)} if the serviceReview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-reviews")
    public ResponseEntity<ServiceReview> createServiceReview(@RequestBody ServiceReview serviceReview) throws URISyntaxException {
        log.debug("REST request to save ServiceReview : {}", serviceReview);
        if (serviceReview.getId() != null) {
            throw new BadRequestAlertException("A new serviceReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceReview result = serviceReviewRepository.save(serviceReview);
        return ResponseEntity
            .created(new URI("/api/service-reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /service-reviews/:id} : Updates an existing serviceReview.
     *
     * @param id the id of the serviceReview to save.
     * @param serviceReview the serviceReview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceReview,
     * or with status {@code 400 (Bad Request)} if the serviceReview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceReview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-reviews/{id}")
    public ResponseEntity<ServiceReview> updateServiceReview(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ServiceReview serviceReview
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceReview : {}, {}", id, serviceReview);
        if (serviceReview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceReview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServiceReview result = serviceReviewRepository.save(serviceReview);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceReview.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-reviews/:id} : Partial updates given fields of an existing serviceReview, field will ignore if it is null
     *
     * @param id the id of the serviceReview to save.
     * @param serviceReview the serviceReview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceReview,
     * or with status {@code 400 (Bad Request)} if the serviceReview is not valid,
     * or with status {@code 404 (Not Found)} if the serviceReview is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceReview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-reviews/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ServiceReview> partialUpdateServiceReview(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ServiceReview serviceReview
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceReview partially : {}, {}", id, serviceReview);
        if (serviceReview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceReview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceReview> result = serviceReviewRepository
            .findById(serviceReview.getId())
            .map(
                existingServiceReview -> {
                    if (serviceReview.getServiceId() != null) {
                        existingServiceReview.setServiceId(serviceReview.getServiceId());
                    }
                    if (serviceReview.getUserId() != null) {
                        existingServiceReview.setUserId(serviceReview.getUserId());
                    }
                    if (serviceReview.getRating() != null) {
                        existingServiceReview.setRating(serviceReview.getRating());
                    }
                    if (serviceReview.getComment() != null) {
                        existingServiceReview.setComment(serviceReview.getComment());
                    }
                    if (serviceReview.getPublishingTime() != null) {
                        existingServiceReview.setPublishingTime(serviceReview.getPublishingTime());
                    }

                    return existingServiceReview;
                }
            )
            .map(serviceReviewRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceReview.getId())
        );
    }

    /**
     * {@code GET  /service-reviews} : get all the serviceReviews.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceReviews in body.
     */
    @GetMapping("/service-reviews")
    public ResponseEntity<List<ServiceReview>> getAllServiceReviews(Pageable pageable) {
        log.debug("REST request to get a page of ServiceReviews");
        Page<ServiceReview> page = serviceReviewRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-reviews/:id} : get the "id" serviceReview.
     *
     * @param id the id of the serviceReview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceReview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-reviews/{id}")
    public ResponseEntity<ServiceReview> getServiceReview(@PathVariable String id) {
        log.debug("REST request to get ServiceReview : {}", id);
        Optional<ServiceReview> serviceReview = serviceReviewRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(serviceReview);
    }

    /**
     * {@code DELETE  /service-reviews/:id} : delete the "id" serviceReview.
     *
     * @param id the id of the serviceReview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-reviews/{id}")
    public ResponseEntity<Void> deleteServiceReview(@PathVariable String id) {
        log.debug("REST request to delete ServiceReview : {}", id);
        serviceReviewRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
