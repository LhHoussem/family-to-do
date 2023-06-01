package com.mycompany.familytodo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfilMapperTest {

    private ProfilMapper profilMapper;

    @BeforeEach
    public void setUp() {
        profilMapper = new ProfilMapperImpl();
    }
}
