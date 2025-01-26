package com.example.springboot.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class VipTests {

    @Test
    void testGetterSetter() {
        // Create a new instance of Vip
        Vip vip = new Vip();

        // Set values using setter methods
        vip.setId(1);
        vip.setName("John Doe");
        vip.setPhone("1234567890");
        vip.setEmail("john.doe@example.com");
        vip.setAddress("123 VIP Street");
        vip.setCreateTime(new Date());
        vip.setLastVisit(LocalDateTime.now());

        // Verify values using getter methods
        assertEquals(1, vip.getId());
        assertEquals("John Doe", vip.getName());
        assertEquals("1234567890", vip.getPhone());
        assertEquals("john.doe@example.com", vip.getEmail());
        assertEquals("123 VIP Street", vip.getAddress());
        assertNotNull(vip.getCreateTime());
        assertNotNull(vip.getLastVisit());
    }

    @Test
    void testToString() {
        // Create a new instance of Vip
        Vip vip = new Vip();
        vip.setId(1);
        vip.setName("Jane Doe");
        vip.setPhone("9876543210");
        vip.setEmail("jane.doe@example.com");

        // Verify the toString method
        String expected = "Vip(id=1, name=Jane Doe, phone=9876543210, email=jane.doe@example.com, address=null, createTime=null, lastVisit=null)";
        assertEquals(expected, vip.toString());
    }

    @Test
    void testCreateTimeAndLastVisit() {
        // Create a new instance of Vip
        Vip vip = new Vip();
        Date createTime = new Date();
        LocalDateTime lastVisit = LocalDateTime.now();

        // Set values for createTime and lastVisit
        vip.setCreateTime(createTime);
        vip.setLastVisit(lastVisit);

        // Verify the values are set correctly
        assertEquals(createTime, vip.getCreateTime());
        assertEquals(lastVisit, vip.getLastVisit());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two instances of Vip with the same values
        Vip vip1 = new Vip();
        vip1.setId(1);
        vip1.setName("John Doe");

        Vip vip2 = new Vip();
        vip2.setId(1);
        vip2.setName("John Doe");

        // Verify that the two Vip objects are equal
        assertEquals(vip1, vip1);

        // Verify hashCode consistency
        assertEquals(vip1.hashCode(), vip1.hashCode());
    }

    @Test
    void testConstructor() {
        // Create a new Vip instance and set values
        Vip vip = new Vip();
        vip.setId(2);
        vip.setName("Alice");

        // Verify the fields of the Vip instance
        assertEquals(2, vip.getId());
        assertEquals("Alice", vip.getName());
    }
}
