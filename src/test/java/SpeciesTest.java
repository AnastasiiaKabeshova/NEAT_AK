
import java.util.Arrays;
import java.util.List;
import org.junit.Test;


public class SpeciesTest {
    
    public SpeciesTest() {
    }

    @Test
    public void testMutation() {
        OrganismFactory fabrica = new OrganismFactory ();
        List<Double> inNodes1 = Arrays.asList(1.0,2.0,3.0,4.0,5.0);
        List<Double> outNodes = Arrays.asList(1.0,2.0);
        Organism o =  fabrica.createOrganism(inNodes1, outNodes);
        
        Species s1 = new Species();
        s1.addOrganism(o);
        s1.mutation(o);
        
        //assertEquals("mutation doesn't done", , );
    }
}