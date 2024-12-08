package com.example.clients_management.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientEmail;
    private Long serviceProviderId;
    private String providerName;
    private String providerPhone;
    private String providerEmail;
    private String providerLocation;
    private String preferredService;
    private Double budget; // Changed from Integer to Double

    // Getters
    public Long getId() { return id; }
    public String getClientEmail() { return clientEmail; }
    public Long getServiceProviderId() { return serviceProviderId; }
    public String getProviderName() { return providerName; }
    public String getProviderPhone() { return providerPhone; }
    public String getProviderEmail() { return providerEmail; }
    public String getProviderLocation() { return providerLocation; }
    public String getPreferredService() { return preferredService; }
    public Double getBudget() { return budget; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    public void setServiceProviderId(Long serviceProviderId) { this.serviceProviderId = serviceProviderId; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public void setProviderPhone(String providerPhone) { this.providerPhone = providerPhone; }
    public void setProviderEmail(String providerEmail) { this.providerEmail = providerEmail; }
    public void setProviderLocation(String providerLocation) { this.providerLocation = providerLocation; }
    public void setPreferredService(String preferredService) { this.preferredService = preferredService; }
    public void setBudget(Double budget) { this.budget = budget; }

    // Default constructor
    public CartItem() {}
}