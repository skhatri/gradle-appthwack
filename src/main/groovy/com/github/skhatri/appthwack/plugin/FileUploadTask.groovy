package com.github.skhatri.appthwack.plugin

import com.github.skhatri.appthwack.client.ATClient
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class FileUploadTask extends AppthwackTask {

    @Input
    String ipaName

    @Input
    String file

    @Input
    String type

    public FileUploadTask() {
    }

    @TaskAction
    public void perform() {
        logger.lifecycle "Uploading File " + getIpaName() + " from " + getFile()
        ATClient atClient = new ATClient(getApiKey())
        Map uploadResult = atClient.uploadFile(getIpaName(), getFile(), getType())
        if(uploadResult.containsKey("file_id")) {
            logger.lifecycle "Uploaded, File id is " + uploadResult.get("file_id")
        } else {
            logger.lifecycle "Error " + uploadResult
        }
    }
}
