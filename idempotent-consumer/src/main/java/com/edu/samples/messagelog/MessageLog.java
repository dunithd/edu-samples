package com.edu.samples.messagelog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class MessageLog {

    private static final Logger LOG = LoggerFactory.getLogger(MessageLog.class);

    @PersistenceContext
    EntityManager entityManager;

    @Transactional(value= Transactional.TxType.MANDATORY)
    public void processed(String eventId) {
        entityManager.persist(new ConsumedMessage(eventId, Instant.now()));
    }

    @Transactional(value= Transactional.TxType.MANDATORY)
    public boolean alreadyProcessed(String eventId) {
        LOG.debug("Looking for event with id {} in message log", eventId);
        return entityManager.find(ConsumedMessage.class, eventId) != null;
    }
}
