/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.database;

public class DBRecRepo {

    private int id;
    private long repo_id;
    private boolean isStar;
    private boolean isFork;
    private boolean isSkip;
    private long createTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFork() {
        return isFork;
    }

    public void setIsFork(boolean isFork) {
        this.isFork = isFork;
    }

    public boolean isSkip() {
        return isSkip;
    }

    public void setIsSkip(boolean isSkip) {
        this.isSkip = isSkip;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setIsStar(boolean isStar) {
        this.isStar = isStar;
    }

    public long getRepo_id() {
        return repo_id;
    }

    public void setRepo_id(long repo_id) {
        this.repo_id = repo_id;
    }
}
