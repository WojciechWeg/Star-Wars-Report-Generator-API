package com.wojtek.api.StarWarsReportGeneratorAPI.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class APIService {

    public APIService() {
    }


    public JsonObject getBuilder(String path) throws Exception {

        HttpGet httpGet = new HttpGet("https://swapi.co/api/" + path );

        return getRequest(httpGet);
    }

    public JsonObject getBuilderFullPath(String fullPath) throws Exception {

        HttpGet httpGet = new HttpGet(fullPath );

        return getRequest(httpGet);
    }



    public JsonObject getRequest(HttpGet getRequest) throws IOException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        getRequest.addHeader("accept", "application/json");
        HttpResponse response = httpClient.execute(getRequest);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        JsonObject jsonObject = deserialize(stringBuilder.toString());
        bufferedReader.close();

        return jsonObject;
    }

    public JsonObject deserialize(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, JsonObject.class);
    }

}
