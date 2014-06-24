package com.github.skhatri.appthwack.plugin

import com.github.skhatri.appthwack.client.ATClient
import com.github.skhatri.appthwack.client.ProjectType
import org.gradle.api.tasks.TaskAction

/**
 *
 */
class ListFilesTask extends AppthwackTask {

    public ListFilesTask() {
    }

    @TaskAction
    public void perform() {
        logger.lifecycle "Listing Uploaded Files "
        ATClient atClient = new ATClient(getApiKey())
        List files = atClient.getFiles()
        logger.lifecycle "File Name ".padRight(40, " ") + "File ID".padRight(10, " ") + "Type".padRight(10, " ")
        logger.lifecycle "".padRight(40, "-") + "".padRight(10, "-") + "".padRight(10, "-")
        for(Map<String, Object> file: files) {
            logger.lifecycle file.get("name").padRight(40, " ") + file.get("file_id").toString().padRight(10, " ") + file.get("type").padRight(10, " ")
        }

    }
}
