/*
 * Nextcloud - Android Client
 *
 * SPDX-FileCopyrightText: 2024 Alper Ozturk <alper.ozturk@nextcloud.com>
 * SPDX-FileCopyrightText: 2024 Nextcloud GmbH
 * SPDX-License-Identifier: AGPL-3.0-or-later OR GPL-2.0-only
 */
package com.nextcloud.client.assistant.repository

import com.nextcloud.common.NextcloudClient
import com.owncloud.android.lib.common.operations.RemoteOperationResult
import com.owncloud.android.lib.resources.assistant.CreateTaskRemoteOperation
import com.owncloud.android.lib.resources.assistant.DeleteTaskRemoteOperation
import com.owncloud.android.lib.resources.assistant.GetTaskListRemoteOperation
import com.owncloud.android.lib.resources.assistant.GetTaskTypesRemoteOperation
import com.owncloud.android.lib.resources.assistant.model.TaskList
import com.owncloud.android.lib.resources.assistant.model.TaskTypeData

class AssistantRepository(private val client: NextcloudClient) : AssistantRepositoryType {

    override fun getTaskTypes(): RemoteOperationResult<List<TaskTypeData>> {
        return GetTaskTypesRemoteOperation().execute(client)
    }

    override fun createTask(input: String, taskType: TaskTypeData): RemoteOperationResult<Void> {
        return CreateTaskRemoteOperation(input, taskType).execute(client)
    }

    override fun getTaskList(taskType: String): RemoteOperationResult<TaskList> {
        return GetTaskListRemoteOperation(taskType).execute(client)
    }

    override fun deleteTask(id: Long): RemoteOperationResult<Void> {
        return DeleteTaskRemoteOperation(id).execute(client)
    }
}
