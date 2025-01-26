package com.example.springboot.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilesTests {

    @Test
    void testGetterSetter() {
        // Create a new instance of Files
        Files file = new Files();

        // Set values using setter methods
        file.setId(1);
        file.setName("example.jpg");
        file.setType("image/jpeg");
        file.setSize(1024L);
        file.setUrl("http://example.com/example.jpg");
        file.setMd5("d41d8cd98f00b204e9800998ecf8427e");
        file.setIsDelete(false);
        file.setEnable(true);

        // Verify values using getter methods
        assertEquals(1, file.getId());
        assertEquals("example.jpg", file.getName());
        assertEquals("image/jpeg", file.getType());
        assertEquals(1024L, file.getSize());
        assertEquals("http://example.com/example.jpg", file.getUrl());
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", file.getMd5());
        assertFalse(file.getIsDelete());
        assertTrue(file.getEnable());
    }

    @Test
    void testToString() {
        // Create a new instance of Files
        Files file = new Files();
        file.setId(1);
        file.setName("example.jpg");
        file.setType("image/jpeg");
        file.setSize(1024L);
        file.setUrl("http://example.com/example.jpg");
        file.setMd5("d41d8cd98f00b204e9800998ecf8427e");
        file.setIsDelete(false);
        file.setEnable(true);

        // Verify the toString method
        String expected = "Files(id=1, name=example.jpg, type=image/jpeg, size=1024, url=http://example.com/example.jpg, md5=d41d8cd98f00b204e9800998ecf8427e, isDelete=false, enable=true)";
        assertEquals(expected, file.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two instances with the same values
        Files file1 = new Files();
        file1.setId(1);
        file1.setName("example.jpg");
        file1.setType("image/jpeg");
        file1.setSize(1024L);
        file1.setUrl("http://example.com/example.jpg");
        file1.setMd5("d41d8cd98f00b204e9800998ecf8427e");
        file1.setIsDelete(false);
        file1.setEnable(true);

        Files file2 = new Files();
        file2.setId(1);
        file2.setName("example.jpg");
        file2.setType("image/jpeg");
        file2.setSize(1024L);
        file2.setUrl("http://example.com/example.jpg");
        file2.setMd5("d41d8cd98f00b204e9800998ecf8427e");
        file2.setIsDelete(false);
        file2.setEnable(true);

        // Verify they are equal
        assertEquals(file1, file2);

        // Verify hashCode is consistent
        assertEquals(file1.hashCode(), file2.hashCode());
    }

    @Test
    void testConstructor() {
        // Create an instance using the constructor and verify values
        Files file = new Files();
        file.setId(1);
        file.setName("example.jpg");
        file.setType("image/jpeg");
        file.setSize(1024L);
        file.setUrl("http://example.com/example.jpg");
        file.setMd5("d41d8cd98f00b204e9800998ecf8427e");
        file.setIsDelete(false);
        file.setEnable(true);

        assertEquals(1, file.getId());
        assertEquals("example.jpg", file.getName());
        assertEquals("image/jpeg", file.getType());
        assertEquals(1024L, file.getSize());
        assertEquals("http://example.com/example.jpg", file.getUrl());
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", file.getMd5());
        assertFalse(file.getIsDelete());
        assertTrue(file.getEnable());
    }
}
