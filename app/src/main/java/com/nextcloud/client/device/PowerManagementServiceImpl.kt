/*
 * Nextcloud - Android Client
 *
 * SPDX-FileCopyrightText: 2020 Chris Narkiewicz <hello@ezaquarii.com>
 * SPDX-License-Identifier: AGPL-3.0-or-later OR GPL-2.0-only
 */
package com.nextcloud.client.device

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.PowerManager
import com.nextcloud.client.preferences.AppPreferences
import com.nextcloud.client.preferences.AppPreferencesImpl
import com.nextcloud.utils.extensions.registerBroadcastReceiver
import com.owncloud.android.datamodel.ReceiverFlag

internal class PowerManagementServiceImpl(
    private val context: Context,
    private val platformPowerManager: PowerManager,
    private val preferences: AppPreferences,
    private val deviceInfo: DeviceInfo = DeviceInfo()
) : PowerManagementService {

    companion object {
        /**
         * Vendors on this list use aggressive power saving methods that might
         * break application experience.
         */
        val OVERLY_AGGRESSIVE_POWER_SAVING_VENDORS = setOf("samsung", "huawei", "xiaomi")

        @JvmStatic
        fun fromContext(context: Context): PowerManagementServiceImpl {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val preferences = AppPreferencesImpl.fromContext(context)

            return PowerManagementServiceImpl(context, powerManager, preferences, DeviceInfo())
        }
    }

    override val isPowerSavingEnabled: Boolean
        get() {
            if (preferences.isPowerCheckDisabled) {
                return false
            }

            return platformPowerManager.isPowerSaveMode
        }

    override val isPowerSavingExclusionAvailable: Boolean
        get() = deviceInfo.vendor in OVERLY_AGGRESSIVE_POWER_SAVING_VENDORS

    @Suppress("MagicNumber") // 100% is 100, we're not doing Cobol
    override val battery: BatteryStatus
        get() {
            val intent: Intent? = context.registerBroadcastReceiver(
                null,
                IntentFilter(Intent.ACTION_BATTERY_CHANGED),
                ReceiverFlag.NotExported
            )
            val isCharging = intent?.let {
                when (it.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)) {
                    BatteryManager.BATTERY_PLUGGED_USB -> true
                    BatteryManager.BATTERY_PLUGGED_AC -> true
                    BatteryManager.BATTERY_PLUGGED_WIRELESS -> true
                    else -> false
                }
            } ?: false
            val level = intent?.let { it ->
                val level: Int = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale: Int = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                (level * 100 / scale.toFloat()).toInt()
            } ?: 0
            return BatteryStatus(isCharging, level)
        }
}
