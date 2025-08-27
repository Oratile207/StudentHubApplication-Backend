package za.co.studenthub.factory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import za.co.studenthub.domain.Products;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductFactoryTest {

    @Test
    void createProduct() {

        Products laptop = ProductFactory.createProduct(
            "Laptop",
            "A high-performance laptop suitable for gaming and professional work.",
            null,
            null
        );

        Products textbook = ProductFactory.createProduct(
            "Textbook",
            "An essential textbook for computer science students covering algorithms and data structures.",
            null,
            null
        );

        assertNotNull(laptop);
        assertNotNull(textbook);
        assertNotNull(laptop.getProductId());
        assertNotNull(textbook.getProductId());
        assertEquals("Laptop", laptop.getProductName());
        assertEquals("Textbook", textbook.getProductName());
    }
}