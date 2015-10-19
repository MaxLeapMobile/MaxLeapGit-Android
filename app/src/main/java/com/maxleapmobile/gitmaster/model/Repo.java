/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Repo {
    @Expose
    @SerializedName("archive_url")
    private String archiveUrl;
    @Expose
    @SerializedName("assignees_url")
    private String assigneesUrl;
    @Expose
    @SerializedName("blobs_url")
    private String blobsUrl;
    @Expose
    @SerializedName("branches_url")
    private String branchesUrl;
    @Expose
    @SerializedName("clone_url")
    private String cloneUrl;
    @Expose
    @SerializedName("collaborators_url")
    private String collaboratorsUrl;
    @Expose
    @SerializedName("comments_url")
    private String commentsUrl;
    @Expose
    @SerializedName("commits_url")
    private String commitsUrl;
    @Expose
    @SerializedName("compare_url")
    private String compareUrl;
    @Expose
    @SerializedName("contents_url")
    private String contentsUrl;
    @Expose
    @SerializedName("contributors_url")
    private String contributorsUrl;
    @Expose
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    @SerializedName("default_branch")
    private String defaultBranch;
    @Expose
    private String description;
    @Expose
    @SerializedName("downloads_url")
    private String downloadsUrl;
    @Expose
    @SerializedName("events_url")
    private String eventsUrl;
    @Expose
    private boolean fork;
    @Expose
    private long forks;
    @Expose
    @SerializedName("forks_count")
    private long forksCount;
    @Expose
    @SerializedName("forks_url")
    private String forksUrl;
    @Expose
    @SerializedName("full_name")
    private String fullName;
    @Expose
    @SerializedName("git_commits_url")
    private String gitCommitsUrl;
    @Expose
    @SerializedName("git_refs_url")
    private String gitRefsUrl;
    @Expose
    @SerializedName("git_tags_url")
    private String gitTagsUrl;
    @Expose
    @SerializedName("git_url")
    private String gitUrl;
    @Expose
    @SerializedName("has_downloads")
    private boolean hasDownloads;
    @Expose
    @SerializedName("has_issues")
    private boolean hasIssues;
    @Expose
    @SerializedName("has_pages")
    private boolean hasPages;
    @Expose
    @SerializedName("has_wiki")
    private boolean hasWiki;
    @Expose
    private String homepage;
    @Expose
    @SerializedName("hooks_url")
    private String hooksUrl;
    @Expose
    @SerializedName("html_url")
    private String htmlUrl;
    @Expose
    private long id;
    @Expose
    @SerializedName("issue_comment_url")
    private String issueCommentUrl;
    @Expose
    @SerializedName("issue_events_url")
    private String issueEventsUrl;
    @Expose
    @SerializedName("issues_url")
    private String issuesUrl;
    @Expose
    @SerializedName("keys_url")
    private String keysUrl;
    @Expose
    @SerializedName("labels_url")
    private String labelsUrl;
    @Expose
    private String language;
    @Expose
    @SerializedName("languages_url")
    private String languageUrl;
    @Expose
    @SerializedName("merges_url")
    private String mergesUrl;
    @Expose
    @SerializedName("milestones_url")
    private String milestonesUrl;
    @Expose
    @SerializedName("mirror_url")
    private String mirrorUrl;
    @Expose
    private String name;
    @Expose
    @SerializedName("notifications_url")
    private String notificationsUrl;
    @Expose
    @SerializedName("open_issues")
    private long openIssues;
    @Expose
    @SerializedName("open_issues_count")
    private long openIssuesCount;
    @Expose
    private Owner owner;
    @Expose
    @SerializedName("private")
    private boolean privateRepo;
    @Expose
    @SerializedName("pulls_url")
    private String pullsUrl;
    @Expose
    @SerializedName("pushed_at")
    private String pushedAt;
    @Expose
    @SerializedName("releases_url")
    private String releasesUrl;
    @Expose
    private long size;
    @Expose
    @SerializedName("ssh_url")
    private String sshUrl;
    @Expose
    @SerializedName("stargazers_count")
    private long stagazersCount;
    @Expose
    @SerializedName("stargazers_url")
    private String stagazersUrl;
    @Expose
    @SerializedName("statuses_url")
    private String statusesUrl;
    @Expose
    @SerializedName("subscribers_url")
    private String subscribersUrl;
    @Expose
    @SerializedName("subscription_url")
    private String subscriptionUrl;
    @Expose
    @SerializedName("svn_url")
    private String svnUrl;
    @Expose
    @SerializedName("tags_url")
    private String tagsUrl;
    @Expose
    @SerializedName("teams_url")
    private String teamsUrl;
    @Expose
    @SerializedName("trees_url")
    private String treesUrl;
    @Expose
    @SerializedName("updated_at")
    private String updatedAt;
    @Expose
    private String url;
    @Expose
    private long watchers;
    @Expose
    @SerializedName("watchers_count")
    private long watchers_count;

    public String getArchiveUrl() {
        return archiveUrl;
    }

    public void setArchiveUrl(String archiveUrl) {
        this.archiveUrl = archiveUrl;
    }

    public String getAssigneesUrl() {
        return assigneesUrl;
    }

    public void setAssigneesUrl(String assigneesUrl) {
        this.assigneesUrl = assigneesUrl;
    }

    public String getBlobsUrl() {
        return blobsUrl;
    }

    public void setBlobsUrl(String blobsUrl) {
        this.blobsUrl = blobsUrl;
    }

    public String getBranchesUrl() {
        return branchesUrl;
    }

    public void setBranchesUrl(String branchesUrl) {
        this.branchesUrl = branchesUrl;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getCollaboratorsUrl() {
        return collaboratorsUrl;
    }

    public void setCollaboratorsUrl(String collaboratorsUrl) {
        this.collaboratorsUrl = collaboratorsUrl;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public String getCommitsUrl() {
        return commitsUrl;
    }

    public void setCommitsUrl(String commitsUrl) {
        this.commitsUrl = commitsUrl;
    }

    public String getCompareUrl() {
        return compareUrl;
    }

    public void setCompareUrl(String compareUrl) {
        this.compareUrl = compareUrl;
    }

    public String getContentsUrl() {
        return contentsUrl;
    }

    public void setContentsUrl(String contentsUrl) {
        this.contentsUrl = contentsUrl;
    }

    public String getContributorsUrl() {
        return contributorsUrl;
    }

    public void setContributorsUrl(String contributorsUrl) {
        this.contributorsUrl = contributorsUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadsUrl() {
        return downloadsUrl;
    }

    public void setDownloadsUrl(String downloadsUrl) {
        this.downloadsUrl = downloadsUrl;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public long getForks() {
        return forks;
    }

    public void setForks(long forks) {
        this.forks = forks;
    }

    public long getForksCount() {
        return forksCount;
    }

    public void setForksCount(long forksCount) {
        this.forksCount = forksCount;
    }

    public String getForksUrl() {
        return forksUrl;
    }

    public void setForksUrl(String forksUrl) {
        this.forksUrl = forksUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGitCommitsUrl() {
        return gitCommitsUrl;
    }

    public void setGitCommitsUrl(String gitCommitsUrl) {
        this.gitCommitsUrl = gitCommitsUrl;
    }

    public String getGitRefsUrl() {
        return gitRefsUrl;
    }

    public void setGitRefsUrl(String gitRefsUrl) {
        this.gitRefsUrl = gitRefsUrl;
    }

    public String getGitTagsUrl() {
        return gitTagsUrl;
    }

    public void setGitTagsUrl(String gitTagsUrl) {
        this.gitTagsUrl = gitTagsUrl;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public boolean isHasDownloads() {
        return hasDownloads;
    }

    public void setHasDownloads(boolean hasDownloads) {
        this.hasDownloads = hasDownloads;
    }

    public boolean isHasIssues() {
        return hasIssues;
    }

    public void setHasIssues(boolean hasIssues) {
        this.hasIssues = hasIssues;
    }

    public boolean isHasPages() {
        return hasPages;
    }

    public void setHasPages(boolean hasPages) {
        this.hasPages = hasPages;
    }

    public boolean isHasWiki() {
        return hasWiki;
    }

    public void setHasWiki(boolean hasWiki) {
        this.hasWiki = hasWiki;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getHooksUrl() {
        return hooksUrl;
    }

    public void setHooksUrl(String hooksUrl) {
        this.hooksUrl = hooksUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIssueCommentUrl() {
        return issueCommentUrl;
    }

    public void setIssueCommentUrl(String issueCommentUrl) {
        this.issueCommentUrl = issueCommentUrl;
    }

    public String getIssueEventsUrl() {
        return issueEventsUrl;
    }

    public void setIssueEventsUrl(String issueEventsUrl) {
        this.issueEventsUrl = issueEventsUrl;
    }

    public String getIssuesUrl() {
        return issuesUrl;
    }

    public void setIssuesUrl(String issuesUrl) {
        this.issuesUrl = issuesUrl;
    }

    public String getKeysUrl() {
        return keysUrl;
    }

    public void setKeysUrl(String keysUrl) {
        this.keysUrl = keysUrl;
    }

    public String getLabelsUrl() {
        return labelsUrl;
    }

    public void setLabelsUrl(String labelsUrl) {
        this.labelsUrl = labelsUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageUrl() {
        return languageUrl;
    }

    public void setLanguageUrl(String languageUrl) {
        this.languageUrl = languageUrl;
    }

    public String getMergesUrl() {
        return mergesUrl;
    }

    public void setMergesUrl(String mergesUrl) {
        this.mergesUrl = mergesUrl;
    }

    public String getMilestonesUrl() {
        return milestonesUrl;
    }

    public void setMilestonesUrl(String milestonesUrl) {
        this.milestonesUrl = milestonesUrl;
    }

    public String getMirrorUrl() {
        return mirrorUrl;
    }

    public void setMirrorUrl(String mirrorUrl) {
        this.mirrorUrl = mirrorUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotificationsUrl() {
        return notificationsUrl;
    }

    public void setNotificationsUrl(String notificationsUrl) {
        this.notificationsUrl = notificationsUrl;
    }

    public long getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(long openIssues) {
        this.openIssues = openIssues;
    }

    public long getOpenIssuesCount() {
        return openIssuesCount;
    }

    public void setOpenIssuesCount(long openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public boolean isPrivateRepo() {
        return privateRepo;
    }

    public void setPrivateRepo(boolean privateRepo) {
        this.privateRepo = privateRepo;
    }

    public String getPullsUrl() {
        return pullsUrl;
    }

    public void setPullsUrl(String pullsUrl) {
        this.pullsUrl = pullsUrl;
    }

    public String getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(String pushedAt) {
        this.pushedAt = pushedAt;
    }

    public String getReleasesUrl() {
        return releasesUrl;
    }

    public void setReleasesUrl(String releasesUrl) {
        this.releasesUrl = releasesUrl;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public long getStagazersCount() {
        return stagazersCount;
    }

    public void setStagazersCount(long stagazersCount) {
        this.stagazersCount = stagazersCount;
    }

    public String getStagazersUrl() {
        return stagazersUrl;
    }

    public void setStagazersUrl(String stagazersUrl) {
        this.stagazersUrl = stagazersUrl;
    }

    public String getStatusesUrl() {
        return statusesUrl;
    }

    public void setStatusesUrl(String statusesUrl) {
        this.statusesUrl = statusesUrl;
    }

    public String getSubscribersUrl() {
        return subscribersUrl;
    }

    public void setSubscribersUrl(String subscribersUrl) {
        this.subscribersUrl = subscribersUrl;
    }

    public String getSubscriptionUrl() {
        return subscriptionUrl;
    }

    public void setSubscriptionUrl(String subscriptionUrl) {
        this.subscriptionUrl = subscriptionUrl;
    }

    public String getSvnUrl() {
        return svnUrl;
    }

    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    public String getTagsUrl() {
        return tagsUrl;
    }

    public void setTagsUrl(String tagsUrl) {
        this.tagsUrl = tagsUrl;
    }

    public String getTeamsUrl() {
        return teamsUrl;
    }

    public void setTeamsUrl(String teamsUrl) {
        this.teamsUrl = teamsUrl;
    }

    public String getTreesUrl() {
        return treesUrl;
    }

    public void setTreesUrl(String treesUrl) {
        this.treesUrl = treesUrl;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getWatchers() {
        return watchers;
    }

    public void setWatchers(long watchers) {
        this.watchers = watchers;
    }

    public long getWatchers_count() {
        return watchers_count;
    }

    public void setWatchers_count(long watchers_count) {
        this.watchers_count = watchers_count;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "archiveUrl='" + archiveUrl + '\'' +
                ", assigneesUrl='" + assigneesUrl + '\'' +
                ", blobsUrl='" + blobsUrl + '\'' +
                ", branchesUrl='" + branchesUrl + '\'' +
                ", cloneUrl='" + cloneUrl + '\'' +
                ", collaboratorsUrl='" + collaboratorsUrl + '\'' +
                ", commentsUrl='" + commentsUrl + '\'' +
                ", commitsUrl='" + commitsUrl + '\'' +
                ", compareUrl='" + compareUrl + '\'' +
                ", contentsUrl='" + contentsUrl + '\'' +
                ", contributorsUrl='" + contributorsUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", defaultBranch='" + defaultBranch + '\'' +
                ", description='" + description + '\'' +
                ", downloadsUrl='" + downloadsUrl + '\'' +
                ", eventsUrl='" + eventsUrl + '\'' +
                ", fork=" + fork +
                ", forks=" + forks +
                ", forksCount=" + forksCount +
                ", forksUrl='" + forksUrl + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gitCommitsUrl='" + gitCommitsUrl + '\'' +
                ", gitRefsUrl='" + gitRefsUrl + '\'' +
                ", gitTagsUrl='" + gitTagsUrl + '\'' +
                ", gitUrl='" + gitUrl + '\'' +
                ", hasDownloads=" + hasDownloads +
                ", hasIssues=" + hasIssues +
                ", hasPages=" + hasPages +
                ", hasWiki=" + hasWiki +
                ", homepage='" + homepage + '\'' +
                ", hooksUrl='" + hooksUrl + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", id=" + id +
                ", issueCommentUrl='" + issueCommentUrl + '\'' +
                ", issueEventsUrl='" + issueEventsUrl + '\'' +
                ", issuesUrl='" + issuesUrl + '\'' +
                ", keysUrl='" + keysUrl + '\'' +
                ", labelsUrl='" + labelsUrl + '\'' +
                ", language='" + language + '\'' +
                ", languageUrl='" + languageUrl + '\'' +
                ", mergesUrl='" + mergesUrl + '\'' +
                ", milestonesUrl='" + milestonesUrl + '\'' +
                ", mirrorUrl='" + mirrorUrl + '\'' +
                ", name='" + name + '\'' +
                ", notificationsUrl='" + notificationsUrl + '\'' +
                ", openIssues=" + openIssues +
                ", openIssuesCount=" + openIssuesCount +
                ", owner=" + owner +
                ", privateRepo=" + privateRepo +
                ", pullsUrl='" + pullsUrl + '\'' +
                ", pushedAt='" + pushedAt + '\'' +
                ", releasesUrl='" + releasesUrl + '\'' +
                ", size=" + size +
                ", sshUrl='" + sshUrl + '\'' +
                ", stagazersCount=" + stagazersCount +
                ", stagazersUrl='" + stagazersUrl + '\'' +
                ", statusesUrl='" + statusesUrl + '\'' +
                ", subscribersUrl='" + subscribersUrl + '\'' +
                ", subscriptionUrl='" + subscriptionUrl + '\'' +
                ", svnUrl='" + svnUrl + '\'' +
                ", tagsUrl='" + tagsUrl + '\'' +
                ", teamsUrl='" + teamsUrl + '\'' +
                ", treesUrl='" + treesUrl + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", url='" + url + '\'' +
                ", watchers=" + watchers +
                ", watchers_count=" + watchers_count +
                '}';
    }
}