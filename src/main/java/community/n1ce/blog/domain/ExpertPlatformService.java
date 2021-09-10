package community.n1ce.blog.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ExpertPlatformService.
 */
@Document(collection = "expert_platform_service")
public class ExpertPlatformService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("expert_id")
    private String expertId;

    @Field("featured_img")
    private String featuredImg;

    @Field("category")
    private String category;

    @Field("starting_price")
    private BigDecimal startingPrice;

    @Field("publishing_time")
    private String publishingTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExpertPlatformService id(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public ExpertPlatformService title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public ExpertPlatformService content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExpertId() {
        return this.expertId;
    }

    public ExpertPlatformService expertId(String expertId) {
        this.expertId = expertId;
        return this;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getFeaturedImg() {
        return this.featuredImg;
    }

    public ExpertPlatformService featuredImg(String featuredImg) {
        this.featuredImg = featuredImg;
        return this;
    }

    public void setFeaturedImg(String featuredImg) {
        this.featuredImg = featuredImg;
    }

    public String getCategory() {
        return this.category;
    }

    public ExpertPlatformService category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getStartingPrice() {
        return this.startingPrice;
    }

    public ExpertPlatformService startingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
        return this;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getPublishingTime() {
        return this.publishingTime;
    }

    public ExpertPlatformService publishingTime(String publishingTime) {
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
        if (!(o instanceof ExpertPlatformService)) {
            return false;
        }
        return id != null && id.equals(((ExpertPlatformService) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpertPlatformService{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", expertId='" + getExpertId() + "'" +
            ", featuredImg='" + getFeaturedImg() + "'" +
            ", category='" + getCategory() + "'" +
            ", startingPrice=" + getStartingPrice() +
            ", publishingTime='" + getPublishingTime() + "'" +
            "}";
    }
}
