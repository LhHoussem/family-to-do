package com.mycompany.familytodo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.familytodo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ToDoListDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToDoListDTO.class);
        ToDoListDTO toDoListDTO1 = new ToDoListDTO();
        toDoListDTO1.setId("id1");
        ToDoListDTO toDoListDTO2 = new ToDoListDTO();
        assertThat(toDoListDTO1).isNotEqualTo(toDoListDTO2);
        toDoListDTO2.setId(toDoListDTO1.getId());
        assertThat(toDoListDTO1).isEqualTo(toDoListDTO2);
        toDoListDTO2.setId("id2");
        assertThat(toDoListDTO1).isNotEqualTo(toDoListDTO2);
        toDoListDTO1.setId(null);
        assertThat(toDoListDTO1).isNotEqualTo(toDoListDTO2);
    }
}
