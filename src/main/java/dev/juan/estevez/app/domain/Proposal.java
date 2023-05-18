package dev.juan.estevez.app.domain;

import dev.juan.estevez.app.domain.enumeration.State;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import lombok.Getter;
import lombok.Setter;

/**
 * not an ignored comment
 */
@Entity
@Getter
@Setter
@Table(name = "proposal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Proposal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "archive")
    private String archive;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Column(name = "comments")
    private String comments;

    @Column(name = "student")
    private String student;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Proposal id(Long id) {
        this.setId(id);
        return this;
    }

    public Proposal title(String title) {
        this.setTitle(title);
        return this;
    }

    public Proposal archive(String archive) {
        this.setArchive(archive);
        return this;
    }

    public Proposal state(State state) {
        this.setState(state);
        return this;
    }

    public Proposal comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public Proposal student(String student) {
        this.setStudent(student);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proposal)) {
            return false;
        }
        return id != null && id.equals(((Proposal) o).id);
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
        return "Proposal{" +
                "id=" + getId() +
                ", title='" + getTitle() + "'" +
                ", archive='" + getArchive() + "'" +
                ", state='" + getState() + "'" +
                ", comments='" + getComments() + "'" +
                ", student='" + getStudent() + "'" +
                "}";
    }
}
