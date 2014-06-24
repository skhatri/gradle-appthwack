package com.github.skhatri.appthwack.plugin

import com.github.skhatri.appthwack.client.ATClient
import com.github.skhatri.appthwack.client.ProjectType
import org.gradle.api.tasks.TaskAction

/**
 *
 */
class ListDevicesTask extends AppthwackTask {

    public ListDevicesTask() {
    }

    @TaskAction
    public void perform() {
        logger.lifecycle "List of devices"
        ATClient atClient = new ATClient(getApiKey())
        List devices = atClient.getDevices()
        logger.lifecycle "Project Name ".padRight(40, " ") + "ID".padRight(10, " ") + "Type".padRight(10, " ")
        logger.lifecycle "".padRight(40, "-") + "".padRight(10, "-") + "".padRight(10, "-")
        logger.lifecycle "Devices " + devices
    }
}
