package com.ecommerce.dao;

import com.ecommerce.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static List<User> users = new ArrayList<>();
    private static int nextId = 1;

    static {
        // Usuário admin padrão
        users.add(new User(nextId++, "admin", "admin123", "admin@ecommerce.com", "Administrador", "Rua Admin, 123", "(11) 99999-9999", User.TipoUsuario.ADMIN));
        // Usuário cliente exemplo
        users.add(new User(nextId++, "cliente", "123456", "cliente@email.com", "João Silva", "Rua das Flores, 456", "(11) 88888-8888", User.TipoUsuario.CLIENTE));
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public User findById(int id) {
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public User findByUsername(String username) {
        return users.stream().filter(u -> u.getNomeUsuario().equals(username)).findFirst().orElse(null);
    }

    public User authenticate(String username, String password) {
        return users.stream()
                .filter(u -> u.getNomeUsuario().equals(username) && u.getSenha().equals(password))
                .findFirst()
                .orElse(null);
    }

    public void save(User user) {
        if (user.getId() == 0) {
            user.setId(nextId++);
            users.add(user);
        } else {
            User existing = findById(user.getId());
            if (existing != null) {
                int index = users.indexOf(existing);
                users.set(index, user);
            }
        }
    }

    public boolean usernameExists(String username) {
        return users.stream().anyMatch(u -> u.getNomeUsuario().equals(username));
    }

    public void delete(int id) {
        users.removeIf(u -> u.getId() == id);
    }
}