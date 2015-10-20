/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.model;

public enum SortEnumRepo {

    DEFAULT(""),

    STARS("stars"),

    FORKS("forks"),

    UPDATED("updated");

    private String sort;

    SortEnumRepo(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }

    @Override
    public String toString() {
        return sort;
    }
}