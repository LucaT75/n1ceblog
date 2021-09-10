package community.n1ce.blog.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A BlogPost.
 */
@Document(collection = "blog_post")
public class BlogPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("snippet")
    private String snippet;

    @Field("expert_id")
    private String expertId;

    @Field("featured_img")
    private String featuredImg;

    @Field("category")
    private String category;

    @Field("publishing_time")
    private String publishingTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BlogPost id(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public BlogPost title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public BlogPost content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSnippet() {
        return this.snippet;
    }

    public BlogPost snippet(String snippet) {
        this.snippet = snippet;
        return this;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getExpertId() {
        return this.expertId;
    }

    public BlogPost expertId(String expertId) {
        this.expertId = expertId;
        return this;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getFeaturedImg() {
        return this.featuredImg;
    }

    public BlogPost featuredImg(String featuredImg) {
        this.featuredImg = featuredImg;
        return this;
    }

    public void setFeaturedImg(String featuredImg) {
        this.featuredImg = featuredImg;
    }

    public String getCategory() {
        return this.category;
    }

    public BlogPost category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublishingTime() {
        return this.publishingTime;
    }

    public BlogPost publishingTime(String publishingTime) {
        this.publishingTime = publishingTime;
        return this;
    }

    public void setPublishingTime(String publishingTime) {
        this.publishingTime = publishingTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlogPost)) {
            return false;
        }
        return id != null && id.equals(((BlogPost) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlogPost{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", snippet='" + getSnippet() + "'" +
            ", expertId='" + getExpertId() + "'" +
            ", featuredImg='" + getFeaturedImg() + "'" +
            ", category='" + getCategory() + "'" +
            ", publishingTime='" + getPublishingTime() + "'" +
            "}";
    }
}
