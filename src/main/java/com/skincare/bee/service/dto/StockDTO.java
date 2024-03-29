package com.skincare.bee.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.skincare.bee.domain.Stock} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockDTO implements Serializable {

    private Long id;

    private Integer quantityStock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantityStock() {
        return quantityStock;
    }

    public void setQuantityStock(Integer quantityStock) {
        this.quantityStock = quantityStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockDTO)) {
            return false;
        }

        StockDTO stockDTO = (StockDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockDTO{" +
            "id=" + getId() +
            ", quantityStock=" + getQuantityStock() +
            "}";
    }
}
