package com.github.skhatri.appthwack.client

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.binary.Base64
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.gradle.api.GradleException
import org.joda.time.LocalDateTime

import java.nio.charset.Charset

public class ATClient {
    private String apiKey
    private boolean debug

    public ATClient(String apiKey) {
        this(apiKey, false)
    }

    public ATClient(String apiKey, boolean debug) {
        this.apiKey = apiKey
        this.debug = false
    }

    public List getProjects() {
        HttpClient client = HttpClients.custom().build()
        HttpUriRequest request = createGetRequest("/api/project")
        callApi(client, request, List)
    }

    public List getDevices() {
        HttpClient client = HttpClients.custom().build()
        HttpUriRequest request = createGetRequest("/api/device")
        callApi(client, request, List)
    }

    public List getPools() {
        HttpClient client = HttpClients.custom().build()
        HttpUriRequest request = createGetRequest("/api/devicepool")
        callApi(client, request, List)
    }

    public List getFiles() {
        HttpClient client = HttpClients.custom().build()
        HttpUriRequest request = createGetRequest("/api/file")
        callApi(client, request, List)
    }

    private <T> T callApi(CloseableHttpClient client, HttpUriRequest request, Class<T> t) {
        HttpResponse response = client.execute(request)
        byte[] bytes = response.getEntity().getContent().getBytes()
        int statusCode = response.getStatusLine().getStatusCode()
        if(debug) {
            println (new String(bytes))
        }
        if (statusCode >= 400) {
            throw new GradleException("Appthwack Error: " + new String(bytes, Charset.forName('UTF-8')))
        } else {
            ObjectMapper mapper = new ObjectMapper()
            mapper.readValue(bytes, t)
        }
    }

    public Map runTests(int projectId, int ipaFileId, int testFileId, int poolId) {
        HttpClient client = HttpClients.custom().build()
        HttpPost request = (HttpPost) createPostRequest("/api/run")
        List<BasicNameValuePair> data = new ArrayList<>()
        data.add(new BasicNameValuePair("project", projectId.toString()))
        data.add(new BasicNameValuePair("name", "test-" + projectId + "." + System.currentTimeMillis()))
        data.add(new BasicNameValuePair("app", ipaFileId.toString()))
        data.add(new BasicNameValuePair("calabash", testFileId.toString()))
        if (poolId != -1) {
            data.add(new BasicNameValuePair("pool", poolId.toString()))
        }
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(data)

        request.setEntity(formEntity)
        callApi(client, request, Map)
    }

    public Map createDevicePool(String poolName, String devices) {
        HttpClient client = HttpClients.custom().build()
        HttpPost request = (HttpPost) createPostRequest("/api/devicepool")
        List<BasicNameValuePair> data = new ArrayList<>()
        data.add(new BasicNameValuePair("name", poolName))
        data.add(new BasicNameValuePair("devices", devices))
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(data)

        request.setEntity(formEntity)
        callApi(client, request, Map)
    }


    public Map uploadFile(String name, String fileName, String type) {
        HttpClient client = HttpClients.custom().build()
        HttpPost request = (HttpPost) createPostRequest(String.format("/api/file?name=%s&save=true&type=%s", name, type))
        request.setHeader("enctype", "multipart/form-data")
        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addPart("file", new FileBody(new File(fileName)))
                .build()
        request.setEntity(multipartEntity)
        callApi(client, request, Map)
    }

    public Map createProject(String name, String type) {
        HttpClient client = HttpClients.custom().build()
        HttpPost request = (HttpPost) createPostRequest("/api/project")
        List<BasicNameValuePair> data = new ArrayList<>()
        data.add(new BasicNameValuePair("name", name))
        data.add(new BasicNameValuePair("type", type))
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(data)

        request.setEntity(formEntity)
        callApi(client, request, Map)
    }


    def createGetRequest(String uri) {
        HttpUriRequest request = new HttpGet("https://appthwack.com" + uri)
        createRequest(request)
        request
    }

    def createRequest(HttpUriRequest request) {
        byte[] userPass = Base64.encodeBase64("$apiKey:".getBytes())
        String base64UsernamePassword = (new String(userPass, Charset.defaultCharset()))
        request.setHeader("Authorization", "Basic ${base64UsernamePassword}")
    }

    def createPostRequest(String uri) {
        HttpUriRequest request = new HttpPost("https://appthwack.com" + uri)
        createRequest(request)
        request
    }
}
