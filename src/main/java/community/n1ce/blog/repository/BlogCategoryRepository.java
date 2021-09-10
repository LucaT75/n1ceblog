package community.n1ce.blog.repository;

import community.n1ce.blog.domain.BlogCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the BlogCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlogCategoryRepository extends MongoRepository<BlogCategory, String> {}
