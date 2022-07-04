package net.archiscape.app.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import net.archiscape.app.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ElementConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElementConfiguration.class);
        ElementConfiguration elementConfiguration1 = new ElementConfiguration();
        elementConfiguration1.setId("id1");
        ElementConfiguration elementConfiguration2 = new ElementConfiguration();
        elementConfiguration2.setId(elementConfiguration1.getId());
        assertThat(elementConfiguration1).isEqualTo(elementConfiguration2);
        elementConfiguration2.setId("id2");
        assertThat(elementConfiguration1).isNotEqualTo(elementConfiguration2);
        elementConfiguration1.setId(null);
        assertThat(elementConfiguration1).isNotEqualTo(elementConfiguration2);
    }
}
