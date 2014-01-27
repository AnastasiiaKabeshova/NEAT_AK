
import java.util.ArrayList;
import java.util.List;


public class Population {
    /** The organisms in the Population */
	  public List<Species> species;
          
    /** Current label number available for nodes */
	  private int cur_node_id;
   
   /** The last generation played */
	  int final_gen;

          
    public int getCur_node_id() {
        return cur_node_id;
    }

    public void setCur_node_id(int cur_node_id) {
        this.cur_node_id = cur_node_id;
    }
    
    /**
   *	Special constructor to create a population of random topologies uses 
   *	Genome (int i, int o, int n,int nmax, bool r, double linkprob) 
   *  	size = number of organisms
   *  	i    = number of inputs
   *	o    = number of output
   *	nmax = max index of nodes
   *	r    = the net can be recurrent ?
   *	linkprob = probability of connecting two nodes.
   */ 
    public Population(int size,int i,int o, int nmax, boolean r, double linkprob) {
         Genome new_genome = null;
         int count;
         //List<Link> organisms = new ArrayList<Link> ();
         
         for(count=0; count < size; count++) {
            //new_genome = new Genome(count,i,o,NeatRoutine.randint(0,nmax),nmax,r,linkprob);
            //organisms.add(new Organism(0,new_genome,1));
	}
    }
    
    /**
     * clusterind gemomes into species
     */
    public void speciate() {
        //TODO
    } 
    
    /**
     * As the reproduction mechanism for NEAT, we use explicit fitness sharing,
     * where organisms in the same species must share the fitness of their niche.
     */
    public void explicitFitness() {
        for (int i = 0; i < species.size(); i++) {
            for (int j = 0; j < species.get(i).getNumberOrganisms(); j++) {
                double newFitness = species.get(i).getOrganism(j).getFitness()/
                        (double)sumSharingFunc( species.get(i).getOrganism(j));
                species.get(i).getOrganism(j).setFitness(newFitness);
            }
        }
    }

    private int sumSharingFunc(Organism organism) {
        int sum = 0;
        for (int i = 0; i < species.size(); i++) {
            for (int j = 0; j < species.get(i).getNumberOrganisms(); j++) {
                if (organism.countDistanceDelta(species.get(i).getOrganism(j)) < NeatClass.p_compat_threshold) {
                    sum+=1;
                }
            }
            
        }
        return sum;
    }
    
    public void GA() {
        /**
         * mutation
         * crossover
         * specification (speciate)
         * провести селекцию на уровне Популяции
         */
    }
}
