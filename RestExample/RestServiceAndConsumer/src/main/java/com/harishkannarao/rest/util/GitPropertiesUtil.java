package com.harishkannarao.rest.util;

import com.harishkannarao.rest.domain.GitProperties;

import java.io.IOException;
import java.util.Properties;

public class GitPropertiesUtil {
    private final Properties properties;

    private GitPropertiesUtil() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("git.properties"));
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

    public GitProperties getGitProperties() {
        GitProperties gitProperties = new GitProperties();
        gitProperties.setGitCommitId(properties.getProperty("git.commit.id"));
        gitProperties.setMavenBuildNumber(properties.getProperty("git.build.version"));
        return gitProperties;
    }
}
