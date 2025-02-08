package org.example;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MethodOrderByName {

    @Test
    public void testB() {
        System.out.println("Running test B");
    }

    @Test
    public void testC() {
        System.out.println("Running test C");
    }

    @Test
    public void testA() {
        System.out.println("Running test A");
    }
}
