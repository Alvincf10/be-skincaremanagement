package com.skincare.bee.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.skincare.bee.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngridientDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngridientDTO.class);
        IngridientDTO ingridientDTO1 = new IngridientDTO();
        ingridientDTO1.setId(1L);
        IngridientDTO ingridientDTO2 = new IngridientDTO();
        assertThat(ingridientDTO1).isNotEqualTo(ingridientDTO2);
        ingridientDTO2.setId(ingridientDTO1.getId());
        assertThat(ingridientDTO1).isEqualTo(ingridientDTO2);
        ingridientDTO2.setId(2L);
        assertThat(ingridientDTO1).isNotEqualTo(ingridientDTO2);
        ingridientDTO1.setId(null);
        assertThat(ingridientDTO1).isNotEqualTo(ingridientDTO2);
    }
}
