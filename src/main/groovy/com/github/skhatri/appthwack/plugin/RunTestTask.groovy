package com.github.skhatri.appthwack.plugin

import com.github.skhatri.appthwack.client.ATClient
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeParser

class RunTestTask extends AppthwackTask {

    @Input
    String projectId
    @Input
    String projectName
    @Input
    String testFileId
    @Input
    String testFileName
    @Input
    String ipaFileId
    @Input
    String ipaFileName
    @Input
    String poolId
    @Input
    String poolName

    private ATClient atClient

    private List lastRetrievedProjectFiles

    public RunTestTask() {
    }

    @TaskAction
    public void perform() {
        atClient = new ATClient(getApiKey())
        logger.lifecycle "Running Tests: "
        int projectId = validateProject()
        int testFileId = validateTest()
        int ipaFileId = validateIpa()
        int poolId = validatePool()
        Map runResult = atClient.runTests(projectId, ipaFileId, testFileId, poolId)
        if (runResult.containsKey("run_id")) {
            logger.lifecycle "Test Queued. Run id " + runResult.get("run_id")
        } else {
            logger.lifecycle "Test Queue Error " + runResult
        }
    }

    private int validateProject() {
        String projectId = getProjectId()?.trim()
        String projectName = getProjectName()?.trim()

        if (!projectId && !projectName) {
            throw new GradleException("Specify which project to run test against via projectId or projectName.")
        }
        List projects = atClient.getProjects()
        Map matched = projects.sort(new FileAddedDateComparator()).find { Map item -> item.get("id") as int == project || item.get("name") == projectName }
        Integer result = matched?.get("id")
        if (!result) {
            throw new GradleException("Could not find matching project in Appthwack")
        }
        result
    }

    private int validateIpa() {
        String ipaId = getIpaFileId()?.trim()?:'-1'
        String ipaName = getIpaFileName()?.trim()
        if (!ipaId && !ipaName) {
            throw new GradleException("Specify which uploaded binary file id or name to test.")
        }
        def files = getFiles()

        Map matched = files.sort(new FileAddedDateComparator()).find { Map item -> item.get("file_id") as int == ipaId as int || item.get("name") == ipaName }
        Integer ipaResult = matched?.get("file_id")
        if (!ipaResult) {
            throw new GradleException("Could not find matching mobile binary in Appthwack")
        }
        ipaResult
    }


    private int validateTest() {
        String testFileId = getTestFileId()?.trim()?:'-1'
        String testFileName = getTestFileName()?.trim()
        if (!testFileId && !testFileName) {
            throw new GradleException("Specify which uploaded test file id or name to use for test.")
        }
        def files = getFiles()
        Map testMatched = files.sort(new FileAddedDateComparator()).find { Map item -> item.get("file_id") as int == testFileId as int || item.get("name") == testFileName }
        Integer testResult = testMatched?.get("file_id")
        if (!testResult) {
            throw new GradleException("Could not find matching test features in Appthwack")
        }
        testResult
    }

    private int validatePool() {
        String poolId = getPoolId()?.trim()?:'-1'
        String poolName = getPoolName()?.trim()
        if (!poolId && !poolName) {
            return -1
        }
        def pools = atClient.getPools()
        Map matched = pools.find { Map item -> item.get("id") as int == poolId as int || item.get("name") == poolName }
        Integer poolResult = matched?.get("id")
        if (!poolResult) {
            poolResult = -1
        }
        poolResult
    }

    private List getFiles() {
        if (!lastRetrievedProjectFiles) {
            lastRetrievedProjectFiles = atClient.getFiles()
        }
        lastRetrievedProjectFiles
    }
}
