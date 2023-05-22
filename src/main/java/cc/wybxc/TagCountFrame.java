package cc.wybxc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class TagCountFrame extends JFrame {
    public TagCountFrame(HashMap<String, Integer> tagCounts) {
        setTitle("Tag Counts");
        setSize(600, 400);
        setLocationRelativeTo(null);

        add(new BarChartPanel(tagCounts));
    }

    private static class BarChartPanel extends JPanel {
        private final Map<String, Integer> tagCounts;

        public BarChartPanel(Map<String, Integer> tagCounts) {
            this.tagCounts = tagCounts;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x = getWidth() / 10;
            int y = getHeight() / 10;
            int width = getWidth() - 2 * x;
            int height = getHeight() - 2 * y;
            int maxCount = getMaxCount(tagCounts);
            int gap = 10;
            int barWidth = (width - gap * (tagCounts.size() - 1)) / tagCounts.size();
            // Draw bars
            int i = 0;
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                g.setColor(getColor(i));
                int barHeight = entry.getValue() * height / maxCount;
                int barX = x + i * (barWidth + gap);
                int barY = y + height - barHeight;
                g.fillRect(barX, barY, barWidth, barHeight);
                // Draw value label
                g.setColor(Color.BLACK);
                g.setFont(new Font("Dialog", Font.PLAIN, 12));
                String valueLabel = String.valueOf(entry.getValue());
                int labelX = barX + barWidth / 2 - g.getFontMetrics().stringWidth(valueLabel) / 2;
                int labelY = barY - 5;
                g.drawString(valueLabel, labelX, labelY);
                // Draw key label
                String keyLabel = entry.getKey();
                int keyLabelX = barX + barWidth / 2 - g.getFontMetrics().stringWidth(keyLabel) / 2;
                int keyLabelY = y + height + 20;
                g.drawString(keyLabel, keyLabelX, keyLabelY);
                i++;
            }
        }

        private int getMaxCount(Map<String, Integer> tagCounts) {
            int maxCount = 0;
            for (int count : tagCounts.values()) {
                if (count > maxCount) {
                    maxCount = count;
                }
            }
            return maxCount;
        }

        private Color getColor(int i) {
            return switch (i % 5) {
                case 0 -> Color.RED;
                case 1 -> Color.BLUE;
                case 2 -> Color.GREEN;
                case 3 -> Color.MAGENTA;
                case 4 -> Color.ORANGE;
                default -> Color.BLACK;
            };
        }
    }
}
