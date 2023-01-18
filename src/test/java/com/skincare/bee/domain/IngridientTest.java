package com.skincare.bee.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.skincare.bee.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngridientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ingridient.class);
        Ingridient ingridient1 = new Ingridient();
        ingridient1.setId(1L);
        Ingridient ingridient2 = new Ingridient();
        ingridient2.setId(ingridient1.getId());
        assertThat(ingridient1).isEqualTo(ingridient2);
        ingridient2.setId(2L);
        assertThat(ingridient1).isNotEqualTo(ingridient2);
        ingridient1.setId(null);
        assertThat(ingridient1).isNotEqualTo(ingridient2);
    }
}
