
// Get the stock symbol from the HTML page (the value is passed from the server-side model)
const stockSymbol = document.getElementById("stock-symbol").textContent.trim();
// Get the live price element where we will display the updated stock price
const livePriceElement = document.getElementById("live-price");

if (stockSymbol) {
    const apiKey = "cugahr9r01qo5mul14b0cugahr9r01qo5mul14bg";
     // Create a WebSocket connection to Finnhub's WebSocket endpoint
    const socket = new WebSocket(`wss://ws.finnhub.io?token=${apiKey}`);


    // Event: When the WebSocket connection is successfully opened
    socket.addEventListener("open", () => {
        console.log("Connected to Finnhub WebSocket JS ###########");

        // Send a subscription message to the WebSocket for the given stock symbol
        // Finnhub expects this format to subscribe to a specific stock ticker
        socket.send(JSON.stringify({ type: "subscribe", symbol: stockSymbol }));
    });

// Event: When the WebSocket receives a new message
    socket.addEventListener("message", async (event) => {
        try {
            // Parse the incoming data from the WebSocket
            const data = JSON.parse(event.data);

            // Check if the message contains trade data (type "trade")
            if (data.type === "trade") {
                const tradeData = data.data[0];

                // Ensure the trade data matches the requested stock symbol
                if (tradeData && tradeData.s === stockSymbol) {
                    // Update the live price on the HTML page with the trade price
                    livePriceElement.textContent = tradeData.p.toFixed(2); // Display with two decimal places

                }
            }
        } catch (error) {
            console.error("Error processing WebSocket message:", error);
        }
    });

    // Event: When the WebSocket connection is closed
    socket.addEventListener("close", () => {
        console.log("WebSocket closed");
    });

    // Event: If the WebSocket encounters an error
    socket.addEventListener("error", (error) => {
        console.error("WebSocket error:", error);
    });

    // Close the WebSocket connection when the user leaves or reloads the page
    // This ensures clean resource management and prevents memory leaks
    window.addEventListener("beforeunload", () => {
        socket.close();
    });
} else {
    // If no stock symbol is provided, log an error to the console
    console.error("No stock symbol provided");
}

document.addEventListener('DOMContentLoaded', () => {
        const stockSymbol = document.getElementById('stockSymbol');
        const stockPrice = document.getElementById('stockPrice');
        const buyBtn = document.getElementById('buyBtn');
        const sellBtn = document.getElementById('sellBtn');
        const updateBtn = document.getElementById('updateBtn');
        const watchlistBtn = document.getElementById('watchlistBtn');

        // Price color logic
        function updatePriceColor() {
            const price = parseFloat(stockPrice.textContent.replace('$', ''));
            stockPrice.classList.remove('positive', 'negative');

            if (price > 0) {
                stockPrice.classList.add('positive');
            } else if (price < 0) {
                stockPrice.classList.add('negative');
            }
        }

        // Simulate price update
        function updatePrice() {
            const currentPrice = parseFloat(stockPrice.textContent.replace('$', ''));
            const variation = (Math.random() * 10 - 5).toFixed(2);
            const newPrice = (currentPrice + parseFloat(variation)).toFixed(2);
            stockPrice.textContent = `$${newPrice}`;
            updatePriceColor();
        }

        // Button click effects
        function addButtonEffect(button) {
            button.addEventListener('click', () => {
                button.style.transform = 'scale(0.95)';
                setTimeout(() => {
                    button.style.transform = 'scale(1)';
                }, 100);
            });
        }

        // Add effects to buttons
        [buyBtn, sellBtn, updateBtn, watchlistBtn].forEach(addButtonEffect);

        // Update price button
        updateBtn.addEventListener('click', updatePrice);

        // Initialize price color
        updatePriceColor();
    });


