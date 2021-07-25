package com.edu.samples;

import com.edu.samples.messagelog.MessageLog;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

@QuarkusTest
public class MessageLogTest {

    @Inject
    MessageLog messageLog;

    @Test
    @Transactional
    void testMessageUniqueness() {
        UUID uuid = UUID.randomUUID();

        messageLog.processed(uuid.toString());
        Assertions.assertTrue(messageLog.alreadyProcessed(uuid.toString()));
    }

    @Test
    void testEventDeserialization() {

    }
}
