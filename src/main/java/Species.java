
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * The idea is to divide the population into species such that similar
 * topologies are in the same species. This task appears to be a topology
 * matching problem.
 */
public final class Species extends NeatClass {
    /* list of all organisms in the Species*/

    private final List<Organism> organisms = new ArrayList<Organism>();
    /* The average fitness of the Species*/
    double ave_fitness;
    /* Organism with Max fitness of the Species*/
    private int max_fitness_organism;
    /* The max it ever had*/
    private double max_fitness_organism_ever;
    /* permanent representative */
    int representativeIndex = 0;

    Iterable<Organism> getOrganisms;

    public Species() {
    }

    public Species(Organism newOrg) {
        addOrganism(newOrg);
    }

    /*
     * add an organism to list of organisms in this specie
     */
    public void addOrganism(Organism newOrganism) {
        getOrganisms().add(newOrganism);
    }

    public int getNumberOrganisms() {
        return getOrganisms().size();
    }

    public Organism getOrganism(int index) {
        return getOrganisms().get(index);
    }

    public Organism getRepresentOrganism() {
        if (getOrganisms().isEmpty()) {
            System.err.println(this.toString());
        }
        return getOrganisms().get(representativeIndex);
    }

    public Organism makeOffspring(int organism1, int organism2) {
        /*A new structure that combines the overlapping parts of the two parents 
         * as well as their different parts can be created in crossover. In this
         * case, equal fitnesses are assumed, so each disjoint and excess gene is 
         * inherited from either parent randomly.
         * Otherwise the genes would be inherited from the more fit parent.*/

        List<Gene> resultList = new ArrayList<Gene>();
        List<Gene> parent1 = null, parent2 = null;
        OrganismFactory fabrica = new OrganismFactory();

        /**
         * crossover for equally fit parents
         */
        if (getOrganisms().get(organism1).getFitness() == getOrganisms().get(organism2).getFitness()) {
            parent1 = getOrganisms().get(organism1).getGenome().getGenes();
            parent2 = getOrganisms().get(organism2).getGenome().getGenes();
            int i = 0, j = 0;
            while (i < getOrganisms().get(organism1).getGenomeSize() && j < getOrganisms().get(organism2).getGenomeSize()) {
                //for matching genes
                if (parent1.get(i).getInnovation_num() == parent2.get(j).getInnovation_num()) {
                    resultList.add(matchingGeneSelect_firstMethod(parent1.get(i), parent2.get(j)));
                    i++;
                    j++;
                } // disjuint and excess genes
                else if (parent1.get(i).getInnovation_num() < parent2.get(j).getInnovation_num()) {
                    int x = (Math.random() < 0.5) ? 0 : 1;
                    if (x == 1) {
                        resultList.add(new Gene(parent1.get(i)));
                    }
                    i++;
                } else {
                    int x = (Math.random() < 0.5) ? 0 : 1;
                    if (x == 1) {
                        resultList.add(new Gene(parent2.get(j)));
                    }
                    j++;
                }
            }
            while (i < getOrganisms().get(organism1).getGenomeSize()) {
                int x = (Math.random() < 0.5) ? 0 : 1;
                if (x == 1) {
                    resultList.add(new Gene(parent1.get(i)));
                }
                i++;
            }
            while (j < getOrganisms().get(organism2).getGenomeSize()) {
                int x = (Math.random() < 0.5) ? 0 : 1;
                if (x == 1) {
                    resultList.add(new Gene(parent2.get(j)));
                }
                j++;
            }

        } /**
         * not equally fitted parents
         */
        else {
            if (getOrganisms().get(organism1).getFitness()> getOrganisms().get(organism2).getFitness()) {
                parent1 = getOrganisms().get(organism1).getGenome().getGenes(); //more fit parent
                parent2 = getOrganisms().get(organism2).getGenome().getGenes();
            } else if (getOrganisms().get(organism1).getFitness() < getOrganisms().get(organism2).getFitness()) {
                parent1 = getOrganisms().get(organism2).getGenome().getGenes();
                parent2 = getOrganisms().get(organism1).getGenome().getGenes();
            }
            int i = 0, j = 0;
            while (i < parent1.size() && j < parent2.size()) {
                if (parent1.get(i).getInnovation_num() == parent2.get(j).getInnovation_num()) {
                    resultList.add(matchingGeneSelect_firstMethod(parent1.get(i), parent2.get(j)));
                    i++;
                    j++;
                } else if (parent1.get(i).getInnovation_num() < parent2.get(j).getInnovation_num()) {
                    resultList.add(new Gene(parent1.get(i)));
                    i++;
                } else {
                    j++;
                }
            }
            while (i < parent1.size()) {
                resultList.add(new Gene(parent1.get(i)));
                i++;
            }
        }

        // Disabled gen can -> enabled
        for (int i = 0; i < resultList.size(); i++) {
            if (Math.random() < NeatClass.p_mutate_gene_reenable_prob) {
                if (!resultList.get(i).isEnable()) {
                    resultList.get(i).setEnable(true);
                    // it was disable, it was deleted from input and output Nodes
                    // need to be added
                    addInOutLink(resultList.get(i));
                }
            }
        }

        Organism offspring = fabrica.createOrganism(resultList);
        return (offspring);
    }

