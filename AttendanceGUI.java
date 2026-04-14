import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AttendanceGUI extends JFrame {

    private JTextField txtTotal;
    private JTextField txtAttended;
    private JLabel lblPercentage;
    private JLabel lblMarks;
    private JLabel lblZone;
    private JLabel lblCurrentInsight;
    private JLabel lblNextInsight;

    public AttendanceGUI() {
        setTitle("Attendance Optimization Engine - Pro UI");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Layer 1: Gradient Background Panel
        GradientBackgroundPanel bgPanel = new GradientBackgroundPanel();
        bgPanel.setLayout(new GridBagLayout());
        setContentPane(bgPanel);

        // Layer 2: Main Glass Card Container
        GlassCardPanel mainCard = new GlassCardPanel();
        mainCard.setLayout(new BorderLayout(20, 20));
        mainCard.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Title
        JLabel title = new JLabel("Optimization Engine", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        mainCard.add(title, BorderLayout.NORTH);

        // Center Content (Inputs on left, Outcomes on right)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        centerPanel.setOpaque(false);

        // Input Section
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel lblTotal = new JLabel("Total Classes Conducted:");
        lblTotal.setForeground(new Color(230, 230, 250));
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 15));
        txtTotal = createTextField();
        
        JLabel lblAtt = new JLabel("Total Classes Attended:");
        lblAtt.setForeground(new Color(230, 230, 250));
        lblAtt.setFont(new Font("SansSerif", Font.BOLD, 15));
        txtAttended = createTextField();

        inputPanel.add(lblTotal);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(txtTotal);
        inputPanel.add(Box.createVerticalStrut(30));
        inputPanel.add(lblAtt);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(txtAttended);

        // Output Section
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        outputPanel.setOpaque(false);
        outputPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        lblPercentage = createOutputLabel("Percentage: --");
        lblPercentage.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblMarks = createOutputLabel("Marks: --");
        lblZone = createOutputLabel("Status: --");

        outputPanel.add(lblPercentage);
        outputPanel.add(Box.createVerticalStrut(20));
        outputPanel.add(lblMarks);
        outputPanel.add(Box.createVerticalStrut(20));
        outputPanel.add(lblZone);

        centerPanel.add(inputPanel);
        centerPanel.add(outputPanel);
        mainCard.add(centerPanel, BorderLayout.CENTER);

        // Insights Section (Bottom)
        JPanel insightsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        insightsPanel.setOpaque(false);
        insightsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        lblCurrentInsight = new JLabel("Safety Buffer: --");
        lblCurrentInsight.setForeground(Color.WHITE);
        lblCurrentInsight.setFont(new Font("SansSerif", Font.PLAIN, 15));
        
        lblNextInsight = new JLabel("Next Goal: --");
        lblNextInsight.setForeground(Color.WHITE);
        lblNextInsight.setFont(new Font("SansSerif", Font.PLAIN, 15));

        insightsPanel.add(lblCurrentInsight);
        insightsPanel.add(lblNextInsight);
        mainCard.add(insightsPanel, BorderLayout.SOUTH);

        bgPanel.add(mainCard);

        setupListeners();
    }

    private JLabel createOutputLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        return lbl;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.BOLD, 20));
        tf.setOpaque(false); // Translucent text box
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return tf;
    }

    private void setupListeners() {
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calculate(); }
            public void removeUpdate(DocumentEvent e) { calculate(); }
            public void changedUpdate(DocumentEvent e) { calculate(); }
        };
        txtTotal.getDocument().addDocumentListener(dl);
        txtAttended.getDocument().addDocumentListener(dl);
    }

    private void calculate() {
        try {
            int total = Integer.parseInt(txtTotal.getText().trim());
            int attended = Integer.parseInt(txtAttended.getText().trim());
            
            if (total <= 0 || attended < 0 || attended > total) {
                resetOutputs();
                return;
            }

            double percentage = AttendanceLogic.calculatePercentage(total, attended);
            int marks = AttendanceLogic.calculateMarks(percentage);
            String zone = AttendanceLogic.determineZone(percentage);

            lblPercentage.setText(String.format("Percentage: %.2f%%", percentage));
            lblMarks.setText("Marks Details: " + marks + " Marks Secured");
            lblZone.setText("Current Status: " + zone);

            if (zone.equals("Defaulter Zone")) {
                lblZone.setForeground(new Color(255, 120, 120)); // Soft Red
                int neededFor75 = AttendanceLogic.calculateRecovery(total, attended, 75.0);
                lblCurrentInsight.setText(String.format("Recovery: Need %d consecutive classes to reach 75%% (Safe).", neededFor75));
            } else {
                if (zone.equals("Safe Zone")) {
                    lblZone.setForeground(new Color(120, 255, 120)); // Soft Green
                } else {
                    lblZone.setForeground(new Color(255, 210, 100)); // Warm Orange
                }
                double currentTarget = AttendanceLogic.getCurrentTarget(percentage);
                int safeLeaves = AttendanceLogic.calculateLeaves(total, attended, currentTarget);
                lblCurrentInsight.setText(String.format("Safety Buffer: Can miss %d upcoming classes without dropping below %d marks.", safeLeaves, marks));
            }

            double nextTarget = AttendanceLogic.getNextTarget(percentage);
            if (nextTarget > 0) {
                int targetMarks = AttendanceLogic.calculateMarks(nextTarget);
                int neededForNext = AttendanceLogic.calculateRecovery(total, attended, nextTarget);
                lblNextInsight.setText(String.format("Next Goal: Attend %d more consecutive classes to reach %.0f%% (%d marks).", neededForNext, nextTarget, targetMarks));
            } else {
                lblNextInsight.setText("Next Goal: You are already in the maximum marks bracket!");
            }

        } catch (NumberFormatException ex) {
            resetOutputs();
        }
    }

    private void resetOutputs() {
        lblPercentage.setText("Percentage: --");
        lblMarks.setText("Marks: --");
        lblZone.setText("Status: --");
        lblZone.setForeground(Color.WHITE);
        lblCurrentInsight.setText("Safety Buffer: --");
        lblNextInsight.setText("Next Goal: --");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AttendanceGUI().setVisible(true);
        });
    }

    // --- GLASSMORPHISM COMPONENTS ---

    class GradientBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Diagonal primary mesh background
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(43, 88, 118), 
                getWidth(), getHeight(), new Color(78, 67, 118));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Bright glowing orb Top Right
            RadialGradientPaint rgp1 = new RadialGradientPaint(
                    getWidth() * 0.85f, getHeight() * 0.15f, getWidth() * 0.5f, 
                    new float[]{0f, 1f}, new Color[]{new Color(255, 117, 140, 180), new Color(255, 117, 140, 0)});
            g2d.setPaint(rgp1);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Cyan glowing orb Bottom Left
            RadialGradientPaint rgp2 = new RadialGradientPaint(
                    getWidth() * 0.15f, getHeight() * 0.85f, getWidth() * 0.5f, 
                    new float[]{0f, 1f}, new Color[]{new Color(55, 236, 186, 140), new Color(55, 236, 186, 0)});
            g2d.setPaint(rgp2);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.dispose();
        }
    }

    class GlassCardPanel extends JPanel {
        public GlassCardPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(700, 420));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Translucent white foreground panel (Frosted Glass simulation)
            g2d.setColor(new Color(255, 255, 255, 30)); 
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            
            // Semi-transparent border outline for the "glass edge" specular highlight
            g2d.setColor(new Color(255, 255, 255, 90));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 40, 40));
            
            g2d.dispose();
            super.paintComponent(g); 
        }
    }
}

