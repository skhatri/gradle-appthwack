package com.github.skhatri.appthwack.plugin

import com.github.skhatri.appthwack.client.ATClient
import com.github.skhatri.appthwack.client.ProjectType
import org.gradle.api.tasks.TaskAction

/**
 *
 */
class ListPoolsTask extends AppthwackTask {

    public ListPoolsTask() {
    }

    @TaskAction
    public void perform() {
        logger.lifecycle "Listing Device Pools"
        ATClient atClient = new ATClient(getApiKey())
        List pools = atClient.getPools()
        logger.lifecycle "Project Name ".padRight(40, " ") + "ID".padRight(10, " ") + "Type".padRight(10, " ")
        logger.lifecycle "".padRight(40, "-") + "".padRight(10, "-") + "".padRight(10, "-")
        logger.lifecycle "Pools " + pools
    }
}
