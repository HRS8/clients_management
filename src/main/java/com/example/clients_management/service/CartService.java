package com.example.clients_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.clients_management.entities.CartItem;
import com.example.clients_management.entities.ServiceProviderDetails;
import com.example.clients_management.repositories.CartItemRepository;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public void addToCart(ServiceProviderDetails provider, String clientEmail, int quantity) {
        for(int i = 0; i < quantity; i++) {
            CartItem cartItem = new CartItem();
            cartItem.setClientEmail(clientEmail);
            cartItem.setServiceProviderId(provider.getId());
            cartItem.setProviderName(provider.getName());
            cartItem.setProviderPhone(provider.getMobile());
            cartItem.setProviderEmail(provider.getEmail());
            cartItem.setProviderLocation(provider.getLocation());
            cartItem.setPreferredService(provider.getPreferredService());
            cartItem.setBudget(provider.getBudget());// Each item has quantity 1 since we're duplicating
            
            cartItemRepository.save(cartItem);
        }
    }

    public List<CartItem> getCartItems(String clientEmail) {
        return cartItemRepository.findByClientEmail(clientEmail);
    }

    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}