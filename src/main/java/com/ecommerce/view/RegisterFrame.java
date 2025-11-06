package com.ecommerce.view;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterFrame extends JFrame {
    private JTextField usernameField, emailField, fullNameField, addressField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private UserDAO userDAO;

    public RegisterFrame() {
        this.userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("E-Commerce - Cadastro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Título
        JLabel titleLabel = new JLabel("CADASTRO DE USUÁRIO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(20, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1; gbc.insets = new Insets(5, 10, 5, 10);

        // Campos
        addField(mainPanel, gbc, "Usuário:", usernameField = new JTextField(20), 1);
        addField(mainPanel, gbc, "Senha:", passwordField = new JPasswordField(20), 2);
        addField(mainPanel, gbc, "Confirmar Senha:", confirmPasswordField = new JPasswordField(20), 3);
        addField(mainPanel, gbc, "Email:", emailField = new JTextField(20), 4);
        addField(mainPanel, gbc, "Nome Completo:", fullNameField = new JTextField(20), 5);
        addField(mainPanel, gbc, "Endereço:", addressField = new JTextField(20), 6);
        addField(mainPanel, gbc, "Telefone:", phoneField = new JTextField(20), 7);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton registerButton = new JButton("Cadastrar");
        JButton backButton = new JButton("Voltar");

        registerButton.addActionListener(this::registerAction);
        backButton.addActionListener(this::backAction);

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    private void registerAction(ActionEvent e) {
        if (!validateFields()) return;

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        if (userDAO.usernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Nome de usuário já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User(0, username, password, email, fullName, address, phone, User.TipoUsuario.CLIENTE);
        userDAO.save(user);

        JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new LoginFrame().setVisible(true);
    }

    private boolean validateFields() {
        if (usernameField.getText().trim().isEmpty() ||
            passwordField.getPassword().length == 0 ||
            confirmPasswordField.getPassword().length == 0 ||
            emailField.getText().trim().isEmpty() ||
            fullNameField.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!new String(passwordField.getPassword()).equals(new String(confirmPasswordField.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Senhas não coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void backAction(ActionEvent e) {
        dispose();
        new LoginFrame().setVisible(true);
    }
}