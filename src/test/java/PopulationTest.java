
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


public class PopulationTest {
    
    public PopulationTest() {
    }

    @Test
    public void testCreateInitPopulation() {
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        
        Population testPopulation = new Population(inNodes1, outNodes);
        Assert.assertEquals("wrong size of Population", 150, testPopulation.getSize());
    }
    
    @Test
    public void testExplicitFitness() {
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        
        Population testPopulation = new Population(inNodes1, outNodes);
        
        testPopulation.countFitnessAllPop();
        testPopulation.explicitFitness();
    }
    
    @Test
    public void testGA() {
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        NeatClass.p_pop_size = 10;
        
        Population instance = new Population(inNodes1, outNodes);
        NeatClass.p_mutate_add_node = 0.5;
        instance.GA();
        
        Assert.assertEquals("wrong size of Population", NeatClass.p_pop_size, instance.getSize());
    }
}