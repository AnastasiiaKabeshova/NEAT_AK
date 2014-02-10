
/**
 * Is a superclass for definition of all parameters , threshold and others
 * values.
 */
public class NeatClass {
    /**
     * * factor multiply for gene not equal 
     */
    public static double p_disjoint_coeff = 1.0; //c2
    /**
     * * factor multiply for gene excedeed
     */
    public static double p_excess_coeff = 1.0; //c1
    /**
     * * factor multiply weight difference  0.4 - 3.0
     */
    public static double p_mutdiff_coeff = 0.4; //c3
    /**
     * * threshold under which two Genomes are the same species
     */
    public static double p_compat_threshold = 3.0;
    /**
     * * Percent of ave fitness for survival (0.2)
     */
    public static double p_survival_thresh;
    /**
     * * The power of a linkweight mutation 80%
     */
    public static double p_weight_mut_power;
    // Compatibility Modifier (0.3)
    /**
     * * Size of population
     */
    public static int p_pop_size = 150;
    /**
     * * Age where Species starts to be penalized (15)
     */
    public static int p_dropoff_age;
    /**
     * параметр наклона сигмоидальной функции
     */
    public static double p_alpha_activationFunction = 4.9;
    /**
     * Probability of switch status to ena of gene 
     * 75% chance that an inherited gene was disabled if it was disable in either parent
     */ 
    public static double p_mutate_gene_reenable_prob = 0.15;
    /**
     * the probability of adding a new node was 0.03
     */
    public static double p_mutate_add_node = 0.1;
    /**
     * and the probability of a new link mutation 0.05 - 0.3
     */
    public static double p_mutate_add_link = 0.3;
    /**
     * Probability of a mate being outside species
     */
    public static double p_interspecies_mate_rate = 0.001;
    /**
     * training coefficient for back propagandation
     */
    public static double p_training_coefficient;
   
    
    //TODO
    private void addRandomSimpleOrganism() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //TODO
    private void calculateFitness() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void initiatePopulation(int size) {
        int mMaxNeuronsCount = 3; // input + neuron + output

        for (int i = size - 1; i >= 0; --i) {
            addRandomSimpleOrganism();
        }
        calculateFitness();
    }
    
    public void buildNeuralNetwork(Organism organism){
        
    }
    
    public void nextGeneration() {
        
    }
    
     public void normalization() {
         
     }
    
}
