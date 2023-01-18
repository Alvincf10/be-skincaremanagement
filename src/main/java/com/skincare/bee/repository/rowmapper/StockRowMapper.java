package com.skincare.bee.repository.rowmapper;

import com.skincare.bee.domain.Stock;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Stock}, with proper type conversions.
 */
@Service
public class StockRowMapper implements BiFunction<Row, String, Stock> {

    private final ColumnConverter converter;

    public StockRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Stock} stored in the database.
     */
    @Override
    public Stock apply(Row row, String prefix) {
        Stock entity = new Stock();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setQuantityStock(converter.fromRow(row, prefix + "_quantity_stock", Integer.class));
        return entity;
    }
}
