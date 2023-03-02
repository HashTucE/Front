package com.mediscreen.front.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {


    private static final Logger log = LogManager.getLogger(MenuController.class);


    /**
     * Returns the view for the home page
     * @return the view for the home page
     */
    @GetMapping("/home")
    @Operation(summary = "View home page",
            description = "Displays the home page of the application.")
    @ApiResponse(responseCode = "200", description = "Home page displayed successfully")
    public String viewHome(){
        log.info("Displaying the home page");
        return "menu/home";
    }

    /**
     * Returns the view for the contact page
     * @return the view for the contact page
     */
    @GetMapping("/contact")
    @Operation(summary = "View contact page",
            description = "Displays the contact page of the application.")
    @ApiResponse(responseCode = "200", description = "Contact page displayed successfully")
    public String viewContact(){
        log.info("Displaying the contact page");
        return "menu/contact";
    }

    /**
     * Returns the view for the resources page
     * @return the view for the resources page
     */
    @GetMapping("/resources")
    @Operation(summary = "View resources page",
            description = "Displays the resources page of the application.")
    @ApiResponse(responseCode = "200", description = "Resources page displayed successfully")
    public String viewResources(){
        log.info("Displaying the resources page");
        return "menu/resources";
    }
}
