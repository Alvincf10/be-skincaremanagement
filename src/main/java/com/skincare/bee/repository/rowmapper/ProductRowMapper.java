package com.skincare.bee.repository.rowmapper;

import com.skincare.bee.domain.Product;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Product}, with proper type conversions.
 */
@Service
public class ProductRowMapper implements BiFunction<Row, String, Product> {

    private final ColumnConverter converter;

    public ProductRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Product} stored in the database.
     */
    @Override
    public Product apply(Row row, String prefix) {
        Product entity = new Product();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setProductName(converter.fromRow(row, prefix + "_product_name", String.class));
        entity.setExpiredProduct(converter.fromRow(row, prefix + "_expired_product", LocalDate.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", LocalDate.class));
        entity.setStockId(converter.fromRow(row, prefix + "_stock_id", Long.class));
        entity.setCategoryId(converter.fromRow(row, prefix + "_category_id", Long.class));
        entity.setIngridientId(converter.fromRow(row, prefix + "_ingridient_id", Long.class));
        return entity;
    }
}
