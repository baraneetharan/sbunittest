package com.kgisl.sbunittest;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

public class ProductCrudTest {

    private static final String BASE_URL = "http://localhost:8080"; // Change if needed
    
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    
    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)   // set true in CI
                .setSlowMo(300));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate(BASE_URL);
        // Wait for initial load
        page.waitForLoadState();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    void shouldPerformFullCRUDOnProducts() throws Exception {
        // -------------------------------------------------
        // 1. ADD PRODUCT
        // -------------------------------------------------
        String productName = "Playwright Book";
        String category = "Books";
        String price = "29.99";
        boolean available = true;

        fillProductForm(productName, category, price, available);
        clickSave(); // Button says "Save"

        // Wait for table row to appear
        Locator newRow = waitForProductRow(productName, category, price, "true");
        assertThat(newRow).isVisible();

        // Extract ID from first column
        String productId = newRow.locator("td").nth(0).textContent().trim();
        assertThat(productId).matches(Pattern.compile("\\d+"));

        // -------------------------------------------------
        // 2. EDIT PRODUCT
        // -------------------------------------------------
        newRow.locator("button:has-text('Edit')").click();

        // Button should now say "Update"
        assertThat(page.locator("#myBtn")).hasText("Update");

        String updatedName = "Playwright Guide";
        String updatedPrice = "39.99";
        boolean updatedAvailable = false;

        fillProductForm(updatedName, category, updatedPrice, updatedAvailable);
        clickSave(); // Now performs UPDATE

        // Verify updated row
        Locator updatedRow = waitForProductRow(updatedName, category, updatedPrice, "false");
        assertThat(updatedRow).isVisible();
        assertThat(updatedRow.locator("td").nth(0)).hasText(productId); // ID unchanged

        // Button back to "Save"
        assertThat(page.locator("#myBtn")).hasText("Save");

        // -------------------------------------------------
        // 3. DELETE PRODUCT
        // -------------------------------------------------
        updatedRow.locator("button:has-text('Delete')").click();

        // Wait for row to disappear
        assertThat(page.locator("tbody#products")).not().containsText(updatedName);
        assertThat(page.locator("tbody#products")).not().containsText(productId);
    }

    // =========================================================
    // Helper Methods
    // =========================================================

    private void fillProductForm(String name, String category, String price, boolean available) {
        page.fill("#productname", name);
        page.fill("#productcategory", category);
        page.fill("#productprice", price);
        page.selectOption("#productavailable", String.valueOf(available));
    }

    private void clickSave() {
        page.click("#myBtn");
        // Wait for fetch + table re-render
        page.waitForTimeout(800);
        page.waitForLoadState();
    }

    private Locator waitForProductRow(String name, String category, String price, String available) {
        return page.locator("tbody#products tr")
                .filter(new Locator.FilterOptions().setHasText(name))
                .filter(new Locator.FilterOptions().setHasText(category))
                .filter(new Locator.FilterOptions().setHasText(price))
                .filter(new Locator.FilterOptions().setHasText(available))
                .first();
    }
}