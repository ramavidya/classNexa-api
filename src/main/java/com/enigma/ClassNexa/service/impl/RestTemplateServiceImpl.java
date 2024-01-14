package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.model.request.TargetNumberRequest;
import com.enigma.ClassNexa.service.RestTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestTemplateServiceImpl implements RestTemplateService {
    @Value("${app.class-nexa.token-wa}")
    private String tokenWa;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String sendMessageRegisterWhatsapp(TargetNumberRequest request) throws IOException {
        // Form data key-value pair
        String keyTarget = "target";
        String keyMessage = "message";
        String valueMessage = "Hello Enigmanians \n \nYou have been successfully added to the Instructor LED App (*ClassNexa*). Stay tuned for schedule updates and activities from Instructor-LED sessions.\n \n";

        for (String targetNumber : request.getNumber()) {
            // URL endpoint
            String url = "https://api.fonnte.com/send";
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            // Add form data to the request
            List<BasicNameValuePair> formData = new ArrayList<>();
            formData.add(new BasicNameValuePair(keyTarget, targetNumber));
            formData.add(new BasicNameValuePair(keyMessage, valueMessage));
            httpPost.setEntity(new UrlEncodedFormEntity(formData));

            // Add Authorization header
            Header authorizationHeader = new BasicHeader("Authorization", tokenWa);
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
            log.info("Message success : {}",responseContent.toString());
        }
        return "success";
    }
}
