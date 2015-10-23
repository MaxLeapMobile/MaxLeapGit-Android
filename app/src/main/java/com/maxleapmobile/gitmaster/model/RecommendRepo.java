/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.model;

import org.json.JSONObject;

public class RecommendRepo {

    String description;
    int forksCount;
    String htmlUrl;
    String id;
    boolean isPrivate;
    String language;
    String name;
    String ownerAvatarUrl;
    String ownerFollowersUrl;
    String ownerFollowingUrl;
    String ownerHtmlUrl;
    String ownerId;
    String ownerLogin;
    String ownerReposUrl;
    String stargazersCount;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getForksCount() {
        return forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerAvatarUrl() {
        return ownerAvatarUrl;
    }

    public void setOwnerAvatarUrl(String ownerAvatarUrl) {
        this.ownerAvatarUrl = ownerAvatarUrl;
    }

    public String getOwnerFollowersUrl() {
        return ownerFollowersUrl;
    }

    public void setOwnerFollowersUrl(String ownerFollowersUrl) {
        this.ownerFollowersUrl = ownerFollowersUrl;
    }

    public String getOwnerFollowingUrl() {
        return ownerFollowingUrl;
    }

    public void setOwnerFollowingUrl(String ownerFollowingUrl) {
        this.ownerFollowingUrl = ownerFollowingUrl;
    }

    public String getOwnerHtmlUrl() {
        return ownerHtmlUrl;
    }

    public void setOwnerHtmlUrl(String ownerHtmlUrl) {
        this.ownerHtmlUrl = ownerHtmlUrl;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public String getOwnerReposUrl() {
        return ownerReposUrl;
    }

    public void setOwnerReposUrl(String ownerReposUrl) {
        this.ownerReposUrl = ownerReposUrl;
    }

    public String getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(String stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public static RecommendRepo from(JSONObject object) throws Exception {
        RecommendRepo recommendRepo = new RecommendRepo();

        recommendRepo.setDescription(object.getString("description"));
        recommendRepo.setForksCount(object.getInt("forksCount"));
        recommendRepo.setHtmlUrl(object.getString("htmlUrl"));
        recommendRepo.setId(object.getString("id"));
        recommendRepo.setIsPrivate(object.getBoolean("isPrivate"));
        recommendRepo.setLanguage(object.getString("language"));
        recommendRepo.setName(object.getString("name"));
        recommendRepo.setOwnerAvatarUrl(object.getString("ownerAvatarUrl"));
        recommendRepo.setOwnerFollowersUrl(object.getString("ownerFollowersUrl"));
        recommendRepo.setOwnerFollowingUrl(object.getString("ownerFollowingUrl"));
        recommendRepo.setOwnerHtmlUrl(object.getString("ownerHtmlUrl"));
        recommendRepo.setOwnerId(object.getString("ownerId"));
        recommendRepo.setOwnerLogin(object.getString("ownerLogin"));
        recommendRepo.setOwnerReposUrl(object.getString("ownerReposUrl"));
        recommendRepo.setStargazersCount(object.getString("stargazersCount"));

        return recommendRepo;
    }

    @Override
    public String toString() {
        return "RecommendRepo{" +
                "description='" + description + '\'' +
                ", forksCount=" + forksCount +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", id='" + id + '\'' +
                ", isPrivate=" + isPrivate +
                ", language='" + language + '\'' +
                ", name='" + name + '\'' +
                ", ownerAvatarUrl='" + ownerAvatarUrl + '\'' +
                ", ownerFollowersUrl='" + ownerFollowersUrl + '\'' +
                ", ownerFollowingUrl='" + ownerFollowingUrl + '\'' +
                ", ownerHtmlUrl='" + ownerHtmlUrl + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", ownerLogin='" + ownerLogin + '\'' +
                ", ownerReposUrl='" + ownerReposUrl + '\'' +
                ", stargazersCount='" + stargazersCount + '\'' +
                '}';
    }
}
