
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
    static Properties local = new Properties();

    static {
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
        try {
            f = new FileInputStream("local.properties");
            local.load(f);
        } catch (FileNotFoundException ex) {
            System.out.println("Cannot find local properties");
        } catch (IOException ex) {
            System.out.println("Cannot find local properties");
        } finally {
            try {
                f.close();
            } catch (IOException ex) {
                Logger.getLogger(AppProperties.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    public static String fileName(){
        return getProperty("input.filename");
    }
    
    public static int iterationCount(){
        return Integer.parseInt(getProperty("iterationCount", "100"));
    }
    
    public static int answerColNumber() {
        return Integer.parseInt(getProperty("colon.number.answer","100"));
    }
    
    public static double compatThreshold () {
        return Double.parseDouble(getProperty("compat.threshold", "2.00"));
    }
    
    public static int populationSize() {
        return Integer.parseInt(getProperty("pop.size","50"));
    }
    
    public static int coefMultiplEpoch() {
        return Integer.parseInt(getProperty("coef.multipl.epoch", "10"));
    }
    
    public static double coefMutateAddNode() {
        return Double.parseDouble(getProperty("mutate.add.node", "0.1"));
    }
    
    public static double coefMutateAddLink() {
        return Double.parseDouble(getProperty("mutate.add.link", "0.3"));
    }
    
    private static String getProperty(String propName) {
        return getProperty(propName, null);
    }

    private static String getProperty(String propName, String defValue) {
        if(local.containsKey(propName)){
            return local.getProperty(propName);
        } else {
            return prop.getProperty(propName, defValue);
        }
    }
}
