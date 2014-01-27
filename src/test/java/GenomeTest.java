/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Administrateur
 */
public class GenomeTest {
    
    public GenomeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testFindNumberByInnovNumber() {
        OrganismFactory fabrica = new OrganismFactory ();
        List<Double> inNodes = Arrays.asList(1.0,2.0,3.0);
        List<Double> outNodes = Arrays.asList(1.0);
        Organism o =  fabrica.createOrganism(inNodes, outNodes);
        Genome instance = o.getGenome();
        
        int linkInnovNum = 3;
        int index = instance.findNumberByInnovNumber(linkInnovNum);
        
        Assert.assertEquals("expected that was added", 2, index);
    }

    /**
     * Test of mutate_addNode method, of class Genome.
     */

    @Test
    public void testMutate_addNode() {
        System.out.println("mutate_addNode");
        OrganismFactory fabrica = new OrganismFactory ();
        List<Double> inNodes = Arrays.asList(1.0,2.0,3.0);
        List<Double> outNodes = Arrays.asList(1.0);
        Organism o =  fabrica.createOrganism(inNodes, outNodes);
        Genome instance = o.getGenome();
        
        int linkInnovNum = 5;
        int oldNodeQuantity = instance.getNumberGenes();
        
        instance.mutate_addNode(linkInnovNum);
        Assert.assertEquals("expected that was added", oldNodeQuantity + 2, instance.getNumberGenes());
    }

    /**
     * Test of mutate_addConnection method, of class Genome.
     */
    @Ignore
    @Test
    public void testMutate_addConnection() {
        System.out.println("mutate_addConnection");
        Population pop = null;
        Node nodeIn = null;
        Node nodeOut = null;
        boolean enable = false;
        Genome instance = null;
        instance.mutate_addConnection(nodeIn, nodeOut, enable);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mutate_linkWeight method, of class Genome.
     */
    @Ignore
    @Test
    public void testMutate_linkWeight() {
        System.out.println("mutate_linkWeight");
        double power = 0.0;
        double rate = 0.0;
        int mutation_type = 0;
        Genome instance = null;
        instance.mutate_linkWeight(power, rate, mutation_type);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}