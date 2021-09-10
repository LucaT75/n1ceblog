package community.n1ce.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import community.n1ce.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceCategory.class);
        ServiceCategory serviceCategory1 = new ServiceCategory();
        serviceCategory1.setId("id1");
        ServiceCategory serviceCategory2 = new ServiceCategory();
        serviceCategory2.setId(serviceCategory1.getId());
        assertThat(serviceCategory1).isEqualTo(serviceCategory2);
        serviceCategory2.setId("id2");
        assertThat(serviceCategory1).isNotEqualTo(serviceCategory2);
        serviceCategory1.setId(null);
        assertThat(serviceCategory1).isNotEqualTo(serviceCategory2);
    }
}
