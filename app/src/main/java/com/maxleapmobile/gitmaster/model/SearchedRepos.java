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

import java.util.List;

public class SearchedRepos {

    @Expose
    @SerializedName("incomplete_results")
    private boolean incompleteResults;
    @Expose
    private List<Item> items;
    @Expose
    @SerializedName("total_count")
    private int totalCount;

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }


    @Override
    public String toString() {
        return "SearchRepoResult{" +
                "incompleteResults=" + incompleteResults +
                ", items=" + items +
                ", totalCount=" + totalCount +
                '}';
    }

    public class Item {
        @Expose
        @SerializedName("created_at")
        private String createdAt;
        @Expose
        @SerializedName("default_branch")
        private String defaultBranch;
        @Expose
        private String description;
        @Expose
        private boolean fork;
        @Expose
        @SerializedName("forks_count")
        private long forksCount;
        @Expose
        @SerializedName("full_name")
        private String fullName;
        @Expose
        private String homepage;
        @Expose
        @SerializedName("html_url")
        private String htmlUrl;
        @Expose
        private long id;
        @Expose
        private String language;
        @Expose
        @SerializedName("master_branch")
        private String masterBranch;
        @Expose
        private String name;
        @Expose
        @SerializedName("open_issues_count")
        private long openIssuesCount;
        @Expose
        private Owner owner;
        @Expose
        @SerializedName("private")
        private boolean isPrivate;
        @Expose
        @SerializedName("pushed_at")
        private String pushedAt;
        @Expose
        private double score;
        @Expose
        private long size;
        @Expose
        @SerializedName("stargazers_count")
        private long stargaersCount;
        @Expose
        @SerializedName("updated_at")
        private String updatedAt;
        @Expose
        private String url;
        @Expose
        @SerializedName("watchers_count")
        private long watchersCount;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
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

        public boolean isFork() {
            return fork;
        }

        public void setFork(boolean fork) {
            this.fork = fork;
        }

        public long getForksCount() {
            return forksCount;
        }

        public void setForksCount(long forksCount) {
            this.forksCount = forksCount;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getHomepage() {
            return homepage;
        }

        public void setHomepage(String homepage) {
            this.homepage = homepage;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getMasterBranch() {
            return masterBranch;
        }

        public void setMasterBranch(String masterBranch) {
            this.masterBranch = masterBranch;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public boolean isPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
        }

        public String getPushedAt() {
            return pushedAt;
        }

        public void setPushedAt(String pushedAt) {
            this.pushedAt = pushedAt;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public long getStargaersCount() {
            return stargaersCount;
        }

        public void setStargaersCount(long stargaersCount) {
            this.stargaersCount = stargaersCount;
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

        public long getWatchersCount() {
            return watchersCount;
        }

        public void setWatchersCount(long watchersCount) {
            this.watchersCount = watchersCount;
        }

        public class Owner {
            @Expose
            @SerializedName("avatar_url")
            private String avatarUrl;
            @Expose
            @SerializedName("gravatar_id")
            private String gravatarId;
            private long id;
            private String login;
            @Expose
            @SerializedName("received_events_url")
            private String receivedEventsUrl;
            private String type;
            private String url;

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public String getGravatarId() {
                return gravatarId;
            }

            public void setGravatarId(String gravatarId) {
                this.gravatarId = gravatarId;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getLogin() {
                return login;
            }

            public void setLogin(String login) {
                this.login = login;
            }

            public String getReceivedEventsUrl() {
                return receivedEventsUrl;
            }

            public void setReceivedEventsUrl(String receivedEventsUrl) {
                this.receivedEventsUrl = receivedEventsUrl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            @Override
            public String toString() {
                return "Owner{" +
                        "avatarUrl='" + avatarUrl + '\'' +
                        ", gravatarId='" + gravatarId + '\'' +
                        ", id=" + id +
                        ", login='" + login + '\'' +
                        ", receivedEventsUrl='" + receivedEventsUrl + '\'' +
                        ", type='" + type + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }
    }
}