import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
    public class AdminInterface extends JFrame {
        private JPanel contentPane;
        private Font headerFont = new Font("Monospaced", Font.BOLD, 40);
        private Font subheaderFont = new Font("Monospaced", Font.BOLD, 24);
        private Font bodyFont = new Font("Monospaced", Font.PLAIN, 16);
        private Color mainBgColor = new Color(230, 245, 255);
        private Color headerBgColor = new Color(60, 120, 180);
        private Color buttonColor = new Color(80, 150, 210);
        private Color buttonHoverColor = new Color(100, 180, 240);
        private Color inputBgColor = new Color(245, 250, 255);
        private Color inputBorderColor = new Color(120, 170, 210);
        private Connection conn;
        public AdminInterface() {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(100, 100, 900, 700); // This line can be removed if you want full-screen only
            contentPane = new JPanel();
            contentPane.setBackground(mainBgColor);
            contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPane.setLayout(new BorderLayout(0, 0));
            setContentPane(contentPane);

            makeFullScreen(); // Call the method to make the window full-screen

            connectToDatabase();

            JPanel headerPanel = new JPanel();
            headerPanel.setBackground(headerBgColor);
            JLabel lblNewLabel = new JLabel("Interface Administrateur", SwingConstants.CENTER);
            lblNewLabel.setFont(headerFont);
            lblNewLabel.setForeground(Color.WHITE);
            headerPanel.add(lblNewLabel);
            contentPane.add(headerPanel, BorderLayout.NORTH);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(subheaderFont);
            tabbedPane.addTab("Ajouter un enseignant", createAddPanel("enseignant"));
            tabbedPane.addTab("Ajouter un étudiant", createAddPanel("étudiant"));
            tabbedPane.addTab("Ajouter une classe", createAddPanel("classe"));
            tabbedPane.addTab("Ajouter une matière", createAddPanel("matière"));
            tabbedPane.addTab("Affecter les enseignants", createAssignTeacherPanel());

            contentPane.add(tabbedPane, BorderLayout.CENTER);
            setLocationRelativeTo(null);
        }

        private void makeFullScreen() {
            setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        }

       

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/mydatabase1", "root", "root");
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Connection Failed.");
        }
    }

    private JPanel createAddPanel(String type) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 20, 20));
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));
        panel.setBackground(mainBgColor);

        JLabel titleLabel = new JLabel("Ajouter " + type, SwingConstants.CENTER);
        titleLabel.setFont(subheaderFont);
        titleLabel.setForeground(headerBgColor);
        panel.add(titleLabel);
        panel.add(new JLabel(""));

        JComponent[] fields = new JComponent[5];
        String[] labels = new String[5];

        switch (type) {
            case "enseignant":
                labels = new String[]{"Nom", "Prénom", "Grade", "Specialité", "Email"};
                fields[2] = new JComboBox<>(new String[]{"Doctorat", "Professeur Assistant", "Maître de Conférences", "Professeur"});
                break;
            case "étudiant":
                labels = new String[]{"Nom", "Prénom", "Email", "ID Classe", "Date de naissance"};
                fields[3] = new JComboBox<>(new String[]{"1Gmam", "1Ginfo", "1Gelec", "1Gindus", "1Gcivil", "2Gmam", "2Ginfo", "2Gelec", "2Gindus", "2Gcivil", "3Gmam", "3Ginfo", "3Gelec", "3Gindus", "3Gcivil", "Autre"});
                break;
            case "classe":
                labels = new String[]{"ID Classe", "Nom de la classe", "Niveau", "Section"};
                break;
            case "matière":
                labels = new String[]{"Nom de la matière", "Description"};
                break;
        }

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(bodyFont);
            label.setForeground(headerBgColor);
            panel.add(label);

            if (fields[i] == null) {
                fields[i] = new JTextField();
            }
            fields[i].setBackground(inputBgColor);
            fields[i].setForeground(headerBgColor.darker());
            fields[i].setFont(bodyFont);
            ((JComponent) fields[i]).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(inputBorderColor, 2),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            panel.add(fields[i]);
        }

        JButton addButton = new JButton("Ajouter");
        addButton.setFont(subheaderFont);
        addButton.setBackground(buttonColor);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setBackground(buttonHoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setBackground(buttonColor);
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addToDatabase(type, fields);
            }
        });
        panel.add(new JLabel(""));
        panel.add(addButton);

        return panel;
    }

    private JPanel createAssignTeacherPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));
        panel.setBackground(mainBgColor);

        JLabel titleLabel = new JLabel("Affecter les enseignants aux classes", SwingConstants.CENTER);
        titleLabel.setFont(subheaderFont);
        titleLabel.setForeground(headerBgColor);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        centerPanel.setBackground(mainBgColor);

        JLabel teacherLabel = new JLabel("Enseignant :");
        teacherLabel.setFont(bodyFont);
        teacherLabel.setForeground(headerBgColor);
        JComboBox<String> teacherComboBox = new JComboBox<>();
        teacherComboBox.setFont(bodyFont);
        loadTeachers(teacherComboBox);

        JLabel classLabel = new JLabel("Classe :");
        classLabel.setFont(bodyFont);
        classLabel.setForeground(headerBgColor);
        JComboBox<String> classComboBox = new JComboBox<>();
        classComboBox.setFont(bodyFont);
        loadClasses(classComboBox);

        centerPanel.add(teacherLabel);
        centerPanel.add(teacherComboBox);
        centerPanel.add(classLabel);
        centerPanel.add(classComboBox);

        panel.add(centerPanel, BorderLayout.CENTER);

        JButton assignButton = new JButton("Affecter");
        assignButton.setFont(subheaderFont);
        assignButton.setBackground(buttonColor);
        assignButton.setForeground(Color.WHITE);
        assignButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTeacher = (String) teacherComboBox.getSelectedItem();
                String selectedClass = (String) classComboBox.getSelectedItem();

                int teacherId = getTeacherId(selectedTeacher);
                String classId = getClassId(selectedClass);

                if (teacherId > 0 && classId != null) {
                    assignTeacherToClass(teacherId, classId);
                    JOptionPane.showMessageDialog(panel, "Enseignant affecté avec succès!");
                } else {
                    JOptionPane.showMessageDialog(panel, "Erreur lors de l'affectation.");
                }
            }
        });

        panel.add(assignButton, BorderLayout.SOUTH);
        return panel;
    }

    private void loadTeachers(JComboBox<String> comboBox) {
        try {
            String query = "SELECT id_ens, nom_ens, prenom_ens FROM enseignant";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboBox.addItem(rs.getString("nom_ens") + " " + rs.getString("prenom_ens"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadClasses(JComboBox<String> comboBox) {
        try {
            String query = "SELECT id_classe, nom_classe, niveau, section FROM classe";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboBox.addItem(rs.getString("nom_classe") + " - " + rs.getString("niveau") + " - " + rs.getString("section"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int getTeacherId(String teacherName) {
        try {
            String query = "SELECT id_ens FROM enseignant WHERE CONCAT(nom_ens, ' ', prenom_ens) = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, teacherName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_ens");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    private String getClassId(String className) {
        try {
            String query = "SELECT id_classe FROM classe WHERE CONCAT(nom_classe, ' - ', niveau, ' - ', section) = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, className);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("id_classe");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void assignTeacherToClass(int teacherId, String classId) {
        try {
            // Check if the teacher is already assigned to the class
            String checkQuery = "SELECT COUNT(*) FROM dispense WHERE id_ens = ? AND id_classe = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, teacherId);
            checkStmt.setString(2, classId);

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                JOptionPane.showMessageDialog(null, "L'enseignant est déjà affecté à cette classe.");
            } else {
                // Proceed with the assignment
                String insertQuery = "INSERT INTO dispense (id_ens, id_classe) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, teacherId);
                insertStmt.setString(2, classId);
                insertStmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Enseignant affecté avec succès!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de l'affectation.");
        }
    }

    private void addToDatabase(String type, JComponent[] fields) {
        try {
            String query = "";
            int fieldCount = 0; // Number of fields to process

            switch (type) {
                case "enseignant":
                    query = "INSERT INTO enseignant (nom_ens, prenom_ens, grade, specialite, email) VALUES (?, ?, ?, ?, ?)";
                    fieldCount = 5;
                    break;
                case "étudiant":
                    query = "INSERT INTO etudiant (nom, prenom, email, id_classe, date_naissance) VALUES (?, ?, ?, ?, ?)";
                    fieldCount = 5;
                    break;
                case "classe":
                    query = "INSERT INTO classe (id_classe, nom_classe, niveau, section) VALUES (?, ?, ?, ?)";
                    fieldCount = 4;
                    break;
                case "matière":
                    query = "INSERT INTO matiere (nom_matiere, description) VALUES (?, ?)";
                    fieldCount = 2;
                    break;
            }

            PreparedStatement pst = conn.prepareStatement(query);
            for (int i = 0; i < fieldCount; i++) { // Only process the required number of fields
                if (fields[i] instanceof JTextField) {
                    pst.setString(i + 1, ((JTextField) fields[i]).getText());
                } else if (fields[i] instanceof JComboBox) {
                    pst.setString(i + 1, (String) ((JComboBox<?>) fields[i]).getSelectedItem());
                }
            }
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, type + " ajouté avec succès.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de " + type);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AdminInterface frame = new AdminInterface();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}