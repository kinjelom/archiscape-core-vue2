package net.archiscape.app.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import net.archiscape.app.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectViewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectView.class);
        ProjectView projectView1 = new ProjectView();
        projectView1.setId(1L);
        ProjectView projectView2 = new ProjectView();
        projectView2.setId(projectView1.getId());
        assertThat(projectView1).isEqualTo(projectView2);
        projectView2.setId(2L);
        assertThat(projectView1).isNotEqualTo(projectView2);
        projectView1.setId(null);
        assertThat(projectView1).isNotEqualTo(projectView2);
    }
}
