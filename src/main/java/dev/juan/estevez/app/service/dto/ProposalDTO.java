package dev.juan.estevez.app.service.dto;

import dev.juan.estevez.app.domain.enumeration.State;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link dev.juan.estevez.app.domain.Proposal} entity.
 */
@Schema(description = "not an ignored comment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProposalDTO implements Serializable {

    private Long id;

    private String title;

    private String archive;

    private State state;

    private String comments;

    private String student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProposalDTO)) {
            return false;
        }

        ProposalDTO proposalDTO = (ProposalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proposalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProposalDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", archive='" + getArchive() + "'" +
            ", state='" + getState() + "'" +
            ", comments='" + getComments() + "'" +
            ", student='" + getStudent() + "'" +
            "}";
    }
}
