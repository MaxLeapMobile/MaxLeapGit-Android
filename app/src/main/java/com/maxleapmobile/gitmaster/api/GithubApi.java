/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.api;


import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.model.AccessToken;
import com.maxleapmobile.gitmaster.model.ForkRepo;
import com.maxleapmobile.gitmaster.model.OrderEnum;
import com.maxleapmobile.gitmaster.model.Organzation;
import com.maxleapmobile.gitmaster.model.Owner;
import com.maxleapmobile.gitmaster.model.Repo;
import com.maxleapmobile.gitmaster.model.SearchedRepos;
import com.maxleapmobile.gitmaster.model.SearchedUsers;
import com.maxleapmobile.gitmaster.model.SortEnumRepo;
import com.maxleapmobile.gitmaster.model.SortEnumUser;
import com.maxleapmobile.gitmaster.model.TimeLineEvent;
import com.maxleapmobile.gitmaster.model.User;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface GithubApi {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/login/oauth/access_token")
    void getAccessToken(@Field("client_id") String clientId,
                        @Field("client_secret") String secret,
                        @Field("redirect_uri") String callbackUrl,
                        @Field("code") String code,
                        ApiCallback<AccessToken> callback);

    @GET("/user")
    void getCurrentUser(ApiCallback<User> callback);

    @GET("/users/{username}")
    void getUser(@Path("username") String username,
                 ApiCallback<User> callback);

    @GET("/users/{username}/repos")
    void listRepos(@Path("username") String username,
                   @Query("page") String pageCount,
                   @Query("per_page") String perPageCount,
                   ApiCallback<List<Repo>> callback);

    @GET("/user/starred/{owner}/{repo}")
    void starStatus(@Path("owner") String owner,
                    @Path("repo") String repo,
                    ApiCallback<Object> callback);

    @PUT("/user/starred/{owner}/{repo}")
    void star(@Body String emptyString,
              @Path("owner") String owner,
              @Path("repo") String repo,
              ApiCallback<Object> callback);

    @DELETE("/user/starred/{owner}/{repo}")
    void unstar(@Path("owner") String owner,
                @Path("repo") String repo,
                ApiCallback<Object> callback);

    @GET("/users/{username}/starred")
    void listStarredRepoByUser(@Path("username") String username,
                               @Query("page") String pageCount,
                               @Query("per_page") String perPageCount,
                               ApiCallback<List<Repo>> callback);

    @GET("/user/starred")
    void listStaredRepoByAuthUser(@Query("page") String pageCount,
                                  @Query("per_page") String perPageCount,
                                  ApiCallback<List<Repo>> callback);

    @POST("/repos/{owner}/{repo}/forks")
    void forkRepo(@Body TypedJsonString emptyBody,
                  @Path("owner") String owner,
                  @Path("repo") String repo,
                  ApiCallback<ForkRepo> callback);

    @GET("/search/repositories")
    void searchRepo(@Query("q") String keyword,
                    @Query("sort") SortEnumRepo sort,
                    @Query("order") OrderEnum order,
                    @Query("page") String page,
                    @Query("per_page") String perPage,
                    ApiCallback<SearchedRepos> callback);

    @GET("/search/users")
    void searchUser(@Query("q") String username,
                    @Query("sort") SortEnumUser sort,
                    @Query("order") OrderEnum order,
                    @Query("page") String page,
                    @Query("per_page") String perPage,
                    ApiCallback<SearchedUsers> callback);

    @GET("/user/following/{username}")
    void followStatus(@Path("username") String username,
                      ApiCallback<Object> callback);

    @PUT("/user/following/{username}")
    void follow(@Body String emptyBody,
                @Path("username") String username,
                ApiCallback<Object> callback);

    @DELETE("/user/following/{username}")
    void unfollow(@Path("username") String username,
                  ApiCallback<Object> callback);

    @GET("/users/{username}/followers")
    void getFollowersList(@Path("username") String username,
                          @Query("page") String page,
                          @Query("per_page") String perPage,
                          ApiCallback<List<Owner>> callback);

    @GET("/users/{username}/following")
    void getFollowingList(@Path("username") String username,
                          @Query("page") String page,
                          @Query("per_page") String perPage,
                          ApiCallback<List<Owner>> callback);

    @GET("/users/{username}/received_events")
    void receivedEvents(@Path("username") String username,
                        @Query("page") String page,
                        @Query("per_page") String perPage,
                        ApiCallback<List<TimeLineEvent>> callback);

    @GET("/users/{username}/events")
    void userEvents(@Path("username") String username,
                    @Query("page") String page,
                    @Query("per_page") String perPage,
                    ApiCallback<List<TimeLineEvent>> callback);

    @GET("/repos/{owner}/{repo}/events")
    void repoEvents(@Path("owner") String owner,
                    @Path("repo") String repoName,
                    ApiCallback<List<TimeLineEvent>> callback);

    @GET("/user/orgs")
    void getOrg(ApiCallback<List<Organzation>> callback);

    @GET("/users/{username}/orgs")
    void getUserOrgs(@Path("username") String username, ApiCallback<List<Organzation>> callback);

}
