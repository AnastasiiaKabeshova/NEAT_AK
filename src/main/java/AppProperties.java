
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author anastasiiakabeshova
 */
public class AppProperties {

    static Properties prop = new Properties();

    {
        FileInputStream f = null;
        try {
            f = new FileInputStream("application.properties");
            prop.load(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AppProperties.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppProperties.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                f.close();
            } catch (IOException ex) {
                Logger.getLogger(AppProperties.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static String fileName(){
        return prop.getProperty("input.filename");
    }
    
    public static int iterationCount(){
        return Integer.parseInt(prop.getProperty("iterationCount", "100"));
    }
}
