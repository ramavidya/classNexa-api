package com.enigma.ClassNexa.controller;

import lombok.RequiredArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/send")
    public ResponseEntity<String> sendFormData() throws IOException {
        // Form data key-value pair
        String keyTarget = "target";
        String valueTarget = "085895780479";
        List<Map<String, String>> target = List.of(
                Map.of("key", "target", "value", "081398817317"),
                Map.of("key", "target", "value", "082131311342"),
                Map.of("key", "target", "value", "087717980555"),
                Map.of("key", "target", "value", "085895780479"),
                Map.of("key", "target", "value", "083896660222"),
                Map.of("key", "target", "value", "082120315611"),
                Map.of("key", "target", "value", "085783531887"));

        String keyMessage = "message";
        String valueMessage = "added as a participant in java batch 100#, via application class-nexa";

        String notifResponse = "";
        for (Map<String, String> targetApi : target) {

            // URL endpoint
            String url = "https://api.fonnte.com/send";

            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            // Add form data to the request
            List<BasicNameValuePair> formData = new ArrayList<>();
            formData.add(new BasicNameValuePair(targetApi.get("key"), targetApi.get("value")));
            formData.add(new BasicNameValuePair(keyMessage, valueMessage));
            httpPost.setEntity(new UrlEncodedFormEntity(formData));

            // Add Authorization header
            Header authorizationHeader = new BasicHeader("Authorization", "XXtVxw50c-1b7ki_@iVz");
            httpPost.addHeader(authorizationHeader);

            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder responseContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            // Print the response
            System.out.println("Response: " + responseContent.toString());
            notifResponse = responseContent.toString();
        }
        return ResponseEntity.ok("success");
    }
}
