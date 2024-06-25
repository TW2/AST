package org.wingate.ast;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.suuft.libretranslate.Translator;
import org.wingate.ast.util.Language;

/**
 *
 * @author util2
 */
public class Ast {
    
    private static final String APP_NAME = "AST";
    private static final String APP_LONG_NAME = "ASS Subtitles Translate";
    private static final String APP_VERSION = "0.1.1";
    
    private static final String URL = "http://127.0.0.1:5000/translate";

    public static void main(String[] args) {
        if(args.length > 0){
            if(args.length == 3){
                Translator.setUrlApi(URL);
                translate(args[0], args[1], args[2]);
            }else{
                System.out.println("Wrong command line!");
            }
        }else{
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
    
    private static void translate(String lngs, String text, String outputFilePath){
        String source = clearArtifacts(text);
        if(source.isEmpty()) return;
        
        String result = "";
        
        if(lngs.matches(".{1}\\w{2}")){
            Language lngTo = Language.getFromISO(lngs.substring(1));
            result = Translator.translate("auto", lngTo.getISO_639(), source);
        }else if(lngs.matches("\\w{2}.{1}\\w{2}")){
            Language lngFrom = Language.getFromISO(lngs.substring(0, 2));
            Language lngTo = Language.getFromISO(lngs.substring(3));
            result = Translator.translate(lngFrom.getISO_639(), lngTo.getISO_639(), source);
        }
        
        if(result.isEmpty()) return;
        
        try(PrintWriter pw = new PrintWriter(outputFilePath, StandardCharsets.UTF_8);){
            pw.println(result);
        } catch (IOException ex) {
            Logger.getLogger(Ast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static String clearArtifacts(String s){
        String str = s;
        str = str.replaceAll("\\{[^\\}]+\\}", "");
        str = str.replace("\\N", "");
        str = str.replace("\\n", "");
        str = str.replace("\\t", "");
        return str;
    }
}
