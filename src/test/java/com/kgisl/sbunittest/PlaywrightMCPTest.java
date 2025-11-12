package com.kgisl.sbunittest;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlaywrightMCPTest {
    private Playwright playwright;
    private Browser browser;
    private Page page;
    private BrowserContext context;
    private static final String BASE_URL = "http://localhost:8080";

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false) // Set to true for CI/CD pipeline
                .setSlowMo(100)); // Slow down execution for visual verification
    }

    @AfterAll
    void closeBrowser() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setRecordVideoDir(Paths.get("videos/")));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    void testProductCRUD() {
        // Generate unique product name for test
        String productName = "TestProduct_" + UUID.randomUUID().toString().substring(0, 8);

        // 1. Navigate to the application
        page.navigate(BASE_URL);

        // 2. Add a new product
        testAddProduct(productName);

        // 3. Edit the product
        testEditProduct(productName);

        // 4. Delete the product
        testDeleteProduct("Updated_" + productName);
    }

    private void testAddProduct(String productName) {
        // Click on Add Product button
        page.click("button:has-text('Add Product')");

        // Fill in the form
        fillProductForm(productName, "Test Category", "99.99", true);

        // Submit the form
        page.click("button:has-text('Save')");

        // Verify the product is added to the table
        String productRowSelector = String.format("tr:has-text('%s')", productName);
        assertTrue(page.isVisible(productRowSelector), "Product should be visible in the table");
    }

    private void testEditProduct(String originalName) {
        // Find the edit button for the product
        String editButtonSelector = String.format("tr:has-text('%s') button:has-text('Edit')", originalName);
        page.click(editButtonSelector);

        // Update the product details
        String updatedName = "Updated_" + originalName;
        fillProductForm(updatedName, "Updated Category", "199.99", false);

        // Save the changes
        page.click("button:has-text('Update')");

        // Verify the product is updated in the table
        String updatedProductRow = String.format("tr:has-text('%s')", updatedName);
        assertTrue(page.isVisible(updatedProductRow), "Updated product should be visible in the table");
    }

    private void fillProductForm(String updatedName, String string, String string2, boolean b) {
        // fix form controls name/id from index.html
        // name attribute values are unique identifiers
        // id attribute values are used by the form to identify form controls
        // so we need to use the correct id attribute values
        page.fill("#productname", updatedName);
        page.fill("#productprice", string2);
        page.fill("#productcategory", string);
        page.selectOption("#productavailable", String.valueOf(b));
    }

    private void testDeleteProduct(String productName) {
        // Find the delete button for the product
        String deleteButtonSelector = String.format("tr:has-text('%s') button:has-text('Delete')", productName);

        // Click delete and confirm
        page.onceDialog(dialog -> dialog.accept()); // Handle the confirmation dialog
        page.click(deleteButtonSelector);

        // Verify the product is removed from the table
        String productRowSelector = String.format("tr:has-text('%s')", productName);
        assertFalse(page.isVisible(productRowSelector), "Product should be removed from the table");
    }
}
