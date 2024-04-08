/*
 * Nextcloud - Android Client
 *
 * SPDX-FileCopyrightText: 2020 Chris Narkiewicz <hello@ezaquarii.com>
 * SPDX-FileCopyrightText: 2018 Andy Scherzinger <info@andy-scherzinger.de>
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package com.owncloud.android.utils;

import android.content.res.Resources;
import android.view.Menu;

import com.nextcloud.client.account.User;
import com.owncloud.android.R;
import com.owncloud.android.lib.resources.status.OCCapability;

import androidx.annotation.Nullable;

/**
 * A helper class for drawer menu related operations.
 */
public final class DrawerMenuUtil {
    private DrawerMenuUtil() {
    }

    public static void filterSearchMenuItems(Menu menu,
                                             User user,
                                             Resources resources) {
        if (user.isAnonymous()) {
            filterMenuItems(menu, R.id.nav_gallery, R.id.nav_favorites);
        }

        if (!resources.getBoolean(R.bool.recently_modified_enabled)) {
            menu.removeItem(R.id.nav_recently_modified);
        }
    }

    public static void filterTrashbinMenuItem(Menu menu, @Nullable OCCapability capability) {
        if (capability != null && capability.getFilesUndelete().isFalse() ||
            capability != null && capability.getFilesUndelete().isUnknown()) {
            filterMenuItems(menu, R.id.nav_trashbin);
        }
    }

    public static void filterActivityMenuItem(Menu menu, @Nullable OCCapability capability) {
        if (capability != null && capability.getActivity().isFalse()) {
            filterMenuItems(menu, R.id.nav_activity);
        }
    }

    public static void filterAssistantMenuItem(Menu menu, @Nullable OCCapability capability, Resources resources) {
        boolean showCondition = capability != null && capability.getAssistant().isTrue() && !resources.getBoolean(R.bool.is_branded_client);
        if (!showCondition) {
            filterMenuItems(menu, R.id.nav_assistant);
        }
    }

    public static void filterGroupfoldersMenuItem(Menu menu, @Nullable OCCapability capability) {
        if (capability != null && !capability.getGroupfolders().isTrue()) {
            filterMenuItems(menu, R.id.nav_groupfolders);
        }
    }

    public static void removeMenuItem(Menu menu, int id, boolean remove) {
        if (remove) {
            menu.removeItem(id);
        }
    }

    public static void setupHomeMenuItem(Menu menu, Resources resources) {
        if (resources.getBoolean(R.bool.use_home) && menu.findItem(R.id.nav_all_files) != null) {
            menu.findItem(R.id.nav_all_files).setTitle(resources.getString(R.string.drawer_item_home));
            menu.findItem(R.id.nav_all_files).setIcon(R.drawable.ic_home);
        }
    }

    private static void filterMenuItems(Menu menu, int... menuIds) {
        if (menuIds != null) {
            for (int menuId : menuIds) {
                menu.removeItem(menuId);
            }
        }
    }
}
