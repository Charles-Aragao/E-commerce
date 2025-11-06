package com.ecommerce.dao;

import com.ecommerce.model.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDAO {
    private static List<Product> products = new ArrayList<>();
    private static int nextId = 1;

    static {
        // Produtos iniciais
        products.add(new Product(nextId++, "Smartphone Samsung Galaxy", "Smartphone com 128GB", new BigDecimal("899.99"), 50, "Eletrônicos", ""));
        products.add(new Product(nextId++, "Notebook Dell Inspiron", "Notebook i5 8GB RAM", new BigDecimal("2499.99"), 20, "Eletrônicos", ""));
        products.add(new Product(nextId++, "Tênis Nike Air Max", "Tênis esportivo confortável", new BigDecimal("299.99"), 100, "Calçados", ""));
        products.add(new Product(nextId++, "Camiseta Polo", "Camiseta polo masculina", new BigDecimal("79.99"), 200, "Roupas", ""));
        products.add(new Product(nextId++, "Livro Java Programming", "Livro sobre programação Java", new BigDecimal("89.99"), 30, "Livros", ""));
    }

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public Product findById(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public List<Product> findByCategory(String category) {
        return products.stream().filter(p -> p.getCategoria().equalsIgnoreCase(category)).collect(Collectors.toList());
    }

    public List<Product> searchByName(String name) {
        return products.stream().filter(p -> p.getNome().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }

    public void save(Product product) {
        if (product.getId() == 0) {
            product.setId(nextId++);
            products.add(product);
        } else {
            Product existing = findById(product.getId());
            if (existing != null) {
                int index = products.indexOf(existing);
                products.set(index, product);
            }
        }
    }

    public void delete(int id) {
        products.removeIf(p -> p.getId() == id);
    }

    public void updateStock(int productId, int newStock) {
        Product product = findById(productId);
        if (product != null) {
            product.setEstoque(newStock);
        }
    }
}