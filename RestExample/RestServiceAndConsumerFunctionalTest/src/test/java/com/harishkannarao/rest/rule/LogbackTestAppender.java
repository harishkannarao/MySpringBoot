package com.harishkannarao.rest.rule;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.util.AssertionErrors.assertTrue;


public class LogbackTestAppender {
    private static final String LOGGING_PATTERN = "%-5level %message%n";
    private static final String LOG_FILE_LOCATION = "target/logs";
    private static final String LOG_FILE_PREFIX = "test_log_";
    private static final String TEST_APPENDER_NAME_FORMAT = "TEST_APPENDER_%s";
    private final static String LOG_FILENAME_FORMAT = "%s/%s_%s.log";

    private final String logFileLocation;
    private final String logFilePrefix;

    private final Logger logger;
    private final LoggerContext context;
    private final PatternLayoutEncoder encoder;

    private FileAppender<ILoggingEvent> testAppender;
    private String logFile;

    public LogbackTestAppender(String loggerName) {
        this(loggerName, LOGGING_PATTERN, LOG_FILE_LOCATION, LOG_FILE_PREFIX);
    }

    public LogbackTestAppender(String loggerName, String loggingPattern, String logFileLocation, String logFilePrefix) {
        this.logFileLocation = logFileLocation;
        this.logFilePrefix = logFilePrefix;

        logger = (Logger) LoggerFactory.getLogger(loggerName);
        context = (LoggerContext) LoggerFactory.getILoggerFactory();
        encoder = createPatternLayoutEncoder(loggingPattern, context);
    }


    public void startLogsCapture() {
        logFile = String.format(LOG_FILENAME_FORMAT, logFileLocation, logFilePrefix, UUID.randomUUID());

        testAppender = new FileAppender<>();
        testAppender.setFile(logFile);
        testAppender.setName(String.format(TEST_APPENDER_NAME_FORMAT, UUID.randomUUID()));
        testAppender.setContext(context);
        testAppender.setEncoder(encoder);
        testAppender.start();

        logger.addAppender(testAppender);
    }

    public void stopLogsCapture() {
        logger.detachAppender(testAppender);
        testAppender.stop();
    }

    public void assertLogEntry(String expectedString) throws IOException {
        try(Stream<String> lines = Files.lines(Paths.get(logFile))) {
            boolean matchFound = lines.anyMatch(s -> s.contains(expectedString));
            assertTrue("\nLog file: " + logFile
                    + "\ndoes not contain expected string \n"
                    + expectedString, matchFound);
        }
    }


    private PatternLayoutEncoder createPatternLayoutEncoder(String loggingPattern, LoggerContext context) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern(loggingPattern);
        encoder.setContext(context);
        encoder.start();
        return encoder;
    }
}
