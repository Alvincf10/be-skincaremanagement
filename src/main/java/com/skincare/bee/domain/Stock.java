package com.skincare.bee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Stock.
 */
@Table("stock")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("quantity_stock")
    private Integer quantityStock;

    @Transient
    @JsonIgnoreProperties(value = { "stock", "category", "ingridient" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Stock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantityStock() {
        return this.quantityStock;
    }

    public Stock quantityStock(Integer quantityStock) {
        this.setQuantityStock(quantityStock);
        return this;
    }

    public void setQuantityStock(Integer quantityStock) {
        this.quantityStock = quantityStock;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setStock(null));
        }
        if (products != null) {
            products.forEach(i -> i.setStock(this));
        }
        this.products = products;
    }

    public Stock products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Stock addProduct(Product product) {
        this.products.add(product);
        product.setStock(this);
        return this;
    }

    public Stock removeProduct(Product product) {
        this.products.remove(product);
        product.setStock(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock)) {
            return false;
        }
        return id != null && id.equals(((Stock) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", quantityStock=" + getQuantityStock() +
            "}";
    }
}
