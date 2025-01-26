package com.example.springboot.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleMenuTests {

    @Test
    void testGetterSetter() {
        // Create a new instance of RoleMenu
        RoleMenu roleMenu = new RoleMenu();

        // Set values using setter methods
        roleMenu.setRoleId(1);
        roleMenu.setMenuId(100);

        // Verify values using getter methods
        assertEquals(1, roleMenu.getRoleId());
        assertEquals(100, roleMenu.getMenuId());
    }

    @Test
    void testToString() {
        // Create a new instance of RoleMenu
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(1);
        roleMenu.setMenuId(100);

        // Verify the toString method
        String expected = "RoleMenu(roleId=1, menuId=100)";
        assertEquals(expected, roleMenu.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two instances of RoleMenu with the same values
        RoleMenu roleMenu1 = new RoleMenu();
        roleMenu1.setRoleId(1);
        roleMenu1.setMenuId(100);

        RoleMenu roleMenu2 = new RoleMenu();
        roleMenu2.setRoleId(1);
        roleMenu2.setMenuId(100);

        // Verify that the two RoleMenu objects are equal
        assertEquals(roleMenu1, roleMenu2);

        // Verify hashCode consistency
        assertEquals(roleMenu1.hashCode(), roleMenu2.hashCode());
    }

    @Test
    void testConstructor() {
        // Create a new RoleMenu instance and set values
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(2);
        roleMenu.setMenuId(200);

        // Verify the fields of the RoleMenu instance
        assertEquals(2, roleMenu.getRoleId());
        assertEquals(200, roleMenu.getMenuId());
    }
}
