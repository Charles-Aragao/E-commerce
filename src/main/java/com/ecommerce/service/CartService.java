package com.ecommerce.service;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private List<CartItem> cartItems;

    public CartService() {
        this.cartItems = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) {
        CartItem existingItem = cartItems.stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            cartItems.add(new CartItem(product, quantity));
        }
    }

    public void removeItem(int productId) {
        cartItems.removeIf(item -> item.getProduct().getId() == productId);
    }

    public void updateQuantity(int productId, int newQuantity) {
        CartItem item = cartItems.stream()
                .filter(i -> i.getProduct().getId() == productId)
                .findFirst()
                .orElse(null);

        if (item != null) {
            if (newQuantity <= 0) {
                removeItem(productId);
            } else {
                item.setQuantity(newQuantity);
            }
        }
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(cartItems);
    }

    public BigDecimal getTotal() {
        return cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getItemCount() {
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public void clear() {
        cartItems.clear();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}