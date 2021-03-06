/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.fragment;

import android.content.Intent;
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
import com.maxleapmobile.gitmaster.model.User;
import com.maxleapmobile.gitmaster.ui.activity.ContainerActivity;
import com.maxleapmobile.gitmaster.ui.activity.GeneActivity;
import com.maxleapmobile.gitmaster.util.CircleTransform;
import com.maxleapmobile.gitmaster.util.Const;
import com.maxleapmobile.gitmaster.util.PreferenceUtil;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MineFragment extends Fragment implements View.OnClickListener {

    private ImageView mAvatar;

    private CardView mGene;
    private CardView mFollowers;
    private CardView mFollowings;
    private CardView mRepos;
    private CardView mStars;
    private CardView mOrgs;

    private FragmentUserinfoBinding mUserinfoBinding;
    private String mUsername;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinfo, container, false);
        mUserinfoBinding = DataBindingUtil.bind(view);
        mUsername = getArguments().getString(Const.USERNAME);
        initViews(view);
        getUserInfo();
        getStarCount();

        getOrgs();

        return view;
    }

    private void getStarCount() {
        ApiManager.getInstance().countStar(mUsername, new ApiCallback<retrofit.Response<ResponseBody>>() {
            @Override
            public void onSuccess(retrofit.Response<ResponseBody> response) {
                Headers headers = response.headers();
                String link = headers.get("Link");
                PageLinks pageLinks = new PageLinks(link);
                mUserinfoBinding.setPagelinks(pageLinks);
            }

            @Override
            public void onFail(Throwable throwable) {

            }
        });
    }

    private void getUserInfo() {
        ApiManager.getInstance().getUser(mUsername, new ApiCallback<User>() {
            @Override
            public void onSuccess(User user) {
                mUserinfoBinding.setUser(user);
                Picasso.with(getContext())
                        .load(user.getAvatarUrl())
                        .centerCrop().fit()
                        .transform(new CircleTransform())
                        .placeholder(R.mipmap.ic_user_portrait_big)
                        .into(mAvatar);
            }

            @Override
            public void onFail(Throwable throwable) {

            }
        });
    }

    private void getOrgs() {
        ApiManager.getInstance().getOrgs(mUsername, new ApiCallback<List<Organzation>>() {
            @Override
            public void onSuccess(List<Organzation> organzations) {
                String orgs = "";
                for (Organzation organzation : organzations) {
                    orgs += organzation.getLogin() + " ";
                }
                mUserinfoBinding.setOrgs(orgs);
            }

            @Override
            public void onFail(Throwable throwable) {

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
        mOrgs = (CardView) view.findViewById(R.id.mine_card_orgs);

        if (!mUsername.equals(PreferenceUtil.getString(getContext(), Const.USERNAME, ""))) {
            mGene.setVisibility(View.GONE);
        }

        mGene.setOnClickListener(this);
        mFollowers.setOnClickListener(this);
        mFollowings.setOnClickListener(this);
        mRepos.setOnClickListener(this);
        mStars.setOnClickListener(this);
        mOrgs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_card_gene:
                Intent geneIntent = new Intent(getActivity(), GeneActivity.class);
                startActivity(geneIntent);
                break;
            case R.id.mine_card_followers:
                Intent followersIntent = new Intent(getActivity(), ContainerActivity.class);
                followersIntent.putExtra(ContainerActivity.INTENT_KEY_TITLE, getActivity().getString(R.string.mine_label_followers));
                followersIntent.putExtra(ContainerActivity.INTENT_KEY_USERNAME, mUsername);
                startActivity(followersIntent);
                break;
            case R.id.mine_card_followerings:
                Intent followingIntent = new Intent(getActivity(), ContainerActivity.class);
                followingIntent.putExtra(ContainerActivity.INTENT_KEY_TITLE, getActivity().getString(R.string.mine_label_following));
                followingIntent.putExtra(ContainerActivity.INTENT_KEY_USERNAME, mUsername);
                startActivity(followingIntent);
                break;
            case R.id.mine_card_repos:
                Intent repoIntent = new Intent(getActivity(), ContainerActivity.class);
                repoIntent.putExtra(ContainerActivity.INTENT_KEY_TITLE, getActivity().getString(R.string.mine_label_repos));
                repoIntent.putExtra(ContainerActivity.INTENT_KEY_USERNAME, mUsername);
                startActivity(repoIntent);
                break;
            case R.id.mine_card_stars:
                Intent starIntent = new Intent(getActivity(), ContainerActivity.class);
                starIntent.putExtra(ContainerActivity.INTENT_KEY_TITLE, getActivity().getString(R.string.mine_label_stars));
                starIntent.putExtra(ContainerActivity.INTENT_KEY_USERNAME, mUsername);
                startActivity(starIntent);
                break;
            case R.id.mine_card_orgs:
                Intent orgIntent = new Intent(getActivity(), ContainerActivity.class);
                orgIntent.putExtra(ContainerActivity.INTENT_KEY_TITLE, getActivity().getString(R.string.mine_label_organzation));
                orgIntent.putExtra(ContainerActivity.INTENT_KEY_USERNAME, mUsername);
                startActivity(orgIntent);
                break;
            default:
                break;
        }
    }
}