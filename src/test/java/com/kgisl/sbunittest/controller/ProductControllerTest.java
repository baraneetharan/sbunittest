package com.kgisl.sbunittest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgisl.sbunittest.entity.Product;
import com.kgisl.sbunittest.service.ProductService;
import org.mockito.Mockito;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }

    private Product sampleProduct(Long id) {
        // Product p = new Product();
        // p.setId(id);
        // p.setName("Phone");
        // p.setCategory("Electronics");
        // p.setPrice(499.99);
        // p.setAvailable(true);
        Product p = new Product(id, "Phone", "Electronics", 499.99, true);
        return p;
    }

    @Test
    void getAll_shouldReturnOkWithList() throws Exception {
        Product p1 = sampleProduct(1L);
        Product p2 = sampleProduct(2L);
        given(productService.getAllProducts()).willReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getById_whenFound_shouldReturnOk() throws Exception {
        Product p = sampleProduct(10L);
        given(productService.getProductById(10L)).willReturn(Optional.of(p));

        mockMvc.perform(get("/api/products/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.price").value(499.99))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void getById_whenNotFound_shouldReturn404() throws Exception {
        given(productService.getProductById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/products/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201WithLocation() throws Exception {
        Product request = sampleProduct(null);
        request.id();
        Product saved = sampleProduct(5L);
        given(productService.createProduct(any(Product.class))).willReturn(saved);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/5"))
                .andExpect(jsonPath("$.id").value(5));
    }

    // @Test
    // void update_whenFound_shouldReturn200WithBody() throws Exception {
    //     Product updateReq = sampleProduct(null);
    //     updateReq.name("Phone X");
    //     Product updated = sampleProduct(7L);
    //     updated.name("Phone X");
    //     given(productService.updateProduct(eq(7L), any(Product.class))).willReturn(Optional.of(updated));

    //     mockMvc.perform(put("/api/products/{id}", 7)
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(updateReq)))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.id").value(7))
    //             .andExpect(jsonPath("$.name").value("Phone X"));
    // }

    @Test
    void update_whenNotFound_shouldReturn404() throws Exception {
        Product updateReq = sampleProduct(null);
        given(productService.updateProduct(eq(77L), any(Product.class))).willReturn(null);

        mockMvc.perform(put("/api/products/{id}", 77)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenFound_shouldReturn204() throws Exception {
        given(productService.deleteProduct(3L)).willReturn(true);

        mockMvc.perform(delete("/api/products/{id}", 3))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenNotFound_shouldReturn404() throws Exception {
        given(productService.deleteProduct(333L)).willReturn(false);

        mockMvc.perform(delete("/api/products/{id}", 333))
                .andExpect(status().isNotFound());
    }
}
