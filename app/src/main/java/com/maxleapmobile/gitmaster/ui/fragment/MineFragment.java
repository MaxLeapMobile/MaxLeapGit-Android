/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.databinding.FragmentUserinfoBinding;
import com.maxleapmobile.gitmaster.model.Organzation;
import com.maxleapmobile.gitmaster.model.PageLinks;
import com.maxleapmobile.gitmaster.model.Repo;
import com.maxleapmobile.gitmaster.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

public class MineFragment extends Fragment implements View.OnClickListener {

    private ImageView mAvatar;

    private CardView mGene;
    private CardView mFollowers;
    private CardView mFollowings;
    private CardView mRepos;
    private CardView mStars;

    private FragmentUserinfoBinding mUserinfoBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinfo, container, false);
        mUserinfoBinding = DataBindingUtil.bind(view);
        initViews(view);
        getUserInfo();
        getStarCount();

        ApiManager.getInstance().getUserOrgs("daimajia", new ApiCallback<List<Organzation>>() {
            @Override
            public void success(List<Organzation> organzations, Response response) {
                String orgs = "";
                for (Organzation organzation : organzations) {
                    orgs += organzation.getLogin() + " ";
                }
                mUserinfoBinding.setOrgs(orgs);
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
            }
        });

        return view;
    }

    private void getStarCount() {
        ApiManager.getInstance().listStarredRepoByAuthUser(1, 1, new ApiCallback<List<Repo>>() {
            @Override
            public void success(List<Repo> repos, Response response) {
                List<Header> headers = response.getHeaders();
                for (Header header : headers) {
                    if (header.getName().equals("Link")) {
                        PageLinks pageLinks = new PageLinks(header.getValue());
                        mUserinfoBinding.setPagelinks(pageLinks);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
            }
        });
    }

    private void getUserInfo() {
        ApiManager.getInstance().getUser("awind", new ApiCallback<User>() {
            @Override
            public void success(User user, Response response) {
                mUserinfoBinding.setUser(user);
                Picasso.with(getContext())
                        .load(user.getAvatarUrl())
                        .centerCrop().fit()
                        .into(mAvatar);
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
            }
        });
    }

    private void initViews(View view) {
        mAvatar = (ImageView) view.findViewById(R.id.mine_avatar);

        mGene = (CardView) view.findViewById(R.id.mine_card_gene);
        mFollowers = (CardView) view.findViewById(R.id.mine_card_followers);
        mFollowings = (CardView) view.findViewById(R.id.mine_card_followerings);
        mRepos = (CardView) view.findViewById(R.id.mine_card_repos);
        mStars = (CardView) view.findViewById(R.id.mine_card_stars);

        mGene.setOnClickListener(this);
        mFollowers.setOnClickListener(this);
        mFollowings.setOnClickListener(this);
        mRepos.setOnClickListener(this);
        mStars.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}