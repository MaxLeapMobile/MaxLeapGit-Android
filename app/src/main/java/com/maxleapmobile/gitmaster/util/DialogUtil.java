/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.model.Radio;
import com.maxleapmobile.gitmaster.model.SortEnumRepo;
import com.maxleapmobile.gitmaster.model.SortEnumUser;
import com.maxleapmobile.gitmaster.ui.adapter.RadioAdapter;
import com.maxleapmobile.gitmaster.ui.widget.BaseDialog;

import java.util.ArrayList;

public class DialogUtil {

    public interface CheckListener {
        void onCheck(int checkedId);
    }

    public static void showRepoSortByDialog(Context context, SortEnumRepo sortEnumRepo, final CheckListener listener) {
        final Dialog dialog = new BaseDialog(context, R.style.CustomizeDialog);
        dialog.setContentView(R.layout.dialog_sort_by);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.dialog_radio_group);
        TextView cancelView = (TextView) dialog.findViewById(R.id.dialog_cancel);
        int checkedId = 0;
        if (sortEnumRepo == SortEnumRepo.DEFAULT) {
            checkedId = R.id.dialog_radio_1;
        } else if (sortEnumRepo == SortEnumRepo.STARS) {
            checkedId = R.id.dialog_radio_2;
        } else if (sortEnumRepo == SortEnumRepo.FORKS) {
            checkedId = R.id.dialog_radio_3;
        } else if (sortEnumRepo == SortEnumRepo.UPDATED) {
            checkedId = R.id.dialog_radio_4;
        }
        radioGroup.check(checkedId);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (listener != null) {
                    listener.onCheck(checkedId);
                }
                dialog.dismiss();
            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showUserSortByDialog(Context context, SortEnumUser sortEnumUser, final CheckListener listener) {
        final Dialog dialog = new BaseDialog(context, R.style.CustomizeDialog);
        dialog.setContentView(R.layout.dialog_sort_by);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.dialog_radio_group);
        TextView cancelView = (TextView) dialog.findViewById(R.id.dialog_cancel);
        ((RadioButton) dialog.findViewById(R.id.dialog_radio_2)).setText(R.string.dialog_sort_most_followers);
        ((RadioButton) dialog.findViewById(R.id.dialog_radio_3)).setText(R.string.dialog_sort_most_repositories);
        ((RadioButton) dialog.findViewById(R.id.dialog_radio_4)).setText(R.string.dialog_sort_most_joined);

        int checkedId = 0;
        if (sortEnumUser == SortEnumUser.DEFAULT) {
            checkedId = R.id.dialog_radio_1;
        } else if (sortEnumUser == SortEnumUser.FOLLOWERS) {
            checkedId = R.id.dialog_radio_2;
        } else if (sortEnumUser == SortEnumUser.RESPOSITORIES) {
            checkedId = R.id.dialog_radio_3;
        } else if (sortEnumUser == SortEnumUser.JOINED) {
            checkedId = R.id.dialog_radio_4;
        }
        radioGroup.check(checkedId);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (listener != null) {
                    listener.onCheck(checkedId);
                }
                dialog.dismiss();
            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showGeneDialog(Context context, ArrayList<Radio> radios, final CheckListener listener) {
        final Dialog dialog = new BaseDialog(context, R.style.CustomizeDialog);
        dialog.setContentView(R.layout.dialog_gene);

        ListView listView = (ListView) dialog.findViewById(R.id.list_view);
        listView.setAdapter(new RadioAdapter(context, radios));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onCheck(position);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
