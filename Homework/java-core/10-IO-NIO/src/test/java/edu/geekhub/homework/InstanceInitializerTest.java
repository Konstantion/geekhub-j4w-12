package edu.geekhub.homework;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InstanceInitializerTest {
    @Test
    void process_shouldInitializeAllFields_whenInstantInitialize() {
        InstanceInitializer instanceInitializer = new InstanceInitializer();

        assertThat(instanceInitializer).hasNoNullFieldsOrProperties();
    }
}