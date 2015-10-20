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

public class SearchedUsers {

    @Expose
    @SerializedName("total_count")
    private int totalCount;
    @Expose
    @SerializedName("incomplete_results")
    private boolean incompleteResults;
    @Expose
    private List<Item> items;


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

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

    @Override
    public String toString() {
        return "SearchedUsers{" +
                "totalCount=" + totalCount +
                ", incompleteResults=" + incompleteResults +
                ", items=" + items +
                '}';
    }

    public class Item {
        @Expose
        private String login;
        @Expose
        private long id;
        @Expose
        @SerializedName("avatar_url")
        private String avatarUrl;
        @Expose
        @SerializedName("gravatar_id")
        private String gravatarId;
        @Expose
        private String url;
        @Expose
        @SerializedName("html_url")
        private String htmlUrl;
        @Expose
        @SerializedName("followers_url")
        private String followersUrl;
        @Expose
        @SerializedName("subscriptions_url")
        private String subscriptionsUrl;
        @Expose
        @SerializedName("organization_url")
        private String organizationsUrl;
        @Expose
        @SerializedName("repos_url")
        private String reposUrl;
        @Expose
        @SerializedName("received_events_url")
        private String receivedEventsUrl;
        @Expose
        private String type;
        @Expose
        private double score;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        public String getFollowersUrl() {
            return followersUrl;
        }

        public void setFollowersUrl(String followersUrl) {
            this.followersUrl = followersUrl;
        }

        public String getSubscriptionsUrl() {
            return subscriptionsUrl;
        }

        public void setSubscriptionsUrl(String subscriptionsUrl) {
            this.subscriptionsUrl = subscriptionsUrl;
        }

        public String getOrganizationsUrl() {
            return organizationsUrl;
        }

        public void setOrganizationsUrl(String organizationsUrl) {
            this.organizationsUrl = organizationsUrl;
        }

        public String getReposUrl() {
            return reposUrl;
        }

        public void setReposUrl(String reposUrl) {
            this.reposUrl = reposUrl;
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

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "login='" + login + '\'' +
                    ", id=" + id +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", gravatarId='" + gravatarId + '\'' +
                    ", url='" + url + '\'' +
                    ", htmlUrl='" + htmlUrl + '\'' +
                    ", followersUrl='" + followersUrl + '\'' +
                    ", subscriptionsUrl='" + subscriptionsUrl + '\'' +
                    ", organizationsUrl='" + organizationsUrl + '\'' +
                    ", reposUrl='" + reposUrl + '\'' +
                    ", receivedEventsUrl='" + receivedEventsUrl + '\'' +
                    ", type='" + type + '\'' +
                    ", score=" + score +
                    '}';
        }
    }
}