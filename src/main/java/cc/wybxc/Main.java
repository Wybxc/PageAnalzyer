package cc.wybxc;

import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialLiteTheme;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;


public class Main {
    static {
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialLiteTheme()));
            FontUIResource fontRes = new FontUIResource(new Font("Dialog", Font.PLAIN, 12));
            for (var keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource)
                    UIManager.put(key, fontRes);
            }
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame.setDefaultLookAndFeelDecorated(true);
            var frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
