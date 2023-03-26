package com.konstantion.config;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import static java.util.Map.Entry;

@Configuration
public class FlywayConfiguration {
    @Bean
    @Qualifier("flywayProps")
    public Properties flywayProperties() {
        Properties propsPath = new Properties();
        Properties propsBytes = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("flyway.properties")) {
            propsPath.load(input);
            Set<Entry<Object, Object>> entries = propsPath.entrySet();
            for(Entry<Object, Object> entry : entries) {
                String entryKey = entry.getKey().toString();
                String entryValuePath = entry.getValue().toString();
                String encodedString = "";
                byte[] entryBytes;
                try {
                    entryBytes = IOUtils.toByteArray(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(entryValuePath)));
                    encodedString = DatatypeConverter.printHexBinary(entryBytes);
                } catch (IOException e) {
                    entryBytes = new byte[0];
                }
                propsBytes.put(entryKey, encodedString);
            }

        } catch (IOException e) {

        }
        return propsBytes;
    }
}