    /**
     * for Crossover make average new gene from 2 parental genes
     *
     * @param par1 gene Parent1
     * @param par2 gene Parent2
     * @return
     */
    private Gene matchingGeneSelect_firstMethod(Gene par1, Gene par2) {
        double averWeigth = (par1.getLink().getWeight() + par2.getLink().getWeight()) / 2;
        int x = (Math.random() < 0.5) ? 0 : 1;
        if (x == 0) {
            Link copyLink = new Link(averWeigth, par1.getLink());
            Gene g = new Gene(copyLink, par1.getInnovation_num(), par1.isEnable());
            return g;
        } else {
            Link copyLink = new Link(averWeigth, par2.getLink());
            Gene g = new Gene(copyLink, par2.getInnovation_num(), par2.isEnable());
            return g;
        }
    }

    private Gene matchingGeneSelect_secondMethod(Gene par1, Gene par2) {
        int x = (Math.random() < 0.5) ? 0 : 1;
        if (x == 0) {
            Link copyLink = new Link(par1.getLink().getWeight(), par1.getLink());
            Gene g = new Gene(copyLink, par1.getInnovation_num(), par1.isEnable());
            return g;
        } else {
            Link copyLink = new Link(par2.getLink().getWeight(), par2.getLink());
            Gene g = new Gene(copyLink, par2.getInnovation_num(), par2.isEnable());
            return g;
        }
    }

    //TODO второй тип кроссовера!!!!
    private void findAverageFitness() {
        //TODO
    }

    private void sortByFitness() {
        Collections.sort(getOrganisms(), new Comparator<Organism>() {
            @Override
            public int compare(Organism o1, Organism o2) {
                return o2.getFitness() > o1.getFitness() ? 1
                        : o2.getFitness() < o1.getFitness() ? -1 : 0;
            }
        });
    }

    private double findMedianFitness() {
        //before calculate fitness for all organisms
        /*for (int i = 0; i < organisms.size(); i++) {
         organisms.get(i).countFitnessOut();
         }
         //*/
        sortByFitness();
        
        double length = getOrganisms().size();
        double median;
        
        if (length % 2 == 0) {
            median = (getOrganisms().get((int)length/2).getFitness() + 
                    getOrganisms().get((int)length/2 - 1).getFitness()) / 2;
        } else {
            median = getOrganisms().get((int)length/2).getFitness();
        }

        return median;
    }

