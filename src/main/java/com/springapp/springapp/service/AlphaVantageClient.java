package com.springapp.springapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AlphaVantageClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiKey = "P7K9NNS5Y4WRFN11";
    private final RestTemplate restTemplate = new RestTemplate();

    public JsonNode getHistoricalData(String stockSymbol){
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + stockSymbol + "&apikey=" + apiKey;
        try{
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = new ObjectMapper().readTree(response);
            return root.path("Time Series (Daily)"); //Returns the daily time series JSON node
        }catch (Exception e){
            System.err.println("Error fetching the historical data: "+e.getMessage());
            return null;
        }
    }

}
