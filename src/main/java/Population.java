
import java.util.ArrayList;
import java.util.List;

public final class Population {

    /**
     * The organisms in the Population
     */
    public List<Species> species = new ArrayList<Species>();
    /**
     * The last generation played
     */
    int final_gen;
    /**
     * population size
     */
    private int size;

    /**
     * Special constructor to create a population of random topologies uses
     * Genome (int i, int o, int n,int nmax, bool r, double linkprob) size =
     * number of organisms i = number of inputs o = number of output nmax = max
     * index of nodes r = the net can be recurrent ? linkprob = probability of
     * connecting two nodes.
     */
    public Population(List<Double> inNodes, List<Double> outNodes) {
        this.setSize(NeatClass.p_pop_size);
        OrganismFactory fabrica = new OrganismFactory();
        OrganismFactory.resetIndexes();
        List<Organism> organisms = new ArrayList<Organism>();
        for (int count = 0; count < this.getSize(); count++) {
            organisms.add(fabrica.createOrganism(inNodes, outNodes));
        }
        initialSpeciate(organisms);
    }

    /**
     * clusterind gemomes into species
     */
    private void initialSpeciate(List<Organism> organisms) {
        if (species.isEmpty()) {
            /**
             * In the first generation, since there are no preexisting species,
             * NEAT begins by creating species 1 and placing the first genome
             * into that species.
             */
            species.add(new Species(organisms.get(0)));
            organisms.remove(0);
            speciate(organisms);
        }
    }

    public void speciate(List<Organism> organisms) {
        /* All other genomes are placed into species as follows: 
         * A random member of each existing species is chosen as its permanent representative.
         * Genomes are tested one at a time; if a genome’s distance to the representative of any 
         * existing species is less than t, a compatibility threshold, it is placed into this species.*/
        for (int i = 1; i < organisms.size(); i++) {
            boolean added = false;
            for (int j = 0; j < species.size(); j++) {
                double delta = organisms.get(i).countDistanceDelta(species.get(j).getRepresentOrganism());
                if (delta < NeatClass.p_compat_threshold) {
                    species.get(j).addOrganism(organisms.get(i));
                    j = species.size(); // go out from second for
                    added = true;
                }
            }
            /**
             * Otherwise, if it is not compatible with any existing species, a
             * new species is created and given a new number.
             */
            if (added == false) {
                species.add(new Species(organisms.get(i)));
            }
        }
    }

    /**
     * As the reproduction mechanism for NEAT, we use explicit fitness sharing,
     * where organisms in the same species must share the fitness of their
     * niche.
     */
    public void explicitFitness() {
        for (int i = 0; i < species.size(); i++) {
            for (int j = 0; j < species.get(i).getNumberOrganisms(); j++) {
                double newFitness = species.get(i).getOrganism(j).getFitness()
                        / (double) sumSharingFunc(species.get(i).getOrganism(j));
                species.get(i).getOrganism(j).setFitness(newFitness);
            }
        }
    }

    private int sumSharingFunc(Organism organism) {
        int sum = 0;
        for (int i = 0; i < species.size(); i++) {
            for (int j = 0; j < species.get(i).getNumberOrganisms(); j++) {
                if (organism.countDistanceDelta(species.get(i).getOrganism(j)) < NeatClass.p_compat_threshold) {
                    sum += 1;
                }
            }

        }
        return sum;
    }

    public void GA() {
        /**
         * mutation crossover specification (speciate) провести селекцию на
         * уровне Популяции
         */
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }
}
