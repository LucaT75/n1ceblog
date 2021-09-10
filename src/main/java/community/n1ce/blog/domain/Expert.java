package community.n1ce.blog.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Expert.
 */
@Document(collection = "expert")
public class Expert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("short_bio")
    private String shortBio;

    @Field("expertise")
    private String expertise;

    @Field("rating")
    private Double rating;

    @Field("reviews")
    private Double reviews;

    @Field("candidature_votes")
    private Double candidatureVotes;

    @Field("candidature_end_time")
    private String candidatureEndTime;

    @Field("candidature_staked_amount")
    private BigDecimal candidatureStakedAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Expert id(String id) {
        this.id = id;
        return this;
    }

    public String getShortBio() {
        return this.shortBio;
    }

    public Expert shortBio(String shortBio) {
        this.shortBio = shortBio;
        return this;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public String getExpertise() {
        return this.expertise;
    }

    public Expert expertise(String expertise) {
        this.expertise = expertise;
        return this;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public Double getRating() {
        return this.rating;
    }

    public Expert rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getReviews() {
        return this.reviews;
    }

    public Expert reviews(Double reviews) {
        this.reviews = reviews;
        return this;
    }

    public void setReviews(Double reviews) {
        this.reviews = reviews;
    }

    public Double getCandidatureVotes() {
        return this.candidatureVotes;
    }

    public Expert candidatureVotes(Double candidatureVotes) {
        this.candidatureVotes = candidatureVotes;
        return this;
    }

    public void setCandidatureVotes(Double candidatureVotes) {
        this.candidatureVotes = candidatureVotes;
    }

    public String getCandidatureEndTime() {
        return this.candidatureEndTime;
    }

    public Expert candidatureEndTime(String candidatureEndTime) {
        this.candidatureEndTime = candidatureEndTime;
        return this;
    }

    public void setCandidatureEndTime(String candidatureEndTime) {
        this.candidatureEndTime = candidatureEndTime;
    }

    public BigDecimal getCandidatureStakedAmount() {
        return this.candidatureStakedAmount;
    }

    public Expert candidatureStakedAmount(BigDecimal candidatureStakedAmount) {
        this.candidatureStakedAmount = candidatureStakedAmount;
        return this;
    }

    public void setCandidatureStakedAmount(BigDecimal candidatureStakedAmount) {
        this.candidatureStakedAmount = candidatureStakedAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expert)) {
            return false;
        }
        return id != null && id.equals(((Expert) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Expert{" +
            "id=" + getId() +
            ", shortBio='" + getShortBio() + "'" +
            ", expertise='" + getExpertise() + "'" +
            ", rating=" + getRating() +
            ", reviews=" + getReviews() +
            ", candidatureVotes=" + getCandidatureVotes() +
            ", candidatureEndTime='" + getCandidatureEndTime() + "'" +
            ", candidatureStakedAmount=" + getCandidatureStakedAmount() +
            "}";
    }
}
