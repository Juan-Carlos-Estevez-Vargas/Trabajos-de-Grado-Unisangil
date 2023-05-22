package dev.juan.estevez.app.domain;

import dev.juan.estevez.app.domain.enumeration.State;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Entity
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

    public Long getId() {
        return this.id;
    }

    public Proposal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Proposal title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArchive() {
        return this.archive;
    }

    public Proposal archive(String archive) {
        this.setArchive(archive);
        return this;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public State getState() {
        return this.state;
    }

    public Proposal state(State state) {
        this.setState(state);
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getComments() {
        return this.comments;
    }

    public Proposal comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStudent() {
        return this.student;
    }

    public Proposal student(String student) {
        this.setStudent(student);
        return this;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
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
