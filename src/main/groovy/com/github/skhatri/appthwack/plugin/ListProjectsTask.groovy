package com.github.skhatri.appthwack.plugin

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.skhatri.appthwack.client.ATClient
import com.github.skhatri.appthwack.client.ProjectType
import org.apache.http.HttpHost
import org.apache.http.HttpResponse
import org.apache.http.auth.AuthSchemeProvider
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.AuthCache
import org.apache.http.client.AuthenticationStrategy
import org.apache.http.client.CredentialsProvider
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.config.Lookup
import org.apache.http.impl.auth.BasicScheme
import org.apache.http.impl.auth.BasicSchemeFactory
import org.apache.http.impl.client.BasicAuthCache
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.charset.Charset

/**
 * [{"name": "ap", "url": "http://app", "id": 22612, "project_type_id": 3}]
 */
class ListProjectsTask extends AppthwackTask {

    public ListProjectsTask() {
    }

    @TaskAction
    public void perform() {
        logger.lifecycle "List projects"
        ATClient atClient = new ATClient(getApiKey())
        List projects = atClient.getProjects()
        logger.lifecycle "Project Name ".padRight(40, " ") + "ID".padRight(10, " ") + "Type".padRight(10, " ")
        logger.lifecycle "".padRight(40, "-") + "".padRight(10, "-") + "".padRight(10, "-")
        for(Map<String, Object> projectDetail: projects) {
            int projectType = projectDetail.get("project_type_id") as int
            logger.lifecycle projectDetail.get("name").padRight(40, " ") + projectDetail.get("id").toString().padRight(10, " ") + ProjectType.fromOrdinal(projectType-1).toString().padRight(10, " ")
        }
    }
}
