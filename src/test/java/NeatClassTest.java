
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Administrateur
 */
public class NeatClassTest {

    public NeatClassTest() {
    }

    @Test
    public void testReadInputDataFromExcel() {
        NeatClass instance = new NeatClass();
        instance.readInputDataFromExcel("C:/Users/Administrateur/Desktop/these_softPartie/database/PCR4_PCPA_2008_2010(for StstSoft).xls", 25);

        Assert.assertEquals("wrong number of parametres red from tha base", 26, instance.getNumberCharacteristics());
        Assert.assertEquals("wrong number of patients read from the base", 3525, instance.getNumberSubjects());
    }
    
    @Test 
    public void testMakeThreeSamplesRandomized() {
        NeatClass instance = new NeatClass();
        instance.readInputDataFromExcel("C:/Users/Administrateur/Desktop/these_softPartie/database/PCR4_PCPA_2008_2010(for StstSoft).xls", 25);
        
        instance.makeThreeSamplesRandomized();
        
        Assert.assertEquals("wrong number in created samples", 2467, instance.getNumberTrainingSample());
    }
    
    @Test 
    public void testBackPropag3samples() throws Exception {
        OrganismFactory.resetIndexes();
        NeatClass instance = new NeatClass();
        instance.readInputDataFromExcel("C:/Users/Administrateur/Desktop/these_softPartie/database/PCR4_PCPA_test_20.xls", 25);
        
        List< List<Double>> errors = new ArrayList< List<Double>>();
        instance.makeThreeSamplesRandomized();
        instance.buildPopulation();
        NeatClass.p_BP_max_iterations = 20;
        //errors = instance.backPropagation();
        
    }
}