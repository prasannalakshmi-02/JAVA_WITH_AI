package com.example;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String apiKey = System.getenv("GEMINI_API_KEY");
        String uri = "https://generativelanguage.googleapis.com/v1beta/openai/chat/completions";
        String requestBody = """
                           {
                              "model":"gemini-3.6-flash",
                              "messages":[
                               {
                                  "role":"user",
                                  "content":"Tell me a telugu joke"
                                }
                              ]
                           }
                       """;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                              .uri(URI.create(uri))
                              .header(
                            "Content-Type",
                            "application/json"
                               ).header(
                              "Authorization",
                              "Bearer " + apiKey
                               ).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());

        String res = response.body();
        ObjectMapper mapper = new ObjectMapper();
        GeminiResponse geminiResponse = mapper.readValue(res, GeminiResponse.class);
        String answer =
                geminiResponse
                        .getChoices()
                        .get(0)
                        .getMessage()
                        .getContent();

        System.out.println(answer);
    }
}
