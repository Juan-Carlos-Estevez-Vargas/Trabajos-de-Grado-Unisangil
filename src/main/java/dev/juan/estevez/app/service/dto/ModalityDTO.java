package dev.juan.estevez.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link dev.juan.estevez.app.domain.Modality} entity.
 */
@Schema(description = "Task entity.\n@author The JHipster team.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModalityDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String document;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModalityDTO)) {
            return false;
        }

        ModalityDTO modalityDTO = (ModalityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, modalityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModalityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", document='" + getDocument() + "'" +
            "}";
    }
}
