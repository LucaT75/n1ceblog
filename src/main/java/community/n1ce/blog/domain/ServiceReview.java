package community.n1ce.blog.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ServiceReview.
 */
@Document(collection = "service_review")
public class ServiceReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("service_id")
    private String serviceId;

    @Field("user_id")
    private String userId;

    @Field("rating")
    private Double rating;

    @Field("comment")
    private String comment;

    @Field("publishing_time")
    private String publishingTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ServiceReview id(String id) {
        this.id = id;
        return this;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public ServiceReview serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUserId() {
        return this.userId;
    }

    public ServiceReview userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getRating() {
        return this.rating;
    }

    public ServiceReview rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return this.comment;
    }

    public ServiceReview comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublishingTime() {
        return this.publishingTime;
    }

    public ServiceReview publishingTime(String publishingTime) {
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
        if (!(o instanceof ServiceReview)) {
            return false;
        }
        return id != null && id.equals(((ServiceReview) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceReview{" +
            "id=" + getId() +
            ", serviceId='" + getServiceId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", rating=" + getRating() +
            ", comment='" + getComment() + "'" +
            ", publishingTime='" + getPublishingTime() + "'" +
            "}";
    }
}
