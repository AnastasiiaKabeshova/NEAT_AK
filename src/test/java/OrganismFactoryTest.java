/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrateur
 */
public class OrganismFactoryTest {
    
    public OrganismFactoryTest() {
    }

    @Test
    public void testCreateOrganism() {
        OrganismFactory fabrica = new OrganismFactory ();
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        Organism o1 =  fabrica.createOrganismFromSpecimen();
        int numberGenes = o1.getGenomeSize();
        Assert.assertEquals("number created genes wrong", 10, numberGenes);
    }
}