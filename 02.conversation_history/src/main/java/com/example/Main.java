package com.example;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String apiKey = System.getenv("GEMINI_API_KEY");
        String uri = "https://generativelanguage.googleapis.com/v1beta/openai/chat/completions";
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        List<Message> conversationHistory = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("You :");
            String prompt = scanner.nextLine();
            if(prompt.equalsIgnoreCase("exit")){
                break;
            }
            conversationHistory.add(new Message("user", prompt));
            GeminiRequest geminiRequest = new GeminiRequest("gemini-3.6-flash", conversationHistory);

            String requestBody = mapper.writeValueAsString(geminiRequest);

            // Create HTTP request
            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .header(
                                    "Authorization",
                                    "Bearer " + apiKey
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(requestBody)
                            )
                            .build();

            // Send request
            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );
            // Convert JSON response to Java object
            GeminiResponse geminiResponse =
                    mapper.readValue(
                            response.body(),
                            GeminiResponse.class
                    );

            // Extract only answer
            String answer =
                    geminiResponse
                            .getChoices()
                            .get(0)
                            .getMessage()
                            .getContent();

            // Add AI response to history
            conversationHistory.add(
                    new Message("assistant", answer)
            );

            System.out.println("Gemini: " + answer);
        }

        System.out.println("Chat ended.");
        }


    }

