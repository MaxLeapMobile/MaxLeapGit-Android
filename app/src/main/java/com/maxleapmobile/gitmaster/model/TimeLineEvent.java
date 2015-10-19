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

public class TimeLineEvent {

    @Expose
    private Actor actor;
    @Expose
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    private long id;
    @Expose
    private Org org;
    @Expose
    private PayLoad payload;
    @Expose
    @SerializedName("public")
    private boolean isPublic;
    @Expose
    private SimpleRepo repo;
    @Expose
    private ActionType type;


    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public PayLoad getPayload() {
        return payload;
    }

    public void setPayload(PayLoad payload) {
        this.payload = payload;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public SimpleRepo getRepo() {
        return repo;
    }

    public void setRepo(SimpleRepo repo) {
        this.repo = repo;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TimeLineEvent{" +
                "actor=" + actor +
                ", createdAt='" + createdAt + '\'' +
                ", id=" + id +
                ", org=" + org +
                ", payload=" + payload +
                ", isPublic=" + isPublic +
                ", repo=" + repo +
                ", type=" + type +
                '}';
    }

    public class Org {
        @Expose
        @SerializedName("avatar_url")
        private String avatarUrl;
        @Expose
        @SerializedName("gravatar_id")
        private String gravatarId;
        private long id;
        private String login;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Org{" +
                    "avatarUrl='" + avatarUrl + '\'' +
                    ", gravatarId='" + gravatarId + '\'' +
                    ", id=" + id +
                    ", login='" + login + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public class Actor {
        @Expose
        @SerializedName("avatar_url")
        private String avatarUrl;
        @Expose
        @SerializedName("gravatar_id")
        private String gravatarId;
        @Expose
        private long id;
        @Expose
        private String login;
        @Expose
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Actor{" +
                    "avatarUrl='" + avatarUrl + '\'' +
                    ", gravatarId=" + gravatarId +
                    ", id=" + id +
                    ", login='" + login + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }


    public class PayLoad {
        //fork
        @Expose
        @SerializedName("forkee")
        private Repo forkee;
        //watch
        @Expose
        private String action;
        //push
        @Expose
        private String before;
        @Expose
        @SerializedName("distinct_size")
        private long distinctSize;
        @Expose
        private String head;
        @Expose
        @SerializedName("push_id")
        private long pushId;
        @Expose
        private String ref;
        @Expose
        private long size;
        @Expose
        private List<Commit> commits;
        //create
        @Expose
        private String description;
        @Expose
        @SerializedName("master_branch")
        private String masterBranch;
        @Expose
        @SerializedName("pusher_type")
        private String pusherType;
        @Expose
        @SerializedName("ref_type")
        private String refType;

        public Repo getForkee() {
            return forkee;
        }

        public void setForkee(Repo forkee) {
            this.forkee = forkee;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getBefore() {
            return before;
        }

        public void setBefore(String before) {
            this.before = before;
        }

        public long getDistinctSize() {
            return distinctSize;
        }

        public void setDistinctSize(long distinctSize) {
            this.distinctSize = distinctSize;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public long getPushId() {
            return pushId;
        }

        public void setPushId(long pushId) {
            this.pushId = pushId;
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public List<Commit> getCommits() {
            return commits;
        }

        public void setCommits(List<Commit> commits) {
            this.commits = commits;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMasterBranch() {
            return masterBranch;
        }

        public void setMasterBranch(String masterBranch) {
            this.masterBranch = masterBranch;
        }

        public String getPusherType() {
            return pusherType;
        }

        public void setPusherType(String pusherType) {
            this.pusherType = pusherType;
        }

        public String getRefType() {
            return refType;
        }

        public void setRefType(String refType) {
            this.refType = refType;
        }

        @Override
        public String toString() {
            return "PayLoad{" +
                    "forkee=" + forkee +
                    ", action='" + action + '\'' +
                    ", before='" + before + '\'' +
                    ", distinctSize=" + distinctSize +
                    ", head='" + head + '\'' +
                    ", pushId=" + pushId +
                    ", ref='" + ref + '\'' +
                    ", size=" + size +
                    ", commits=" + commits +
                    '}';
        }
    }

    public class SimpleRepo {
        @Expose
        private long id;
        @Expose
        private String name;
        @Expose
        private String url;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Repo{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public class Commit {
        @Expose
        private Author author;
        @Expose
        private boolean distinct;
        @Expose
        private String message;
        @Expose
        private String sha;
        @Expose
        private String url;

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        public boolean isDistinct() {
            return distinct;
        }

        public void setDistinct(boolean distinct) {
            this.distinct = distinct;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSha() {
            return sha;
        }

        public void setSha(String sha) {
            this.sha = sha;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Commit{" +
                    "author=" + author +
                    ", distinct=" + distinct +
                    ", message='" + message + '\'' +
                    ", sha='" + sha + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public class Author {
        @Expose
        private String email;
        @Expose
        private String name;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Author{" +
                    "email='" + email + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}