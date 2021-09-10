package community.n1ce.blog.repository;

import community.n1ce.blog.domain.ExpertPlatformService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ExpertPlatformService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpertPlatformServiceRepository extends MongoRepository<ExpertPlatformService, String> {}
