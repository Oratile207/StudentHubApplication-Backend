package za.co.studenthub.services.channel_services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import za.co.studenthub.domain.Products;
import za.co.studenthub.factory.ProductFactory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductsServiceTest {

    @Autowired
    private ProductsService productsService;
    static Products laptop;
    static Products textbook;
    static Products calculator;
    static Products backpack;
    static Products headphones;

    @BeforeAll
    public static void setUp(){
        laptop = ProductFactory.createProduct(
            "Laptop",
            "A high-performance laptop suitable for gaming and professional work.",
            null,
            null
        );

        textbook = ProductFactory.createProduct(
            "Textbook",
            "An essential textbook for computer science students covering algorithms and data structures.",
            null,
            null
        );
        calculator = ProductFactory.createProduct(
            "Calculator",
            "A scientific calculator with advanced functions for engineering and mathematics.",
            null,
            null
        );
        backpack = ProductFactory.createProduct(
            "Backpack",
            "A durable backpack with multiple compartments for books and a laptop.",
            null,
            null
        );
        headphones = ProductFactory.createProduct(
            "Headphones",
            "Noise-cancelling headphones with high-fidelity sound for an immersive experience.",
            null,
            null
        );
    }

    @Test
    @Order(1)    
    void create() {
        Products createdLaptop = productsService.create(laptop);
        Products createdTextbook = productsService.create(textbook);
        assertNotNull(createdLaptop);
        assertNotNull(createdTextbook);
        assertNotNull(createdLaptop.getProductId());
        assertNotNull(createdTextbook.getProductId());
        assertEquals("Laptop", createdLaptop.getProductName());
        assertEquals("Textbook", createdTextbook.getProductName());
        System.out.println("Created Laptop: " + createdLaptop);
        System.out.println("Created Textbook: " + createdTextbook);
    }

    @Test
    @Order(2)    
    void read() {
        Products retrievedLaptop = productsService.read(laptop.getProductId());
        Products retrievedTextbook = productsService.read(textbook.getProductId());
        assertNotNull(retrievedLaptop);
        assertNotNull(retrievedTextbook);
        assertEquals(laptop.getProductId(), retrievedLaptop.getProductId());
        assertEquals(textbook.getProductId(), retrievedTextbook.getProductId());
        System.out.println("Retrieved Laptop: " + retrievedLaptop);
        System.out.println("Retrieved Textbook: " + retrievedTextbook);
    }

    @Test
    @Order(3)    
    void update() {
        Products updatedLaptop = laptop.toBuilder()
            .productDescription("An updated description for the high-performance laptop.")
            .build();
        Products resultLaptop = productsService.update(updatedLaptop);
        assertNotNull(resultLaptop);
        assertEquals("An updated description for the high-performance laptop.", resultLaptop.getProductDescription());
        System.out.println("Updated Laptop: " + resultLaptop);
    }

    @Test
    @Order(4)
    void delete() {
        productsService.create(calculator);
        productsService.create(backpack);
        productsService.create(headphones);
        assertNotNull(productsService.read(calculator.getProductId()));
        productsService.delete(calculator.getProductId());
        assertNull(productsService.read(calculator.getProductId()));
        System.out.println("Deleted Calculator with ID: " + calculator.getProductId());
    }

    @Test
    @Order(5)
    void getAll() {
        productsService.create(calculator);
        productsService.create(backpack);
        productsService.create(headphones);
        var allProducts = productsService.getAll();
        assertNotNull(allProducts);
        assertTrue(allProducts.size() >= 3);
        System.out.println("All Products: " + allProducts);
    }
}