package com.github.skhatri.appthwack.plugin

import com.github.skhatri.appthwack.client.ATClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class NewProjectTask extends AppthwackTask {

    @Input
    String projectName

    @Input
    String projectType

    public NewProjectTask() {
    }

    @TaskAction
    public void perform() {
        logger.lifecycle "Creating Project: " + getProjectName()
        ATClient atClient = new ATClient(getApiKey())
        Map projectMap = atClient.createProject(getProjectName(), getProjectType())
        logger.lifecycle "Project URL " + projectMap.url
        logger.lifecycle "Project Id " + projectMap.id
    }
}
