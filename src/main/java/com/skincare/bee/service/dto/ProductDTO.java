package com.skincare.bee.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.skincare.bee.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    private String productName;

    private LocalDate expiredProduct;

    private LocalDate createdAt;

    private StockDTO stock;

    private CategoryDTO category;

    private IngridientDTO ingridient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDate getExpiredProduct() {
        return expiredProduct;
    }

    public void setExpiredProduct(LocalDate expiredProduct) {
        this.expiredProduct = expiredProduct;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public StockDTO getStock() {
        return stock;
    }

    public void setStock(StockDTO stock) {
        this.stock = stock;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public IngridientDTO getIngridient() {
        return ingridient;
    }

    public void setIngridient(IngridientDTO ingridient) {
        this.ingridient = ingridient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", expiredProduct='" + getExpiredProduct() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", stock=" + getStock() +
            ", category=" + getCategory() +
            ", ingridient=" + getIngridient() +
            "}";
    }
}
