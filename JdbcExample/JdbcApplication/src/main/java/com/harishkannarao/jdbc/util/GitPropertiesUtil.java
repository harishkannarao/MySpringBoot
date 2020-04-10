package com.harishkannarao.jdbc.util;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class GitPropertiesUtil {
    private final Properties properties;

    private GitPropertiesUtil() {
        properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("git.properties")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class LazyHolder {
        private static final GitPropertiesUtil INSTANCE = new GitPropertiesUtil();
    }

    public static GitPropertiesUtil getInstance() {
        return LazyHolder.INSTANCE;
    }

    public String getGitCommitIdAbbrev() {
        return properties.getProperty("git.commit.id.abbrev");
    }
}
