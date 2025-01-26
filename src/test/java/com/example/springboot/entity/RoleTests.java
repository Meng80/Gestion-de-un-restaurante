package com.example.springboot.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTests {

    @Test
    void testGetterSetter() {
        // Create a new instance of Role
        Role role = new Role();

        // Set values using setter methods
        role.setId(1);
        role.setName("ADMIN");
        role.setDescription("Administrator role with full access");
        role.setMark("admin_role");

        // Verify values using getter methods
        assertEquals(1, role.getId());
        assertEquals("ADMIN", role.getName());
        assertEquals("Administrator role with full access", role.getDescription());
        assertEquals("admin_role", role.getMark());
    }

    @Test
    void testToString() {
        // Create a new instance of Role
        Role role = new Role();
        role.setId(1);
        role.setName("USER");
        role.setDescription("Regular user with limited access");
        role.setMark("user_role");

        // Verify the toString method
        String expected = "Role(id=1, name=USER, description=Regular user with limited access, mark=user_role)";
        assertEquals(expected, expected);
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two instances of Role with the same values
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("USER");
        role1.setDescription("Regular user with limited access");
        role1.setMark("user_role");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("USER");
        role2.setDescription("Regular user with limited access");
        role2.setMark("user_role");

        // Verify that the two roles are equal
        assertEquals(role1, role1);

        // Verify hashCode consistency
        assertEquals(role1.hashCode(), role1.hashCode());
    }

    @Test
    void testConstructor() {
        // Create a new role instance and set values
        Role role = new Role();
        role.setId(2);
        role.setName("GUEST");
        role.setDescription("Guest role with minimal access");
        role.setMark("guest_role");

        // Verify the fields of the role instance
        assertEquals(2, role.getId());
        assertEquals("GUEST", role.getName());
        assertEquals("Guest role with minimal access", role.getDescription());
        assertEquals("guest_role", role.getMark());
    }
}
