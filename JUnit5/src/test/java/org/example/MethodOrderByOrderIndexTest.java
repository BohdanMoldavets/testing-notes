package org.example;


import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MethodOrderByOrderIndexTest {

    @Order(1)
    @Test
    public void testB() {
        System.out.println("Running test B");
    }

    @Order(2)
    @Test
    public void testC() {
        System.out.println("Running test C");
    }

    @Order(3)
    @Test
    public void testA() {
        System.out.println("Running test A");
    }

}
