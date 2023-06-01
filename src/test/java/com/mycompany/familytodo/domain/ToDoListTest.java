package com.mycompany.familytodo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.familytodo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ToDoListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToDoList.class);
        ToDoList toDoList1 = new ToDoList();
        toDoList1.setId("id1");
        ToDoList toDoList2 = new ToDoList();
        toDoList2.setId(toDoList1.getId());
        assertThat(toDoList1).isEqualTo(toDoList2);
        toDoList2.setId("id2");
        assertThat(toDoList1).isNotEqualTo(toDoList2);
        toDoList1.setId(null);
        assertThat(toDoList1).isNotEqualTo(toDoList2);
    }
}
