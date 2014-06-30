gradle-appthwack
================

This plugin creates tasks which consume various API from Appthwack. These tasks can be used to manage project, devicepool, file submissions and queueing up of tests.

This gradle appthwack not to be confused with "appthwack" plugin from Appthwack itself, uses the configuration under the extension
"at".

Following is how you could import the plugin
```
buildscript {
    dependencies {
        classpath 'com.github.skhatri:gradle-appthwack:1.0.0'
        classpath 'org.apache.httpcomponents:httpclient:4.3.4'
        classpath 'org.apache.httpcomponents:httpcore-nio:4.3.2'
        classpath 'org.apache.httpcomponents:httpmime:4.3.4'
        classpath 'com.fasterxml.jackson.core:jackson-core:2.4.1'
        classpath 'com.fasterxml.jackson.core:jackson-databind:2.4.1'
        classpath 'com.fasterxml.jackson.core:jackson-annotations:2.4.1'
    }
    repositories {
        mavenCentral()
    }
}

apply plugin: 'appthwack-tasks'
```


And this is how it can be configured to call various appthwack api methods.

```
at {
    apiKey = 'XB89Faglymbuk9sXLaMR5owOXrasYbDzCIwzPCq2'

    newProject {
        projectName = project.ext['test.project.name']
        projectType = project.ext['type']
    }

    newPool {
        name = project.ext['pool.name']
        devices = project.ext['pool.devices']
    }

    ipa {
        name = project.ext['ipa.name']
        file = project.ext['ipa.file']
        type = 'iosapp'
    }

    tests {
        name = project.ext['test.name']
        file = project.ext['test.file']
        type = 'calabash'
    }

    run {

        projectId = project.ext['test.project.id']
        projectName = project.ext['test.project.name']

        poolId = project.ext['pool.id']
        poolName = project.ext['pool.name']

        ipaFileId = project.ext['ipa.id']
        ipaFileName = project.ext['ipa.name']

        testFileId = project.ext['test.id']
        testFileName = project.ext['test.name']

    }
}
```

The plugin adds the following tasks to the project:

#. atDevices
#. atFiles
#. atNewPool
#. atNewProject
#. atPools
#. atProjects
#. atRun
#. atUploadIpa
#. atUploadTests


