package community.n1ce.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import community.n1ce.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpertPlatformServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpertPlatformService.class);
        ExpertPlatformService expertPlatformService1 = new ExpertPlatformService();
        expertPlatformService1.setId("id1");
        ExpertPlatformService expertPlatformService2 = new ExpertPlatformService();
        expertPlatformService2.setId(expertPlatformService1.getId());
        assertThat(expertPlatformService1).isEqualTo(expertPlatformService2);
        expertPlatformService2.setId("id2");
        assertThat(expertPlatformService1).isNotEqualTo(expertPlatformService2);
        expertPlatformService1.setId(null);
        assertThat(expertPlatformService1).isNotEqualTo(expertPlatformService2);
    }
}
