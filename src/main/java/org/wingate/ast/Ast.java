package org.wingate.ast;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.EventQueue;

/**
 *
 * @author util2
 */
public class Ast {
    
    private static final String APP_NAME = "AST";
    private static final String APP_LONG_NAME = "ASS Subtitles Translate";
    private static final String APP_VERSION = "0.1.1";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            System.out.println(String.format("%s :: %s v%s starts...", APP_NAME, APP_LONG_NAME, APP_VERSION));
            FlatLightLaf.setup();
            MainFrame mf = new MainFrame();
            mf.setLocationRelativeTo(null);
            mf.setTitle(String.format("%s :: %s v%s", APP_NAME, APP_LONG_NAME, APP_VERSION));
            mf.setVisible(true);
        });
    }
}
