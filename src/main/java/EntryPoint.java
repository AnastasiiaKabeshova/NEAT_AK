
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/**
 *
 * @author anastasiiakabeshova
 */
public class EntryPoint {
    
    static NeatClass neatANN = new NeatClass();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            final XYDataset dataset = studyDatasetBaseLyon();
            final JFreeChart chart = createSimpleChart(dataset);
            ChartUtilities.saveChartAsJPEG(new File("chartImage.jpeg"),chart,800,600);
        } catch (Exception ex) {
            Logger.getLogger(EntryPoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static XYDataset studyDatasetBaseLyon() throws IOException, Exception {
        final XYSeries series1 = new XYSeries("Training error");
        final XYSeries series2 = new XYSeries("Testing error");
        final XYSeries series3 = new XYSeries("Validation error");

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        //study network
        // for mac
        neatANN.readInputDataFromExcel("/Users/anastasiiakabeshova/Documents/these_softPartie/database/PCR4_PCPA_test_20.xls", 24);
        // for pc
        //neatANN.readInputDataFromExcel("c:/Users/Administrateur/Desktop/these_softPartie/database/PCR4_PCPA_test_20.xls", 24);
        neatANN.normalization();

        neatANN.makeThreeSamplesRandomized();
        neatANN.buildPopulation();

        //NeatClass.p_GA_max_iterations = 30;

        List< List<Double>> gError = neatANN.NEATalgorithm();

        for (int i = 0; i < gError.get(0).size(); i++) {
            series1.add(i + 1, gError.get(0).get(i));
        }
        for (int i = 0; i < gError.get(1).size(); i++) {
            series2.add(i + 1, gError.get(1).get(i));
        }
        for (int i = 0; i < gError.get(2).size(); i++) {
            series3.add(i + 1, gError.get(2).get(i));
        }

        return dataset;
    }
    
    private static JFreeChart createSimpleChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Errors", // chart title
                "Epoch number", // x axis label
                "Error value", // y axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
        );
        return chart;
    }
    
}
