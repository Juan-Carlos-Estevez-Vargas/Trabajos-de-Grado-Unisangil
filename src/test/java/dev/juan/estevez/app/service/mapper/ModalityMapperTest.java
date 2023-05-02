package dev.juan.estevez.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModalityMapperTest {

    private ModalityMapper modalityMapper;

    @BeforeEach
    public void setUp() {
        modalityMapper = new ModalityMapperImpl();
    }
}
