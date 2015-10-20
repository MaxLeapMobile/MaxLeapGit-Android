/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.model;


import android.net.Uri;
import android.text.TextUtils;

import com.maxleapmobile.gitmaster.util.Const;

public class PageLinks {

    private static final String DELIM_LINKS = ","; //$NON-NLS-1$

    private static final String DELIM_LINK_PARAM = ";"; //$NON-NLS-1$

    private String first;
    private String last;
    private String next;
    private String prev;
    private String page = "";

    public PageLinks(String linkHeader) {
        String[] links = linkHeader.split(DELIM_LINKS);
        for (String link : links) {
            String[] segments = link.split(DELIM_LINK_PARAM);
            if (segments.length < 2)
                continue;

            String linkPart = segments[0].trim();
            if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) //$NON-NLS-1$ //$NON-NLS-2$
                continue;
            linkPart = linkPart.substring(1, linkPart.length() - 1);

            for (int i = 1; i < segments.length; i++) {
                String[] rel = segments[i].trim().split("="); //$NON-NLS-1$
                if (rel.length < 2 || !Const.META_REL.equals(rel[0]))
                    continue;

                String relValue = rel[1];
                if (relValue.startsWith("\"") && relValue.endsWith("\"")) //$NON-NLS-1$ //$NON-NLS-2$
                    relValue = relValue.substring(1, relValue.length() - 1);

                if (Const.META_FIRST.equals(relValue))
                    first = linkPart;
                else if (Const.META_LAST.equals(relValue))
                    last = linkPart;
                else if (Const.META_NEXT.equals(relValue))
                    next = linkPart;
                else if (Const.META_PREV.equals(relValue))
                    prev = linkPart;
            }
        }

    }

    /**
     * @return first
     */
    public String getFirst() {
        return first;
    }

    /**
     * @return last
     */
    public String getLast() {
        return last;
    }

    /**
     * @return next
     */
    public String getNext() {
        return next;
    }

    /**
     * @return prev
     */
    public String getPrev() {
        return prev;
    }

    public void setPage(String pageCount) {
        this.page = pageCount;
    }

    public String getPage() {
        if (!TextUtils.isEmpty(last)) {
            Uri uri = Uri.parse(last);
            page = uri.getQueryParameter("page");
        }
        return page;
    }
}