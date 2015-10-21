/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.manage;

import android.text.TextUtils;

import com.maxleap.LogInCallback;
import com.maxleap.MLDataManager;
import com.maxleap.MLObject;
import com.maxleap.MLRelation;
import com.maxleap.MLUser;
import com.maxleap.MLUserManager;
import com.maxleap.SaveCallback;
import com.maxleap.SignUpCallback;
import com.maxleap.ValidateUsernameCallback;
import com.maxleap.exception.MLException;
import com.maxleapmobile.gitmaster.calllback.OperationCallback;
import com.maxleapmobile.gitmaster.model.Gene;
import com.maxleapmobile.gitmaster.model.User;
import com.maxleapmobile.gitmaster.util.TimeUtil;

import java.util.Date;

public class UserManager {
    private static UserManager instance;
    private static User currentUser;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }

        return instance;
    }

    /**
     * Get current login user
     *
     * @return current login user
     */
    public User getCurrentUser() {
        if (currentUser == null) {
            MLUser mlUser = MLUser.getCurrentUser();
            if (mlUser == null) return null;

            currentUser = new User();
            currentUser.setObjectId(mlUser.getObjectId());
            currentUser.setId(mlUser.getLong("githubId"));
            currentUser.setName(mlUser.getString("nickName"));
            currentUser.setLogin(mlUser.getUserName());
            currentUser.setAvatarUrl(mlUser.getString("avatarUrl"));
            currentUser.setEmail(mlUser.getEmail());
            currentUser.setBlog(mlUser.getString("blog"));
            currentUser.setCompany(mlUser.getString("company"));
            currentUser.setLocation(mlUser.getString("location"));
            currentUser.setGenes(mlUser.getRelation("genes"));
            currentUser.setHireable(mlUser.getBoolean("hireable"));
            currentUser.setFollowers(mlUser.getLong("followers"));
            currentUser.setFollowing(mlUser.getLong("following"));
            currentUser.setPublicRepos(mlUser.getLong("publicRepos"));
            currentUser.setCreateAt(mlUser.getDate("githubCreateTime").toString());
            currentUser.setUpdateAt(mlUser.getDate("githubUpdateTime").toString());
        }

        return currentUser;
    }

    /**
     * Create an user or update an user info with token and user id
     *
     * @param user
     */
    public void login(final User user, final OperationCallback callback) {
        final MLUser mlUser = new MLUser();
        mlUser.setUserName(user.getLogin());
        mlUser.setPassword(user.getLogin());
        mlUser.put("accessToken", user.getAccessToken());
        mlUser.put("nickName", TextUtils.isEmpty(user.getName()) ? "" : user.getName());
        mlUser.put("avatarUrl", user.getAvatarUrl());
        mlUser.put("email", TextUtils.isEmpty(user.getEmail()) ? "" : user.getEmail());
        mlUser.put("blog", TextUtils.isEmpty(user.getBlog()) ? "" : user.getBlog());
        mlUser.put("company", TextUtils.isEmpty(user.getCompany()) ? "" : user.getCompany());
        mlUser.put("location", TextUtils.isEmpty(user.getLocation()) ? "" : user.getLocation());
        mlUser.put("hireable", user.isHireable());
        mlUser.put("followerCount", user.getFollowers());
        mlUser.put("followingCount", user.getFollowing());
        mlUser.put("publicRepoCount", user.getPublicRepos());
        mlUser.put("githubCreateTime", TimeUtil.getDateFromString(user.getCreateAt()));
        mlUser.put("githubUpdateTime", TimeUtil.getDateFromString(user.getUpdateAt()));

        MLUserManager.checkUsernameExistInBackground(user.getLogin(), new ValidateUsernameCallback() {
            @Override
            public void done(MLException e) {
                if (e == null) {
                    MLUserManager.logInInBackground(mlUser.getUserName(), mlUser.getUserName(), new LogInCallback<MLUser>() {
                        @Override
                        public void done(MLUser mlUser, MLException e) {
                            if (e == null) {
                                MLUserManager.saveInBackground(mlUser, new SaveCallback() {
                                    @Override
                                    public void done(MLException e) {
                                        if (e == null) {
                                            callback.success();
                                        } else {
                                            callback.failed(e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                callback.failed(e.getMessage());
                            }
                        }
                    });
                } else {
                    MLUserManager.signUpInBackground(mlUser, new SignUpCallback() {
                        @Override
                        public void done(MLException e) {
                            if (e == null) {
                                callback.success();
                            } else {
                                callback.failed(e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * fill user information into MaxLeap cloud data using full user data from github
     *
     * @param user
     */
    public void updateUserInfo(User user, final OperationCallback callback) {
        if (TextUtils.isEmpty(user.getObjectId())) {
            callback.failed("User object id cannot be empty");
        }

        MLUser mlUser = MLUser.getCurrentUser();
        mlUser.setObjectId(user.getObjectId());

        mlUser.put("githubId", user.getId());
        mlUser.put("nickName", user.getName());
        mlUser.put("avatarUrl", user.getAvatarUrl());
        mlUser.setEmail(user.getEmail());
        mlUser.put("blog", user.getBlog());
        mlUser.put("company", user.getCompany());
        mlUser.put("location", user.getLocation());
        mlUser.put("genes", user.getGenes());
        mlUser.put("hireable", user.isHireable());
        mlUser.put("followers", user.getFollowers());
        mlUser.put("following", user.getFollowing());
        mlUser.put("publicRepos", user.getPublicRepos());
        mlUser.put("githubCreateAt", new Date(user.getCreateAt()));
        mlUser.put("githubUpdateAt", new Date(user.getUpdateAt()));

        MLUserManager.saveInBackground(mlUser, new SaveCallback() {
            @Override
            public void done(MLException e) {
                if (e == null) {
                    callback.success();
                } else {
                    callback.failed(e.getMessage());
                }
            }
        });
    }

    /**
     * add a gene to current user and push gene into cloud data
     *
     * @param gene
     */
    public void addGene(Gene gene, final OperationCallback callback) {
        final MLObject obj = new MLObject("Gene");
        obj.put("language", gene.getLanguage());
        obj.put("skill", gene.getSkill());

        MLDataManager.saveInBackground(obj, new SaveCallback() {
            @Override
            public void done(MLException e) {
                if (e == null) {
                    MLRelation relation = MLUser.getCurrentUser().getRelation("genes");
                    relation.add(obj);
                    getCurrentUser().setGenes(relation);
                    MLUserManager.saveInBackground(MLUser.getCurrentUser(), new SaveCallback() {
                        @Override
                        public void done(MLException e) {
                            if (e == null) {
                                callback.success();
                            } else {
                                callback.failed(e.getMessage());
                            }
                        }
                    });
                } else {
                    callback.failed(e.getMessage());
                }
            }
        });
    }

    /**
     * update the gene data with the same objId
     *
     * @param gene
     */
    public void updateGene(Gene gene, final OperationCallback callback) {
        if (TextUtils.isEmpty(gene.getObjectId())) {
            callback.failed("gene object id must be set");
        }

        MLObject obj = new MLObject("Gene");
        obj.setObjectId(gene.getObjectId());

        obj.put("language", gene.getLanguage());
        obj.put("skill", gene.getSkill());

        MLDataManager.saveInBackground(obj, new SaveCallback() {
            @Override
            public void done(MLException e) {
                if (e == null) {
                    callback.success();
                } else {
                    callback.failed(e.getMessage());
                }
            }
        });
    }

    /**
     * delete a gene from an user and sync data with cloud data
     *
     * @param gene
     */
    public void removeGene(Gene gene, final OperationCallback callback) {
        if (TextUtils.isEmpty(gene.getObjectId())) {
            callback.failed("gene object id must be set");
        }

        MLObject obj = new MLObject("Gene");
        obj.setObjectId(gene.getObjectId());

        MLRelation relation = MLUser.getCurrentUser().getRelation("genes");
        relation.remove(obj);
        getCurrentUser().setGenes(relation);
        MLUserManager.saveInBackground(MLUser.getCurrentUser(), new SaveCallback() {
            @Override
            public void done(MLException e) {
                if (e == null) {
                    callback.success();
                } else {
                    callback.failed(e.getMessage());
                }
            }
        });
    }
}
