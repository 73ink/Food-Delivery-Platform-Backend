/* =========================================================
   Food Delivery Platform Front-End
   This file uses fetch() to connect the HTML page with
   the Spring Boot REST API.
   ========================================================= */

// Base URL of my Spring Boot backend
const API_BASE_URL = "http://localhost:8080";

// These variables store the selected restaurant and cart items
let selectedRestaurantId = null;
let selectedRestaurantName = "";
let cart = [];

// This variable is used for automatic order tracking
let trackingInterval = null;


/* =========================================================
   SECTION SWITCHING
   This function shows one section and hides the others.
   ========================================================= */
function showSection(sectionId) {
    const sections = document.querySelectorAll(".page-section");

    sections.forEach(section => {
        section.classList.remove("active-section");
    });

    document.getElementById(sectionId).classList.add("active-section");
}


/* =========================================================
   SIMPLE MESSAGE HELPER
   This keeps messages clear for loading, success, and errors.
   ========================================================= */
function showMessage(elementId, message, isError = false) {
    const element = document.getElementById(elementId);

    element.textContent = message;

    if (isError) {
        element.classList.add("error-message");
        element.classList.remove("success-message");
    } else {
        element.classList.add("success-message");
        element.classList.remove("error-message");
    }
}


/* =========================================================
   RESTAURANT BROWSE
   Loads all restaurants from GET /api/restaurants
   ========================================================= */
async function loadRestaurants() {
    showMessage("restaurantMessage", "Loading restaurants...");

    try {
        const response = await fetch(`${API_BASE_URL}/api/restaurants`);

        if (!response.ok) {
            throw new Error("Could not load restaurants");
        }

        const restaurants = await response.json();

        displayRestaurants(restaurants);
        showMessage("restaurantMessage", "Restaurants loaded successfully.");
    } catch (error) {
        showMessage("restaurantMessage", error.message, true);
    }
}


/* =========================================================
   SEARCH RESTAURANTS
   Uses GET /api/restaurants/search?keyword=
   ========================================================= */
async function searchRestaurants() {
    const keyword = document.getElementById("restaurantKeyword").value.trim();

    if (keyword === "") {
        showMessage("restaurantMessage", "Please enter a restaurant name to search.", true);
        return;
    }

    showMessage("restaurantMessage", "Searching restaurants...");

    try {
        const response = await fetch(`${API_BASE_URL}/api/restaurants/search?keyword=${encodeURIComponent(keyword)}`);

        if (!response.ok) {
            throw new Error("Search failed");
        }

        const restaurants = await response.json();

        displayRestaurants(restaurants);
        showMessage("restaurantMessage", "Search completed.");
    } catch (error) {
        showMessage("restaurantMessage", error.message, true);
    }
}


/* =========================================================
   FILTER BY CUISINE
   Uses GET /api/restaurants/cuisine/{cuisine}
   ========================================================= */
async function filterByCuisine() {
    const cuisine = document.getElementById("cuisineFilter").value.trim();

    if (cuisine === "") {
        showMessage("restaurantMessage", "Please enter a cuisine type.", true);
        return;
    }

    showMessage("restaurantMessage", "Filtering restaurants...");

    try {
        const response = await fetch(`${API_BASE_URL}/api/restaurants/cuisine/${encodeURIComponent(cuisine)}`);

        if (!response.ok) {
            throw new Error("Cuisine filter failed");
        }

        const restaurants = await response.json();

        displayRestaurants(restaurants);
        showMessage("restaurantMessage", "Cuisine filter completed.");
    } catch (error) {
        showMessage("restaurantMessage", error.message, true);
    }
}


/* =========================================================
   DISPLAY RESTAURANTS
   This creates restaurant cards dynamically.
   ========================================================= */
