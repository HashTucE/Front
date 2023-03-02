package com.mediscreen.front.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MenuControllerTest {

    @InjectMocks
    private MenuController menuController;


    @Test
    @DisplayName("Should return the view for the home page")
    void viewHomeTest() {

        // Arrange
        // Act
        String viewName = menuController.viewHome();

        // Assert
        assertEquals("menu/home", viewName);
    }


    @Test
    @DisplayName("Should return the view for the contact page")
    void viewContactTest() {

        // Arrange
        // Act
        String viewName = menuController.viewContact();

        // Assert
        assertEquals("menu/contact", viewName);
    }


    @Test
    @DisplayName("Should return the view for the resources page")
    void viewResourcesTest() {

        // Arrange
        // Act
        String viewName = menuController.viewResources();

        // Assert
        assertEquals("menu/resources", viewName);
    }
}
