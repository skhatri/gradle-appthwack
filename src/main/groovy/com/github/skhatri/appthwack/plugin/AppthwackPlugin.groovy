package com.github.skhatri.appthwack.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class AppthwackPlugin implements Plugin<Project> {

    void apply(Project project) {
        def appExt = project.extensions.create('at', ClientExtension)
        appExt.extensions.create('newProject', NewProjectExtension)
        appExt.extensions.create('newPool', NewPoolExtension)
        appExt.extensions.create('ipa', FileUploadExtension)
        appExt.extensions.create('tests', FileUploadExtension)
        appExt.extensions.create('run', RunTestExtension)


        addListTasks(project)
        createNewProjectTask(project)
        createDevicePool(project)
        createUploadFileTask(project)
        createRunTask(project)
        project.tasks.withType(AppthwackTask) {
            conventionMapping.apiKey = { appExt.apiKey }
        }
    }

    void addListTasks(Project project) {
        project.tasks.create(name: 'atProjects', type: ListProjectsTask)
        project.tasks.create(name: 'atDevices', type: ListDevicesTask)
        project.tasks.create(name: 'atPools', type: ListPoolsTask)
        project.tasks.create(name: 'atFiles', type: ListFilesTask)
    }

    void createNewProjectTask(Project project) {
        project.tasks.create(name: 'atNewProject', type: NewProjectTask) {

        }
        project.tasks.withType(NewProjectTask) {
            def appthwackExt = project.extensions.getByName('at')
            def newProjectConfig = appthwackExt.extensions.getByName('newProject')

            conventionMapping.projectName = { newProjectConfig.projectName }
            conventionMapping.projectType = { newProjectConfig.projectType }
        }
    }

    void createDevicePool(Project project) {
        project.tasks.create(name: 'atNewPool', type: NewDevicePoolTask) {

        }
        project.tasks.withType(NewDevicePoolTask) {
            def appthwackExt = project.extensions.getByName('at')
            def newPoolConfig = appthwackExt.extensions.getByName('newPool')

            conventionMapping.poolName = { newPoolConfig.name }
            conventionMapping.devices = { newPoolConfig.devices }
        }
    }

    void createRunTask(Project project) {
        project.tasks.create(name: 'atRun', type: RunTestTask) {
        }
        project.tasks.withType(RunTestTask) {
            def appthwackExt = project.extensions.getByName('at')
            def runConfig = appthwackExt.extensions.getByName('run')

            conventionMapping.projectId = { runConfig.projectId }
            conventionMapping.projectName = { runConfig.projectName }

            conventionMapping.poolId = { runConfig.poolId }
            conventionMapping.poolName = { runConfig.poolName }
            conventionMapping.ipaFileId = { runConfig.ipaFileId }
            conventionMapping.ipaFileName = { runConfig.ipaFileName }

            conventionMapping.testFileId = { runConfig.testFileId }
            conventionMapping.testFileName = { runConfig.testFileName }
        }
    }

    void createUploadFileTask(Project project) {
        project.tasks.create(name: 'atUploadIpa', type: FileUploadTask) {
            def appthwackExt = project.extensions.getByName('at')
            def uploadConfig = appthwackExt.extensions.getByName('ipa')

            conventionMapping.ipaName = { uploadConfig.name }
            conventionMapping.file = { uploadConfig.file }
            conventionMapping.type = { uploadConfig.type }
        }

        project.tasks.create(name: 'atUploadTests', type: FileUploadTask) {
            def appthwackExt = project.extensions.getByName('at')
            def uploadConfig = appthwackExt.extensions.getByName('tests')

            conventionMapping.ipaName = { uploadConfig.name }
            conventionMapping.file = { uploadConfig.file }
            conventionMapping.type = { uploadConfig.type }
        }

        project.tasks.withType(FileUploadTask) {

        }
    }

}
