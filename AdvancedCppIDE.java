import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

public class AdvancedCppIDE extends JFrame {
    private JTabbedPane tabbedPane;
    private JFileChooser fileChooser;
    private HashMap<JTextPane, File> fileMap = new HashMap<>();
    private JTextArea outputArea;

    public AdvancedCppIDE() {
        setTitle("Advanced C/C++ IDE");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Apply dark mode (better contrast)
        UIManager.put("TabbedPane.background", new Color(0, 0, 0));  // Tab bar background (black)
        UIManager.put("TabbedPane.foreground", Color.GREEN); // Green text for tab names
        UIManager.put("TabbedPane.selectedBackground", new Color(40, 40, 40)); // Selected tab background (dark gray)
        UIManager.put("TabbedPane.selectedForeground", Color.GREEN); // Green text for selected tab
        UIManager.put("TabbedPane.borderHightlightColor", Color.BLACK); // Black border for tabs
        UIManager.put("TabbedPane.unselectedBackground", new Color(0, 0, 0)); // Unselected tab background (black)
        UIManager.put("TabbedPane.contentAreaColor", new Color(30, 30, 30)); // Editor area background
        UIManager.put("Button.background", new Color(50, 50, 50));
        UIManager.put("Button.foreground", Color.GREEN); // Green text on buttons
        UIManager.put("Button.border", BorderFactory.createLineBorder(new Color(100, 100, 100)));
        UIManager.put("MenuBar.background", new Color(30, 30, 30));
        UIManager.put("Menu.foreground", Color.GREEN); // Green text for menu
        UIManager.put("MenuItem.background", new Color(40, 40, 40));
        UIManager.put("MenuItem.foreground", Color.GREEN); // Green text for menu items

        // Set up the tabbed pane and file chooser
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(0, 0, 0));  // Set the tab bar background to black
        tabbedPane.setUI(new CustomTabbedPaneUI());  // Using the custom TabbedPaneUI

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("C/C++ Files", "c", "cpp"));

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem exitApp = new JMenuItem("Exit");

        newFile.addActionListener(e -> createNewTab());
        openFile.addActionListener(e -> openFile());
        saveFile.addActionListener(e -> saveFile());
        exitApp.addActionListener(e -> System.exit(0));

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.addSeparator();
        fileMenu.add(exitApp);
        menuBar.add(fileMenu);

        // Compile & Run Buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(40, 40, 40));
        JButton compileButton = new JButton("Compile & Run");
        outputArea = new JTextArea(5, 50);
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(30, 30, 30));
        outputArea.setForeground(new Color(0, 255, 0)); // Green text for output
        outputArea.setBorder(new EmptyBorder(5, 5, 5, 5));

        compileButton.addActionListener(e -> compileAndRun());

        bottomPanel.add(compileButton, BorderLayout.WEST);
        bottomPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Add to Frame
        setJMenuBar(menuBar);
        add(tabbedPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        createNewTab(); // Open an initial blank tab
    }

    private void createNewTab() {
        JTextPane editorPane = createEditorPane();
        JScrollPane scrollPane = new JScrollPane(editorPane);

        tabbedPane.addTab("Untitled", scrollPane);
        tabbedPane.setSelectedComponent(scrollPane); // Ensure no errors

        fileMap.put(editorPane, null); // No file linked yet
    }

    private JTextPane createEditorPane() {
        JTextPane editor = new JTextPane();
        editor.setFont(new Font("Monospaced", Font.PLAIN, 14));
        editor.setBackground(new Color(30, 30, 30));
        editor.setForeground(new Color(0, 255, 0)); // Green text for code
        editor.setCaretColor(Color.GREEN); // Green cursor
        return editor;
    }

    private void openFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                JTextPane editor = createEditorPane();
                editor.setText(content.toString());

                JScrollPane scrollPane = new JScrollPane(editor);
                tabbedPane.addTab(selectedFile.getName(), scrollPane);
                tabbedPane.setSelectedComponent(scrollPane);

                fileMap.put(editor, selectedFile);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage());
            }
        }
    }

    private void saveFile() {
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (!(selectedComponent instanceof JScrollPane)) return;

        JTextPane editor = (JTextPane) ((JScrollPane) selectedComponent).getViewport().getView();
        File file = fileMap.get(editor);

        if (file == null) {
            int returnValue = fileChooser.showSaveDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                fileMap.put(editor, file);
                tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), file.getName());
            } else {
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(editor.getText());
            JOptionPane.showMessageDialog(this, "File saved successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
        }
    }

    private void compileAndRun() {
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (!(selectedComponent instanceof JScrollPane)) return;

        JTextPane editor = (JTextPane) ((JScrollPane) selectedComponent).getViewport().getView();
        File file = fileMap.get(editor);

        if (file == null) {
            JOptionPane.showMessageDialog(this, "Save the file before compiling.");
            return;
        }

        String extension = file.getName().endsWith(".cpp") ? "g++" : "gcc";
        String outputFilePath = file.getParent() + "/output.exe";
        String command = extension + " \"" + file.getAbsolutePath() + "\" -o \"" + outputFilePath + "\" && start cmd /k \"" + outputFilePath + "\"";

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", command});

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) output.append(line).append("\n");
            while ((line = errorReader.readLine()) != null) output.append(line).append("\n");

            process.waitFor();
            outputArea.setText(output.toString());
        } catch (Exception ex) {
            outputArea.setText("Error compiling/running file: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdvancedCppIDE().setVisible(true));
    }

    // Custom TabbedPaneUI for modifying the look
    private static class CustomTabbedPaneUI extends BasicTabbedPaneUI {
        protected void paintTabArea(Graphics g, int tabPlacement, int x, int y, int w, int h, int selectedIndex) {
            // Paint the tab area with a nice, smooth background
            g.setColor(new Color(0, 0, 0));  // Black background for the tab area
            g.fillRect(x, y, w, h);
        }

        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            if (isSelected) {
                g.setColor(new Color(40, 40, 40));  // Dark gray background for selected tab
            } else {
                g.setColor(new Color(0, 0, 0));  // Black background for unselected tab
            }
            g.fillRect(x, y, w, h);
        }
    }
}
