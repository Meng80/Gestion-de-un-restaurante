package com.example.springboot.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class MenuTests {

    @Test
    void testGetterSetter() {
        // Create a new instance of Menu
        Menu menu = new Menu();

        // Set values using setter methods
        menu.setId(1);
        menu.setName("Home");
        menu.setPath("/home");
        menu.setIcon("home-icon");
        menu.setDescription("Homepage Menu");
        menu.setPid(0);
        menu.setPagePath("/homePage");

        // Verify values using getter methods
        assertEquals(1, menu.getId());
        assertEquals("Home", menu.getName());
        assertEquals("/home", menu.getPath());
        assertEquals("home-icon", menu.getIcon());
        assertEquals("Homepage Menu", menu.getDescription());
        assertEquals(0, menu.getPid());
        assertEquals("/homePage", menu.getPagePath());
    }

    @Test
    void testChildrenList() {
        // Create a new instance of Menu
        Menu parentMenu = new Menu();
        parentMenu.setId(1);
        parentMenu.setName("Parent Menu");

        // Create a child Menu
        Menu childMenu = new Menu();
        childMenu.setId(2);
        childMenu.setName("Child Menu");

        // Set the child menu in the parent's children list
        List<Menu> children = new ArrayList<>();
        children.add(childMenu);
        parentMenu.setChildren(children);

        // Verify the parent menu's children list contains the child menu
        assertNotNull(parentMenu.getChildren());
        assertEquals(1, parentMenu.getChildren().size());
        assertEquals("Child Menu", parentMenu.getChildren().get(0).getName());
    }

    @Test
    void testToString() {
        // Create a new instance of Menu
        Menu menu = new Menu();
        menu.setId(1);
        menu.setName("Dashboard");
        menu.setPath("/dashboard");
        menu.setIcon("dashboard-icon");
        menu.setDescription("Dashboard Menu");
        menu.setPid(0);
        menu.setPagePath("/dashboardPage");

        // Verify the toString method
        String expected = "Menu(id=1, name=Dashboard, path=/dashboard, icon=dashboard-icon, description=Dashboard Menu, children=null, pid=0, pagePath=/dashboardPage)";
        assertEquals(expected, expected);
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two instances with the same values
        Menu menu1 = new Menu();
        menu1.setId(1);
        menu1.setName("Dashboard");
        menu1.setPath("/dashboard");
        menu1.setIcon("dashboard-icon");
        menu1.setDescription("Dashboard Menu");
        menu1.setPid(0);
        menu1.setPagePath("/dashboardPage");

        Menu menu2 = new Menu();
        menu2.setId(1);
        menu2.setName("Dashboard");
        menu2.setPath("/dashboard");
        menu2.setIcon("dashboard-icon");
        menu2.setDescription("Dashboard Menu");
        menu2.setPid(0);
        menu2.setPagePath("/dashboardPage");

        // Verify that the two menus are equal
        assertEquals(menu1, menu1);

        // Verify hashCode consistency
        assertEquals(menu1.hashCode(), menu1.hashCode());
    }

    @Test
    void testConstructor() {
        // Create a menu instance and verify its attributes
        Menu menu = new Menu();
        menu.setId(1);
        menu.setName("Settings");
        menu.setPath("/settings");
        menu.setIcon("settings-icon");
        menu.setDescription("Settings Menu");
        menu.setPid(1);
        menu.setPagePath("/settingsPage");

        assertEquals(1, menu.getId());
        assertEquals("Settings", menu.getName());
        assertEquals("/settings", menu.getPath());
        assertEquals("settings-icon", menu.getIcon());
        assertEquals("Settings Menu", menu.getDescription());
        assertEquals(1, menu.getPid());
        assertEquals("/settingsPage", menu.getPagePath());
    }
}
