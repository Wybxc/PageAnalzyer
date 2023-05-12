package cc.wybxc;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import javax.swing.*;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;
import org.jsoup.Jsoup;


public class MainFrame extends JFrame {
    private final JButton button;
    private final JTextField textField;
    private final RSyntaxTextArea textArea;
    private final JLabel tagCountLabel;

    private HashMap<String, Integer> tagCounts;

    public MainFrame() {
        setTitle("HTML Tag Counter");
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create panel to hold text field and button.
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JLabel label = new JLabel("Enter a URL: ");
        panel.add(label);

        textField = new JTextField(20);
        panel.add(textField);

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    fetchAndAnalyze();
                }
            }
        });

        button = new JButton("Count");
        panel.add(button);

        button.addActionListener(e -> {
            if (e.getSource() == button) {
                fetchAndAnalyze();
            }
        });

        // Create a styled text pane to display the source code.
        textArea = new RSyntaxTextArea(20, 60);
        textArea.setEditable(false);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        textArea.setCodeFoldingEnabled(true);
        var sp = new RTextScrollPane(textArea);
        add(sp, BorderLayout.CENTER);

        // Create a label to display tag counts.
        tagCountLabel = new JLabel();
        tagCountLabel.setPreferredSize(new Dimension(780, 40));
        tagCountLabel.setVerticalAlignment(JLabel.TOP);
        add(tagCountLabel, BorderLayout.PAGE_END);

        tagCountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    if (tagCounts != null) {
                        var frame = new TagCountFrame(tagCounts);
                        frame.setVisible(true);
                    }
                }
            }
        });

        // Add panel to frame.
        add(panel, BorderLayout.PAGE_START);

        setVisible(true);
    }

    private void fetchAndAnalyze() {
        String url = textField.getText();

        // test for http:// or https:// scheme
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .followRedirects(true) // follow redirects
                    .build();
            Request request = new Request.Builder()
                    .url(url) // set the URL
                    .build();

            String sourceCode = null;

            try (Response response = client.newCall(request).execute()) {
                if (response.body() != null) {
                    sourceCode = response.body().string();
                }
            }

            if (sourceCode == null) {
                JOptionPane.showMessageDialog(this, "Error reading URL: " + url);
                return;
            }

            // Set text of text pane to source code.
            textArea.setText(formatHTML(sourceCode));


            // Count number of HTML tags.
            tagCounts = countTags(sourceCode);
            int count = 0;
            for (var entry : tagCounts.entrySet()) {
                count += entry.getValue();
            }

            // Set text of tag count label.
            tagCountLabel.setText("<html>The page '" + url + "' contains <b>" + count + "</b> HTML tags. " +
                    "Double-click here to see details.</html>");

        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(this, "Invalid URL: " + url);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading URL: " + url);
        }
    }

    private String formatHTML(String html) {
        var doc = Jsoup.parse(html);
        return doc.toString();
    }

    private HashMap<String, Integer> countTags(String html) {
        HashMap<String, Integer> map = new HashMap<>();

        var doc = Jsoup.parse(html);
        var tags = doc.select("*");
        for (var tag : tags) {
            var tagName = tag.tagName();
            if (map.containsKey(tagName)) {
                map.put(tagName, map.get(tagName) + 1);
            } else {
                map.put(tagName, 1);
            }
        }

        return map;
    }
}
