
import java.util.ArrayList;
import java.util.List;

/**
 * Organisms are Genomes and Networks with fitness information i.e. The genotype
 * and phenotype together
 */
public class Organism {

    private int organism_id;
    /**
     * A measure of fitness for the Organism
     */
    //private double average! fitness;
    private double fitness = 0.0;
    /**
     * Used just for reporting purposes
     */
    private double error; // fitness = error
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
    /**
     * averaged error on testing sample afret BP
     */
    private List< List<Double>> averErrors = new ArrayList< List<Double>>();

    public Organism() {
    }

    public Organism(Genome xgenome) {
        genome = xgenome;
        this.net = new Network(xgenome);
        species = null;
        expected_offspring = 0;
        error = 0;
        organism_id = genome.getGenome_id();
    }

    public Organism(Genome xgenome, Network net) {
        genome = xgenome;
        this.net = net;
        species = null;
        expected_offspring = 0;
        error = 0;
        organism_id = genome.getGenome_id();
    }

    public void setInputData(List<Double> inNodes, List<Double> outNodes) {
        this.getNet().setInputData(inNodes, outNodes);
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
        int i = 0, j = 0;
        double count = 0.0;
        while (i < this.getGenomeSize() && j < org.getGenomeSize()) {
            if (this.getGenome().getGenes().get(i).getInnovation_num() == org.getGenome().getGenes().get(j).getInnovation_num()) {
                W += Math.abs(this.getGenome().getGenes().get(i).getLink().getWeight()
                        - org.getGenome().getGenes().get(j).getLink().getWeight());
                count += 1.0;
                i++;
                j++;
            } else if (this.getGenome().getGenes().get(i).getInnovation_num() < org.getGenome().getGenes().get(j).getInnovation_num()) {
                i++;
            } else {
                j++;
            }
        }
        if (count == 0.0) {
            return 0.0;
        }
        return (W / count);
    }

    /**
     * Count Errors on Training and validation samples
     * @throws Exception 
     */
    public void countFitnessOut() throws Exception {
        this.getNet().changeOuter();
        this.setError(getNet().getError4Organism());
    }
    
    /**
     * Count Errors & Fitness on Testing sample
     * @param nPatient4averageFitness
     * @throws Exception 
     */
    public void countFitnessOut(int nPatient4averageFitness) throws Exception {
        this.getNet().changeOuter();
        this.setError(getNet().getError4Organism());
        //this.setFitness(getNet().getOut4Organism());
        this.setFitness(getError(), nPatient4averageFitness);
    }

    /*
     * The number of excess and disjoint genes between a pair of genomes is a natural
     measure of their compatibility distance. The more disjoint two genomes are, the less
     evolutionary history they share, and thus the less compatible they are.
     */
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

    public void refreshNet() {
        this.setNet(new Network(this.getGenome()));
    }

    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    public double getFitness() {
        return fitness;
    }

    /**
     * Average fitness = average error on all sample (after BP)
     *
     * @param next_fitness
     * @param iteration
     */
    public void setFitness(double next_fitness, int iteration) {
        if (iteration == 1) {
            this.fitness = 0.0;
        }
        this.fitness = ((iteration - 1) * fitness + next_fitness) / iteration;
    }

    void setFitness(double newFitness) {
        this.fitness = newFitness;
    }

    /**
     * @return the net
     */
    public Network getNet() {
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(Network net) {
        this.net = net;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.getOrganism_id();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Organism) {
            return this.getOrganism_id() == ((Organism) obj).getOrganism_id();
        }
        return false;
    }

    /**
     * @return the organism_id
     */
    public int getOrganism_id() {
        return organism_id;
    }

    /**
     * @return the error
     */
    public double getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(double error) {
        this.error = error;
    }

    /**
     * @return the averErrors
     */
    public List< List<Double>> getAverErrors() {
        return averErrors;
    }

    /**
     * @param l_averErrors the averErrors to set
     */
    public void setAverErrors(List< List<Double>> l_averErrors) {
        if(!this.averErrors.isEmpty()) {this.averErrors.clear();}
        /*this.averErrors.add(new ArrayList<Double>()); //trainig
        this.averErrors.add(new ArrayList<Double>()); //testing
        this.averErrors.add(new ArrayList<Double>()); //validation*/
        this.averErrors = l_averErrors;
    }
}
