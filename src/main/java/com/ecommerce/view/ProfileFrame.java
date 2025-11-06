package com.ecommerce.view;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ProfileFrame extends JFrame {
    private JTextField usernameField, emailField, fullNameField, addressField, phoneField;
    private JPasswordField passwordField;
    private UserDAO userDAO;
    private User currentUser;

    public ProfileFrame() {
        this.userDAO = new UserDAO();
        this.currentUser = SessionManager.getInstance().getCurrentUser();
        initComponents();
        loadUserData();
    }

    private void initComponents() {
        setTitle("Perfil do Usuário");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Título
        JLabel titleLabel = new JLabel("MEU PERFIL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(20, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1; gbc.insets = new Insets(5, 10, 5, 10);

        // Campos
        addField(mainPanel, gbc, "Usuário:", usernameField = new JTextField(20), 1);
        addField(mainPanel, gbc, "Nova Senha:", passwordField = new JPasswordField(20), 2);
        addField(mainPanel, gbc, "Email:", emailField = new JTextField(20), 3);
        addField(mainPanel, gbc, "Nome Completo:", fullNameField = new JTextField(20), 4);
        addField(mainPanel, gbc, "Endereço:", addressField = new JTextField(20), 5);
        addField(mainPanel, gbc, "Telefone:", phoneField = new JTextField(20), 6);

        usernameField.setEditable(false); // Username não pode ser alterado

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(this::saveProfile);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    private void loadUserData() {
        usernameField.setText(currentUser.getNomeUsuario());
        emailField.setText(currentUser.getEmail());
        fullNameField.setText(currentUser.getNomeCompleto());
        addressField.setText(currentUser.getEndereco());
        phoneField.setText(currentUser.getTelefone());
    }

    private void saveProfile(ActionEvent e) {
        if (!validateFields()) return;

        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        // Atualizar dados do usuário
        if (!password.isEmpty()) {
            currentUser.setSenha(password);
        }
        currentUser.setEmail(email);
        currentUser.setNomeCompleto(fullName);
        currentUser.setEndereco(address);
        currentUser.setTelefone(phone);

        userDAO.save(currentUser);

        JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private boolean validateFields() {
        if (emailField.getText().trim().isEmpty() || fullNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}