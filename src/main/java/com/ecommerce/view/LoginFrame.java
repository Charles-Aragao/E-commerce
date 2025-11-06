package com.ecommerce.view;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;

    public LoginFrame() {
        this.userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("E-Commerce - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Título
        JLabel titleLabel = new JLabel("E-COMMERCE LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(20, 0, 30, 0);
        mainPanel.add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1; gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Usuário:"), gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Senha:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Entrar");
        JButton registerButton = new JButton("Cadastrar");

        loginButton.addActionListener(this::loginAction);
        registerButton.addActionListener(this::registerAction);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);

        // Info
        JLabel infoLabel = new JLabel("<html><center>Admin: admin/admin123<br>Cliente: cliente/123456</center></html>");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        gbc.gridy = 4; gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(infoLabel, gbc);

        add(mainPanel);
    }

    private void loginAction(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userDAO.authenticate(username, password);
        if (user != null) {
            SessionManager.getInstance().login(user);
            dispose();
            new MainFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerAction(ActionEvent e) {
        dispose();
        new RegisterFrame().setVisible(true);
    }
}