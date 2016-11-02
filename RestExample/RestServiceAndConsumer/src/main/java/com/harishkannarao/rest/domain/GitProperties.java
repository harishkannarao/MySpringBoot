package com.harishkannarao.rest.domain;

public class GitProperties {
    private String gitCommitId;
    private String mavenBuildNumber;

    public String getGitCommitId() {
        return gitCommitId;
    }

    public void setGitCommitId(String gitCommitId) {
        this.gitCommitId = gitCommitId;
    }

    public String getMavenBuildNumber() {
        return mavenBuildNumber;
    }

    public void setMavenBuildNumber(String mavenBuildNumber) {
        this.mavenBuildNumber = mavenBuildNumber;
    }
}
