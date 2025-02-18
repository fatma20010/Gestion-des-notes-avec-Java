
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BienvenueSelect {
    JFrame frame;
    private JComboBox<String> fieldComboBox;
    private JComboBox<String> levelComboBox;
    private JComboBox<String> sessionTypeComboBox;
    private JComboBox<String> subjectComboBox;

    // Soft Blue Pastel Theme Colors
    private Color mainBgColor = new Color(230, 245, 255); // Light blue background
    private Color headerBgColor = new Color(60, 120, 180); // Darker calm blue for header
    private Color headerTextColor = Color.WHITE; // White text for header
    private Color buttonColor = new Color(80, 150, 210); // Soft blue for buttons
    private Color buttonHoverColor = new Color(100, 180, 240); // Lighter blue for hover
    private Color inputBgColor = new Color(245, 250, 255); // Lightest blue for inputs
    private Color inputBorderColor = new Color(120, 170, 210); // Subtle blue border

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                BienvenueSelect window = new BienvenueSelect();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public BienvenueSelect() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Veuillez remplir les informations");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(mainBgColor);
        frame.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(headerBgColor);
        headerPanel.setBorder(new EmptyBorder(40, 0, 40, 0)); // Increased padding
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Bienvenue, veuillez remplir les informations suivantes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Modern font
        titleLabel.setForeground(headerTextColor);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(mainBgColor);
        mainPanel.setBorder(new EmptyBorder(50, 100, 50, 100)); // Increased padding
        mainPanel.setLayout(new GridLayout(5, 2, 30, 30)); // Increased spacing
        frame.add(mainPanel, BorderLayout.CENTER);

        // Labels and ComboBoxes
        addLabeledComboBox(mainPanel, "Section :", new String[]{
            "Génie Mathématique",
            "Génie Informatique",
            "Génie Industriel",
            "Génie Électrique",
            "Génie Civil",
            "Génie Mécanique"
        }, combo -> fieldComboBox = combo);

        addLabeledComboBox(mainPanel, "Niveau :", new String[]{"1ére", "2ème", "3ème", "Master"}, combo -> levelComboBox = combo);
        addLabeledComboBox(mainPanel, "Type de session :", new String[]{"DS", "Examen", "TP"}, combo -> sessionTypeComboBox = combo);
        addLabeledComboBox(mainPanel, "Matière :", new String[]{"Probabilité", "Java", "Stat","AI"}, combo -> subjectComboBox = combo);

        // Submit Button
        JButton submitButton = new JButton("Valider") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isArmed()) {
                    g2.setColor(buttonHoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(buttonHoverColor);
                } else {
                    g2.setColor(buttonColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded corners
                g2.setColor(inputBorderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30); // Rounded border
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Modern font
        submitButton.setForeground(Color.WHITE);
        submitButton.setContentAreaFilled(false); // Disable default background
        submitButton.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Increased padding
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitButton.setFocusPainted(false);
        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.repaint();
            }
        });
        submitButton.addActionListener(e -> handleSubmission());

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(mainBgColor);
        footerPanel.add(submitButton);
        frame.add(footerPanel, BorderLayout.SOUTH);
    }

    private void addLabeledComboBox(JPanel panel, String labelText, String[] comboItems, ComboBoxHandler handler) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 24)); // Modern font
        label.setForeground(headerBgColor);
        panel.add(label);

        JComboBox<String> comboBox = new JComboBox<>(comboItems);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 22)); // Modern font
        comboBox.setBackground(inputBgColor);
        comboBox.setForeground(headerBgColor.darker());
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(inputBorderColor, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15) // Increased padding
        ));
        panel.add(comboBox);
        handler.handle(comboBox);
    }

    private void handleSubmission() {
        String selectedField = (String) fieldComboBox.getSelectedItem();
        String selectedLevel = (String) levelComboBox.getSelectedItem();
        String selectedSessionType = (String) sessionTypeComboBox.getSelectedItem();
        String selectedSubject = (String) subjectComboBox.getSelectedItem();

        int result = JOptionPane.showConfirmDialog(frame, String.format(
            "Informations sélectionnées :\nFilière : %s\nNiveau : %s\nType de session : %s\nMatière : %s",
            selectedField, selectedLevel, selectedSessionType, selectedSubject
        ), "Confirmation", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            SwingUtilities.invokeLater(() -> {
                new StudentListFrame(selectedField, selectedLevel, selectedSessionType, selectedSubject).setVisible(true);
            });
            frame.dispose();
        }
    }

    @FunctionalInterface
    private interface ComboBoxHandler {
        void handle(JComboBox<String> comboBox);
    }
}