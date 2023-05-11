package com.tutoringhub.supervisorconfirmationservice.domainclientlayer.lesson;


import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class Address {

    private String streetAddress;
    private String city;
    private String postalCode;

    @SuppressWarnings("unused")
    Address() {
    }

    public Address(@NotNull String streetAddress, @NotNull String city, @NotNull String postalCode) {

        Objects.requireNonNull(this.streetAddress = streetAddress);
        Objects.requireNonNull(this.city = city);
        Objects.requireNonNull(this.postalCode = postalCode);

    }

    public @NotNull String getStreetAddress() {
        return streetAddress;
    }

    public @NotNull String getCity() {
        return city;
    }

    public @NotNull String getPostalCode() {
        return postalCode;
    }
}