function displayRestaurants(restaurants) {
    const container = document.getElementById("restaurantsList");

    container.innerHTML = "";

    if (!restaurants || restaurants.length === 0) {
        container.innerHTML = `<p>No restaurants found.</p>`;
        return;
    }

    restaurants.forEach(restaurant => {
        const card = document.createElement("div");
        card.className = "card";

        card.innerHTML = `
            <h3>${restaurant.name}</h3>
            <p>${restaurant.description || "No description available"}</p>
            <p><strong>Cuisine:</strong> ${restaurant.cuisineType}</p>
            <p><strong>Delivery Fee:</strong> ${formatMoney(restaurant.deliveryFee)}</p>
            <p><strong>Accepting Orders:</strong> ${restaurant.acceptingOrders ? "Yes" : "No"}</p>
            <p class="small-text">Restaurant ID: ${restaurant.id}</p>
            <button onclick="selectRestaurant(${restaurant.id}, '${escapeText(restaurant.name)}')">View Menu</button>
        `;

        container.appendChild(card);
    });
}


/* =========================================================
   SELECT RESTAURANT
   Saves the selected restaurant and loads its menu.
   ========================================================= */
function selectRestaurant(id, name) {
    selectedRestaurantId = id;
    selectedRestaurantName = name;

    document.getElementById("selectedRestaurantText").textContent =
        `Selected Restaurant: ${name} (ID: ${id})`;

    showSection("cartSection");
    loadMenuForRestaurant(id);
}


/* =========================================================
   LOAD MENU
   Uses GET /api/restaurants/{id}/menu
   ========================================================= */
async function loadMenuForRestaurant(restaurantId) {
    const menuContainer = document.getElementById("menuList");

    menuContainer.innerHTML = "Loading menu...";

    try {
        const response = await fetch(`${API_BASE_URL}/api/restaurants/${restaurantId}/menu`);

        if (!response.ok) {
            throw new Error("Could not load menu");
        }

        const menuItems = await response.json();

        displayMenu(menuItems);
    } catch (error) {
        menuContainer.innerHTML = `<p class="error-message">${error.message}</p>`;
    }
}


/* =========================================================
   DISPLAY MENU ITEMS
   This creates menu item rows with Add to Cart buttons.
   ========================================================= */
function displayMenu(menuItems) {
    const menuContainer = document.getElementById("menuList");

    menuContainer.innerHTML = "";

    if (!menuItems || menuItems.length === 0) {
        menuContainer.innerHTML = "<p>No menu items available for this restaurant.</p>";
        return;
    }

    menuItems.forEach(item => {
        const div = document.createElement("div");
        div.className = "menu-item";

        div.innerHTML = `
            <h4>${item.name}</h4>
            <p>${item.description || "No description"}</p>
            <p><strong>Price:</strong> ${formatMoney(item.price)}</p>
            <p><strong>Calories:</strong> ${item.calories || "N/A"}</p>
            <p><strong>Available:</strong> ${item.isAvailable ? "Yes" : "No"}</p>
            <button onclick="addToCart(${item.id}, '${escapeText(item.name)}', ${item.price})" ${item.isAvailable ? "" : "disabled"}>
                Add to Cart
            </button>
        `;

        menuContainer.appendChild(div);
    });
}


/* =========================================================
   ADD TO CART
   If the item already exists, quantity will increase.
   ========================================================= */
function addToCart(menuItemId, name, price) {
    const existingItem = cart.find(item => item.menuItemId === menuItemId);

    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({
            menuItemId: menuItemId,
            name: name,
            price: price,
            quantity: 1
        });
    }

    renderCart();
    showMessage("cartMessage", "Item added to cart.");
}


/* =========================================================
   REMOVE FROM CART
   Removes an item from the local cart before placing order.
   ========================================================= */
function removeFromCart(menuItemId) {
    cart = cart.filter(item => item.menuItemId !== menuItemId);
    renderCart();
}


/* =========================================================
   RENDER CART
   Shows all cart items and calculates the subtotal.
   ========================================================= */
