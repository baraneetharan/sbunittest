package com.kgisl.sbunittest;


import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;
 
public class ProductApiAutomation {
 
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
 
            page.navigate("http://localhost:8080/index.html");
 
            System.out.println(" Starting Browser API Automation...");
 
            RunnableWithArgs showInBrowser = (url, responseText) -> {
                page.evaluate("window.location = '" + url + "'");
                page.waitForTimeout(1000);
 
                page.evaluate("document.body.innerHTML = '<pre style=\"color:black; font-size:16px;\">' + " +
                        "JSON.stringify(JSON.parse(" + JSONObject.quote(responseText) + "), null, 2) + '</pre>'");
                page.waitForTimeout(4000);
            };
 
           
            String baseUrl = "http://localhost:8080/api/products";
 
            APIRequestContext request = playwright.request().newContext();
 
            APIResponse createResponse = request.post(baseUrl,
                RequestOptions.create()
                    .setHeader("Content-Type", "application/json")
                    .setData("{\"name\":\"Playwright Laptop\",\"category\":\"Electronics\",\"price\":999.99,\"available\":true}")
            );
            String createJson = createResponse.text();
            System.out.println("POST Response: " + createJson);
            showInBrowser.run(baseUrl, createJson);
 
            JSONObject created = new JSONObject(createJson);
            int id = created.getInt("id");
 
            APIResponse getAllResponse = request.get(baseUrl);
            String getAllJson = getAllResponse.text();
            System.out.println("GET ALL Response: " + getAllJson);
            showInBrowser.run(baseUrl, getAllJson);
 
            String getByIdUrl = baseUrl + "/" + id;
            APIResponse getByIdResponse = request.get(getByIdUrl);
            String getByIdJson = getByIdResponse.text();
            System.out.println("GET BY ID Response: " + getByIdJson);
            showInBrowser.run(getByIdUrl, getByIdJson);
 
            String updateUrl = baseUrl + "/" + id;
            APIResponse updateResponse = request.put(updateUrl,
                RequestOptions.create()
                    .setHeader("Content-Type", "application/json")
                    .setData("{\"name\":\"Updated Playwright Laptop\",\"category\":\"Electronics\",\"price\":1099.99,\"available\":true}")
            );
            String updateJson = updateResponse.text();
            System.out.println("PUT Response: " + updateJson);
            showInBrowser.run(updateUrl, updateJson);
 
            String deleteUrl = baseUrl + "/" + id;
            APIResponse deleteResponse = request.delete(deleteUrl);
            String deleteJson = "{\"status\":" + deleteResponse.status() + "}";
            System.out.println("DELETE Response: " + deleteJson);
            showInBrowser.run(deleteUrl, deleteJson);
 
            System.out.println(" Automation completed successfully!");
            TimeUnit.SECONDS.sleep(2);
            browser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FunctionalInterface
    interface RunnableWithArgs {
        void run(String url, String responseText);
    }
}
 
 
 
 