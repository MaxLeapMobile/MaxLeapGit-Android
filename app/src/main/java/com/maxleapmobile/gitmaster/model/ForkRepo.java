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

public class ForkRepo extends Repo {

    private Permissions permissions;

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    private class Permissions {
        @Expose
        private boolean admin;
        @Expose
        private boolean push;
        @Expose
        private boolean pull;

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public boolean isPush() {
            return push;
        }

        public void setPush(boolean push) {
            this.push = push;
        }

        public boolean isPull() {
            return pull;
        }

        public void setPull(boolean pull) {
            this.pull = pull;
        }

        @Override
        public String toString() {
            return "Permissions{" +
                    "admin=" + admin +
                    ", push=" + push +
                    ", pull=" + pull +
                    '}';
        }
    }
}