function renderCart() {
    const cartContainer = document.getElementById("cartItems");
    const subtotalElement = document.getElementById("cartSubtotal");

    cartContainer.innerHTML = "";

    if (cart.length === 0) {
        cartContainer.innerHTML = "<p>Your cart is empty.</p>";
        subtotalElement.textContent = "0.000 OMR";
        return;
    }

    let subtotal = 0;

    cart.forEach(item => {
        const itemTotal = item.price * item.quantity;
        subtotal += itemTotal;

        const div = document.createElement("div");
        div.className = "cart-item";

        div.innerHTML = `
            <div>
                <strong>${item.name}</strong>
                <p>Quantity: ${item.quantity}</p>
                <p>Total: ${formatMoney(itemTotal)}</p>
            </div>
            <button onclick="removeFromCart(${item.menuItemId})">Remove</button>
        `;

        cartContainer.appendChild(div);
    });

    subtotalElement.textContent = formatMoney(subtotal);
}


/* =========================================================
   PLACE ORDER
   Step 1: Create empty order
   Step 2: Add each cart item to the order
   ========================================================= */
async function placeOrder() {
    const customerId = document.getElementById("customerIdInput").value;

    if (!customerId) {
        showMessage("cartMessage", "Please enter customer ID.", true);
        return;
    }

    if (!selectedRestaurantId) {
        showMessage("cartMessage", "Please select a restaurant first.", true);
        return;
    }

    if (cart.length === 0) {
        showMessage("cartMessage", "Cart is empty.", true);
        return;
    }

    showMessage("cartMessage", "Creating order...");

    try {
        // Create empty order first
        const orderResponse = await fetch(
            `${API_BASE_URL}/api/orders/customer/${customerId}/restaurant/${selectedRestaurantId}`,
            {
                method: "POST"
            }
        );

        if (!orderResponse.ok) {
            const errorData = await orderResponse.json();
            throw new Error(errorData.message || "Could not create order");
        }

        const order = await orderResponse.json();

        // Add all cart items to the created order
        for (const item of cart) {
            const itemResponse = await fetch(`${API_BASE_URL}/api/orders/${order.id}/items`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    menuItemId: item.menuItemId,
                    quantity: item.quantity,
                    specialInstructions: ""
                })
            });

            if (!itemResponse.ok) {
                const errorData = await itemResponse.json();
                throw new Error(errorData.message || "Could not add item to order");
            }
        }

        cart = [];
        renderCart();

        showMessage("cartMessage", `Order created successfully. Order ID: ${order.id}`);

        // Move the created order ID to the tracking section
        document.getElementById("trackingOrderId").value = order.id;

    } catch (error) {
        showMessage("cartMessage", error.message, true);
    }
}


/* =========================================================
   ORDER TRACKING
   Uses GET /api/orders/{id}
   ========================================================= */
async function trackOrder() {
    const orderId = document.getElementById("trackingOrderId").value;

    if (!orderId) {
        showMessage("trackingMessage", "Please enter an order ID.", true);
        return;
    }

    showMessage("trackingMessage", "Loading order details...");

    try {
        const response = await fetch(`${API_BASE_URL}/api/orders/${orderId}`);

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Order not found");
        }

        const order = await response.json();

        displayOrderTracking(order);
        updateProgress(order.status);

        showMessage("trackingMessage", "Order loaded successfully.");
    } catch (error) {
        showMessage("trackingMessage", error.message, true);
    }
}


/* =========================================================
   DISPLAY ORDER TRACKING
   Shows order information in a simple card.
   ========================================================= */
