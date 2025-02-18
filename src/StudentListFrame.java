import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
public class StudentListFrame extends JFrame {
    private JList<StudentInfo> studentList;
    private DefaultListModel<StudentInfo> listModel;
    private JTextField dsField, tpField, examenField;
    private JLabel nameLabel, emailLabel, classLabel;
    private Connecter connecter;
    private String subject;
    private String sessionType;
    private List<StudentInfo> students;
    
   

    // Modern soft pastel blue theme colors
    private final Color BACKGROUND_COLOR = new Color(230, 245, 255); // Light blue background
    private final Color HEADER_COLOR = new Color(60, 120, 180); // Darker calm blue for header
    private final Color ACCENT_COLOR = new Color(181, 201, 255); // Light blue accent
    private final Color INPUT_BORDER_COLOR = new Color(120, 170, 210); // Subtle blue border
    private final Color BUTTON_COLOR = new Color(80, 150, 210); // Soft blue for buttons
    private final Color BUTTON_HOVER_COLOR = new Color(100, 180, 240); // Lighter blue for hover
    private final Color TEXT_COLOR = new Color(45, 55, 72); // Dark text for contrast
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 16); // Modern font
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24); // Larger header font
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 16); // Bold button font
    private final int BORDER_RADIUS = 15; // Rounded corners for buttons
    // Custom barcode panel for exam mode
    private static class BarcodePanel extends JPanel {
        private final String studentId;
        private BufferedImage barcodeImage;

        public BarcodePanel(String studentId) {
            this.studentId = studentId;
            setPreferredSize(new Dimension(200, 100)); // Adjust size as needed
            setBackground(Color.WHITE);

            try {
                generateBarcode();
            } catch (IOException | WriterException e) {
                e.printStackTrace();
            }
        }

        private void generateBarcode() throws WriterException, IOException {
            Code128Writer barcodeWriter = new Code128Writer();
            BitMatrix bitMatrix = barcodeWriter.encode(studentId, BarcodeFormat.CODE_128, 300, 50);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", bos);
            barcodeImage = ImageIO.read(new ByteArrayInputStream(bos.toByteArray()));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (barcodeImage != null) {
                int x = (getWidth() - barcodeImage.getWidth()) / 2;
                int y = (getHeight() - barcodeImage.getHeight() - 20) / 2; // 20 is the height for the text
                g.drawImage(barcodeImage, x, y, this);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g.drawString(studentId, x, y + barcodeImage.getHeight() + 15);
            }
        }
    }

    // Custom cell renderer to handle both normal and exam modes
    private class StudentListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            StudentInfo student = (StudentInfo) value;

            if ("Examen".equals(sessionType)) {
                BarcodePanel barcodePanel = new BarcodePanel(String.valueOf(student.id));
                barcodePanel.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
                barcodePanel.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
                return barcodePanel;
            } else {
                return super.getListCellRendererComponent(list, student.toString(),
                        index, isSelected, cellHasFocus);
            }
        }
    }

    private static class StudentInfo {
        int id;
        String nom;
        String prenom;
        String email;
        String classe;
        double ds;
        double tp;
        double examen;

        public StudentInfo(int id, String nom, String prenom, String email, String classe,
                           double ds, double tp, double examen) {
            this.id = id;
            this.nom = nom;
            this.prenom = prenom;
            this.email = email;
            this.classe = classe;
            this.ds = ds;
            this.tp = tp;
            this.examen = examen;
        }

        @Override
        public String toString() {
            return nom + " " + prenom;
        }
    }

    public StudentListFrame(String field, String level, String sessionType, String subject) {
        this.subject = subject;
        this.sessionType = sessionType;
        setTitle("Gestion des Notes - " + field + " - " + level + " - " + subject + " (" + sessionType + ")");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        connecter = new Connecter();
        students = new ArrayList<>();

        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setBackground(BACKGROUND_COLOR);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        splitPane.setBackground(BACKGROUND_COLOR);

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        mainContainer.add(splitPane, BorderLayout.CENTER);

        add(mainContainer);

        studentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateFormWithSelectedStudent();
            }
        });

        String dbLevel = level.split("")[0];
        loadStudents(field, dbLevel, sessionType, subject);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Liste des Étudiants");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel searchLabel = new JLabel("Rechercher");
        searchLabel.setFont(MAIN_FONT);
        searchLabel.setForeground(TEXT_COLOR);

        JTextField searchField = new JTextField();
        searchField.setFont(MAIN_FONT);
        searchField.setPreferredSize(new Dimension(0, 35));
        searchField.setBorder(new CompoundBorder(
                new LineBorder(INPUT_BORDER_COLOR, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        if (!"Examen".equals(sessionType)) {
            searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
                public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }

                private void search() {
                    String searchTerm = searchField.getText().toLowerCase();
                    listModel.clear();
                    for (StudentInfo student : students) {
                        if ("Examen".equals(sessionType) ||
                                (student.nom + " " + student.prenom).toLowerCase().contains(searchTerm)) {
                            listModel.addElement(student);
                        }
                    }
                }
            });
        } else {
            searchField.setEnabled(false);
            searchField.setVisible(false);
        }

        searchPanel.add(searchLabel, BorderLayout.NORTH);
        searchPanel.add(searchField, BorderLayout.CENTER);

        listModel = new DefaultListModel<>();
        studentList = new JList<>(listModel);
        studentList.setFont(MAIN_FONT);
        studentList.setBackground(Color.WHITE);
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentList.setSelectionBackground(ACCENT_COLOR);
        studentList.setSelectionForeground(TEXT_COLOR);
        studentList.setCellRenderer(new StudentListCellRenderer());

        if ("Examen".equals(sessionType)) {
            studentList.setFixedCellHeight(60); // Accommodate barcode height
        }

        JScrollPane scrollPane = new JScrollPane(studentList);
        scrollPane.setBorder(new LineBorder(INPUT_BORDER_COLOR, 2, true));

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(BACKGROUND_COLOR);
        topSection.add(headerPanel, BorderLayout.NORTH);
        topSection.add(searchPanel, BorderLayout.CENTER);

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Student info section
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        infoPanel.setBackground(BACKGROUND_COLOR);
        infoPanel.setBorder(createModernTitledBorder1("Information de l'Étudiant"));

        nameLabel = createStyledLabel("Nom complet: ");
        emailLabel = createStyledLabel("Email: ");
        classLabel = createStyledLabel("Classe: ");

        if (!"Examen".equals(sessionType)) {
            infoPanel.add(nameLabel);
            infoPanel.add(emailLabel);
            infoPanel.add(classLabel);
        }

        // Grades section
        JPanel gradesPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        gradesPanel.setBackground(BACKGROUND_COLOR);
        gradesPanel.setBorder(createModernTitledBorder1("Notes"));

        // Initialize all fields
        dsField = createStyledTextField();
        tpField = createStyledTextField();
        examenField = createStyledTextField();

        // Add only the relevant field based on session type
        switch (sessionType) {
            case "DS":
                gradesPanel.add(createStyledLabel("DS:"));
                gradesPanel.add(dsField);
                tpField.setEnabled(false);
                examenField.setEnabled(false);
                break;
            case "TP":
                gradesPanel.add(createStyledLabel("TP:"));
                gradesPanel.add(tpField);
                dsField.setEnabled(false);
                examenField.setEnabled(false);
                break;
            case "Examen":
                gradesPanel.add(createStyledLabel("Examen:"));
                gradesPanel.add(examenField);
                dsField.setEnabled(false);
                tpField.setEnabled(false);
                break;
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton saveButton = createStyledButton1("Enregistrer les Notes");
        JButton printButton = createStyledButton1("Imprimer les Résultats");
        JButton logoutButton = createStyledButton1("Déconnecter");

        saveButton.addActionListener(e -> saveNotes());
        printButton.addActionListener(e -> printResults());
        logoutButton.addActionListener(e -> {
            dispose();
            Authentication authentication = new Authentication();
            authentication.frame.setVisible(true);
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(printButton);
        buttonPanel.add(logoutButton);

        panel.add(infoPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(gradesPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(buttonPanel);

        return panel;
    }
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(MAIN_FONT);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(MAIN_FONT);
        field.setBorder(new CompoundBorder(
                new LineBorder(INPUT_BORDER_COLOR, 2, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JButton createStyledButton1(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setBorder(new EmptyBorder(12, 25, 12, 25));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    private Border createModernTitledBorder1(String title) {
        JLabel titleLabel = new JLabel(" " + title + " ");
        titleLabel.setFont(MAIN_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        return BorderFactory.createCompoundBorder(
                new LineBorder(INPUT_BORDER_COLOR, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
    }

    private void updateFormWithSelectedStudent() {
        StudentInfo selectedStudent = studentList.getSelectedValue();
        if (selectedStudent != null) {
            if (!"Examen".equals(sessionType)) {
                nameLabel.setText("Nom complet: " + selectedStudent.nom + " " + selectedStudent.prenom);
                emailLabel.setText("Email: " + selectedStudent.email);
                classLabel.setText("Classe: " + selectedStudent.classe);
            }

            dsField.setText(String.valueOf(selectedStudent.ds));
            tpField.setText(String.valueOf(selectedStudent.tp));
            examenField.setText(String.valueOf(selectedStudent.examen));
        }
    }
    private int toInt(double value) {
        return (int) Math.round(value);
    }
    private void drawHeader(Graphics2D g2d, PageFormat pf, String title) {
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        Font dateFont = new Font("Arial", Font.PLAIN, 12);
        g2d.setFont(titleFont);
        FontMetrics titleMetrics = g2d.getFontMetrics();
        int titleWidth = titleMetrics.stringWidth(title);
        int titleHeight = titleMetrics.getHeight();
        double marginX = pf.getImageableX();
        double marginY = pf.getImageableY();

        g2d.drawString(title, toInt(marginX + (pf.getImageableWidth() - titleWidth) / 2), toInt(marginY + titleHeight));

        g2d.setFont(dateFont);
        FontMetrics dateMetrics = g2d.getFontMetrics();
        String dateStr = "Date: " + java.time.LocalDate.now();
        int dateWidth = dateMetrics.stringWidth(dateStr);
        g2d.drawString(dateStr, toInt(marginX + (pf.getImageableWidth() - dateWidth) / 2), toInt(marginY + titleHeight + 20));
    }
    private void drawFooter(Graphics2D g2d, PageFormat pf, int currentPage, int totalPages) {
        Font footerFont = new Font("Arial", Font.PLAIN, 10);
        g2d.setFont(footerFont);
        FontMetrics footerMetrics = g2d.getFontMetrics();
        String footerText = "Page " + currentPage + " of " + totalPages;
        int footerWidth = footerMetrics.stringWidth(footerText);
        double marginX = pf.getImageableX();
        double marginY = pf.getImageableY();
        double imageableWidth = pf.getImageableWidth();
        double imageableHeight = pf.getImageableHeight();

        int x = toInt(marginX + imageableWidth - footerWidth - 10);
        int y = toInt(marginY + imageableHeight - 10);

        g2d.drawString(footerText, x, y);
    }
    private void printResults() {
        Printable printable;

        switch (sessionType) {
            case "Examen":
                printable = new ExamPrintable();
                break;
            case "DS":
                printable = new DSPrintable();
                break;
            case "TP":
                printable = new TPrintable();
                break;
            default:
                printable = new DefaultPrintable();
                break;
        }

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(printable);

        if (job.printDialog()) {
            try {
                job.print();
                showSuccess("Impression réussie!");
            } catch (PrinterException e) {
                showError("Erreur lors de l'impression: " + e.getMessage());
            }
        }
    }
    private class ExamPrintable implements Printable {
        private Font font = new Font("Arial", Font.PLAIN, 12);

        public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(font);
            FontMetrics fontMetrics = g2d.getFontMetrics();

            int marginX = (int) pf.getImageableX();
            int marginY = (int) pf.getImageableY();

            String title = "Liste des Notes - " + subject;
            drawHeader(g2d, pf, title);

            int headerHeight = 50; // Adjust based on header content
            int contentY = marginY + headerHeight;

            int barcodeWidth = 200;
            int barcodeHeight = 50;
            int verticalSpacing = 60;

            int barcodesPerPage = (int) ((pf.getImageableHeight() - headerHeight - 50) / verticalSpacing);

            int startIndex = pageIndex * barcodesPerPage;
            int endIndex = Math.min(startIndex + barcodesPerPage, students.size());

            if (startIndex >= students.size()) {
                return Printable.NO_SUCH_PAGE;
            }

            int currentPage = pageIndex + 1;
            int totalPages = (int) Math.ceil((double) students.size() / barcodesPerPage);

            drawFooter(g2d, pf, currentPage, totalPages);

            double y = contentY;

            for (int i = startIndex; i < endIndex; i++) {
                StudentInfo student = students.get(i);

                try {
                    BufferedImage barcodeImage = generateBarcodeImage(String.valueOf(student.id), barcodeWidth, barcodeHeight);
                    g2d.drawImage(barcodeImage, (int) (marginX + 10), (int) y, barcodeWidth, barcodeHeight, null);
                    g2d.setFont(new Font("Arial", Font.PLAIN, 14));
                    g2d.drawString("Examen: " + String.valueOf(student.examen), (int) (marginX + barcodeWidth + 20), (int) (y + barcodeHeight / 2));
                } catch (IOException | WriterException e) {
                    e.printStackTrace();
                }

                y += verticalSpacing;
            }

            return Printable.PAGE_EXISTS;
        }
    }
    private class DSPrintable implements Printable {
        private Font font = new Font("Arial", Font.PLAIN, 12);

        public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(font);
            FontMetrics fontMetrics = g2d.getFontMetrics();

            int marginX = (int) pf.getImageableX();
            int marginY = (int) pf.getImageableY();

            String title = "Liste des Notes - " + subject;
            drawHeader(g2d, pf, title);

            int headerHeight = 50; // Adjust based on header content
            int contentY = marginY + headerHeight;

            int lineHeight = fontMetrics.getHeight();
            int rowHeight = lineHeight * 2; // Two lines per student (name and email, grade)

            int columns = 3;
            int columnWidth = (int) (pf.getImageableWidth() / columns);

            int studentsPerPage = (int) ((pf.getImageableHeight() - headerHeight - 50) / rowHeight);

            int startIndex = pageIndex * studentsPerPage;
            int endIndex = Math.min(startIndex + studentsPerPage, students.size());

            if (startIndex >= students.size()) {
                return Printable.NO_SUCH_PAGE;
            }

            int currentPage = pageIndex + 1;
            int totalPages = (int) Math.ceil((double) students.size() / studentsPerPage);

            drawFooter(g2d, pf, currentPage, totalPages);

            // Draw table headers
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("Nom", marginX + 10, contentY + lineHeight);
            g2d.drawString("Email", marginX + columnWidth + 10, contentY + lineHeight);
            g2d.drawString("DS", marginX + 2 * columnWidth + 10, contentY + lineHeight);

            contentY += rowHeight;

            for (int i = startIndex; i < endIndex; i++) {
                StudentInfo student = students.get(i);

                g2d.setFont(font);
                g2d.drawString(student.nom + " " + student.prenom, marginX + 10, contentY + lineHeight);
                g2d.drawString(student.email, marginX + columnWidth + 10, contentY + lineHeight);
                g2d.drawString(String.valueOf(student.ds), marginX + 2 * columnWidth + 10, contentY + lineHeight);

                contentY += rowHeight;
                g2d.drawLine(marginX + 10, contentY, (int) (marginX + pf.getImageableWidth() - 10), contentY);
            }

            // Draw table borders
            g2d.setColor(Color.BLACK);
            g2d.drawRect(marginX + 10, contentY - rowHeight * studentsPerPage, (int) (pf.getImageableWidth() - 20), rowHeight * studentsPerPage + rowHeight);

            return Printable.PAGE_EXISTS;
        }
    }
    private class TPrintable implements Printable {
        private Font font = new Font("Arial", Font.PLAIN, 12);

        public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(font);
            FontMetrics fontMetrics = g2d.getFontMetrics();

            double marginX = pf.getImageableX();
            double marginY = pf.getImageableY();

            String title = "Liste des Notes - " + subject;
            drawHeader(g2d, pf, title);

            int headerHeight = 50; // Adjust based on header content
            int contentY = toInt(marginY + headerHeight);

            int lineHeight = fontMetrics.getHeight();
            int rowHeight = lineHeight * 2; // Two lines per student (name and email, grade)

            int columns = 3;
            int columnWidth = (int) (pf.getImageableWidth() / columns);

            int studentsPerPage = (int) ((pf.getImageableHeight() - headerHeight - 50) / rowHeight);

            int startIndex = pageIndex * studentsPerPage;
            int endIndex = Math.min(startIndex + studentsPerPage, students.size());

            if (startIndex >= students.size()) {
                return Printable.NO_SUCH_PAGE;
            }

            int currentPage = pageIndex + 1;
            int totalPages = (int) Math.ceil((double) students.size() / studentsPerPage);

            drawFooter(g2d, pf, currentPage, totalPages);

            // Draw table headers
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("Nom", toInt(marginX + 10), contentY + lineHeight);
            g2d.drawString("Email", toInt(marginX + columnWidth + 10), contentY + lineHeight);
            g2d.drawString("TP", toInt(marginX + 2 * columnWidth + 10), contentY + lineHeight);

            contentY += rowHeight;

            for (int i = startIndex; i < endIndex; i++) {
                StudentInfo student = students.get(i);

                g2d.setFont(font);
                g2d.drawString(student.nom + " " + student.prenom, toInt(marginX + 10), contentY + lineHeight);
                g2d.drawString(student.email, toInt(marginX + columnWidth + 10), contentY + lineHeight);
                g2d.drawString(String.valueOf(student.tp), toInt(marginX + 2 * columnWidth + 10), contentY + lineHeight);

                contentY += rowHeight;
                g2d.drawLine(toInt(marginX + 10), contentY, toInt(marginX + pf.getImageableWidth() - 10), contentY);
            }

            // Draw table borders
            g2d.setColor(Color.BLACK);
            g2d.drawRect(toInt(marginX + 10), contentY - rowHeight * studentsPerPage, (int) (pf.getImageableWidth() - 20), rowHeight * studentsPerPage + rowHeight);

            return Printable.PAGE_EXISTS;
        }

        private int toInt(double value) {
            return (int) Math.round(value);
        }
    }
    private class DefaultPrintable implements Printable {
        public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
            // Default printing logic if sessionType is not recognized
            return Printable.NO_SUCH_PAGE;
        }
    }

    private BufferedImage generateBarcodeImage1(String barcodeData, int width, int height) throws WriterException, IOException {
        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeData, BarcodeFormat.CODE_128, width, height);

        BufferedImage barcodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                barcodeImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        return barcodeImage;
    }

    private BufferedImage generateBarcodeImage(String barcodeData, int width, int height) throws WriterException, IOException {
        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeData, BarcodeFormat.CODE_128, width, height);

        BufferedImage barcodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                barcodeImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        return barcodeImage;
    }
    private String generateBarcode(int studentId) {
        // Simple textual barcode representation
        String barcode = "";
        for (int i = 0; i < String.valueOf(studentId).length(); i++) {
            barcode += "*";
        }
        return barcode;
    }

	// Helper method for success messages
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Helper method for error messages
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
    }

    private JLabel createStyledLabel1(String text) {
        JLabel label = new JLabel(text);
        label.setFont(MAIN_FONT);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JTextField createStyledTextField1() {
        JTextField field = new JTextField();
        field.setFont(MAIN_FONT);
        field.setBorder(new CompoundBorder(
                new LineBorder(INPUT_BORDER_COLOR, 2, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setBorder(new EmptyBorder(12, 25, 12, 25));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    private Border createModernTitledBorder(String title) {
        JLabel titleLabel = new JLabel(" " + title + " ");
        titleLabel.setFont(MAIN_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        return BorderFactory.createCompoundBorder(
                new LineBorder(INPUT_BORDER_COLOR, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
    }

    private void loadStudents(String field, String level, String sessionType, String subject) {
        Connection conn = connecter.obtenirconnexion();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Impossible de se connecter à la base de données!");
            return;
        }

        try {
            String query =
                    "SELECT DISTINCT e.id_etud, e.nom, e.prenom, e.date_naissance, e.email, " +
                            "c.nom_classe, m.nom_matiere, " +
                            "COALESCE(n.DS, 0) as DS, COALESCE(n.TP, 0) as TP, COALESCE(n.Examen, 0) as Examen " +
                            "FROM etudiant e " +
                            "INNER JOIN classe c ON e.id_classe = c.id_classe " +
                            "INNER JOIN etudier et ON e.id_etud = et.id_etud " +
                            "INNER JOIN matiere m ON et.id_matiere = m.id_matiere " +
                            "LEFT JOIN typedesession n ON e.id_etud = n.id_etud AND m.id_matiere = n.id_matiere " +
                            "WHERE c.niveau = ? AND m.nom_matiere = ? " +
                            "AND c.section LIKE ? " +
                            "ORDER BY e.nom, e.prenom";  // Added ordering

            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, level);
                statement.setString(2, subject.toLowerCase());
                statement.setString(3, "%" + field + "%");

                try (ResultSet resultSet = statement.executeQuery()) {
                    students.clear();
                    listModel.clear();

                    while (resultSet.next()) {
                        StudentInfo student = new StudentInfo(
                                resultSet.getInt("id_etud"),
                                resultSet.getString("nom"),
                                resultSet.getString("prenom"),
                                resultSet.getString("email"),
                                resultSet.getString("nom_classe"),
                                resultSet.getDouble("DS"),
                                resultSet.getDouble("TP"),
                                resultSet.getDouble("Examen")
                        );
                        students.add(student);
                        listModel.addElement(student);
                    }

                    if (students.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Aucun étudiant trouvé pour ces critères:\n" +
                                        "Section: " + field + "\n" +
                                        "Niveau: " + level + "\n" +
                                        "Matière: " + subject,
                                "Aucun Résultat",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        studentList.setSelectedIndex(0);
                        updateFormWithSelectedStudent();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des étudiants:\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFormWithSelectedStudent1() {
        StudentInfo selectedStudent = studentList.getSelectedValue();
        if (selectedStudent != null) {
            if (!"Examen".equals(sessionType)) {
                nameLabel.setText("Nom complet: " + selectedStudent.nom + " " + selectedStudent.prenom);
            }
            emailLabel.setText("Email: " + selectedStudent.email);
            classLabel.setText("Classe: " + selectedStudent.classe);

            dsField.setText(String.valueOf(selectedStudent.ds));
            tpField.setText(String.valueOf(selectedStudent.tp));
            examenField.setText(String.valueOf(selectedStudent.examen));
        }
    }

    private void saveNotes() {
        StudentInfo selectedStudent = studentList.getSelectedValue();
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant!");
            return;
        }

        Connection conn = connecter.obtenirconnexion();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Impossible de se connecter à la base de données!");
            return;
        }

        try {
            conn.setAutoCommit(false);

            String query =
                    "INSERT INTO typedesession (id_matiere, id_etud, DS, TP, Examen) " +
                            "VALUES ((SELECT id_matiere FROM matiere WHERE nom_matiere = ?), ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE DS = VALUES(DS), TP = VALUES(TP), Examen = VALUES(Examen)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                double ds = 0, tp = 0, examen = 0;

                // Validate only the relevant grade based on session type
                switch (sessionType) {
                    case "DS":
                        ds = validateGrade(dsField.getText(), "DS");
                        tp = selectedStudent.tp;  // Keep existing values
                        examen = selectedStudent.examen;
                        break;
                    case "TP":
                        ds = selectedStudent.ds;  // Keep existing values
                        tp = validateGrade(tpField.getText(), "TP");
                        examen = selectedStudent.examen;
                        break;
                    case "Examen":
                        ds = selectedStudent.ds;  // Keep existing values
                        tp = selectedStudent.tp;
                        examen = validateGrade(examenField.getText(), "Examen");
                        break;
                }

                preparedStatement.setString(1, subject.toLowerCase());
                preparedStatement.setInt(2, selectedStudent.id);
                preparedStatement.setDouble(3, ds);
                preparedStatement.setDouble(4, tp);
                preparedStatement.setDouble(5, examen);

                int result = preparedStatement.executeUpdate();

                if (result > 0) {
                    conn.commit();
                    // Update the student object with new grades
                    selectedStudent.ds = ds;
                    selectedStudent.tp = tp;
                    selectedStudent.examen = examen;
                    JOptionPane.showMessageDialog(this, "Notes enregistrées avec succès!");
                }
            } catch (IllegalArgumentException e) {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                        "Erreur de validation: " + e.getMessage(),
                        "Erreur de Validation",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement des notes:\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private double validateGrade(String gradeStr, String fieldName) {
        try {
            double grade = Double.parseDouble(gradeStr);
            if (grade < 0 || grade > 20) {
                throw new IllegalArgumentException(fieldName + " doit être entre 0 et 20");
            }
            return grade;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " doit être un nombre valide");
        }
    }
}