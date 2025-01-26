package com.example.springboot.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleEnumTests {

    @Test
    void testEnumValues() {
        // Ensure that the enum contains the expected constants
        assertTrue(RoleEnum.ROLE_ADMIN instanceof RoleEnum);
        assertTrue(RoleEnum.ROLE_USER instanceof RoleEnum);
    }

    @Test
    void testEnumSize() {
        // Ensure that the enum has exactly 2 constants
        assertEquals(2, RoleEnum.values().length);
    }

    @Test
    void testEnumNames() {
        // Ensure the enum constants' names are correct
        assertEquals("ROLE_ADMIN", RoleEnum.ROLE_ADMIN.name());
        assertEquals("ROLE_USER", RoleEnum.ROLE_USER.name());
    }

    @Test
    void testEnumValuesCorrect() {
        // Verify that the enum contains the correct values
        assertEquals(RoleEnum.ROLE_ADMIN, RoleEnum.valueOf("ROLE_ADMIN"));
        assertEquals(RoleEnum.ROLE_USER, RoleEnum.valueOf("ROLE_USER"));
    }

    @Test
    void testEnumValuesInvalid() {
        // Ensure that invalid values throw an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> RoleEnum.valueOf("INVALID_ROLE"));
    }
}
