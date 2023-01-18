package com.skincare.bee.service.mapper;

import com.skincare.bee.domain.Stock;
import com.skincare.bee.service.dto.StockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stock} and its DTO {@link StockDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockMapper extends EntityMapper<StockDTO, Stock> {}
