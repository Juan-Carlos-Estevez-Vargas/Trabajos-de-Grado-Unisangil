package dev.juan.estevez.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import lombok.Getter;
import lombok.Setter;

/**
 * Task entity.\n@author The JHipster team.
 */
@Entity
@Getter
@Setter
@Table(name = "modality")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Modality implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "document")
    private String document;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Modality id(Long id) {
        this.setId(id);
        return this;
    }

    public Modality name(String name) {
        this.setName(name);
        return this;
    }

    public Modality description(String description) {
        this.setDescription(description);
        return this;
    }

    public Modality document(String document) {
        this.setDocument(document);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Modality)) {
            return false;
        }
        return id != null && id.equals(((Modality) o).id);
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Modality{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                ", document='" + getDocument() + "'" +
                "}";
    }
}
