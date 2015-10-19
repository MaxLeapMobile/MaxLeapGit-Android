/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.api;

import android.content.Context;

import com.maxleapmobile.gitmaster.GithubApplication;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.model.ForkRepo;
import com.maxleapmobile.gitmaster.model.OrderEnum;
import com.maxleapmobile.gitmaster.model.Owner;
import com.maxleapmobile.gitmaster.model.Repo;
import com.maxleapmobile.gitmaster.model.SearchedRepos;
import com.maxleapmobile.gitmaster.model.SearchedUsers;
import com.maxleapmobile.gitmaster.model.SortEnumRepo;
import com.maxleapmobile.gitmaster.model.SortEnumUser;
import com.maxleapmobile.gitmaster.model.TimeLineEvent;
import com.maxleapmobile.gitmaster.model.User;
import com.maxleapmobile.gitmaster.util.Const;
import com.maxleapmobile.gitmaster.util.PreferenceUtil;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class ApiManager {
    private static final String USER_AGENT = "GitMaster Android";
    private static final String ACCEPT = "application/vnd.github.v3+json";

    private static final String API_URL = "https://api.github.com";

    private static ApiManager mInstance;

    private Context mContext;
    private RestAdapter mRestAdapter;
    private GithubApi mGithubApi;
    private String mAccessToken;

    private ApiManager() {
        mContext = GithubApplication.getInstance();
        mAccessToken = PreferenceUtil.getString(mContext,
                Const.ACCESS_TOKEN_KEY, null);
        mRestAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(mRequestInterceptor)
                .setEndpoint(API_URL)
                .setClient(new OkClient(new OkHttpClient()))
                .build();

        mGithubApi = mRestAdapter.create(GithubApi.class);
    }


    public static ApiManager getInstance() {
        if (mInstance == null) {
            mInstance = new ApiManager();
        }
        return mInstance;
    }

    /**
     * Get authenticated user info
     * @param callback
     */
    public void getCurrentUser(ApiCallback<User> callback) {
        mGithubApi.getCurrentUser(callback);
    }

    /**
     * Get specified user info
     * @param username
     * @param callback
     */
    public void getUser(String username, ApiCallback<User> callback) {
        mGithubApi.getUser(username, callback);
    }

    /**
     * List user's repos
     * @param username
     * @param callback
     */
    public void listRepos(String username, ApiCallback<List<Repo>> callback) {
        mGithubApi.listRepos(username, null, null, callback);
    }

    /**
     * List user's repos by page
     * @param username
     * @param pageCount current page
     * @param perPageCount result per page
     * @param callback
     */
    public void listReposByPage(String username, int pageCount, int perPageCount,
                                ApiCallback<List<Repo>> callback) {
        mGithubApi.listRepos(username, String.valueOf(pageCount),
                String.valueOf(perPageCount), callback);
    }


    /**
     * Get repo star status
     * already starrd:204 not starred:404
     * @param owner repo owner
     * @param repo repo name
     * @param callback callback
     */
    public void isStarred(String owner, String repo, ApiCallback<Object> callback) {
        mGithubApi.starStatus(owner, repo, callback);
    }

    /**
     * Star a repo
     * success:204 fail:404
     * @param owner repo owner
     * @param repo repo name
     * @param callback callback
     */
    public void star(String owner, String repo, ApiCallback<Object> callback) {
        mGithubApi.star("", owner, repo, callback);
    }

    /**
     * Unstar a repo
     * @param owner repo owner
     * @param repo repo name
     * @param callback callback
     */
    public void unstart(String owner, String repo, ApiCallback<Object> callback) {
        mGithubApi.unstar(owner, repo, callback);
    }

    /**
     * List starred repo by specified user
     * @param username
     * @param callback
     */
    public void listStarRepoByUser(String username, ApiCallback<List<Repo>> callback) {
        mGithubApi.listStarredRepoByUser(username, null, null, callback);
    }

    /**
     * List starred repo by specified user by page
     * @param username
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void listStarRepoByUser(String username, int pageCount, int perPageCount,
                                   ApiCallback<List<Repo>> callback) {
        mGithubApi.listStarredRepoByUser(username, String.valueOf(pageCount),
                String.valueOf(perPageCount), callback);
    }

    /**
     * List starred repo by authenticated user
     * @param callback
     */
    public void listStarredRepoByAuthUser(ApiCallback<List<Repo>> callback) {
        mGithubApi.listStaredRepoByAuthUser(null, null, callback);
    }


    /**
     * List starred repo by authenticated user by page
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void listStarredRepoByAuthUser(int pageCount, int perPageCount,
                                          ApiCallback<List<Repo>> callback) {
        mGithubApi.listStaredRepoByAuthUser(String.valueOf(pageCount),
                String.valueOf(perPageCount), callback);
    }

    /**
     * Fork a repo for authenticated user
     * @param owner repo owner
     * @param repo repo name
     * @param callback
     */
    public void fork(String owner, String repo, ApiCallback<ForkRepo> callback) {
        mGithubApi.forkRepo(new TypedJsonString(""), owner, repo, callback);
    }

    /**
     * Search repos, best match, desc, default page config
     * @param keyword repo name
     * @param callback
     */
    public void searchRepo(String keyword, ApiCallback<SearchedRepos> callback) {
        mGithubApi.searchRepo(keyword, null, null, null, null, callback);
    }


    /**
     * Search repos by page, best match and desc order
     * @param keyword
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void searchRepo(String keyword, int pageCount, int perPageCount,
                           ApiCallback<SearchedRepos> callback) {
        mGithubApi.searchRepo(keyword, null, null, String.valueOf(pageCount),
                String.valueOf(perPageCount), callback);
    }

    /**
     * Search repos
     * @param keyword
     * @param sort
     * @param order
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void searchRepo(String keyword, SortEnumRepo sort, OrderEnum order,
                           int pageCount, int perPageCount,
                           ApiCallback<SearchedRepos> callback) {
        mGithubApi.searchRepo(keyword, sort, order, String.valueOf(pageCount),
                String.valueOf(perPageCount), callback);
    }

    /**
     * Search users, best match, desc order and default page config
     * @param username
     * @param callback
     */
    public void searchUser(String username, ApiCallback<SearchedUsers> callback) {
        mGithubApi.searchUser(username, null, null, null, null, callback);
    }

    /**
     * Search users, best match and desc order
     * @param username
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void searchUser(String username, int pageCount, int perPageCount,
                           ApiCallback<SearchedUsers> callback) {
        mGithubApi.searchUser(username, null, null, String.valueOf(pageCount),
                String.valueOf(perPageCount), callback);
    }

    /**
     *  Search users
     * @param username
     * @param sortEnumUser
     * @param orderEnum
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void searchUser(String username, SortEnumUser sortEnumUser, OrderEnum orderEnum,
                           int pageCount, int perPageCount,
                           ApiCallback<SearchedUsers> callback) {
        mGithubApi.searchUser(username, sortEnumUser, orderEnum, String.valueOf(pageCount),
                String.valueOf(perPageCount), callback);
    }

    /**
     * Get follow status
     * Not followed : 404, already followed : 204
     * @param username specified user
     * @param callback
     */
    public void followStatus(String username, ApiCallback<Object> callback) {
        mGithubApi.followStatus(username, callback);
    }

    /**
     * follow a user
     * success : 204 fail : 404
     * @param username
     * @param callback
     */
    public void follow(String username, ApiCallback<Object> callback) {
        mGithubApi.follow("", username, callback);
    }

    /**
     * unfollow a user
     * success : 204, fail : 404
     * @param username
     * @param callback
     */
    public void unfollow(String username, ApiCallback<Object> callback) {
        mGithubApi.unfollow(username, callback);
    }

    /**
     * Get specified user's followers list
     * @param username
     * @param callback
     */
    public void getFollowersList(String username, ApiCallback<List<Owner>> callback) {
        mGithubApi.getFollowersList(username, null, null, callback);
    }

    /**
     * Get specified user's followers list by page
     * @param username
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void getFollowersList(String username, int pageCount, int perPageCount,
                                 ApiCallback<List<Owner>> callback) {
        mGithubApi.getFollowersList(username, String.valueOf(pageCount),
                String.valueOf(perPageCount), callback);
    }

    /**
     * Get specified user's following list
     * @param username
     * @param callback
     */
    public void getFollowingList(String username, ApiCallback<List<Owner>> callback) {
        mGithubApi.getFollowingList(username, null, null, callback);
    }

    /**
     * Get specified user's following list by page
     * @param username
     * @param callback
     */
    public void getFollowingList(String username, int pageCount, int perPageCount,
                                 ApiCallback<List<Owner>> callback) {
        mGithubApi.getFollowingList(username, String.valueOf(pageCount),
                String.valueOf(perPageCount), callback);
    }

    /**
     * List public events that a user has received
     * @param username
     * @param callback
     */
    public void getReceivedEvents(String username, ApiCallback<List<TimeLineEvent>> callback) {
        mGithubApi.receivedEvents(username, null, null, callback);
    }

    /**
     * List public events that a user has received by page
     * @param username
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void getReceivedEvents(String username, int pageCount, int perPageCount,
                                  ApiCallback<List<TimeLineEvent>> callback) {
        mGithubApi.receivedEvents(username, String.valueOf(pageCount),
                String.valueOf(perPageCount), callback);
    }

    /**
     * List events performed by a user
     * @param username
     * @param callback
     */
    public void getUserEvents(String username, ApiCallback<List<TimeLineEvent>> callback) {
        mGithubApi.userEvents(username, null, null, callback);
    }

    /**
     * List specified user's performed events by page
     * @param username
     * @param callback
     */
    public void getUserEvents(String username, int pageCount, int perPageCount,
                              ApiCallback<List<TimeLineEvent>> callback) {
        mGithubApi.userEvents(username, String.valueOf(pageCount),
                String.valueOf(perPageCount), callback);
    }

    /**
     * List repository events
     * @param owner repo's owner
     * @param repoName repo name
     * @param callback
     */
    public void getRepoEvents(String owner, String repoName,
                              ApiCallback<List<TimeLineEvent>> callback) {
        mGithubApi.repoEvents(owner, repoName, callback);
    }

    RequestInterceptor mRequestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            // add header
            request.addHeader("User-Agent", USER_AGENT);
            request.addHeader("Accept", ACCEPT);
            request.addQueryParam("access_token", mAccessToken);
        }
    };

}