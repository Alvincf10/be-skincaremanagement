package com.skincare.bee.service.mapper;

import com.skincare.bee.domain.Ingridient;
import com.skincare.bee.service.dto.IngridientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ingridient} and its DTO {@link IngridientDTO}.
 */
@Mapper(componentModel = "spring")
public interface IngridientMapper extends EntityMapper<IngridientDTO, Ingridient> {}
