
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
public class Species extends NeatClass {
    /* list of all organisms in the Species*/
    public List<Organism> organisms;
    /* The average fitness of the Species*/
    double ave_fitness;
    /* Max fitness of the Species*/
    double max_fitness;
    /* The max it ever had*/
    double max_fitness_ever;

    /*
     * add an organism to list of organisms in this specie
     */
    public void addOrganism(Organism newOrganism) {
        organisms.add(newOrganism);
    }
    
    public int getNumberOrganisms () {
        return organisms.size();
    }
    
    public Organism getOrganism(int index) {
        return organisms.get(index);
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

        if (organisms.get(organism1).getFitness() == organisms.get(organism2).getFitness()) { //equal fitness
            parent1 = organisms.get(organism1).getGenome().getGenes();
            parent2 = organisms.get(organism2).getGenome().getGenes();
            int i = 0, j = 0;
            while (i < organisms.get(organism1).getGenomeSize() && j < organisms.get(organism2).getGenomeSize()) {
                if (parent1.get(i).getInnovation_num() == parent2.get(j).getInnovation_num()) {
                    int x = (Math.random() < 0.5) ? 0 : 1;
                    if (x == 0) {
                        resultList.add(parent1.get(i));
                    } else {
                        resultList.add(parent2.get(i));
                    }
                    i++;
                    j++;
                } else if (parent1.get(i).getInnovation_num() < parent2.get(j).getInnovation_num()) {
                    int x = (Math.random() < 0.5) ? 0 : 1;
                    if (x == 1) {
                        resultList.add(parent1.get(i));
                    }
                    i++;
                } else {
                    int x = (Math.random() < 0.5) ? 0 : 1;
                    if (x == 1) {
                        resultList.add(parent2.get(j));
                    }
                    j++;
                }
            }
            while (i < organisms.get(organism1).getGenomeSize()) {
                int x = (Math.random() < 0.5) ? 0 : 1;
                if (x == 1) {
                    resultList.add(parent1.get(i));
                }
                i++;
            }
            while (j < organisms.get(organism2).getGenomeSize()) {
                int x = (Math.random() < 0.5) ? 0 : 1;
                if (x == 1) {
                    resultList.add(parent2.get(j));
                }
                j++;
            }

        } else {
            if (organisms.get(organism1).getFitness() > organisms.get(organism2).getFitness()) {
                parent1 = organisms.get(organism1).getGenome().getGenes(); //more fit parent
                parent2 = organisms.get(organism2).getGenome().getGenes();
            } else if (organisms.get(organism1).getFitness() > organisms.get(organism2).getFitness()) { //equal fitness
                parent1 = organisms.get(organism1).getGenome().getGenes();
                parent2 = organisms.get(organism2).getGenome().getGenes();
            }
            int i = 0, j = 0;
            while (i < organisms.get(organism1).getGenomeSize() && j < organisms.get(organism2).getGenomeSize()) {
                if (parent1.get(i).getInnovation_num() == parent2.get(j).getInnovation_num()) {
                    int x = (Math.random() < 0.5) ? 0 : 1;
                    if (x == 0) {
                        resultList.add(parent1.get(i));
                    } else {
                        resultList.add(parent2.get(i));
                    }
                    i++;
                    j++;
                } else if (parent1.get(i).getInnovation_num() < parent2.get(j).getInnovation_num()) {
                    resultList.add(parent1.get(i));
                    i++;
                } else {
                    j++;
                }
            }
            while (i < organisms.get(organism1).getGenomeSize()) {
                resultList.add(parent1.get(i));
                i++;
            }
        }

        // Disabled gen can -> enabled
        for (int i = 0; i < resultList.size(); i++) {
            double rand = Math.random();
            if (rand > NeatClass.p_mutate_gene_reenable_prob) {
                resultList.get(i).setEnable(true);
            }
        }

        Organism offspring = fabrica.createOrganism(resultList);
        return (offspring);
    }

    //TODO второй тип кроссовера!!!!
    

    private void findAverageFitness() {
        //TODO
    }

    private double findMedianFitness() {
        Collections.sort(organisms, new Comparator<Organism>() {
            @Override
            public int compare(Organism o1, Organism o2) {
                return o2.getFitness() > o1.getFitness() ? 1
                        : o2.getFitness() < o1.getFitness() ? -1 : 0;
            }
        });
        return organisms.get((int) (organisms.size() / 2) + 1).getFitness();
    }

    public void crossover() {
        //find number of parents
        int N = this.organisms.size();
        int nParents = (N % 2 == 0) ? N / 2 : N / 2 + 1;
        double mediane = this.findMedianFitness();

        int counter = 0;
        List<Integer> parents = new ArrayList<Integer>();
        while (counter <= nParents) {
            int Min = 0, Max = N - 1;
            int randomIndex = Min + (int) (Math.random() * ((Max - Min) + 1));
            if (this.organisms.get(randomIndex).getFitness() >= mediane) {
                parents.add(randomIndex);
                counter++;
            }
        }
        for (int i = 0; i < parents.size(); i++) {
            Organism offspring = this.makeOffspring(i, i + 1);
            i++;
            this.organisms.add(offspring);
        }
        //mutation
        //specification (speciate)
        //провести селекцию на уровне Популяции
        
        // обновить сеть в организме!!!
    }

    public void mutation(Organism offspring) {
        for (int i = 0; i < offspring.getGenomeSize(); i++) {
            if (Math.random() < NeatClass.p_mutate_add_node) { // p_mutate_add_node < p_mutate_add_link!!!
                offspring.getGenome().mutate_addNode(offspring.getGenome().getGenes().get(i).getInnovation_num());
            } else if(Math.random() < NeatClass.p_mutate_add_link) {
                // TODO ?????
                // offspring.getGenome().mutate_addConnection(null, null, true);
            }
        }
    }
}
