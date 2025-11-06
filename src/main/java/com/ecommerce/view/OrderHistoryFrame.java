package com.ecommerce.view;

import com.ecommerce.model.Order;
import com.ecommerce.service.OrderService;
import com.ecommerce.util.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderHistoryFrame extends JFrame {
    private OrderService orderService;
    private JList<Order> orderList;
    private DefaultListModel<Order> listModel;
    private JTextArea detailsArea;

    public OrderHistoryFrame() {
        this.orderService = new OrderService();
        initComponents();
        loadOrders();
    }

    private void initComponents() {
        setTitle("Histórico de Pedidos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("MEUS PEDIDOS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Order List
        listModel = new DefaultListModel<>();
        orderList = new JList<>(listModel);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderList.setCellRenderer(new OrderListCellRenderer());

        JScrollPane orderScrollPane = new JScrollPane(orderList);
        orderScrollPane.setPreferredSize(new Dimension(400, 500));

        // Order Details
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Detalhes do Pedido"));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setLineWrap(true);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);

        // Update details when selection changes
        orderList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Order selected = orderList.getSelectedValue();
                if (selected != null) {
                    showOrderDetails(selected);
                }
            }
        });

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Fechar");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(orderScrollPane, BorderLayout.WEST);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadOrders() {
        listModel.clear();
        List<Order> orders = orderService.getOrdersByUser(SessionManager.getInstance().getCurrentUser().getId());
        for (Order order : orders) {
            listModel.addElement(order);
        }

        if (orders.isEmpty()) {
            detailsArea.setText("Nenhum pedido encontrado.");
        }
    }

    private void showOrderDetails(Order order) {
        StringBuilder details = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        details.append("PEDIDO #").append(order.getId()).append("\n");
        details.append("Data: ").append(order.getOrderDate().format(formatter)).append("\n");
        details.append("Status: ").append(getStatusText(order.getStatus())).append("\n");
        details.append("Endereço de Entrega: ").append(order.getDeliveryAddress()).append("\n\n");

        details.append("ITENS:\n");
        details.append("----------------------------------------\n");
        
        order.getItems().forEach(item -> {
            details.append(String.format("%-30s Qtd: %2d  R$ %8s\n",
                    item.getProduct().getNome(),
                    item.getQuantity(),
                    item.getSubtotal()));
        });

        details.append("----------------------------------------\n");
        details.append(String.format("TOTAL: R$ %s", order.getTotal()));

        detailsArea.setText(details.toString());
    }

    private String getStatusText(Order.OrderStatus status) {
        return switch (status) {
            case PENDING -> "Pendente";
            case CONFIRMED -> "Confirmado";
            case SHIPPED -> "Enviado";
            case DELIVERED -> "Entregue";
            case CANCELLED -> "Cancelado";
        };
    }

    private class OrderListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Order) {
                Order order = (Order) value;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                setText(String.format("<html><b>Pedido #%d</b><br/>%s - R$ %s<br/>%s</html>",
                        order.getId(),
                        order.getOrderDate().format(formatter),
                        order.getTotal(),
                        getStatusText(order.getStatus())));
            }

            return this;
        }
    }
}