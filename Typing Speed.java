import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * TypingTestApp
 * ---------------------------------------------------------
 * A single-file Java Swing typing test with:
 *   - Start button to begin a timed round
 *   - Countdown timer
 *   - In-memory score & history storage (no external libraries needed)
 *   - "PERFECT!" animation when accuracy >= 95%
 *   - "CONGRATULATIONS!" animation when the user finishes
 *     typing before time runs out (accuracy < 95%)
 *   - History viewer dialog
 *
 * NO EXTERNAL DEPENDENCIES — just plain Java + Swing.
 *
 * HOW TO RUN IN VS CODE:
 *   1. Save this file as TypingTestApp.java
 *   2. Open a terminal in VS Code (Terminal > New Terminal)
 *   3. Compile:  javac TypingTestApp.java
 *   4. Run:      java TypingTestApp
 *
 *   (Or, if you have the "Extension Pack for Java" installed,
 *    just click the "Run" button that appears above main().)
 * ---------------------------------------------------------
 */
public class TypingTestApp extends JFrame {

    // ---------- Theme ----------
    private static final Color BG_DARK      = new Color(0x1B, 0x12, 0x16);
    private static final Color PANEL_DARK   = new Color(0x22, 0x16, 0x1B);
    private static final Color TEXT_LIGHT   = new Color(0xF3, 0xE9, 0xEC);
    private static final Color ACCENT_RED   = new Color(0xE0, 0x4F, 0x5F);
    private static final Color ACCENT_GOLD  = new Color(0xF2, 0xC1, 0x4E);
    private static final Color ACCENT_GREEN = new Color(0x4C, 0xD9, 0x7B);
    private static final Color INPUT_BG     = new Color(0x2A, 0x1B, 0x20);

    // ---------- Round state ----------
    private boolean roundActive = false;
    private String targetText = "";
    private long roundStartMillis;
    private int totalSeconds = 60;
    private int secondsLeft = totalSeconds;
    private Timer countdownTimer;
    private String currentPlayer = "Player";

    // ---------- UI ----------
    private JTextArea promptArea;
    private JTextArea inputArea;
    private JLabel statusLabel;
    private JLabel timerLabel;
    private JLabel playerLabel;
    private JButton startButton;
    private JButton historyButton;
    private JLayeredPane layeredPane;
    private CelebrationOverlay overlay;

    // ---------- Sample prompts ----------
    private static final String[] SAMPLE_TEXTS = {
        "The quick brown fox jumps over the lazy dog while the sun sets slowly behind the hills.",
        "Practice makes perfect, and consistent effort every day builds skills that last a lifetime.",
        "Typing quickly and accurately is a skill that improves the more you challenge yourself.",
        "Focus on rhythm and accuracy first; speed will naturally follow with steady practice.",
        "A journey of a thousand miles begins with a single step taken with confidence and courage."
    };

    public TypingTestApp() {
        super("Typing Speed & Accuracy Test");
        buildUI();
        promptForPlayerName();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 560);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ======================================================
    //  UI CONSTRUCTION
    // ======================================================
    private void buildUI() {
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        // ----- Top bar -----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG_DARK);
        topBar.setBorder(new EmptyBorder(14, 18, 6, 18));

        JLabel title = new JLabel("⌨  Typing Speed & Accuracy Test");
        title.setForeground(TEXT_LIGHT);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        topBar.add(title, BorderLayout.WEST);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightTop.setOpaque(false);
        playerLabel = new JLabel("Player: " + currentPlayer);
        playerLabel.setForeground(TEXT_LIGHT);
        timerLabel = new JLabel("⏱ " + totalSeconds + "s");
        timerLabel.setForeground(ACCENT_GOLD);
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        rightTop.add(playerLabel);
        rightTop.add(timerLabel);
        topBar.add(rightTop, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // ----- Center: prompt + input -----
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(BG_DARK);
        centerPanel.setBorder(new EmptyBorder(10, 18, 10, 18));

        promptArea = new JTextArea("Press \"Start\" to begin a new round.");
        promptArea.setEditable(false);
        promptArea.setLineWrap(true);
        promptArea.setWrapStyleWord(true);
        promptArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        promptArea.setBackground(PANEL_DARK);
        promptArea.setForeground(TEXT_LIGHT);
        promptArea.setBorder(new EmptyBorder(14, 14, 14, 14));
        promptArea.setRows(3);

        inputArea = new JTextArea();
        inputArea.setEditable(false);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        inputArea.setBackground(INPUT_BG);
        inputArea.setForeground(TEXT_LIGHT);
        inputArea.setCaretColor(TEXT_LIGHT);
        inputArea.setBorder(new EmptyBorder(14, 14, 14, 14));
        inputArea.setRows(6);

        inputArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { checkProgress(); }
            public void removeUpdate(DocumentEvent e)  { checkProgress(); }
            public void changedUpdate(DocumentEvent e) { checkProgress(); }
        });

