package com.skincare.bee.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IngridientMapperTest {

    private IngridientMapper ingridientMapper;

    @BeforeEach
    public void setUp() {
        ingridientMapper = new IngridientMapperImpl();
    }
}
