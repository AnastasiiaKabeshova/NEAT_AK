
import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;


public class SpeciesTest {
    
    public SpeciesTest() {
    }

    @Ignore
    @Test
    public void testMutation() {
        OrganismFactory fabrica = new OrganismFactory ();
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        Organism o =  fabrica.createOrganism(inNodes1, outNodes);
        
        Species s1 = new Species();
        s1.addOrganism(o);
        s1.mutateOrganism(o);
        
        //assertEquals("mutation doesn't done", , );
    }
    
    @Test
    public void testCrossover() {
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        NeatClass.p_pop_size = 10;
        Population pop = new Population(inNodes1, outNodes);
        pop.countFitnessAllPop();
        Species instance = pop.getSpecies().get(0);
        instance.crossover();
        
        Assert.assertEquals("expected that was added", 10+4, instance.getNumberOrganisms());
    }
    
    @Test
    public void testRemoveOrganism() {
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        NeatClass.p_pop_size = 10;
        Population pop = new Population(inNodes1, outNodes);
        pop.countFitnessAllPop();
        Species instance = pop.getSpecies().get(0);
        int oldNumber = instance.getNumberOrganisms();
        instance.removeOrganism(instance.getOrganism(3));
        
        Assert.assertEquals("expected that was added", oldNumber-1, instance.getNumberOrganisms());
    }
}