package com.mycompany.familytodo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.familytodo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfilDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfilDTO.class);
        ProfilDTO profilDTO1 = new ProfilDTO();
        profilDTO1.setId("id1");
        ProfilDTO profilDTO2 = new ProfilDTO();
        assertThat(profilDTO1).isNotEqualTo(profilDTO2);
        profilDTO2.setId(profilDTO1.getId());
        assertThat(profilDTO1).isEqualTo(profilDTO2);
        profilDTO2.setId("id2");
        assertThat(profilDTO1).isNotEqualTo(profilDTO2);
        profilDTO1.setId(null);
        assertThat(profilDTO1).isNotEqualTo(profilDTO2);
    }
}
