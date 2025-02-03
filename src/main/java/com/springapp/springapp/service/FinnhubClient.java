package com.springapp.springapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FinnhubClient{

    // Jackson ObjectMapper to map the json from Finnhub API in java Object
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String apiKey = "cugahr9r01qo5mul14b0cugahr9r01qo5mul14bg";
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Fetch live price for a stock symbol from Finnhub API
     */
    public double getLivePrice(String stockSymbol){
        if(stockSymbol == null || stockSymbol.isEmpty()){
            System.err.println("Stock symbol cannot be empty....");
            return 0.0;
        }

        //Make the Api call to finnhub for real time stock price
        String url =  "https://finnhub.io/api/v1/quote?symbol=" + stockSymbol + "&token=" + apiKey;

        try{
            //Make api call and get the response as a string
            String response = restTemplate.getForObject(url, String.class);

            //Parse the response Json to extract the live price
            JsonNode root = objectMapper.readTree(response);
            double currentPrice = root.path("c").asDouble(); // "c" is the current price in Finnhub API response
            System.out.println("-----------------------------------");
            System.out.println("-----------------------------------");
            System.out.println("Live price for "+stockSymbol+": "+currentPrice+" Finnhub API..");
            System.out.println("-----------------------------------");
            System.out.println("-----------------------------------");

            return currentPrice;
        }catch (Exception e){
            System.err.println("Error fetching live price for symbol: "+stockSymbol+". Error: "+e.getMessage());
            return 0.0;
        }

    }



}