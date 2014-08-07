
import java.util.ArrayList;
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
    public void testCrossover() throws Exception {
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        NeatClass.p_pop_size = 10;
        Population pop = new Population(inNodes1, outNodes);
        pop.countInitialFitnessAllPop();
        Species instance = pop.getSpecies().get(0);
        instance.crossover(1);
        
        Assert.assertEquals("expected that was added", 10, instance.getNumberOrganisms());
    }
    
    @Test
    public void testRemoveOrganism() throws Exception {
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        NeatClass.p_pop_size = 10;
        Population pop = new Population(inNodes1, outNodes);
        pop.countInitialFitnessAllPop();
        Species instance = pop.getSpecies().get(0);
        int oldNumber = instance.getNumberOrganisms();
        instance.removeOrganism(instance.getOrganism(3));
        
        Assert.assertEquals("expected that was added", oldNumber-1, instance.getNumberOrganisms());
    }
    
    @Test
    public void testMakeOffspring() throws Exception {
        List<Double> inNodes = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        OrganismFactory fabrica = new OrganismFactory();
        OrganismFactory.resetIndexes();
        
        Organism o1 = fabrica.createOrganism(inNodes, outNodes);
        List<Gene> genes = new ArrayList<Gene>();
        genes.add(o1.getGenome().getGenes().get(1));
        genes.add(o1.getGenome().getGenes().get(2));
        genes.add(o1.getGenome().getGenes().get(3));
        genes.add(o1.getGenome().getGenes().get(6));
        genes.add(o1.getGenome().getGenes().get(7));
        genes.add(o1.getGenome().getGenes().get(8));
        Genome g = new Genome(OrganismFactory.nextEnabeledGenomeID(), genes);
        g.mutate_addNode(3);
        g.mutate_addNode(7);
        g.mutate_addConnection(g.getGenes().get(0).getLink().getIn_node(), g.getGenes().get(6).getLink().getOut_node(), true);
        Organism o2 = new Organism(g);
        o1.countFitnessOut(1);
        o2.countFitnessOut(1);
        Species s = new Species(o1);
        s.addOrganism(o2);
        
        int oldNumber = s.getNumberOrganisms();
        Organism instance = s.makeOffspring(0, 1);
        s.addOrganism(instance);
        
        Assert.assertEquals("expected that was added", oldNumber+1, s.getNumberOrganisms());
    }
}