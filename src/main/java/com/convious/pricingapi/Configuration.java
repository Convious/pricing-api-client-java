package com.convious.pricingapi;

public class Configuration {
    private final String authEndpoint;
    private final String inventoryEndpoint;
    private final String pricingEndpoint;

    public Configuration(String authEndpoint, String inventoryEndpoint, String pricingEndpoint) {
        this.authEndpoint = authEndpoint;
        this.inventoryEndpoint = inventoryEndpoint;
        this.pricingEndpoint = pricingEndpoint;
    }

    public String authEndpoint() {
        return authEndpoint;
    }

    public String inventoryEndpoint() {
        return inventoryEndpoint;
    }

    public String pricingEndpoint() {
        return pricingEndpoint;
    }

    public static Configuration defaultConfiguration() {
        return new Configuration(
                "https://identity.convious.com",
                "https://inventory.convious.com",
                "https://pricer.convious.com"
        );
    }
}
