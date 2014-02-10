
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Population {

    /**
     * The organisms in the Population
     */
    private List<Species> species = new ArrayList<Species>();
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
        if (getSpecies().isEmpty()) {
            /**
             * In the first generation, since there are no preexisting species,
             * NEAT begins by creating species 1 and placing the first genome
             * into that species.
             */
            getSpecies().add(new Species(organisms.get(0)));
            organisms.remove(0);
            speciate(organisms);
        }
    }

    public void speciate(List<Organism> organisms) {
        /* All other genomes are placed into species as follows: 
         * A random member of each existing species is chosen as its permanent representative.
         * Genomes are tested one at a time; if a genome’s distance to the representative of any 
         * existing species is less than t, a compatibility threshold, it is placed into this species.*/
        for (int i = 0; i < organisms.size(); i++) {
            boolean added = false;
            for (int j = 0; j < getSpecies().size(); j++) {
                double delta = organisms.get(i).countDistanceDelta(getSpecies().get(j).getRepresentOrganism());
                if (delta < NeatClass.p_compat_threshold) {
                    getSpecies().get(j).addOrganism(organisms.get(i));
                    j = getSpecies().size(); // go out from second for
                    added = true;
                }
            }
            /**
             * Otherwise, if it is not compatible with any existing species, a
             * new species is created and given a new number.
             */
            if (added == false) {
                getSpecies().add(new Species(organisms.get(i)));
            }
        }
    }

    public void countFitnessAllPop() {
        for (int i = 0; i < getSpecies().size(); i++) {
            for (int j = 0; j < getSpecies().get(i).getNumberOrganisms(); j++) {
                getSpecies().get(i).getOrganism(j).countFitnessOut();
            }
        }
    }

    /**
     * As the reproduction mechanism for NEAT, we use explicit fitness sharing,
     * where organisms in the same species must share the fitness of their
     * niche.
     */
    public void explicitFitness() {
        for (int i = 0; i < getSpecies().size(); i++) {
            for (int j = 0; j < getSpecies().get(i).getNumberOrganisms(); j++) {
                double newFitness = getSpecies().get(i).getOrganism(j).getFitness()
                        / (double) sumSharingFunc(getSpecies().get(i).getOrganism(j), i, j);
                getSpecies().get(i).getOrganism(j).setFitness(newFitness);
            }
        }
    }

    private int sumSharingFunc(Organism organism, int speciesI, int organismJ) {
        int sum = 0;
        for (int i = 0; i < getSpecies().size(); i++) {
            for (int j = 0; j < getSpecies().get(i).getNumberOrganisms(); j++) {
                if (i != speciesI || j != organismJ) {
                    if (organism.countDistanceDelta(getSpecies().get(i).getOrganism(j)) < NeatClass.p_compat_threshold) {
                        sum += 1;
                    }
                }
            }

        }
        return sum;
    }

    public int getSpeciesNumber() {
        return getSpecies().size();
    }

    public void GA() {
        /* crossover entre les species !!! */
        // TODO

        /* mutation crossover */
        for (int i = 0; i < this.getSpeciesNumber(); i++) {
            species.get(i).mutation();
            species.get(i).crossover();
        }

        this.countFitnessAllPop();
        /* провести селекцию*/
        /**
         * все сложить в одну кучу отсортировать по фитнесу удалить все лишние
         */
        Map<Integer, Species> typeByOrganism = new HashMap<Integer, Species>();
        List<Organism> allOrganisms = new ArrayList<Organism>();
        for (int i = 0; i < species.size(); i++) {
            for (int j = 0; j < species.get(i).getNumberOrganisms(); j++) {
                typeByOrganism.put(species.get(i).getOrganism(j).getOrganism_id(), species.get(i));
                allOrganisms.add(species.get(i).getOrganism(j));
            }
        }
        Collections.sort(allOrganisms, new Comparator<Organism>() {
            @Override
            public int compare(Organism o1, Organism o2) {
                return o2.getFitness() > o1.getFitness() ? 1
                        : o2.getFitness() < o1.getFitness() ? -1 : 0;
            }
        });
        // … … … … … … … … … … … … … …
        for (int i = getSize() - 1; i < allOrganisms.size(); i++) {
            Species type = typeByOrganism.get(allOrganisms.get(i).getOrganism_id());
            type.removeOrganism(allOrganisms.get(i));
        }
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
//    public Organism getIJOrganism (int speciesI, int organismJ) {
//        return (species.get(speciesI).getOrganism(organismJ));
//    }
//    
//    public int getNumberOrganismsInSpecies (int speciesI) {
//        return (species.get(speciesI).getNumberOrganisms());
//    }

    /**
     * @return the species
     */
    public List<Species> getSpecies() {
        return species;
    }
}
