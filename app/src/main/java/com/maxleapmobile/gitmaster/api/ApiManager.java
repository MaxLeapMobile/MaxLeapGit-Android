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
import com.maxleapmobile.gitmaster.model.Organzation;
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
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiManager {
    private static final String USER_AGENT = "GitMaster Android";
    private static final String ACCEPT = "application/vnd.github.v3+json";

    private static final String API_URL = "https://api.github.com/";

    private static ApiManager mInstance;

    private Context mContext;
    private Retrofit mRetrofit;
    private GithubApi mGithubApi;
    private String mAccessToken;

    private ApiManager() {
        mContext = GithubApplication.getInstance();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                mAccessToken = PreferenceUtil.getString(mContext,
                        Const.ACCESS_TOKEN_KEY, null);
                HttpUrl url;
                if (mAccessToken != null) {
                    url = chain.request().httpUrl()
                            .newBuilder()
                            .addQueryParameter("access_token", mAccessToken)
                            .build();
                } else {
                    url = chain.request().httpUrl();
                }

                Request request = chain.request();
                Request newRequest;
                newRequest = request.newBuilder()
                        .url(url)
                        .addHeader("User-Agent", USER_AGENT)
                        .addHeader("Accept", ACCEPT)
                        .build();
                return chain.proceed(newRequest);
            }
        });

        okHttpClient.interceptors().add(new LoggingInterceptor());

        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mGithubApi = mRetrofit.create(GithubApi.class);
    }


    public static ApiManager getInstance() {
        if (mInstance == null) {
            mInstance = new ApiManager();
        }
        return mInstance;
    }

    public GithubApi getGithubApi() {
        return mGithubApi;
    }

    /**
     * Get authenticated user info
     * @param callback
     */
    public void getCurrentUser(final ApiCallback callback) {
        mGithubApi.getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(User user) {
                        callback.onSuccess(user);
                    }
                });
    }

    /**
     * Get specified user info
     * @param username
     * @param callback
     */
    public void getUser(String username, final ApiCallback<User> callback) {
        mGithubApi.getUser(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(User user) {
                        callback.onSuccess(user);
                    }
                });
    }

    public void getRepo(String username, String repoName, final ApiCallback<Repo> callback) {
        mGithubApi.getRepo(username, repoName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Repo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(Repo repo) {
                        callback.onSuccess(repo);
                    }
                });
    }

    /**
     * List user's repos
     * @param username
     * @param callback
     */
    public void listRepos(String username, final ApiCallback<List<Repo>> callback) {
        mGithubApi.listRepos(username, null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Repo> repos) {
                        callback.onSuccess(repos);
                    }
                });
    }

    /**
     * List user's repos by page
     * @param username
     * @param pageCount current page
     * @param perPageCount result per page
     * @param callback
     */
    public void listReposByPage(String username, int pageCount, int perPageCount,
                                final ApiCallback<List<Repo>> callback) {
        mGithubApi.listRepos(username, String.valueOf(pageCount),
                String.valueOf(perPageCount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Repo> repos) {
                        callback.onSuccess(repos);
                    }
                });
    }


    /**
     * Get repo star status
     * already starrd:204 not starred:404
     * @param owner repo owner
     * @param repo repo name
     * @param callback callback
     */
    public void isStarred(String owner, String repo, final ApiCallback<Object> callback) {
        mGithubApi.starStatus(owner, repo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        callback.onSuccess(o);
                    }
                });
    }

    /**
     * Star a repo
     * success:204 fail:404
     * @param owner repo owner
     * @param repo repo name
     * @param callback callback
     */
    public void star(String owner, String repo, final ApiCallback<Object> callback) {
        mGithubApi.star("", owner, repo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        callback.onSuccess(o);
                    }
                });
    }

    /**
     * Unstar a repo
     * @param owner repo owner
     * @param repo repo name
     * @param callback callback
     */
    public void unstart(String owner, String repo, final ApiCallback<Object> callback) {
        mGithubApi.unstar(owner, repo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        callback.onSuccess(o);
                    }
                });
    }

    /**
     * List starred repo by specified user
     * @param username
     * @param callback
     */
    public void listStarRepoByUser(String username, final ApiCallback<List<Repo>> callback) {
        mGithubApi.listStarredRepoByUser(username, null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Repo> repos) {
                        callback.onSuccess(repos);
                    }
                });
    }

    /**
     * List starred repo by specified user by page
     * @param username
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void listStarRepoByUser(String username, int pageCount, int perPageCount,
                                   final ApiCallback<List<Repo>> callback) {
        mGithubApi.listStarredRepoByUser(username, String.valueOf(pageCount),
                String.valueOf(perPageCount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Repo> repos) {
                        callback.onSuccess(repos);
                    }
                });
    }

    public void countStar(String username,
                          final ApiCallback<retrofit.Response<ResponseBody>> callback) {
        mGithubApi.countStar(username, "1", "1")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<retrofit.Response<ResponseBody>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(retrofit.Response<ResponseBody> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    /**
     * List starred repo by authenticated user
     * @param callback
     */
    public void listStarredRepoByAuthUser(final ApiCallback<List<Repo>> callback) {
        mGithubApi.listStaredRepoByAuthUser(null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Repo> repos) {
                        callback.onSuccess(repos);
                    }
                });
    }


    /**
     * List starred repo by authenticated user by page
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void listStarredRepoByAuthUser(int pageCount, int perPageCount,
                                          final ApiCallback<List<Repo>> callback) {
        mGithubApi.listStaredRepoByAuthUser(String.valueOf(pageCount),
                String.valueOf(perPageCount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Repo> repos) {
                        callback.onSuccess(repos);
                    }
                });
    }

    /**
     * Fork a repo for authenticated user
     * @param owner repo owner
     * @param repo repo name
     * @param callback
     */
    public void fork(String owner, String repo, final ApiCallback<ForkRepo> callback) {
        mGithubApi.forkRepo(owner, repo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ForkRepo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(ForkRepo forkRepo) {
                        callback.onSuccess(forkRepo);
                    }
                });
    }

    /**
     * Search repos, best match, desc, default page config
     * @param keyword repo name
     * @param callback
     */
    public void searchRepo(String keyword, final ApiCallback<SearchedRepos> callback) {
        mGithubApi.searchRepo(keyword, null, null, null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchedRepos>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(SearchedRepos searchedRepos) {
                        callback.onSuccess(searchedRepos);
                    }
                });
    }


    /**
     * Search repos by page, best match and desc order
     * @param keyword
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void searchRepo(String keyword, int pageCount, int perPageCount,
                           final ApiCallback<SearchedRepos> callback) {
        mGithubApi.searchRepo(keyword, null, null, String.valueOf(pageCount),
                String.valueOf(perPageCount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchedRepos>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(SearchedRepos searchedRepos) {
                        callback.onSuccess(searchedRepos);
                    }
                });
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
                           final ApiCallback<SearchedRepos> callback) {
        mGithubApi.searchRepo(keyword, sort, order, String.valueOf(pageCount),
                String.valueOf(perPageCount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchedRepos>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(SearchedRepos searchedRepos) {
                        callback.onSuccess(searchedRepos);
                    }
                });
    }

    /**
     * Search users, best match, desc order and default page config
     * @param username
     * @param callback
     */
    public void searchUser(String username, final ApiCallback<SearchedUsers> callback) {
        mGithubApi.searchUser(username, null, null, null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchedUsers>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(SearchedUsers searchedUsers) {
                        callback.onSuccess(searchedUsers);
                    }
                });
    }

    /**
     * Search users, best match and desc order
     * @param username
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void searchUser(String username, int pageCount, int perPageCount,
                           final ApiCallback<SearchedUsers> callback) {
        mGithubApi.searchUser(username, null, null, String.valueOf(pageCount),
                String.valueOf(perPageCount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchedUsers>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(SearchedUsers searchedUsers) {
                        callback.onSuccess(searchedUsers);
                    }
                });
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
                           final ApiCallback<SearchedUsers> callback) {
        mGithubApi.searchUser(username, sortEnumUser, orderEnum, String.valueOf(pageCount),
                String.valueOf(perPageCount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchedUsers>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(SearchedUsers searchedUsers) {
                        callback.onSuccess(searchedUsers);
                    }
                });
    }

    /**
     * Get follow status
     * Not followed : 404, already followed : 204
     * @param username specified user
     * @param callback
     */
    public void followStatus(String username, final ApiCallback<Object> callback) {
        mGithubApi.followStatus(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        callback.onSuccess(o);

                    }
                });
    }

    /**
     * follow a user
     * success : 204 fail : 404
     * @param username
     * @param callback
     */
    public void follow(String username, final ApiCallback<Object> callback) {
        mGithubApi.follow("", username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        callback.onSuccess(o);
                    }
                });
    }

    /**
     * unfollow a user
     * success : 204, fail : 404
     * @param username
     * @param callback
     */
    public void unfollow(String username, final ApiCallback<Object> callback) {
        mGithubApi.unfollow(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        callback.onSuccess(o);
                    }
                });
    }

    /**
     * Get specified user's followers list
     * @param username
     * @param callback
     */
    public void getFollowersList(String username, final ApiCallback<List<Owner>> callback) {
        mGithubApi.getFollowersList(username, null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Owner>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Owner> owners) {
                        callback.onSuccess(owners);
                    }
                });
    }

    /**
     * Get specified user's followers list by page
     * @param username
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void getFollowersList(String username, int pageCount, int perPageCount,
                                 final ApiCallback<List<Owner>> callback) {
        mGithubApi.getFollowersList(username, String.valueOf(pageCount),
                String.valueOf(perPageCount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Owner>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Owner> owners) {
                        callback.onSuccess(owners);
                    }
                });
    }

    /**
     * Get specified user's following list
     * @param username
     * @param callback
     */
    public void getFollowingList(String username, final ApiCallback<List<Owner>> callback) {
        mGithubApi.getFollowingList(username, null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Owner>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Owner> owners) {
                        callback.onSuccess(owners);
                    }
                });
    }

    /**
     * Get specified user's following list by page
     * @param username
     * @param callback
     */
    public void getFollowingList(String username, int pageCount, int perPageCount,
                                 final ApiCallback<List<Owner>> callback) {
        mGithubApi.getFollowingList(username, String.valueOf(pageCount),
                String.valueOf(perPageCount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Owner>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Owner> owners) {
                        callback.onSuccess(owners);
                    }
                });
    }

    /**
     * List public events that a user has received
     * @param username
     * @param callback
     */
    public void getReceivedEvents(String username, final ApiCallback<List<TimeLineEvent>> callback) {
        mGithubApi.receivedEvents(username, null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TimeLineEvent>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<TimeLineEvent> timeLineEvents) {
                        callback.onSuccess(timeLineEvents);
                    }
                });
    }

    /**
     * List public events that a user has received by page
     * @param username
     * @param pageCount
     * @param perPageCount
     * @param callback
     */
    public void getReceivedEvents(String username, int pageCount, int perPageCount,
                                  final ApiCallback<List<TimeLineEvent>> callback) {
        mGithubApi.receivedEvents(username, String.valueOf(pageCount),
                String.valueOf(perPageCount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TimeLineEvent>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<TimeLineEvent> timeLineEvents) {
                        callback.onSuccess(timeLineEvents);
                    }
                });
    }

    /**
     * List events performed by a user
     * @param username
     * @param callback
     */
    public void getUserEvents(String username, final ApiCallback<List<TimeLineEvent>> callback) {
        mGithubApi.userEvents(username, null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TimeLineEvent>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<TimeLineEvent> timeLineEvents) {
                        callback.onSuccess(timeLineEvents);
                    }
                });
    }

    /**
     * List specified user's performed events by page
     * @param username
     * @param callback
     */
    public void getUserEvents(String username, int pageCount, int perPageCount,
                              final ApiCallback<List<TimeLineEvent>> callback) {
        mGithubApi.userEvents(username, String.valueOf(pageCount),
                String.valueOf(perPageCount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TimeLineEvent>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<TimeLineEvent> timeLineEvents) {
                        callback.onSuccess(timeLineEvents);
                    }
                });
    }

    /**
     * List repository events
     * @param owner repo's owner
     * @param repoName repo name
     * @param callback
     */
    public void getRepoEvents(String owner, String repoName,
                              final ApiCallback<List<TimeLineEvent>> callback) {
        mGithubApi.repoEvents(owner, repoName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TimeLineEvent>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<TimeLineEvent> timeLineEvents) {
                        callback.onSuccess(timeLineEvents);
                    }
                });
    }

    /**
     * get org list for auth user
     * @param callback
     */
    public void getAuthUserOrg(final ApiCallback<List<Organzation>> callback) {
        mGithubApi.getOrg()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Organzation>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Organzation> organzations) {
                        callback.onSuccess(organzations);
                    }
                });
    }

    /**
     * get org list for specified user
     * @param username
     * @param callback
     */
    public void getUserOrgs(String username, final ApiCallback<List<Organzation>> callback) {
        mGithubApi.getUserOrgs(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Organzation>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Organzation> organzations) {
                        callback.onSuccess(organzations);
                    }
                });
    }

    public void getOrgs(String username, ApiCallback<List<Organzation>> callback) {
        if (PreferenceUtil.getString(mContext, Const.USERNAME, "").equals(username)) {
            getAuthUserOrg(callback);
        } else {
            getUserOrgs(username, callback);
        }
    }

}