class AttendanceLogic {
    public static double calculatePercentage(int total, int attended) {
        if (total == 0) return 0.0;
        return ((double) attended / total) * 100.0;
    }
    public static int calculateMarks(double percentage) {
        if (percentage >= 95.0) return 5;
        if (percentage >= 90.0) return 4;
        if (percentage >= 85.0) return 3;
        if (percentage >= 80.0) return 2;
        if (percentage >= 75.0) return 1;
        return 0; // Defaulter
    }
    public static String determineZone(double percentage) {
        if (percentage < 75.0) return "Defaulter Zone";
        if (percentage < 80.0) return "Risk Zone";
        return "Safe Zone";
    }
    public static double getCurrentTarget(double percentage) {
        if (percentage >= 95.0) return 95.0;
        if (percentage >= 90.0) return 90.0;
        if (percentage >= 85.0) return 85.0;
        if (percentage >= 80.0) return 80.0;
        if (percentage >= 75.0) return 75.0;
        return 0.0; 
    }
    public static double getNextTarget(double percentage) {
        if (percentage < 75.0) return 75.0;
        if (percentage < 80.0) return 80.0;
        if (percentage < 85.0) return 85.0;
        if (percentage < 90.0) return 90.0;
        if (percentage < 95.0) return 95.0;
        return -1.0; 
    }
    public static int calculateLeaves(int total, int attended, double target) {
        if (target == 0.0) return 0;
        double l = (100.0 * attended / target) - total;
        return (int) Math.floor(l);
    }
    public static int calculateRecovery(int total, int attended, double target) {
        if (target >= 100.0) return -1; 
        double r = ((target * total) - (100.0 * attended)) / (100.0 - target);
        if (r < 0) return 0; 
        return (int) Math.ceil(r);
    }
}
