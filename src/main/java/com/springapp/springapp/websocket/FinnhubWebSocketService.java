package com.springapp.springapp.websocket;

import com.springapp.springapp.service.LiveStockPriceService;
import org.springframework.stereotype.Service;

@Service
public class FinnhubWebSocketService {

    private FinnhubWebSocketClient webSocketClient;

    public FinnhubWebSocketService(FinnhubWebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    public void startWebSocket(String apiKey) {
        try {
            // Initialize WebSocket with API key and connect
            webSocketClient = new FinnhubWebSocketClient(apiKey);
            webSocketClient.connect();
        } catch (Exception e) {
            System.err.println("Error starting WebSocket: " + e.getMessage());
        }
    }

    public void stopWebSocket() {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
        }
    }
}

