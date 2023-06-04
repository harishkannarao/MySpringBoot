package com.harishkannarao.jdbc;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.List;


public class LogbackTestAppender {
    private final Logger logger;
    private final ListAppender<ILoggingEvent> testAppender;
    private final Level existingLevel;
    private final Level desiredLevel;

    public LogbackTestAppender(String loggerName, Level desiredLevel) {
        logger = (Logger) LoggerFactory.getLogger(loggerName);
        this.desiredLevel = desiredLevel;
        testAppender = new ListAppender<>();
        existingLevel = logger.getEffectiveLevel();
    }

    public void startLogsCapture() {
        testAppender.start();
        logger.setLevel(desiredLevel);
        logger.addAppender(testAppender);
    }

    public void stopLogsCapture() {
        logger.setLevel(existingLevel);
        logger.detachAppender(testAppender);
        testAppender.stop();
    }

    public List<ILoggingEvent> getLogs() {
        return testAppender.list;
    }
}
