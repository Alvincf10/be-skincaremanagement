package com.skincare.bee.repository.rowmapper;

import com.skincare.bee.domain.Ingridient;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Ingridient}, with proper type conversions.
 */
@Service
public class IngridientRowMapper implements BiFunction<Row, String, Ingridient> {

    private final ColumnConverter converter;

    public IngridientRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Ingridient} stored in the database.
     */
    @Override
    public Ingridient apply(Row row, String prefix) {
        Ingridient entity = new Ingridient();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIngridientName(converter.fromRow(row, prefix + "_ingridient_name", String.class));
        return entity;
    }
}
