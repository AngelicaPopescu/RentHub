package com.codecool.elproyectegrande.service;

import com.codecool.elproyectegrande.model.Category;
import com.codecool.elproyectegrande.model.Property;
import com.codecool.elproyectegrande.model.RentalUnit;
import com.codecool.elproyectegrande.model.Reservation;
import com.codecool.elproyectegrande.model.Review;
import com.codecool.elproyectegrande.utils.AddData;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//@Component
public class PropertyService {
    private List<Property> properties;

    public PropertyService() {
        this.properties = new ArrayList<>();
        AddData.addData(properties);
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public List<Property> getAllProperties() {
        return properties;
    }

    public List<Property> getPropertiesByCategory(Category category) {
        List<Property> propertiesList = new ArrayList<>();
        for (Property property : properties) {
            for (Category category1 : property.getCategories()) {
                if (category.equals(category1)) {
                    propertiesList.add(property);
                }
            }
        }
        return propertiesList;
    }

    public Property getPropertyByName(String name) {
        return properties.stream()
                .filter(property -> name.equals(property.getName()))
                .findAny()
//                .findFirst()
                .orElse(null);
    }

    public void addReviewForProperty(String name, Review review) throws IllegalArgumentException {
        Property property = getPropertyByName(name);
        if (review.getSatisfaction() < 1 || review.getSatisfaction() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        property.addReview(review);
    }

    public void addReservation(int propertyId, Reservation reservation) {
        Property property = getPropertyById(propertyId);
        for (Reservation reservation1 : property.getReservationList()) {
            if (reservation1.equals(reservation)) {
                throw new IllegalArgumentException("Reservation already exists: "
                        + reservation);
            }
        }
        if(checkInValidationForRentalUnit(propertyId,reservation)){
            property.addReservation(reservation);
        }
    }

    public void addCategory(String name, Category category) {
        Property property = getPropertyByName(name);
        for (Category category1 : property.getCategories()) {
            if (category1.equals(category)) {
                throw new IllegalArgumentException("Category already exists: "
                        + category);
            }
        }
        property.addCategory(category);
    }

    public boolean checkInValidationForRentalUnit(int propertyId, Reservation reservation){
        Property property = getPropertyById(propertyId);
        RentalUnit rentalUnit = getRentalUnitById(property, reservation.getRentalUnitID());
        List<Reservation> reservationsForRentalUnitID = rentalUnit.getReservations();
        boolean available = true;
        for (Reservation reservation1: reservationsForRentalUnitID){
            if ((reservation.getCheckIn().isAfter(reservation1.getCheckIn())
                    && reservation.getCheckIn().isBefore(reservation1.getCheckOut()))
                    || (reservation.getCheckOut().isAfter(reservation1.getCheckIn())
                    && reservation.getCheckOut().isBefore(reservation1.getCheckOut()))) {
                available = false;
                break;
                }
            if ((reservation1.getCheckIn().isAfter(reservation.getCheckIn())
                    && reservation1.getCheckIn().isBefore(reservation.getCheckOut()))
                    ||(reservation1.getCheckOut().isAfter(reservation.getCheckIn())
                    && reservation1.getCheckOut().isBefore(reservation.getCheckOut()))) {
                available=false;
                break;
            }
        }
        if (!available) {
            throw new IllegalArgumentException("Check reservation dates! ");
        }
        return available;
    }

    public Property getPropertyById(int propertyId) {
        return properties.stream()
                .filter(property -> propertyId==property.getId())
                .findAny()
                .orElse(null);
    }


    public RentalUnit getRentalUnitById(Property property, int rentalUnitId){
        return property.getRentalUnitList().stream()
                .filter(rentalUnit -> rentalUnitId==rentalUnit.getId())
                .findAny()
                .orElse(null);
    }



}
