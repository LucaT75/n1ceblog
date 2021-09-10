package community.n1ce.blog.repository;

import community.n1ce.blog.domain.ServiceReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ServiceReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceReviewRepository extends MongoRepository<ServiceReview, String> {}
