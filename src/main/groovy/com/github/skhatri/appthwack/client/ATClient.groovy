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
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.joda.time.LocalDateTime

import java.nio.charset.Charset

public class ATClient {
    private String apiKey

    public ATClient(String apiKey) {
        this.apiKey = apiKey
    }

    public List getProjects() {
        HttpClient client = HttpClients.custom().build()
        HttpUriRequest request = createGetRequest("/api/project")
        HttpResponse response = client.execute(request)
        byte[] bytes = response.getEntity().getContent().getBytes()
        ObjectMapper mapper = new ObjectMapper()
        mapper.readValue(bytes, List.class)
    }

    public List getDevices() {
        HttpClient client = HttpClients.custom().build()
        HttpUriRequest request = createGetRequest("/api/device")
        HttpResponse response = client.execute(request)
        byte[] bytes = response.getEntity().getContent().getBytes()
        ObjectMapper mapper = new ObjectMapper()
        mapper.readValue(bytes, List.class)
    }

    public List getPools() {
        HttpClient client = HttpClients.custom().build()
        HttpUriRequest request = createGetRequest("/api/devicepool")
        HttpResponse response = client.execute(request)
        byte[] bytes = response.getEntity().getContent().getBytes()
        println(new String(bytes, Charset.defaultCharset()))
        ObjectMapper mapper = new ObjectMapper()
        mapper.readValue(bytes, List.class)
    }

    public List getFiles() {
        HttpClient client = HttpClients.custom().build()
        HttpUriRequest request = createGetRequest("/api/file")
        HttpResponse response = client.execute(request)
        byte[] bytes = response.getEntity().getContent().getBytes()
        ObjectMapper mapper = new ObjectMapper()
        mapper.readValue(bytes, List.class)
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
        HttpResponse response = client.execute(request)
        byte[] bytes = response.getEntity().getContent().getBytes()
        println new String(bytes, Charset.defaultCharset())
        ObjectMapper mapper = new ObjectMapper()
        mapper.readValue(bytes, Map.class)
    }

    public Map createDevicePool(String poolName, String devices) {
        HttpClient client = HttpClients.custom().build()
        HttpPost request = (HttpPost) createPostRequest("/api/devicepool")
        List<BasicNameValuePair> data = new ArrayList<>()
        data.add(new BasicNameValuePair("name", poolName))
        data.add(new BasicNameValuePair("devices", devices))
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(data)

        request.setEntity(formEntity)
        HttpResponse response = client.execute(request)
        byte[] bytes = response.getEntity().getContent().getBytes()
        println new String(bytes, Charset.defaultCharset())
        ObjectMapper mapper = new ObjectMapper()
        mapper.readValue(bytes, Map.class)
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
        HttpResponse response = client.execute(request)
        byte[] bytes = response.getEntity().getContent().getBytes()
        ObjectMapper mapper = new ObjectMapper()
        mapper.readValue(bytes, Map.class)
    }

    public Map createProject(String name, String type) {
        HttpClient client = HttpClients.custom().build()
        HttpPost request = (HttpPost) createPostRequest("/api/project")
        List<BasicNameValuePair> data = new ArrayList<>()
        data.add(new BasicNameValuePair("name", name))
        data.add(new BasicNameValuePair("type", type))
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(data)

        request.setEntity(formEntity)
        HttpResponse response = client.execute(request)
        byte[] bytes = response.getEntity().getContent().getBytes()
        println new String(bytes, Charset.defaultCharset())
        ObjectMapper mapper = new ObjectMapper()
        mapper.readValue(bytes, Map.class)
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
