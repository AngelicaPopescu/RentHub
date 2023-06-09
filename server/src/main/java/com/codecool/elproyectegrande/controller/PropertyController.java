package com.codecool.elproyectegrande.controller;

import com.codecool.elproyectegrande.model.Category;
import com.codecool.elproyectegrande.model.Property;
import com.codecool.elproyectegrande.model.RentalUnit;
import com.codecool.elproyectegrande.model.Review;
import com.codecool.elproyectegrande.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("properties")
@CrossOrigin(origins = "http://localhost:3000"
        , methods = {RequestMethod.PUT, RequestMethod.GET, RequestMethod.DELETE
        , RequestMethod.POST, RequestMethod.PATCH})
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public List<Property> getAllProperties() {
        return propertyService.getAllProperties();
    }

    @GetMapping("/{id}")
    public Property getPropertyDetails(@PathVariable Long id) {
        return propertyService.getPropertyById(id);
    }

    @PostMapping
    public void addProperty(@RequestBody Property property) {
        propertyService.addProperty(property);
    }

    @PostMapping("/{id}/reviews")
    public void addPropertyReview(@PathVariable Long id, @RequestBody Review review) {
        propertyService.addReviewForProperty(id, review);
    }


    @GetMapping(params = "category")
    public List<Property> getPropertiesByCategory(@RequestParam("category") String category) {
        return propertyService.getPropertiesByCategory(category);
    }

    @PatchMapping("/{propertyId}/category")
    public void addCategory(@PathVariable Long propertyId, @RequestBody Category category) {
        propertyService.addCategory(propertyId, category);
    }

    @PatchMapping("/rentalUnit")
    public void addRentalUnit(@RequestBody RentalUnit rentalUnit) {
        propertyService.addRentalUnit(rentalUnit);
    }
}
