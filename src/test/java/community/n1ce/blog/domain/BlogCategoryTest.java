package community.n1ce.blog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import community.n1ce.blog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BlogCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlogCategory.class);
        BlogCategory blogCategory1 = new BlogCategory();
        blogCategory1.setId("id1");
        BlogCategory blogCategory2 = new BlogCategory();
        blogCategory2.setId(blogCategory1.getId());
        assertThat(blogCategory1).isEqualTo(blogCategory2);
        blogCategory2.setId("id2");
        assertThat(blogCategory1).isNotEqualTo(blogCategory2);
        blogCategory1.setId(null);
        assertThat(blogCategory1).isNotEqualTo(blogCategory2);
    }
}
