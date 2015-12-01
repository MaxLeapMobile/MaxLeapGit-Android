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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
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
    private TextView mUsernameTv;
    private TextView mLoginTv;

    private RelativeLayout mGene;
    private RelativeLayout mFollowers;
    private RelativeLayout mFollowings;
    private RelativeLayout mRepos;
    private RelativeLayout mStars;
    private RelativeLayout mOrgs;

    private TextView mFollowersTv;
    private TextView mFollowingsTv;
    private TextView mReposTv;
    private TextView mStarsTv;

    private TextView mLocation;
    private TextView mCompany;
    private TextView mJoinIn;
    private TextView mOrgsTv;

    private String mUsername;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinfo, container, false);
        mUsername = getArguments().getString(Const.USERNAME);
        initViews(view);
        getUserInfo();
        getStarCount();
        getOrgs();

        return view;
    }

    private void initViews(View view) {
        mAvatar = (ImageView) view.findViewById(R.id.mine_avatar);
        mUsernameTv = (TextView)view.findViewById(R.id.mine_username);
        mLoginTv = (TextView) view.findViewById(R.id.mine_login);

        mGene = (RelativeLayout) view.findViewById(R.id.userinfo_gene);
        mFollowers = (RelativeLayout) view.findViewById(R.id.mine_followers_layout);
        mFollowings = (RelativeLayout) view.findViewById(R.id.mine_followering_layout);
        mRepos = (RelativeLayout) view.findViewById(R.id.mine_repos_layout);
        mStars = (RelativeLayout) view.findViewById(R.id.mine_stars_layout);
        mOrgs = (RelativeLayout) view.findViewById(R.id.userinfo_orgs);

        mFollowersTv = (TextView) view.findViewById(R.id.mine_followers);
        mFollowingsTv = (TextView) view.findViewById(R.id.mine_followings);
        mReposTv = (TextView) view.findViewById(R.id.mine_repos);
        mStarsTv = (TextView) view.findViewById(R.id.mine_stars);

        mLocation = (TextView) view.findViewById(R.id.mine_location);
        mCompany = (TextView) view.findViewById(R.id.mine_company);
        mJoinIn = (TextView) view.findViewById(R.id.mine_join_in);
        mOrgsTv = (TextView) view.findViewById(R.id.mine_orgs);

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

    private void getStarCount() {
        ApiManager.getInstance().countStar(mUsername, new ApiCallback<retrofit.Response<ResponseBody>>() {
            @Override
            public void onSuccess(retrofit.Response<ResponseBody> response) {
                Headers headers = response.headers();
                String link = headers.get("Link");
                PageLinks pageLinks = new PageLinks(link);
                mStarsTv.setText(pageLinks.getPage() + "");
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
                setUserData(user);
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
                mOrgsTv.setText("");
                for (Organzation organzation : organzations) {
                    mOrgsTv.append(organzation.getLogin() + " ");
                }
            }

            @Override
            public void onFail(Throwable throwable) {

            }
        });

    }

    private void setUserData(User user) {
        if (user == null) {
            return;
        }

        Picasso.with(getContext())
                .load(user.getAvatarUrl())
                .centerCrop().fit()
                .transform(new CircleTransform())
                .placeholder(R.mipmap.ic_user_portrait_big)
                .into(mAvatar);
        mUsernameTv.setText(user.getName());
        mLoginTv.setText(user.getLogin());
        mLocation.setText(user.getLocation());
        mCompany.setText(user.getCompany());
        mJoinIn.setText(user.getCreateAt().substring(0, 10));

        mFollowersTv.setText(user.getFollowers() + "");
        mFollowingsTv.setText(user.getFollowing() + "");
        mReposTv.setText(user.getPublicRepos() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userinfo_gene:
                Intent geneIntent = new Intent(getActivity(), GeneActivity.class);
                startActivity(geneIntent);
                break;
            case R.id.mine_followers_layout:
                Intent followersIntent = new Intent(getActivity(), ContainerActivity.class);
                followersIntent.putExtra(ContainerActivity.INTENT_KEY_TITLE, getActivity().getString(R.string.mine_label_followers));
                followersIntent.putExtra(ContainerActivity.INTENT_KEY_USERNAME, mUsername);
                startActivity(followersIntent);
                break;
            case R.id.mine_followering_layout:
                Intent followingIntent = new Intent(getActivity(), ContainerActivity.class);
                followingIntent.putExtra(ContainerActivity.INTENT_KEY_TITLE, getActivity().getString(R.string.mine_label_following));
                followingIntent.putExtra(ContainerActivity.INTENT_KEY_USERNAME, mUsername);
                startActivity(followingIntent);
                break;
            case R.id.mine_repos_layout:
                Intent repoIntent = new Intent(getActivity(), ContainerActivity.class);
                repoIntent.putExtra(ContainerActivity.INTENT_KEY_TITLE, getActivity().getString(R.string.mine_label_repos));
                repoIntent.putExtra(ContainerActivity.INTENT_KEY_USERNAME, mUsername);
                startActivity(repoIntent);
                break;
            case R.id.mine_stars_layout:
                Intent starIntent = new Intent(getActivity(), ContainerActivity.class);
                starIntent.putExtra(ContainerActivity.INTENT_KEY_TITLE, getActivity().getString(R.string.mine_label_stars));
                starIntent.putExtra(ContainerActivity.INTENT_KEY_USERNAME, mUsername);
                startActivity(starIntent);
                break;
            case R.id.userinfo_orgs:
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