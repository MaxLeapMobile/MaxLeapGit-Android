/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.ui.fragment.RepoFragment;
import com.maxleapmobile.gitmaster.ui.fragment.UserFragment;

public class SearchPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public SearchPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = RepoFragment.newInstance(RepoFragment.FLAG_SEARCH, null);
                break;
            case 1:
                fragment = UserFragment.newInstance(UserFragment.FLAG_SEARCH, null);
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.activity_search_repo);
        } else if (position == 1) {
            return mContext.getString(R.string.activity_search_user);
        }
        return super.getPageTitle(position);
    }
}
