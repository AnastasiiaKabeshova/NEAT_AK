
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
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
            studyDatasetBaseLyon();

        } catch (Exception ex) {
            Logger.getLogger(EntryPoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void studyDatasetBaseLyon() throws IOException, Exception {
        //study network
        // for mac
        neatANN.readInputDataFromExcel(AppProperties.fileName(), AppProperties.answerColNumber());
        // for pc
        //neatANN.readInputDataFromExcel("c:/Users/Administrateur/Desktop/these_softPartie/database/PCR4_PCPA_test_20.xls", 24);
        neatANN.normalization();

        neatANN.makeThreeSamplesRandomized();
        neatANN.buildPopulation();

        //NeatClass.p_GA_max_iterations = 30;
        List< GraphDate> dataToPrint = neatANN.NEATalgorithm();

        int counter = 0;
        for (Iterator<GraphDate> data = dataToPrint.iterator(); data.hasNext();) {
            // create images
            XYSeries series1 = new XYSeries("Training error");
            XYSeries series2 = new XYSeries("Testing error");
            XYSeries series3 = new XYSeries("Validation error");

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(series1);
            dataset.addSeries(series2);
            dataset.addSeries(series3);

            GraphDate graphDate = data.next();
            for (int i = 0; i < graphDate.getSize_inSample(); i++) { //graphDate.getSize() = number iterations
                series1.add(i + 1, graphDate.getData(0, i));
                series2.add(i + 1, graphDate.getData(1, i));
                series3.add(i + 1, graphDate.getData(2, i));
            }
            counter++;
            final JFreeChart chart = createSimpleChart(dataset);
            /*final Marker iteration = new ValueMarker(175.0);
            iteration.setPaint(Color.darkGray);
            iteration.setLabel(String.valueOf(graphDate.getNiterations()));
            iteration.setLabelTextAnchor(TextAnchor.TOP_CENTER);*/
            ChartUtilities.saveChartAsJPEG(new File("chartImage" + counter + ".jpeg"), chart, 800, 600);
        }

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
