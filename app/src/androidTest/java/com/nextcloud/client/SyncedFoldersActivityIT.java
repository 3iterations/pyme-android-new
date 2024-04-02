/*
 * Nextcloud - Android Client
 *
 * SPDX-FileCopyrightText: 2020 Tobias Kaminsky <tobias@kaminsky.me>
 * SPDX-FileCopyrightText: 2020 Nextcloud GmbH
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package com.nextcloud.client;

import android.app.Activity;
import android.content.Intent;

import com.nextcloud.client.preferences.SubFolderRule;
import com.owncloud.android.AbstractIT;
import com.owncloud.android.databinding.SyncedFoldersLayoutBinding;
import com.owncloud.android.datamodel.MediaFolderType;
import com.owncloud.android.datamodel.SyncedFolderDisplayItem;
import com.owncloud.android.ui.activity.SyncedFoldersActivity;
import com.owncloud.android.ui.dialog.SyncedFolderPreferencesDialogFragment;
import com.owncloud.android.utils.ScreenshotTest;

import org.junit.Rule;
import org.junit.Test;

import java.util.Objects;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


public class SyncedFoldersActivityIT extends AbstractIT {
    @Rule public IntentsTestRule<SyncedFoldersActivity> activityRule = new IntentsTestRule<>(SyncedFoldersActivity.class,
                                                                                             true,
                                                                                             false);

    @Test
    @ScreenshotTest
    public void open() {
        SyncedFoldersActivity activity = activityRule.launchActivity(null);
        activity.adapter.clear();
        SyncedFoldersLayoutBinding sut = activity.binding;
        shortSleep();
        screenshot(sut.emptyList.emptyListView);
    }

    @Test
    @ScreenshotTest
    public void testSyncedFolderDialog() {
        SyncedFolderDisplayItem item = new SyncedFolderDisplayItem(1,
                                                                   "/sdcard/DCIM/",
                                                                   "/InstantUpload/",
                                                                   true,
                                                                   false,
                                                                   false,
                                                                   true,
                                                                   "test@https://nextcloud.localhost",
                                                                   0,
                                                                   0,
                                                                   true,
                                                                   1000,
                                                                   "Name",
                                                                   MediaFolderType.IMAGE,
                                                                   false,
                                                                   SubFolderRule.YEAR_MONTH,
                                                                   false);
        SyncedFolderPreferencesDialogFragment sut = SyncedFolderPreferencesDialogFragment.newInstance(item, 0);

        Intent intent = new Intent(targetContext, SyncedFoldersActivity.class);
        SyncedFoldersActivity activity = activityRule.launchActivity(intent);

        sut.show(activity.getSupportFragmentManager(), "");

        getInstrumentation().waitForIdleSync();
        shortSleep();

        screenshot(Objects.requireNonNull(sut.requireDialog().getWindow()).getDecorView());
    }
}
