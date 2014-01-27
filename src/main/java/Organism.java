
/**
 * Organisms are Genomes and Networks with fitness information i.e. The genotype
 * and phenotype together
 */
public class Organism{

    /**
     * A measure of fitness for the Organism
     */
    private double fitness;
    private double orig_fitness;
    /**
     * Used just for reporting purposes
     */
    double error;
    /**
     * Number of children this Organism may have
     */
    double expected_offspring;
    /**
     * Tells which generation this Organism is from
     */
    int generation;
    /**
     * The Organism's phenotype
     */
    private Network net;
    /**
     * The Organism's genotype
     */
    public Genome genome;
    /**
     * The Organism's Species
     */
    Species species;

    public Organism(double xfitness, Genome xgenome, int xgeneration) {
        fitness = xfitness;
        orig_fitness = xfitness;
        genome = xgenome;
        net = new Network(xgenome);
        species = null;
        expected_offspring = 0;
        generation = xgeneration;
        error = 0;
    }
    
    public Organism() {
        
    }

    public Organism(Genome xgenome) {
        genome = xgenome;
        this.net = new Network(xgenome);
        species = null;
        expected_offspring = 0;
        error = 0;
    }

    public Organism(Genome xgenome, Network net) {
        genome = xgenome;
        this.net = net;
        species = null;
        expected_offspring = 0;
        error = 0;
    }

    public int getLastGeneInovNumber() {
        return (this.getGenome().lastGeneInovNumber());
    }

    public int getGenomeSize() {
        return (this.getGenome().getNumberGenes());
    }

    public int getNumberDisjoint(Organism organism2) {
        int D = 0;
        int maxNumberShortGenome, counterInnovNumber, i = 0, j = 0;
        if (organism2.getLastGeneInovNumber() > this.getLastGeneInovNumber()) { //this - short genome
            maxNumberShortGenome = this.getGenome().lastGeneInovNumber();
        } else { //organism2 - short genome
            maxNumberShortGenome = organism2.getGenome().lastGeneInovNumber();
        }
        if (organism2.getGenome().getGenes().get(0).getInnovation_num() > this.getGenome().getGenes().get(0).getInnovation_num()) {
            counterInnovNumber = this.getGenome().getGenes().get(0).getInnovation_num();
        } else {
            counterInnovNumber = organism2.getGenome().getGenes().get(0).getInnovation_num();
        }


        while (counterInnovNumber < maxNumberShortGenome) {
            counterInnovNumber = organism2.getGenome().getGenes().get(i).getInnovation_num();
            if (organism2.getGenome().getGenes().get(i).getInnovation_num() > this.getGenome().getGenes().get(j).getInnovation_num()) {
                D++;
                counterInnovNumber = this.getGenome().getGenes().get(j).getInnovation_num();
                i--;
            } else if (organism2.getGenome().getGenes().get(i).getInnovation_num() < this.getGenome().getGenes().get(j).getInnovation_num()) {
                D++;
                counterInnovNumber = organism2.getGenome().getGenes().get(i).getInnovation_num();
                j--;
            }
            i++;
            j++;
        }
        return (D);
    }

    public int getNumberExcess(Organism organism2) {
        int E = 0;
        int maxNumberShortGenome;
        if (organism2.getLastGeneInovNumber() > this.getLastGeneInovNumber()) { //this - short genome
            maxNumberShortGenome = this.getGenome().lastGeneInovNumber();
            for (int i = 0; i < organism2.getGenomeSize(); i++) {
                if (organism2.getGenome().getGenes().get(i).getInnovation_num() > maxNumberShortGenome) {
                    E++;
                }
            }
        }
        if (organism2.getLastGeneInovNumber() < this.getLastGeneInovNumber()) { //organism2 - short genome
            maxNumberShortGenome = organism2.getGenome().lastGeneInovNumber();
            for (int i = 0; i < this.getGenomeSize(); i++) {
                if (this.getGenome().getGenes().get(i).getInnovation_num() > maxNumberShortGenome) {
                    E++;
                }
            }
        }
        //if == -> E=0
        return (E);
    }

    public double averageWeigthDiff(Organism org) {
        double W = 0.0;
        int i = 0, j = 0, count = 0;
        while (i < this.getGenomeSize() && j < org.getGenomeSize()) {
            if (this.getGenome().getGenes().get(i).getInnovation_num() == org.getGenome().getGenes().get(j).getInnovation_num()) {
                W += Math.abs(this.getGenome().getGenes().get(i).getLink().getWeight() - org.getGenome().getGenes().get(j).getLink().getWeight());
                count++;
                i++;
                j++;
            } else if (this.getGenome().getGenes().get(i).getInnovation_num() < org.getGenome().getGenes().get(j).getInnovation_num()) {
                i++;
            } else {
                j++;
            }
        }
        return (W / count);
    }

    public void countFitnessOut() {
        getNet().changeOuter();
       this.setFitness(getNet().getError4Fitness());
    }
    
    
    
    /* the distance between two network encodings can be measured as a linear combination of the number of excess
     (E) and disjoint (D) genes, as well as the average weight differences of matching genes (W) */
    public double countDistanceDelta(Organism organism2) {
        double delta;
        int E = this.getNumberExcess(organism2);
        int D = this.getNumberDisjoint(organism2);
        double W = this.averageWeigthDiff(organism2);
        int N;
        if (this.getGenome().getNumberGenes() >= organism2.getGenome().getNumberGenes()) {
            N = this.getGenome().getNumberGenes();
        } else {
            N = organism2.getGenome().getNumberGenes();
        }
        delta = NeatClass.p_excess_coeff * E / N + NeatClass.p_disjoint_coeff * D / N + NeatClass.p_mutdiff_coeff * W;
        return (delta);
    }
    
    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    /**
     * @return the orig_fitness
     */
    public double getOrig_fitness() {
        return orig_fitness;
    }

    /**
     * @param orig_fitness the orig_fitness to set
     */
    public void setOrig_fitness(double orig_fitness) {
        this.orig_fitness = orig_fitness;
    }

    /**
     * @return the fitness
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * @param fitness the fitness to set
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * @return the net
     */
    public Network getNet() {
        return net;
    }
    
}
