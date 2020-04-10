package com.harishkannarao.jdbc.util;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class GitPropertiesUtil {
    private static final String GIT_COMMIT_ID_ABBREV_KEY = "git.commit.id.abbrev";
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
        return properties.getProperty(GIT_COMMIT_ID_ABBREV_KEY);
    }
}
