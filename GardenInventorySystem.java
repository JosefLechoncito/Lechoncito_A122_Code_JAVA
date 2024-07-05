import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GardenInventorySystem extends JFrame {
    private JTextField seedNameField, seedTypeField, seedQuantityField, seedPriceField;
    private JTextField fertNameField, fertTypeField, fertQuantityField, fertPriceField;
    private JTextField toolNameField, toolTypeField, toolQuantityField, toolPriceField;
    private JTextArea displayArea; 
    private JButton addButton, updateButton, removeButton, viewButton;

    private final String FILE_NAME = "garden_inventory.txt";

    public GardenInventorySystem() {
        super("GreenThumb Inventory");

        
        getContentPane().setBackground(new Color(204, 255, 204));

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel seedPanel = createSeedPanel();
        JPanel fertPanel = createFertPanel();
        JPanel toolPanel = createToolPanel();

        tabbedPane.addTab("Seeds", seedPanel);
        tabbedPane.addTab("Fertilizers", fertPanel);
        tabbedPane.addTab("Gardening Tools", toolPanel);

        add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(204, 255, 204));
        
        addButton = new JButton("Add Item");
        updateButton = new JButton("Update Item");
        removeButton = new JButton("Remove Item");
        viewButton = new JButton("View Inventory");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(viewButton);

        add(buttonPanel, BorderLayout.SOUTH);

        displayArea = new JTextArea(15, 30);
        displayArea.setBackground(new Color(204, 255, 204));
        add(new JScrollPane(displayArea), BorderLayout.EAST);

        
        addButton.addActionListener(e -> addItem(tabbedPane.getSelectedIndex()));
        updateButton.addActionListener(e -> updateItem(tabbedPane.getSelectedIndex()));
        removeButton.addActionListener(e -> removeItem(tabbedPane.getSelectedIndex()));
        viewButton.addActionListener(e -> viewInventory());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
    }

    private JPanel createSeedPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.setBackground(new Color(204, 255, 204));
        panel.add(new JLabel("Name:"));
        seedNameField = new JTextField(10);
        panel.add(seedNameField);

        panel.add(new JLabel("Type:"));
        seedTypeField = new JTextField(10);
        panel.add(seedTypeField);

        panel.add(new JLabel("Quantity:"));
        seedQuantityField = new JTextField(5);
        panel.add(seedQuantityField);

        panel.add(new JLabel("Price:"));
        seedPriceField = new JTextField(5);
        panel.add(seedPriceField);

        return panel;
    }

    private JPanel createFertPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.setBackground(new Color(204, 255, 204));
        panel.add(new JLabel("Name:"));
        fertNameField = new JTextField(10);
        panel.add(fertNameField);

        panel.add(new JLabel("Type:"));
        fertTypeField = new JTextField(10);
        panel.add(fertTypeField);

        panel.add(new JLabel("Quantity:"));
        fertQuantityField = new JTextField(5);
        panel.add(fertQuantityField);

        panel.add(new JLabel("Price:"));
        fertPriceField = new JTextField(5);
        panel.add(fertPriceField);

        return panel;
    }

    private JPanel createToolPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.setBackground(new Color(204, 255, 204));
        panel.add(new JLabel("Name:"));
        toolNameField = new JTextField(10);
        panel.add(toolNameField);

        panel.add(new JLabel("Type:"));
        toolTypeField = new JTextField(10);
        panel.add(toolTypeField);

        panel.add(new JLabel("Quantity:"));
        toolQuantityField = new JTextField(5);
        panel.add(toolQuantityField);

        panel.add(new JLabel("Price:"));
        toolPriceField = new JTextField(5);
        panel.add(toolPriceField);

        return panel;
    }

    private void addItem(int selectedTabIndex) {
        String category = getCategory(selectedTabIndex);
        JTextField nameField = getNameField(selectedTabIndex);
        JTextField typeField = getTypeField(selectedTabIndex);
        JTextField quantityField = getQuantityField(selectedTabIndex);
        JTextField priceField = getPriceField(selectedTabIndex);

        String name = nameField.getText();
        String type = typeField.getText();
        int quantity;
        double price;

        try {
            quantity = Integer.parseInt(quantityField.getText());
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity and Price must be numbers.");
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(category + "," + name + "," + type + "," + quantity + "," + price);
            JOptionPane.showMessageDialog(this, "Item added successfully.");
            clearFields(selectedTabIndex);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file.");
        }
    }

    private void updateItem(int selectedTabIndex) {
        String category = getCategory(selectedTabIndex);
        JTextField nameField = getNameField(selectedTabIndex);
        JTextField typeField = getTypeField(selectedTabIndex);
        JTextField quantityField = getQuantityField(selectedTabIndex);
        JTextField priceField = getPriceField(selectedTabIndex);

        String name = nameField.getText();
        String type = typeField.getText();
        int quantity;
        double price;

        try {
            quantity = Integer.parseInt(quantityField.getText());
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity and Price must be numbers.");
            return;
        }

        List<String> lines = new ArrayList<>();
        boolean itemFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(category) && parts[1].equals(name) && parts[2].equals(type)) {
                    lines.add(category + "," + name + "," + type + "," + quantity + "," + price);
                    itemFound = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading from file.");
            return;
        }

        if (itemFound) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
                for (String line : lines) {
                    writer.println(line);
                }
                JOptionPane.showMessageDialog(this, "Item updated successfully.");
                clearFields(selectedTabIndex);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error writing to file.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Item not found.");
        }
    }

    private void removeItem(int selectedTabIndex) {
        String category = getCategory(selectedTabIndex);
        JTextField nameField = getNameField(selectedTabIndex);
        JTextField typeField = getTypeField(selectedTabIndex);

        String name = nameField.getText();
        String type = typeField.getText();

        List<String> lines = new ArrayList<>();
        boolean itemFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(category) && parts[1].equals(name) && parts[2].equals(type)) {
                    itemFound = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading from file.");
            return;
        }

        if (itemFound) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
                for (String line : lines) {
                    writer.println(line);
                }
                JOptionPane.showMessageDialog(this, "Item removed successfully.");
                clearFields(selectedTabIndex);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error writing to file.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Item not found.");
        }
    }

    private void viewInventory() {
        displayArea.setText("");
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                displayArea.append(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading from file.");
        }
    }

    private void clearFields(int selectedTabIndex) {
        switch (selectedTabIndex) {
            case 0:
                seedNameField.setText("");
                seedTypeField.setText("");
                seedQuantityField.setText("");
                seedPriceField.setText("");
                break;
            case 1:
                fertNameField.setText("");
                fertTypeField.setText("");
                fertQuantityField.setText("");
                fertPriceField.setText("");
                break;
            case 2:
                toolNameField.setText("");
                toolTypeField.setText("");
                toolQuantityField.setText("");
                toolPriceField.setText("");
                break;
        }
    }

    private String getCategory(int selectedTabIndex) {
        switch (selectedTabIndex) {
            case 0: return "Seeds";
            case 1: return "Fertilizers";
            case 2: return "Gardening Tools";
            default: return "";
        }
    }

    private JTextField getNameField(int selectedTabIndex) {
        switch (selectedTabIndex) {
            case 0: return seedNameField;
            case 1: return fertNameField;
            case 2: return toolNameField;
            default: return null;
        }
    }

    private JTextField getTypeField(int selectedTabIndex) {
        switch (selectedTabIndex) {
            case 0: return seedTypeField;
            case 1: return fertTypeField;
            case 2: return toolTypeField;
            default: return null;
        }
    }

    private JTextField getQuantityField(int selectedTabIndex) {
        switch (selectedTabIndex) {
            case 0: return seedQuantityField;
            case 1: return fertQuantityField;
            case 2: return toolQuantityField;
            default: return null;
        }
    }

    private JTextField getPriceField(int selectedTabIndex) {
        switch (selectedTabIndex) {
            case 0: return seedPriceField;
            case 1: return fertPriceField;
            case 2: return toolPriceField;
            default: return null;
        }
    }

    public static void main(String[] args) {
        new GardenInventorySystem();
    }
}


