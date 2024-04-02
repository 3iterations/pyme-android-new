/*
 * Nextcloud - Android Client
 *
 * SPDX-FileCopyrightText: 2018 Andy Scherzinger <info@andy-scherzinger.de>
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package com.owncloud.android.ui.adapter;

import com.nextcloud.client.account.User;
import com.nextcloud.ui.ImageDetailFragment;
import com.owncloud.android.datamodel.OCFile;
import com.owncloud.android.ui.fragment.FileDetailActivitiesFragment;
import com.owncloud.android.ui.fragment.FileDetailSharingFragment;
import com.owncloud.android.utils.MimeTypeUtil;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * File details pager adapter.
 */
public class FileDetailTabAdapter extends FragmentStatePagerAdapter {
    private final OCFile file;
    private final User user;
    private final boolean showSharingTab;

    private FileDetailSharingFragment fileDetailSharingFragment;
    private FileDetailActivitiesFragment fileDetailActivitiesFragment;
    private ImageDetailFragment imageDetailFragment;

    public FileDetailTabAdapter(FragmentManager fm,
                                OCFile file,
                                User user,
                                boolean showSharingTab) {
        super(fm);
        this.file = file;
        this.user = user;
        this.showSharingTab = showSharingTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            default:
                fileDetailActivitiesFragment = FileDetailActivitiesFragment.newInstance(file, user);
                return fileDetailActivitiesFragment;
            case 1:
                fileDetailSharingFragment = FileDetailSharingFragment.newInstance(file, user);
                return fileDetailSharingFragment;
            case 2:
                imageDetailFragment = ImageDetailFragment.newInstance(file, user);
                return imageDetailFragment;
        }
    }

    public FileDetailSharingFragment getFileDetailSharingFragment() {
        return fileDetailSharingFragment;
    }

    public FileDetailActivitiesFragment getFileDetailActivitiesFragment() {
        return fileDetailActivitiesFragment;
    }

    public ImageDetailFragment getImageDetailFragment() {
        return imageDetailFragment;
    }

    @Override
    public int getCount() {
        if (showSharingTab) {
            if (MimeTypeUtil.isImage(file)) {
                return 3;
            }
            return 2;
        } else {
            if (MimeTypeUtil.isImage(file)) {
                return 2;
            }
            return 1;
        }
    }
}
