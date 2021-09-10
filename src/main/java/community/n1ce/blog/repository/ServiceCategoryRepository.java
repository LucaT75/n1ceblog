package community.n1ce.blog.repository;

import community.n1ce.blog.domain.ServiceCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ServiceCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceCategoryRepository extends MongoRepository<ServiceCategory, String> {}
