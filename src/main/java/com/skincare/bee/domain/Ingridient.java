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
 * A Ingridient.
 */
@Table("ingridient")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ingridient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("ingridient_name")
    private String ingridientName;

    @Transient
    @JsonIgnoreProperties(value = { "stock", "category", "ingridient" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ingridient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIngridientName() {
        return this.ingridientName;
    }

    public Ingridient ingridientName(String ingridientName) {
        this.setIngridientName(ingridientName);
        return this;
    }

    public void setIngridientName(String ingridientName) {
        this.ingridientName = ingridientName;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setIngridient(null));
        }
        if (products != null) {
            products.forEach(i -> i.setIngridient(this));
        }
        this.products = products;
    }

    public Ingridient products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Ingridient addProduct(Product product) {
        this.products.add(product);
        product.setIngridient(this);
        return this;
    }

    public Ingridient removeProduct(Product product) {
        this.products.remove(product);
        product.setIngridient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ingridient)) {
            return false;
        }
        return id != null && id.equals(((Ingridient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ingridient{" +
            "id=" + getId() +
            ", ingridientName='" + getIngridientName() + "'" +
            "}";
    }
}
