package community.n1ce.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import community.n1ce.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceReview.class);
        ServiceReview serviceReview1 = new ServiceReview();
        serviceReview1.setId("id1");
        ServiceReview serviceReview2 = new ServiceReview();
        serviceReview2.setId(serviceReview1.getId());
        assertThat(serviceReview1).isEqualTo(serviceReview2);
        serviceReview2.setId("id2");
        assertThat(serviceReview1).isNotEqualTo(serviceReview2);
        serviceReview1.setId(null);
        assertThat(serviceReview1).isNotEqualTo(serviceReview2);
    }
}
