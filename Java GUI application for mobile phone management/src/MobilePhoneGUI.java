import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MobilePhoneGUI extends JFrame {
    // Input fields as instance variables
    private JTextField nameField, modelField, priceField, storageField;
    private JComboBox<String> yearComboBox;
    private JRadioButton blackRadio, whiteRadio, blueRadio, redRadio;
    private JTable table;
    private DefaultTableModel tableModel;
    private final String FILE_NAME = "Mobile.txt";

    public MobilePhoneGUI() {
        try {
            // Set the system look and feel for better macOS compatibility
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the main frame
        setTitle("Mobile Phone Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add input fields with better styling
        inputPanel.add(createStyledLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(createStyledLabel("Model:"));
        modelField = new JTextField();
        inputPanel.add(modelField);

        inputPanel.add(createStyledLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        inputPanel.add(createStyledLabel("Storage:"));
        storageField = new JTextField();
        inputPanel.add(storageField);

        inputPanel.add(createStyledLabel("Year:"));
        String[] years = {"2020", "2021", "2022", "2023", "2024"};
        yearComboBox = new JComboBox<>(years);
        inputPanel.add(yearComboBox);

        inputPanel.add(createStyledLabel("Color:"));
        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        ButtonGroup colorGroup = new ButtonGroup();

        // Create radio buttons with better visibility
        blackRadio = createColorRadioButton("Black", Color.BLACK);
        whiteRadio = createColorRadioButton("White", Color.WHITE);
        blueRadio = createColorRadioButton("Blue", Color.BLUE);
        redRadio = createColorRadioButton("Red", Color.RED);

        colorGroup.add(blackRadio);
        colorGroup.add(whiteRadio);
        colorGroup.add(blueRadio);
        colorGroup.add(redRadio);

        colorPanel.add(blackRadio);
        colorPanel.add(whiteRadio);
        colorPanel.add(blueRadio);
        colorPanel.add(redRadio);

        // Set background for better visibility
        colorPanel.setBackground(new Color(255, 255, 255));
        inputPanel.add(colorPanel);

        // Create button panel with better styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton createButton = createStyledButton("Create", new Color(76, 175, 80));
        JButton clearFieldsButton = createStyledButton("Clear Fields", new Color(33, 150, 243));
        JButton clearFileButton = createStyledButton("Clear File", new Color(244, 67, 54));
        JButton clearAllButton = createStyledButton("Clear All", new Color(255, 152, 0));

        buttonPanel.add(createButton);
        buttonPanel.add(clearFieldsButton);
        buttonPanel.add(clearFileButton);
        buttonPanel.add(clearAllButton);

        // Create table with better styling
        String[] columnNames = {"Name", "Model", "Price", "Storage", "Year", "Color"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(8, 8, 9));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Button actions
        createButton.addActionListener(e -> createRecord());
        clearFieldsButton.addActionListener(e -> clearFields());
        clearFileButton.addActionListener(e -> clearFile());
        clearAllButton.addActionListener(e -> { clearFields(); clearFile(); });
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("San Francisco", Font.BOLD, 14));
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("San Francisco", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }

    private JRadioButton createColorRadioButton(String text, Color color) {
        JRadioButton radio = new JRadioButton(text);
        radio.setFont(new Font("San Francisco", Font.PLAIN, 13));
        radio.setOpaque(true);
        radio.setBackground(new Color(240, 240, 240)); // Light gray background
        radio.setForeground(color.darker());
        radio.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        return radio;
    }

    private void createRecord() {
        // Get values from fields
        String name = nameField.getText().trim();
        String model = modelField.getText().trim();
        String price = priceField.getText().trim();
        String storage = storageField.getText().trim();
        String year = (String) yearComboBox.getSelectedItem();
        String color = "";

        // Check which color is selected
        if (blackRadio.isSelected()) color = "Black";
        else if (whiteRadio.isSelected()) color = "White";
        else if (blueRadio.isSelected()) color = "Blue";
        else if (redRadio.isSelected()) color = "Red";

        // Validate fields
        if (name.isEmpty() || model.isEmpty() || price.isEmpty() || storage.isEmpty() || color.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create record string
        String record = name + "!" + model + "!" + price + "!" + storage + "!" + year + "!" + color;

        // Write to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(record);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add to table
        tableModel.addRow(new Object[]{name, model, price, storage, year, color});

        // Clear fields
        clearFields();
    }

    private void clearFields() {
        nameField.setText("");
        modelField.setText("");
        priceField.setText("");
        storageField.setText("");
        yearComboBox.setSelectedIndex(0);
        blackRadio.setSelected(false);
        whiteRadio.setSelected(false);
        blueRadio.setSelected(false);
        redRadio.setSelected(false);
    }

    private void clearFile() {
        try (PrintWriter writer = new PrintWriter(FILE_NAME)) {
            writer.print("");
            tableModel.setRowCount(0); // Clear the table
            JOptionPane.showMessageDialog(this, "File cleared successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MobilePhoneGUI manager = new MobilePhoneGUI();
            manager.setVisible(true);
            // Center the window on screen
            manager.setLocationRelativeTo(null);
        });
    }
}