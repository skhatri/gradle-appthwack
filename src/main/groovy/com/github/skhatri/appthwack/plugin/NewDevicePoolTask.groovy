package com.github.skhatri.appthwack.plugin

import com.github.skhatri.appthwack.client.ATClient
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class NewDevicePoolTask extends AppthwackTask {

    @Input
    String poolName

    @Input
    String devices

    public NewDevicePoolTask() {
    }

    @TaskAction
    public void perform() {
        logger.lifecycle "Creating Device Pool: " + getPoolName()
        ATClient atClient = new ATClient(getApiKey())
        Map poolMap = atClient.createDevicePool(getPoolName(), getDevices())
        logger.lifecycle "Pool Created: " + poolMap
    }
}