function displayOrderTracking(order) {
    const container = document.getElementById("orderTrackingResult");

    const itemsText = order.items && order.items.length > 0
        ? order.items.map(item => `<li>${item.menuItemName} x ${item.quantity}</li>`).join("")
        : "<li>No items found</li>";

    container.innerHTML = `
        <h3>Order #${order.id}</h3>
        <p><strong>Order Code:</strong> ${order.orderCode}</p>
        <p><strong>Status:</strong> ${order.status}</p>
        <p><strong>Customer:</strong> ${order.customer ? order.customer.fullName : "N/A"}</p>
        <p><strong>Restaurant:</strong> ${order.restaurant ? order.restaurant.name : "N/A"}</p>
        <p><strong>Total:</strong> ${formatMoney(order.totalAmount)}</p>
        <p><strong>Delivery:</strong> ${order.delivery ? order.delivery.status : "Not assigned yet"}</p>
        <h4>Items</h4>
        <ul>${itemsText}</ul>
    `;
}


/* =========================================================
   UPDATE ORDER PROGRESS
   This visually highlights the order status.
   ========================================================= */
function updateProgress(status) {
    const steps = [
        "stepPending",
        "stepPreparing",
        "stepReady",
        "stepDelivered"
    ];

    steps.forEach(stepId => {
        document.getElementById(stepId).classList.remove("active");
    });

    if (!status) {
        return;
    }

    const normalizedStatus = status.toUpperCase();

    if (normalizedStatus === "PENDING" || normalizedStatus === "CONFIRMED") {
        document.getElementById("stepPending").classList.add("active");
    } else if (normalizedStatus === "PREPARING") {
        document.getElementById("stepPending").classList.add("active");
        document.getElementById("stepPreparing").classList.add("active");
    } else if (normalizedStatus === "READY" || normalizedStatus === "OUT_FOR_DELIVERY") {
        document.getElementById("stepPending").classList.add("active");
        document.getElementById("stepPreparing").classList.add("active");
        document.getElementById("stepReady").classList.add("active");
    } else if (normalizedStatus === "DELIVERED") {
        document.getElementById("stepPending").classList.add("active");
        document.getElementById("stepPreparing").classList.add("active");
        document.getElementById("stepReady").classList.add("active");
        document.getElementById("stepDelivered").classList.add("active");
    }
}


/* =========================================================
   AUTO TRACKING
   Polls the order status every 5 seconds.
   ========================================================= */
function startAutoTracking() {
    stopAutoTracking();

    trackingInterval = setInterval(() => {
        trackOrder();
    }, 5000);

    showMessage("trackingMessage", "Auto tracking started. Refreshing every 5 seconds.");
}


function stopAutoTracking() {
    if (trackingInterval) {
        clearInterval(trackingInterval);
        trackingInterval = null;
        showMessage("trackingMessage", "Auto tracking stopped.");
    }
}


/* =========================================================
   DASHBOARD REPORTS
   Loads multiple reporting endpoints.
   ========================================================= */
async function loadDashboardReports() {
    const restaurantId = document.getElementById("reportRestaurantId").value;
    const date = document.getElementById("reportDate").value;

    if (!restaurantId || !date) {
        showMessage("dashboardMessage", "Please enter restaurant ID and date.", true);
        return;
    }

    showMessage("dashboardMessage", "Loading dashboard reports...");

    try {
        await loadDailySummary(date);
        await loadRestaurantRevenue(restaurantId, date);
        await loadTopCustomers();
        await loadDriversLeaderboard();

        showMessage("dashboardMessage", "Dashboard reports loaded successfully.");
    } catch (error) {
        showMessage("dashboardMessage", error.message, true);
    }
}


/* =========================================================
   DAILY SUMMARY REPORT
   Uses GET /api/reports/platform/daily-summary?date=
   ========================================================= */
async function loadDailySummary(date) {
    const response = await fetch(`${API_BASE_URL}/api/reports/platform/daily-summary?date=${date}`);

    if (!response.ok) {
        throw new Error("Could not load daily summary");
    }

    const data = await response.json();

    document.getElementById("dailySummaryReport").innerHTML = `
        <p><strong>Date:</strong> ${data.date}</p>
        <p><strong>Total Orders:</strong> ${data.totalOrders}</p>
        <p><strong>Delivered Revenue:</strong> ${formatMoney(data.deliveredRevenue)}</p>
        <p><strong>Delivery Fees:</strong> ${formatMoney(data.deliveryFeesCollected)}</p>
    `;
}


