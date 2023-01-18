package com.skincare.bee.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.skincare.bee.domain.Ingridient} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngridientDTO implements Serializable {

    private Long id;

    private String ingridientName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIngridientName() {
        return ingridientName;
    }

    public void setIngridientName(String ingridientName) {
        this.ingridientName = ingridientName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngridientDTO)) {
            return false;
        }

        IngridientDTO ingridientDTO = (IngridientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ingridientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngridientDTO{" +
            "id=" + getId() +
            ", ingridientName='" + getIngridientName() + "'" +
            "}";
    }
}
