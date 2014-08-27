
import java.util.ArrayList;
import java.util.List;

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
     * * factor multiply weight difference 0.4 - 3.0
     */
    public static double p_mutdiff_coeff = 3.0; //c3
    /**
     * * threshold under which two Genomes are the same species
     */
    public static double p_compat_threshold = 2.0;
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
    public static int p_pop_size = 50;
    /**
     * * Age where Species starts to be penalized (15)
     */
    public static int p_dropoff_age;
    /**
     * параметр наклона сигмоидальной функции
     */
    public static double p_alpha_activationFunction = 1.0;
    /**
     * Probability of switch status to ena of gene 75% chance that an inherited
     * gene was disabled if it was disable in either parent
     */
    public static double p_mutate_gene_reenable_prob = 0.15;
    /**
     * the probability of adding a new node was 0.03
     */
    public static double p_mutate_add_node = 0.01;
    /**
     * and the probability of a new link mutation 0.05 - 0.3
     */
    public static double p_mutate_add_link = 0.05;
    /**
     * Probability of a mate being outside species
     */
    public static double p_interspecies_mate_rate = 0.001;
    /**
     * threshold of error for stop-criterion of GA
     */
    public static double p_GAerror_threshold = 0.01;
    /**
     * coeffisient of multiplication of epoch number depends on generation
     * number
     */
    public static int p_coef_multipl_epoch = 10;
    /**
     * maximum iterations for GA step
     */
    public static int p_GA_max_iterations = 5;

    //**************************************************************************
    /**
     * training, testing and validation pattern's parts
     */
    public static double p_train_pattern = 0.7;
    public static double p_test_pattern = 0.2;
    public static double p_valid_pattern = 0.7;
    /**
     * training coefficient for back propagandation
     */
    public static double p_training_coefficient = 0.15;
    /**
     * iterations limit
     */
    public static int p_BP_max_iterations = 1000;
    //**************************************************************************
    private List< List<Double>> datas = new ArrayList<List<Double>>();
    private final List<Double> answers = new ArrayList<Double>();
    private final List<List<Integer>> samples = new ArrayList<List<Integer>>();
    private Population population = new Population();

    //**************************************************************************
    public void readInputDataFromExcel(String excelPath, int answerColNumber) {
        ExcelReader reader = new ExcelReader();
        reader.loadFile(excelPath);
        datas = reader.readData();
        for (List<Double> row : datas) {
            answers.add(row.get(answerColNumber - 1));
            row.remove(answerColNumber - 1);
        }
        //p_pop_size = datas.size();
    }

    public void normalization() {
        //for each col in data
        for (int i = 0; i < datas.get(0).size(); i++) {
            //find max and min
            double max = datas.get(0).get(i);
            double min = max;
            for (int j = 0; j < datas.size(); j++) {
                if (datas.get(j).get(i) > max) {
                    max = datas.get(j).get(i);
                }
                if (datas.get(j).get(i) < min) {
                    min = datas.get(j).get(i);
                }
            }
            // Normalization is done by diving each component of the input vector by the vector length
            for (int j = 0; j < datas.size(); j++) {
                datas.get(j).set(i, (2 * datas.get(j).get(i) - (max + min)) / (max - min));
            }
        }
    }

    public void makeThreeSamplesRandomized() {
        samples.clear();
        samples.add(new ArrayList<Integer>());
        samples.add(new ArrayList<Integer>());
        samples.add(new ArrayList<Integer>());

        int countInitialSample = datas.size();
        int countTrainingSample = (int) (countInitialSample * p_train_pattern);
        int countTestSample = (int) (countInitialSample * p_test_pattern);

        List<Integer> allNumbers = new ArrayList<Integer>();
        for (int i = 0; i < countInitialSample; i++) {
            allNumbers.add(i);
        }

        int restInInitialSample = countInitialSample;
        for (int i = 0; i < countTrainingSample; i++) {
            int rand = (int) (Math.random() * restInInitialSample);
            samples.get(0).add(allNumbers.remove(rand));
            restInInitialSample--;
        }
        for (int i = 0; i < countTestSample; i++) {
            int rand = (int) (Math.random() * restInInitialSample);
            samples.get(1).add(allNumbers.remove(rand));
            restInInitialSample--;
        }
        for (int i = 0; i < allNumbers.size(); i++) {
            samples.get(2).add(allNumbers.get(i));
        }
    }

    public void buildPopulation() {
        OrganismFactory.resetIndexes();
        //create Population
        population.initialPopulation(datas, answers);
    }

    /**
     * @return backPropagandation, return error for the best organisms in
     * Generation
     */
    public List< List<Double>> backPropagation(List<Organism> chromosomes) throws Exception {
        List< List<Double>> bestOrgan = new ArrayList< List<Double>>();
        //for 3 samples
        bestOrgan.add(new ArrayList<Double>());
        bestOrgan.add(new ArrayList<Double>());
        bestOrgan.add(new ArrayList<Double>());

        List< List<Double>> curErrorBestOrgan = new ArrayList< List<Double>>();
        curErrorBestOrgan.add(new ArrayList<Double>());
        curErrorBestOrgan.add(new ArrayList<Double>());
        curErrorBestOrgan.add(new ArrayList<Double>());

        /**
         * Прогонять выборку надо несколько эпох. Чем больше номер генерации ГА,
         * тем больше эпох.
         */
        int numIterations = population.getGenerationNumber() * NeatClass.p_coef_multipl_epoch;
        if (numIterations > NeatClass.p_BP_max_iterations) {
            numIterations = NeatClass.p_BP_max_iterations;
        }

        // for every organism in population (at the begining)
        // and for every chromosome on the next steps
        for (Organism organ : chromosomes) {
            for (int e = 0; e < numIterations; e++) {
                //take sous - Datas
                for (int i = 0; i < samples.size(); i++) {
                    double curErr = 0;
                    for (int j = 0; j < samples.get(i).size(); j++) {
                        //count fitness and error for every data line
                        List<Double> outNodes = new ArrayList<Double>();
                        outNodes.add(answers.get(samples.get(i).get(j)));
                        organ.setInputData(datas.get(samples.get(i).get(j)), outNodes);
                        // count fitness only for testing sample
                        if (i == 1) { organ.countFitnessOut(j+1);} 
                        //change weigths
                        organ.getNet().ajustmentWeigth();
                        curErr += organ.getError();
                    }
                    //error = avarege for all patients in this sample
                    curErrorBestOrgan.get(i).add(curErr/samples.get(i).size());
                }
            }
            if (bestOrgan.get(0).isEmpty()) { bestOrgan = curErrorBestOrgan; }
            if (curErrorBestOrgan.get(0).get(curErrorBestOrgan.get(0).size() - 1) < bestOrgan.get(0).get(bestOrgan.get(0).size() - 1)) {
                bestOrgan = curErrorBestOrgan;
            }
        }
        
        /**
         * find best organism in population
         */
        population.setBestOrganism();

        return bestOrgan;
    }

    public List<GraphDate> NEATalgorithm() throws Exception {
        int iterationCounter = 0;
        
        List<GraphDate> fGraphs = new ArrayList<GraphDate>();

        // create list of all organisms
        List<Organism> allOrg = new ArrayList<Organism>();
        for (int i = 0; i < population.getSpeciesNumber(); i++) {
                allOrg.addAll(population.getSpecies().get(i).getOrganisms());
        }
        //count Errors with initial population (in -> out)
        fGraphs.add(countBP_dataGraph(allOrg));

        do {
            //genetic algorithm step
            List<Organism> mutatedOrg = new ArrayList<Organism>();
            mutatedOrg = population.GAstep_mutation();
            if(!mutatedOrg.isEmpty()) {fGraphs.add(countBP_dataGraph(mutatedOrg));}
            population.GAstep_selection();
            population.nextGenerationNumber();

            List<Organism> childOrg = new ArrayList<Organism>();
            population.GAstep_crossover();
            if(!childOrg.isEmpty()) {fGraphs.add(countBP_dataGraph(childOrg));}
            population.GAstep_selection();
            population.nextGenerationNumber();

            iterationCounter++;
            System.out.println("Iteration: " + iterationCounter);
            
            System.out.println("Number of species in population: " + population.getSpeciesNumber());
            
            /*stop criteria : Критерий окончания работы ГА - 
            ошибка обобщения для лучшей сети (хромосомы в популяции) 
            станет меньше, чем некоторый уровень. */
            /*if (population.getBestOrganFitness() < p_GAerror_threshold) {
                break;
            }*/
            int size = fGraphs.size()-1;
            int last = fGraphs.get(size).getSize_inSample() - 1;
            if(fGraphs.get(size).getData(0,last) < p_GAerror_threshold & fGraphs.get(size).getData(2,last) < p_GAerror_threshold & fGraphs.get(size).getData(0,last) < p_GAerror_threshold) {
                break;
            }
            
        } while (iterationCounter < AppProperties.iterationCount());
        
        return fGraphs;
    }

    /**
     * @param gError count error of last network(organism) in generation throw
     * iterations
     */
    private GraphDate countBP_dataGraph(List<Organism> org) throws Exception {
        List< List<Double>> localError = new ArrayList<List<Double>>();
        localError = backPropagation(org);

        //for graph graph
        int numIterations = population.getGenerationNumber() * NeatClass.p_coef_multipl_epoch;
        GraphDate gData = new GraphDate(localError, numIterations);
        return gData;
    }

    public int getNumberCharacteristics() {
        return datas.get(0).size();
    }

    public int getNumberAnswersCol() {
        return answers.size();
    }

    public int getNumberSubjects() {
        return datas.size();
    }

    public int getNumberTrainingSample() {
        return samples.get(0).size();
    }
}
