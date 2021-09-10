package community.n1ce.blog.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A BlogCategory.
 */
@Document(collection = "blog_category")
public class BlogCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("articles")
    private String articles;

    @Field("artciles_per_row")
    private Integer artcilesPerRow;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BlogCategory id(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public BlogCategory title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticles() {
        return this.articles;
    }

    public BlogCategory articles(String articles) {
        this.articles = articles;
        return this;
    }

    public void setArticles(String articles) {
        this.articles = articles;
    }

    public Integer getArtcilesPerRow() {
        return this.artcilesPerRow;
    }

    public BlogCategory artcilesPerRow(Integer artcilesPerRow) {
        this.artcilesPerRow = artcilesPerRow;
        return this;
    }

    public void setArtcilesPerRow(Integer artcilesPerRow) {
        this.artcilesPerRow = artcilesPerRow;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlogCategory)) {
            return false;
        }
        return id != null && id.equals(((BlogCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlogCategory{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", articles='" + getArticles() + "'" +
            ", artcilesPerRow=" + getArtcilesPerRow() +
            "}";
    }
}
