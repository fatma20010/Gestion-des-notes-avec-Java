import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Authentication {

    JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JPanel keyboardPanel;
    private Connecter connecter;
    // Soft Blue Pastel Theme Colors
    private Color mainBgColor = new Color(230, 245, 255); // Light blue background
    private Color headerBgColor = new Color(60, 120, 180); // Darker calm blue for header
    private Color inputBgColor = new Color(245, 250, 255); // Lightest blue for text fields
    private Color buttonColor = new Color(80, 150, 210); // Soft blue for buttons
    private Color buttonHoverColor = new Color(100, 180, 240); // Lighter blue for hover
    private Color borderColor = new Color(120, 170, 210); // Subtle blue border

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Authentication window = new Authentication();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public Authentication() {
        connecter = new Connecter();
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Page d'Authentification");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(mainBgColor);
        frame.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(headerBgColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0)); // Increased padding
        JLabel titleLabel = new JLabel("Bienvenue sur la page d'authentification", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36)); // Larger modern font
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(mainBgColor);
        mainPanel.setLayout(new GridBagLayout());
        frame.add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 25, 25, 25); // Increased spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Role Selection
        JLabel roleLabel = new JLabel("Sélectionnez le rôle :");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24)); // Larger modern font
        roleLabel.setForeground(headerBgColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(roleLabel, gbc);

        roleComboBox = new JComboBox<>(new String[]{"Administrateur", "Enseignant"});
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 22)); // Larger modern font
        roleComboBox.setBackground(inputBgColor);
        roleComboBox.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        gbc.gridx = 1;
        mainPanel.add(roleComboBox, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Nom d'utilisateur :");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24)); // Larger modern font
        usernameLabel.setForeground(headerBgColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(usernameLabel, gbc);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 22)); // Larger modern font
        usernameField.setBackground(inputBgColor);
        usernameField.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24)); // Larger modern font
        passwordLabel.setForeground(headerBgColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 22)); // Larger modern font
        passwordField.setBackground(inputBgColor);
        passwordField.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Se connecter") {
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
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30); // Rounded border
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Larger modern font
        loginButton.setForeground(Color.WHITE);
        loginButton.setContentAreaFilled(false); // Disable default background
        loginButton.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Increased padding
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setFocusPainted(false);
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.repaint();
            }
        });
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);

        // Virtual Keyboard
        keyboardPanel = new JPanel();
        keyboardPanel.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        keyboardPanel.setBackground(inputBgColor);
        keyboardPanel.setLayout(new GridLayout(4, 3, 15, 15)); // Increased spacing
        gbc.gridy = 4;
        mainPanel.add(keyboardPanel, gbc);

        populateKeyboard();
    }

    /**
     * Populate the virtual keyboard with shuffled buttons.
     */
    private void populateKeyboard() {
        keyboardPanel.removeAll();
        ArrayList<String> keys = new ArrayList<>();

        // Add numbers 0-9 and backspace button
        for (int i = 0; i <= 9; i++) {
            keys.add(String.valueOf(i));
        }
        keys.add("Retour");
        keys.add("Effacer");

        // Shuffle keys to make the keyboard dynamic
        Collections.shuffle(keys);

        for (String key : keys) {
            JButton button = new JButton(key) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isArmed()) {
                        g2.setColor(buttonHoverColor.darker());
                    } else if (getModel().isRollover()) {
                        g2.setColor(buttonHoverColor);
                    } else {
                        g2.setColor(Color.WHITE);
                    }
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Rounded corners
                    g2.setColor(borderColor);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15); // Rounded border
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            button.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Larger modern font
            button.setForeground(headerBgColor);
            button.setContentAreaFilled(false); // Disable default background
            button.setFocusPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.addActionListener(e -> handleKeyboardInput(key));
            keyboardPanel.add(button);
        }

        keyboardPanel.revalidate();
        keyboardPanel.repaint();
    }

    /**
     * Handle input from the virtual keyboard.
     */
    private void handleKeyboardInput(String key) {
        String currentText = new String(passwordField.getPassword());
        if ("Retour".equals(key)) {
            if (!currentText.isEmpty()) {
                passwordField.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if ("Effacer".equals(key)) {
            passwordField.setText("");
        } else {
            passwordField.setText(currentText + key);
        }
    }

    /**
     * Handle the login action.
     */
    private void handleLogin() {
        String selectedRole = (String) roleComboBox.getSelectedItem();
        String enteredUsername = usernameField.getText();
        String enteredPassword = new String(passwordField.getPassword());

        // Convert the role to match database enum
        String dbRole = selectedRole.trim().toLowerCase();

        try {
            Connection conn = connecter.obtenirconnexion();
            String query = "SELECT * FROM utilisateurs WHERE username = ? AND password = ? AND role = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, enteredUsername);
            pstmt.setString(2, enteredPassword);
            pstmt.setString(3, dbRole);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Login successful
                if (dbRole.equals("administrateur")) {
                    JOptionPane.showMessageDialog(frame, "Bienvenue, Admin !");
                    frame.dispose();
                    AdminInterface.main(null);
                } else if (dbRole.equals("enseignant")) {
                    JOptionPane.showMessageDialog(frame, "Bienvenue, Enseignant !");
                    frame.dispose();
                    BienvenueSelect.main(null);
                }
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "Identifiants incorrects. Veuillez réessayer.", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                "Erreur de connexion à la base de données: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }

        // Shuffle keyboard after each login attempt
        populateKeyboard();
    }
}