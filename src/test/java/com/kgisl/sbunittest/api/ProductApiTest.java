package com.kgisl.sbunittest.api;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import com.microsoft.playwright.APIRequest;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import com.google.gson.JsonObject;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductApiTest {
    private Playwright playwright;
    private APIRequestContext request;
    private static final String BASE_URL = "http://localhost:8080/api/products";

    @BeforeAll
    void createPlaywright() {
        playwright = Playwright.create();
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(BASE_URL));
    }

    @AfterAll
    void closePlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    @Test
    void testGetAllProducts() {
        APIResponse response = request.get("");
        assertEquals(200, response.status());
        assertNotNull(response.body());
    }

    @Test
    void testCreateAndGetProduct() {
        // Create a new product


        Map<String, Object> newProduct = Map.of(
            "name", "Test Product",
            "price", 99.99,
            "category", "Test Category",
            "available", true
        );

        // Test POST
        APIResponse createResponse = request.post("", 
            RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(newProduct));
        
        assertEquals(201, createResponse.status());
        String location = createResponse.headers().get("location");
        assertNotNull(location);

        // Extract ID from location
        String id = location.substring(location.lastIndexOf('/') + 1);
        
        // Test GET by ID
        APIResponse getResponse = request.get("/" + id);
        assertEquals(200, getResponse.status());
        
        // Verify the created product
        String responseBody = getResponse.text();
        JsonObject createdProduct = com.google.gson.JsonParser.parseString(responseBody).getAsJsonObject();
        assertEquals("Test Product", createdProduct.get("name").getAsString());
        assertEquals(99.99, createdProduct.get("price").getAsDouble());
        
        // Clean up - delete the test product
        APIResponse deleteResponse = request.delete("/" + id);
        assertTrue(deleteResponse.status() == 204 || deleteResponse.status() == 404);
    }

    @Test
    void testUpdateProduct() {
        // First create a product to update
        Map<String, Object> newProduct = Map.of(
            "name", "Initial Product",
            "price", 50.0,
            "category", "Initial Category",
            "available", true
        );

        APIResponse createResponse = request.post("", 
            RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(newProduct));
        String location = createResponse.headers().get("location");
        String id = location.substring(location.lastIndexOf('/') + 1);

        // Update the product
        Map<String, Object> updatedProduct = Map.of(
            "name", "Updated Product",
            "price", 75.0,
            "category", "Updated Category",
            "available", false
        );

        APIResponse updateResponse = request.put("/" + id,
            RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(updatedProduct));
        
        assertEquals(200, updateResponse.status());
        
        // Verify the update
        APIResponse getResponse = request.get("/" + id);
        String responseBody = getResponse.text();
        JsonObject product = com.google.gson.JsonParser.parseString(responseBody).getAsJsonObject();
        assertEquals("Updated Product", product.get("name").getAsString());
        assertEquals(75.0, product.get("price").getAsDouble());
        
        // Clean up
        request.delete("/" + id);
    }

    @Test
    void testDeleteProduct() {
        // First create a product to delete
        Map<String, Object> newProduct = Map.of(
            "name", "Product to Delete",
            "price", 25.0,
            "category", "Will be deleted",
            "available", true
        );

        APIResponse createResponse = request.post("", 
            RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(newProduct));
        String location = createResponse.headers().get("location");
        String id = location.substring(location.lastIndexOf('/') + 1);

        // Delete the product
        APIResponse deleteResponse = request.delete("/" + id);
        assertEquals(204, deleteResponse.status());

        // Verify it's deleted
        APIResponse getResponse = request.get("/" + id);
        assertEquals(404, getResponse.status());
    }

    @Test
    void testGetNonExistentProduct() {
        APIResponse response = request.get("/999999");
        assertEquals(404, response.status());
    }
}
