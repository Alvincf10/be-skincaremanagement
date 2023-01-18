package com.skincare.bee.service.mapper;

import com.skincare.bee.domain.Category;
import com.skincare.bee.domain.Ingridient;
import com.skincare.bee.domain.Product;
import com.skincare.bee.domain.Stock;
import com.skincare.bee.service.dto.CategoryDTO;
import com.skincare.bee.service.dto.IngridientDTO;
import com.skincare.bee.service.dto.ProductDTO;
import com.skincare.bee.service.dto.StockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "stock", source = "stock", qualifiedByName = "stockId")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    @Mapping(target = "ingridient", source = "ingridient", qualifiedByName = "ingridientId")
    ProductDTO toDto(Product s);

    @Named("stockId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StockDTO toDtoStockId(Stock stock);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("ingridientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IngridientDTO toDtoIngridientId(Ingridient ingridient);
}
