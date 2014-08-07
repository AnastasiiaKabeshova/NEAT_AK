
import java.io.*;

public class EcritureTXTfichier {

    String path;
    private static int enableIDfile = 1;

    public EcritureTXTfichier(String filePath) {
        path = filePath;
    }

    public static int nextEnabeledIDfile() {
        return enableIDfile++;
    }

    public static void resetIndexes() {
        enableIDfile = 1;
    }

    public void writeToFile(String textLine) {
        try {
            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.print(textLine + "\n");

            pw.close();
        } catch (IOException e) {
        }
    }

    public void appendToFile(boolean append_value, String textLine) {
        boolean append_to_file = append_value;
        try {
            FileWriter fw = new FileWriter(path, append_to_file);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.printf(textLine);

            pw.close();
        } catch (IOException e) {
        }
    }

    public void appendToFile(boolean append_value, double value) {
        boolean append_to_file = append_value;
        try {
            FileWriter fw = new FileWriter(path, append_to_file);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.print("   " + value);

            pw.close();
        } catch (IOException e) {
        }
    }

}
