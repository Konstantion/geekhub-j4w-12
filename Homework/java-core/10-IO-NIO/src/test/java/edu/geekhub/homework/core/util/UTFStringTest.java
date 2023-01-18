package edu.geekhub.homework.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UTFStringTest {
    @Test
    void process_shouldReturnUTFString_whenGivenWINDOWS1251() {
        String greeting = UTFString.of("Привіт");
        String charset = UTFString.charset("Привіт");
        System.out.println(charset);
        System.out.println(greeting);
    }

    @Test
    void process_shouldReturnUTFString_whenGivenUTF() {
        String greeting = UTFString.of("Привіт");
        String charset = UTFString.charset("Привіт");
        System.out.println(charset);
        System.out.println(greeting);

        String greeting2 = UTFString.of(greeting);
        String charset2 = UTFString.charset(charset);
        System.out.println(charset2);
        System.out.println(greeting2);
    }
}