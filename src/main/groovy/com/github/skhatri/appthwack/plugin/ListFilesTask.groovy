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
        Collections.sort(files, new FileAddedDateComparator())
        logger.lifecycle "File Name ".padRight(30, " ") + "File ID".padRight(10, " ") + "Type".padRight(10, " ") + "Added".padRight(30, " ")
        logger.lifecycle "".padRight(30, "-") + "".padRight(10, "-") + "".padRight(10, "-") + "".padRight(30, "-")
        for(Map<String, Object> file: files) {
            logger.lifecycle file.get("name").padRight(30, " ") + file.get("file_id").toString().padRight(10, " ") + file.get("type").padRight(10, " ") + file.get("added").padRight(30, " ")
        }

    }
}