        centerPanel.add(wrapTitled(promptArea, "Type this:"));
        centerPanel.add(Box.createVerticalStrut(12));
        centerPanel.add(wrapTitled(new JScrollPane(inputArea), "Your input:"));

        add(centerPanel, BorderLayout.CENTER);

        // ----- Bottom bar -----
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_DARK);
        bottomPanel.setBorder(new EmptyBorder(6, 18, 16, 18));

        statusLabel = new JLabel("Ready when you are.");
        statusLabel.setForeground(TEXT_LIGHT);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setOpaque(false);

        startButton = styledButton("▶  Start", ACCENT_GREEN);
        startButton.addActionListener(e -> startRound());

        historyButton = styledButton("📜  History", ACCENT_GOLD);
        historyButton.addActionListener(e -> showHistoryDialog());

        buttonRow.add(historyButton);
        buttonRow.add(startButton);
        bottomPanel.add(buttonRow, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // ----- Overlay for celebration animation -----
        layeredPane = getLayeredPane();
        overlay = new CelebrationOverlay();
        overlay.setBounds(0, 0, 760, 560);
        layeredPane.add(overlay, JLayeredPane.POPUP_LAYER);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                overlay.setBounds(0, 0, getWidth(), getHeight());
            }
        });
    }

    private JPanel wrapTitled(JComponent inner, String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel lbl = new JLabel(title);
        lbl.setForeground(new Color(0xC9, 0xB8, 0xBD));
        lbl.setBorder(new EmptyBorder(0, 2, 4, 0));
        p.add(lbl, BorderLayout.NORTH);
        p.add(inner, BorderLayout.CENTER);
        return p;
    }

    private JButton styledButton(String text, Color accent) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(accent);
        b.setForeground(new Color(0x1B, 0x12, 0x16));
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBorder(new EmptyBorder(10, 18, 10, 18));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void promptForPlayerName() {
        String name = JOptionPane.showInputDialog(this,
                "Enter your name:", "Welcome", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            currentPlayer = name.trim();
            playerLabel.setText("Player: " + currentPlayer);
        }
    }

    // ======================================================
    //  ROUND LOGIC
    // ======================================================
    private void startRound() {
        targetText = SAMPLE_TEXTS[new Random().nextInt(SAMPLE_TEXTS.length)];
        promptArea.setText(targetText);

        inputArea.setText("");
        inputArea.setEditable(true);
        inputArea.setBackground(INPUT_BG);
        inputArea.requestFocusInWindow();

        secondsLeft = totalSeconds;
        timerLabel.setText("⏱ " + secondsLeft + "s");
        timerLabel.setForeground(ACCENT_GOLD);

        statusLabel.setForeground(TEXT_LIGHT);
        statusLabel.setText("Round started — type the text above!");

        startButton.setEnabled(false);
        roundActive = true;
        roundStartMillis = System.currentTimeMillis();

        if (countdownTimer != null) countdownTimer.stop();
        countdownTimer = new Timer(1000, e -> tick());
        countdownTimer.start();
    }

    private void tick() {
        secondsLeft--;
        timerLabel.setText("⏱ " + secondsLeft + "s");
        if (secondsLeft <= 10) timerLabel.setForeground(ACCENT_RED);
        if (secondsLeft <= 0) {
            countdownTimer.stop();
            handleTimeUp();
        }
    }

    private void checkProgress() {
        if (!roundActive) return;
        String typed = inputArea.getText();
        if (typed.length() >= targetText.length()) {
            // Finished before time ran out
            SwingUtilities.invokeLater(() -> {
                if (roundActive) {
                    countdownTimer.stop();
                    roundActive = false;
                    inputArea.setEditable(false);
                    finalizeAttempt(false);
                }
            });
        }
    }

    private void handleTimeUp() {
        if (!roundActive) return;
        roundActive = false;
        inputArea.setEditable(false);
        inputArea.setBackground(new Color(0x2A, 0x18, 0x1E));
        statusLabel.setForeground(ACCENT_RED);
        statusLabel.setText("⏱  Time's Up! Attempt recorded as timed out.");
        finalizeAttempt(true);
        JOptionPane.showMessageDialog(this,
                "⏱ Time's Up!\nYour attempt has been recorded.",
                "Time's Up", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Scores the attempt, saves it to history, and triggers
     * the appropriate feedback (Perfect / Congratulations / plain).
     */
    private void finalizeAttempt(boolean timedOut) {
        startButton.setEnabled(true);

        double elapsedSeconds = Math.max(1.0, (System.currentTimeMillis() - roundStartMillis) / 1000.0);
        String typed = inputArea.getText();

        double accuracy = computeAccuracy(targetText, typed);
        double wordsTyped = typed.trim().isEmpty() ? 0 : typed.trim().split("\\s+").length;
        double wpm = (wordsTyped / elapsedSeconds) * 60.0;

        String result;
        if (timedOut) {
            result = "TIMEOUT";
        } else if (accuracy >= 95.0) {
            result = "PERFECT";
        } else {
            result = "CONGRATULATIONS";
        }

        HistoryStore.saveAttempt(currentPlayer, accuracy, wpm, (int) elapsedSeconds, result);

        statusLabel.setForeground(TEXT_LIGHT);
        statusLabel.setText(String.format(
                "Accuracy: %.1f%%   |   WPM: %.1f   |   Time: %.0fs   |   Result: %s",
                accuracy, wpm, elapsedSeconds, result));

        // Only finished attempts (not timeouts) get the celebration overlay,
        // per spec: PERFECT for >=95% accuracy, CONGRATULATIONS for finishing
        // before time runs out (accuracy < 95%).
        if (!timedOut) {
            if ("PERFECT".equals(result)) {
                overlay.celebrate("PERFECT!", ACCENT_GOLD);
            } else {
                overlay.celebrate("CONGRATULATIONS! 🎉", ACCENT_GREEN);
            }
        }
    }

    /** Character-by-character accuracy against the target text. */
    private double computeAccuracy(String target, String typed) {
        if (target.isEmpty()) return 0.0;
        int matches = 0;
        int len = Math.min(target.length(), typed.length());
        for (int i = 0; i < len; i++) {
            if (target.charAt(i) == typed.charAt(i)) matches++;
        }
        // Penalize missing characters (not typed before time ran out).
        return (matches / (double) target.length()) * 100.0;
    }

    // ======================================================
    //  HISTORY DIALOG
    // ======================================================
    private void showHistoryDialog() {
        List<HistoryStore.Attempt> attempts = HistoryStore.loadHistory(currentPlayer);

        String[] columns = {"Date", "Accuracy %", "WPM", "Time (s)", "Result"};
        Object[][] rows = new Object[attempts.size()][5];
        for (int i = 0; i < attempts.size(); i++) {
            HistoryStore.Attempt a = attempts.get(i);
            rows[i][0] = a.timestamp;
            rows[i][1] = String.format("%.1f", a.accuracy);
            rows[i][2] = String.format("%.1f", a.wpm);
            rows[i][3] = a.durationSeconds;
            rows[i][4] = a.result;
        }

        JTable table = new JTable(rows, columns);
        table.setEnabled(false);
        table.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(520, 320));

        JOptionPane.showMessageDialog(this, scroll,
                "History for " + currentPlayer, JOptionPane.PLAIN_MESSAGE);
    }

    // ======================================================
    //  CELEBRATION OVERLAY (Perfect / Congratulations)
    // ======================================================
    private static class CelebrationOverlay extends JComponent {
        private String message = "";
        private Color color = Color.WHITE;
        private float alpha = 0f;
        private float scale = 0.6f;
        private Timer animTimer;
        private int phase = 0; // 0=fade/scale in, 1=hold, 2=fade out
        private int frame = 0;

        CelebrationOverlay() {
            setOpaque(false);
        }

        void celebrate(String text, Color c) {
            this.message = text;
            this.color = c;
            this.alpha = 0f;
            this.scale = 0.6f;
            this.phase = 0;
            this.frame = 0;
            setVisible(true);

            if (animTimer != null) animTimer.stop();
            animTimer = new Timer(16, e -> step());
            animTimer.start();
        }

        private void step() {
            frame++;
            switch (phase) {
                case 0: // grow in (~25 frames)
                    alpha = Math.min(1f, frame / 20f);
                    scale = 0.6f + 0.4f * Math.min(1f, frame / 20f);
                    if (frame >= 25) { phase = 1; frame = 0; }
                    break;
                case 1: // hold (~70 frames ≈ 1.1s)
                    scale = 1.0f + 0.03f * (float) Math.sin(frame * 0.25);
                    if (frame >= 70) { phase = 2; frame = 0; }
                    break;
                case 2: // fade out (~25 frames)
                    alpha = Math.max(0f, 1f - frame / 25f);
                    if (frame >= 25) {
                        animTimer.stop();
                        setVisible(false);
                    }
                    break;
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (message.isEmpty() || alpha <= 0f) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dim background
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.45f));
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Message text, scaled
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            int baseSize = 50;
            Font f = new Font("SansSerif", Font.BOLD, (int) (baseSize * scale));
            g2.setFont(f);
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(message);
            int tx = (getWidth() - tw) / 2;
            int ty = getHeight() / 2;

            g2.setColor(new Color(0, 0, 0, (int) (150 * alpha)));
            g2.drawString(message, tx + 3, ty + 3);
            g2.setColor(color);
            g2.drawString(message, tx, ty);

            g2.dispose();
        }
    }

    // ======================================================
    //  HISTORY STORE (in-memory, no external database needed)
    // ======================================================
    static class HistoryStore {
        private static final List<Attempt> ALL_ATTEMPTS = new ArrayList<>();
        private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        static void saveAttempt(String player, double accuracy, double wpm,
                                 int durationSeconds, String result) {
            Attempt a = new Attempt();
            a.player = player;
            a.accuracy = accuracy;
            a.wpm = wpm;
            a.durationSeconds = durationSeconds;
            a.result = result;
            a.timestamp = FORMAT.format(new Date());
            ALL_ATTEMPTS.add(0, a); // newest first
        }

        static List<Attempt> loadHistory(String player) {
            List<Attempt> list = new ArrayList<>();
            for (Attempt a : ALL_ATTEMPTS) {
                if (a.player.equals(player)) {
                    list.add(a);
                    if (list.size() >= 100) break;
                }
            }
            return list;
        }

        static class Attempt {
            String player;
            double accuracy;
            double wpm;
            int durationSeconds;
            String result;
            String timestamp;
        }
    }

    // ======================================================
    //  MAIN
    // ======================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TypingTestApp::new);
    }
}
