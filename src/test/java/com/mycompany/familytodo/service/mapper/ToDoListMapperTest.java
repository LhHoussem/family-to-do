package com.mycompany.familytodo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToDoListMapperTest {

    private ToDoListMapper toDoListMapper;

    @BeforeEach
    public void setUp() {
        toDoListMapper = new ToDoListMapperImpl();
    }
}
