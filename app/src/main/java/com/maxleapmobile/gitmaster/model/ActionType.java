/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.model;

public enum  ActionType {

    CommitCommentEvent("CommitCommentEvent"),

    CreateEvent("CreateEvent"),

    DeleteEvent("DeleteEvent"),

    DeploymentEvent("DeploymentEvent"),

    DeploymentStatusEvent("DeploymentStatusEvent"),

    DownloadEvent("DownloadEvent"),

    FollowEvent("FollowEvent"),

    ForkEvent("ForkEvent"),

    ForkApplyEvent("ForkApplyEvent"),

    GistEvent("GistEvent"),

    GollumEvent("GollumEvent"),

    IssueCommentEvent("IssueCommentEvent"),

    IssueEvent("IssueEvent"),

    MemberEvent("MemberEvent"),

    MembershipEvent("MembershipEvent"),

    PageBuildEvent("PageBuildEvent"),

    PublicEvent("PublicEvent"),

    PullRequestEvent("PullRequestEvent"),

    PullRequestReviewCommentEvent("PullRequestReviewCommentEvent"),

    PushEvent("PushEvent"),

    ReleaseEvent("ReleaseEvent"),

    RepositoryEvent("RepositoryEvent"),

    StatusEvent("StatusEvent"),

    TeamAddEvent("TeamAddEvent"),

    WatchEvent("WatchEvent");


    private String type;

    ActionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}