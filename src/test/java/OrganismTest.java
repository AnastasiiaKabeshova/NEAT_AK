/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Administrateur
 */
public class OrganismTest {
    
    public OrganismTest() {
    }
    
    @Before
    public void setUp(){
        OrganismFactory.resetIndexes();
    }

    @Test
    public void testGetNumberExcess() {
        OrganismFactory fabrica = new OrganismFactory ();
        
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> inNodes2 = Arrays.asList(1.0,2.0,3.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        Organism o1 =  fabrica.createOrganism(inNodes1, outNodes);
        Organism o2 =  fabrica.createOrganism(inNodes2, outNodes);
        for (int i = 0; i < 3; i++) {
            o2.getGenome().getGenes().get(i).setInnovation_num(2+i);
        }
        for (int i = 3; i < 6; i++) {
            o2.getGenome().getGenes().get(i).setInnovation_num(6+i);
        }
        
        int actual = o1.getNumberExcess(o2);
        Assert.assertEquals("number excess genes is wrong", 1, actual);
    }
    
    @Test
    public void testGetNumberDisjoint() {
        OrganismFactory fabrica = new OrganismFactory ();
        
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> inNodes2 = Arrays.asList(1.0,2.0,3.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        Organism o1 =  fabrica.createOrganism(inNodes1, outNodes);
        Organism o2 =  fabrica.createOrganism(inNodes2, outNodes);
        for (int i = 0; i < 3; i++) {
            o2.getGenome().getGenes().get(i).setInnovation_num(18+i);
        }
        for (int i = 3; i < 6; i++) {
            o2.getGenome().getGenes().get(i).setInnovation_num(22+i);
        }
        
        int actual = o1.getNumberDisjoint(o2);
        Assert.assertEquals("number disjoint genes is wrong", 10, actual);
    }
    
    @Test
    public void testGetNumberExcessConsecutive() {
        OrganismFactory fabrica = new OrganismFactory ();
        
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        Organism o1 =  fabrica.createOrganism(inNodes1, outNodes);
        Organism o2 =  fabrica.createOrganism(inNodes1, outNodes);
        
        int actual = o2.getNumberExcess(o1);
        Assert.assertEquals("number excess genes wrong", 10, actual);
    }
    
    @Test
    public void testAverageWeigthDiff() {
        OrganismFactory fabrica = new OrganismFactory ();
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> inNodes2 = Arrays.asList(1.0,2.0,3.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        Organism o1 =  fabrica.createOrganism(inNodes1, outNodes);
        Organism o2 =  fabrica.createOrganism(inNodes2, outNodes);
        for (int i = 0; i < 3; i++) {
            o2.getGenome().getGenes().get(i).setInnovation_num(2+i);
        }
        for (int i = 3; i < 6; i++) {
            o2.getGenome().getGenes().get(i).setInnovation_num(6+i);
        }
        
        double actual = o1.averageWeigthDiff(o2);
        Assert.assertNotNull("average weigth not null", actual);
    }
    
    @Test
    public void testCountFitness() {
        OrganismFactory fabrica = new OrganismFactory ();
        List<Double> inNodes2 = Arrays.asList(1.0,2.0,3.0);
        List<Double> outNodes = Arrays.asList(1.0);
        Organism o2 =  fabrica.createOrganism(inNodes2, outNodes);
        setWeigth(o2, 0, 0.1);
        setWeigth(o2, 1, 0.2);
        setWeigth(o2, 2, 0.3);
        
        o2.countFitnessOut();
        double actual = o2.getFitness();
        Assert.assertEquals("fitness was wrong calculated", 0.0, actual, 0.01);
    }

    private void setWeigth_Potential(Organism o2, int index, double w, double p) {
        o2.getGenome().getGenes().get(index).getLink().setWeight(w);
        o2.getGenome().getGenes().get(index).getLink().getOut_node().setPotential(p);
    }

    private void setWeigth(Organism o2, int index, double w) {
        o2.getGenome().getGenes().get(index).getLink().setWeight(w);
    }
    
}