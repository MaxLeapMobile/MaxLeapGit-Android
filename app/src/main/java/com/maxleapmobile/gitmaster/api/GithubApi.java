/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.api;


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

import retrofit.Call;
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
    @POST("https://github.com/login/oauth/access_token")
    Call<AccessToken> getAccessToken(@Field("client_id") String clientId,
                        @Field("client_secret") String secret,
                        @Field("redirect_uri") String callbackUrl,
                        @Field("code") String code);

    @GET("user")
    Call<User> getCurrentUser();

    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);

    @GET("users/{username}/repos")
    Call<List<Repo>> listRepos(@Path("username") String username,
                   @Query("page") String pageCount,
                   @Query("per_page") String perPageCount);

    @GET("repos/{username}/{repo}")
    Call<Repo> getRepo(@Path("username") String username,
                 @Path("repo") String repo);

    @GET("user/starred/{owner}/{repo}")
    Call<Object> starStatus(@Path("owner") String owner,
                    @Path("repo") String repo);

    @PUT("user/starred/{owner}/{repo}")
    Call<Object> star(@Body String emptyString,
              @Path("owner") String owner,
              @Path("repo") String repo);

    @DELETE("user/starred/{owner}/{repo}")
    Call<Object> unstar(@Path("owner") String owner,
                @Path("repo") String repo);

    @GET("users/{username}/starred")
    Call<List<Repo>> listStarredRepoByUser(@Path("username") String username,
                               @Query("page") String pageCount,
                               @Query("per_page") String perPageCount);

    @GET("/user/starred")
    Call<List<Repo>> listStaredRepoByAuthUser(@Query("page") String pageCount,
                                  @Query("per_page") String perPageCount);

    @POST("repos/{owner}/{repo}/forks")
    Call<ForkRepo> forkRepo(
                  @Path("owner") String owner,
                  @Path("repo") String repo);

    @GET("search/repositories")
    Call<SearchedRepos> searchRepo(@Query("q") String keyword,
                    @Query("sort") SortEnumRepo sort,
                    @Query("order") OrderEnum order,
                    @Query("page") String page,
                    @Query("per_page") String perPage);

    @GET("search/users")
    Call<SearchedUsers> searchUser(@Query("q") String username,
                    @Query("sort") SortEnumUser sort,
                    @Query("order") OrderEnum order,
                    @Query("page") String page,
                    @Query("per_page") String perPage);

    @GET("user/following/{username}")
    Call<Object> followStatus(@Path("username") String username);

    @PUT("user/following/{username}")
    Call<Object> follow(@Body String emptyBody,
                @Path("username") String username);

    @DELETE("user/following/{username}")
    Call<Object> unfollow(@Path("username") String username);

    @GET("users/{username}/followers")
    Call<List<Owner>> getFollowersList(@Path("username") String username,
                          @Query("page") String page,
                          @Query("per_page") String perPage);

    @GET("users/{username}/following")
    Call<List<Owner>> getFollowingList(@Path("username") String username,
                          @Query("page") String page,
                          @Query("per_page") String perPage);

    @GET("users/{username}/received_events")
    Call<List<TimeLineEvent>> receivedEvents(@Path("username") String username,
                        @Query("page") String page,
                        @Query("per_page") String perPage);

    @GET("users/{username}/events")
    Call<List<TimeLineEvent>> userEvents(@Path("username") String username,
                    @Query("page") String page,
                    @Query("per_page") String perPage);

    @GET("repos/{owner}/{repo}/events")
    Call<List<TimeLineEvent>> repoEvents(@Path("owner") String owner,
                    @Path("repo") String repoName);

    @GET("user/orgs")
    Call<List<Organzation>> getOrg();

    @GET("users/{username}/orgs")
    Call<List<Organzation>> getUserOrgs(@Path("username") String username);

}