    public List<Organism> crossover(int numForAverageFitness) throws Exception {

        //find number of parents
        int N = this.getOrganisms().size();
        int nParents = (N % 2 == 0) ? N / 2 : N / 2 + 1;
        double mediane = this.findMedianFitness();

        int counter = 0;
        List<Integer> parents = new ArrayList<Integer>();
        while (counter < nParents) {
            int Min = 0, Max = N - 1;
            int randomIndex = Min + (int) (Math.random() * ((Max - Min) + 1));
            if (this.getOrganisms().get(randomIndex).getFitness() >= mediane) {
                if (parents.isEmpty()) {
                    parents.add(randomIndex);
                    counter++;
                } else if (parents.get(parents.size() - 1) != randomIndex) {
                    parents.add(randomIndex);
                    counter++;
                }

            }
        }
        List<Organism> children = new ArrayList<Organism>();
        for (int i = 0; i < parents.size() - 1; i++) {
            Organism offspring = this.makeOffspring(parents.get(i), parents.get(i + 1));
            //i++;
            offspring.countFitnessOut(numForAverageFitness);
            children.add(offspring);
        }
        return children;
    }

    public boolean mutateOrganism(Organism organism4mutation) { // before crossover
        boolean changed = false;
        int N = organism4mutation.getGenomeSize();
        for (int i = 0; i < N; i++) {
            double rand = Math.random();
            if (rand < NeatClass.p_mutate_add_node) { // p_mutate_add_node < p_mutate_add_link!!!
                organism4mutation.getGenome().mutate_addNode(organism4mutation.getGenome().getGenes().get(i).getInnovation_num());
                changed = true;
            } else if (rand < NeatClass.p_mutate_add_link) {
                // get second random Nodes in Network
                if (organism4mutation.getNet().getNumberHidden() != 0) {
                    organism4mutation.refreshNet();
                    Node in = organism4mutation.getGenome().getGenes().get(i).getLink().getIn_node();
                    if (in.getType() != NodeType.OUTPUT) {
                        Node out = organism4mutation.getNet().selectRandomNode(in);
                        if (out.getNodeID() != in.getNodeID()) {
                            organism4mutation.getGenome().mutate_addConnection(in, out, true);
                            changed = true;
                        }
                    }
                }
            }
        }
        //refresh Network in organism
        organism4mutation.refreshNet();
        return (changed);
    }

    public List<Organism> mutation(int numForAverageFitness) throws Exception {
        List<Organism> mutated = new ArrayList<Organism>();
        int N = this.getNumberOrganisms();
        for (int i = 0; i < N; i++) {
            OrganismFactory fabrica = new OrganismFactory();
            Organism organism4mutation = fabrica.dublicate(getOrganisms().get(i));
            boolean changed = mutateOrganism(organism4mutation);
            if (changed) { // if organism was changed => add in population
                organism4mutation.countFitnessOut(numForAverageFitness);
                mutated.add(organism4mutation);
            }
        }
        return mutated;
    }

    public void removeOrganism(Organism org) {
        getOrganisms().remove(org);
        
        if (getOrganisms().isEmpty()) {
            System.out.println("Species is empty (after remove organism)");
            //System.out.println(this.toString());
        }
    }

    private void addInOutLink(Gene get) {
        Link thisLink = get.getLink();
        get.getLink().getIn_node().addOutgoingLink(thisLink);
        get.getLink().getOut_node().addIncomingLink(thisLink);
    }

    public void changeWeigthBP() {
        for (int i = 0; i < getOrganisms().size(); i++) {
            getOrganisms().get(i).getNet().ajustmentWeigth();
        }
    }
    
    /**
     * @return the organisms
     */
    public List<Organism> getOrganisms() {
        return organisms;
    }

    /**
     * @return the max_fitness_organism
     */
    public int getMax_fitness_organism() {
        return max_fitness_organism;
    }

    /**
     * @return the max_fitness_organism_ever
     */
    public double getMax_fitness_organism_ever() {
        return max_fitness_organism_ever;
    }

}
