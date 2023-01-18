package com.skincare.bee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Product.
 */
@Table("product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("product_name")
    private String productName;

    @Column("expired_product")
    private LocalDate expiredProduct;

    @Column("created_at")
    private LocalDate createdAt;

    @Transient
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Stock stock;

    @Transient
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Category category;

    @Transient
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Ingridient ingridient;

    @Column("stock_id")
    private Long stockId;

    @Column("category_id")
    private Long categoryId;

    @Column("ingridient_id")
    private Long ingridientId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return this.productName;
    }

    public Product productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDate getExpiredProduct() {
        return this.expiredProduct;
    }

    public Product expiredProduct(LocalDate expiredProduct) {
        this.setExpiredProduct(expiredProduct);
        return this;
    }

    public void setExpiredProduct(LocalDate expiredProduct) {
        this.expiredProduct = expiredProduct;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Product createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Stock getStock() {
        return this.stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
        this.stockId = stock != null ? stock.getId() : null;
    }

    public Product stock(Stock stock) {
        this.setStock(stock);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.categoryId = category != null ? category.getId() : null;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Ingridient getIngridient() {
        return this.ingridient;
    }

    public void setIngridient(Ingridient ingridient) {
        this.ingridient = ingridient;
        this.ingridientId = ingridient != null ? ingridient.getId() : null;
    }

    public Product ingridient(Ingridient ingridient) {
        this.setIngridient(ingridient);
        return this;
    }

    public Long getStockId() {
        return this.stockId;
    }

    public void setStockId(Long stock) {
        this.stockId = stock;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long category) {
        this.categoryId = category;
    }

    public Long getIngridientId() {
        return this.ingridientId;
    }

    public void setIngridientId(Long ingridient) {
        this.ingridientId = ingridient;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", expiredProduct='" + getExpiredProduct() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
