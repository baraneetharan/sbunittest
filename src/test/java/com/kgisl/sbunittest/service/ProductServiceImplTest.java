package com.kgisl.sbunittest.service;

import com.kgisl.sbunittest.entity.Product;
import com.kgisl.sbunittest.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product p1;
    private Product p2;

    @BeforeEach
    void setUp() {
        p1 = new Product(1L, "A", "Cat1", 10.0, true);
        p2 = new Product(2L, "B", "Cat2", 20.0, false);
    }

    @Test
    void getAllProducts_returnsList() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Product> result = productService.getAllProducts();

        assertThat(result).containsExactly(p1, p2);
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_present() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(p1));

        Optional<Product> result = productService.getProductById(1L);

        assertThat(result).contains(p1);
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_empty() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(99L);

        assertThat(result).isEmpty();
        verify(productRepository).findById(99L);
    }

    @Test
    void createProduct_setsIdNullAndSaves() {
        Product toCreate = new Product(null, "New", "Cat", 5.5, true);
        Product saved = new Product(null, "New", "Cat", 5.5, true);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        Product result = productService.createProduct(toCreate);

        assertThat(result).isEqualTo(saved);
        assertThat(toCreate.id()).isNull();
        verify(productRepository).save(toCreate);
    }

    @Test
    void updateProduct_whenExists_updatesAndSaves() {
        Product updated = new Product(null, "A2", "CatX", 33.3, false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product(1L, "A", "Cat1", 10.0, true)));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.updateProduct(1L, updated);

        assertThat(result).isNotNull();
        Product value = result;
        assertThat(value.id()).isEqualTo(1L);
        assertThat(value.name()).isEqualTo("A2");
        assertThat(value.category()).isEqualTo("CatX");
        assertThat(value.price()).isEqualTo(33.3);
        assertThat(value.available()).isFalse();
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_whenMissing_returnsEmpty() {
        when(productRepository.findById(42L)).thenReturn(Optional.empty());

        Product result = productService.updateProduct(42L, p1);

        assertThat(result).isNull();
        verify(productRepository).findById(42L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_whenExists_deletesAndReturnsTrue() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        boolean result = productService.deleteProduct(1L);

        assertThat(result).isTrue();
        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_whenMissing_returnsFalse() {
        when(productRepository.existsById(99L)).thenReturn(false);

        boolean result = productService.deleteProduct(99L);

        assertThat(result).isFalse();
        verify(productRepository).existsById(99L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
