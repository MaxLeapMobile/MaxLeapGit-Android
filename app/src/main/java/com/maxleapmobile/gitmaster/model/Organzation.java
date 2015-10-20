/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.model;

import com.google.gson.annotations.SerializedName;

public class Organzation {
    private long id;
    private String login;
    private String description;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("members_url")
    private String membersUrl;
    @SerializedName("events_url")
    private String eventsUrl;
    @SerializedName("public_members_url")
    private String publicMembersUrl;
    @SerializedName("repos_url")
    private String reposUrl;
    private String url;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getMembersUrl() {
        return membersUrl;
    }

    public void setMembersUrl(String membersUrl) {
        this.membersUrl = membersUrl;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    public String getPublicMembersUrl() {
        return publicMembersUrl;
    }

    public void setPublicMembersUrl(String publicMembersUrl) {
        this.publicMembersUrl = publicMembersUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Organzation{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", description='" + description + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", membersUrl='" + membersUrl + '\'' +
                ", eventsUrl='" + eventsUrl + '\'' +
                ", publicMembersUrl='" + publicMembersUrl + '\'' +
                ", reposUrl='" + reposUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}