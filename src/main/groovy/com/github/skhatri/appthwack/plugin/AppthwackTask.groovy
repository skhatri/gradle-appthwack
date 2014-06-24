package com.github.skhatri.appthwack.plugin

import org.apache.commons.codec.binary.Base64
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input

import java.nio.charset.Charset


class AppthwackTask extends DefaultTask {
    @Input
    String apiKey

}