/* =========================================================
   RESTAURANT REVENUE REPORT
   Uses GET /api/reports/revenue/restaurant/{id}?date=
   ========================================================= */
async function loadRestaurantRevenue(restaurantId, date) {
    const response = await fetch(`${API_BASE_URL}/api/reports/revenue/restaurant/${restaurantId}?date=${date}`);

    if (!response.ok) {
        throw new Error("Could not load restaurant revenue");
    }

    const data = await response.json();

    document.getElementById("restaurantRevenueReport").innerHTML = `
        <p><strong>Restaurant ID:</strong> ${data.restaurantId}</p>
        <p><strong>Date:</strong> ${data.date}</p>
        <p><strong>Revenue:</strong> ${formatMoney(data.revenue)}</p>
    `;
}


/* =========================================================
   TOP LOYALTY CUSTOMERS
   Uses GET /api/reports/customers/top-loyalty
   ========================================================= */
async function loadTopCustomers() {
    const response = await fetch(`${API_BASE_URL}/api/reports/customers/top-loyalty`);

    if (!response.ok) {
        throw new Error("Could not load top customers");
    }

    const customers = await response.json();

    if (!customers || customers.length === 0) {
        document.getElementById("topCustomersReport").innerHTML = "<p>No customers found.</p>";
        return;
    }

    const rows = customers.map(customer => `
        <tr>
            <td>${customer.id}</td>
            <td>${customer.firstName} ${customer.lastName}</td>
            <td>${customer.loyaltyPoints}</td>
        </tr>
    `).join("");

    document.getElementById("topCustomersReport").innerHTML = `
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Points</th>
            </tr>
            ${rows}
        </table>
    `;
}


/* =========================================================
   DRIVERS LEADERBOARD
   Uses GET /api/reports/drivers/leaderboard
   ========================================================= */
async function loadDriversLeaderboard() {
    const response = await fetch(`${API_BASE_URL}/api/reports/drivers/leaderboard`);

    if (!response.ok) {
        throw new Error("Could not load drivers leaderboard");
    }

    const drivers = await response.json();

    if (!drivers || drivers.length === 0) {
        document.getElementById("driversLeaderboardReport").innerHTML = "<p>No driver data found.</p>";
        return;
    }

    const rows = drivers.map(driver => `
        <tr>
            <td>${driver.driverId}</td>
            <td>${driver.driverName}</td>
            <td>${driver.completedDeliveries}</td>
        </tr>
    `).join("");

    document.getElementById("driversLeaderboardReport").innerHTML = `
        <table>
            <tr>
                <th>ID</th>
                <th>Driver</th>
                <th>Completed</th>
            </tr>
            ${rows}
        </table>
    `;
}


/* =========================================================
   FORMAT MONEY
   Simple money formatting for OMR.
   ========================================================= */
function formatMoney(amount) {
    if (amount === null || amount === undefined) {
        return "0.000 OMR";
    }

    return Number(amount).toFixed(3) + " OMR";
}


/* =========================================================
   ESCAPE TEXT
   Used when adding text inside onclick to avoid breaking quotes.
   ========================================================= */
function escapeText(text) {
    if (!text) {
        return "";
    }

    return text.replace(/'/g, "\\'");
}


/* =========================================================
   PAGE START
   Load restaurants automatically when page opens.
   ========================================================= */
document.addEventListener("DOMContentLoaded", () => {
    loadRestaurants();

    // Set today's date automatically in the dashboard date input
    const today = new Date().toISOString().split("T")[0];
    document.getElementById("reportDate").value = today;

    renderCart();
});