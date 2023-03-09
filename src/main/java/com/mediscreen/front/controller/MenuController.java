package com.mediscreen.front.controller;

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
    public String viewHome(){
        log.info("Displaying the home page");
        return "menu/home";
    }

    /**
     * Returns the view for the contact page
     * @return the view for the contact page
     */
    @GetMapping("/contact")
    public String viewContact(){
        log.info("Displaying the contact page");
        return "menu/contact";
    }

    /**
     * Returns the view for the resources page
     * @return the view for the resources page
     */
    @GetMapping("/resources")
    public String viewResources(){
        log.info("Displaying the resources page");
        return "menu/resources";
    }
}
