
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
    public static double p_GAerror_threshold = 0.1;
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
    public static double p_training_coefficient= 0.15;
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

    public List< List<Double>> backPropagation() throws Exception {
        List< List<Double>> gError = new ArrayList< List<Double>>();
        //for 3 sambples
        gError.add(new ArrayList<Double>());
        gError.add(new ArrayList<Double>());
        gError.add(new ArrayList<Double>());

        List< List<Double>> curErrorCurPatient = new ArrayList< List<Double>>();
        //for 3 sambples
        curErrorCurPatient.add(new ArrayList<Double>());
        curErrorCurPatient.add(new ArrayList<Double>());
        curErrorCurPatient.add(new ArrayList<Double>());

        /**
         * Прогонять выборку надо несколько эпох. Чем больше номер генерации ГА,
         * тем больше эпох.
         */
        int numIterations = population.getGenerationNumber() * NeatClass.p_coef_multipl_epoch;
        if (numIterations > NeatClass.p_BP_max_iterations) {
            numIterations = NeatClass.p_BP_max_iterations;
        }
        for (int e = 0; e < numIterations; e++) {
            //take sous - Datas
            for (int i = 0; i < samples.size(); i++) {
                int counter = 0;
                for (int j = 0; j < samples.get(i).size(); j++) {
                    counter++;
                    // for every organism in population
                    for (int s = 0; s < population.getSpeciesNumber(); s++) {
                        for (Organism organ : population.getSpecies().get(s).getOrganisms()) {

                            //count fitness and error for every data line
                            List<Double> outNodes = new ArrayList<Double>();
                            outNodes.add(answers.get(samples.get(i).get(j)));
                            organ.setInputData(datas.get(samples.get(i).get(j)), outNodes);
                            organ.countFitnessOut(counter);

                            //change weigths
                            organ.getNet().ajustmentWeigth();
                        }
                    }
                    curErrorCurPatient.get(i).add(population.getAllOrganAverageFitness());
                }
            }
            
            /**
             * find best organism in the species
             */
            population.bestOrganisms();
            
            //count average in curErrorCurPatient for total gError
            for (int c = 0; c < curErrorCurPatient.size(); c++) {
                double sum = 0.0;
                for (int j = 0; j < curErrorCurPatient.get(c).size(); j++) {
                    sum += curErrorCurPatient.get(c).get(j);
                }
                gError.get(c).add(sum / curErrorCurPatient.get(c).size());
                curErrorCurPatient.get(c).clear();
            }
        }

        return gError;
    }

    public List< List<Double>> NEATalgorithm() throws Exception {
        int iterationCounter = 0;
        List< List<Double>> gError = new ArrayList< List<Double>>();
        gError.add(new ArrayList<Double>());
        gError.add(new ArrayList<Double>());
        gError.add(new ArrayList<Double>());

        //count Errors with initial population (in -> out)
        countBP_StepError_last(gError);
      
        do {
            //genetic algorithm step
            population.GAstep_mutation();
            countBP_StepError_last(gError);
            population.GAstep_selection();
            population.nextGenerationNumber();

            population.GAstep_crossover();
            countBP_StepError_last(gError);
            population.GAstep_selection();
            population.nextGenerationNumber();
            
            iterationCounter++;
            System.out.println("Iteration - " + iterationCounter);
        } while (iterationCounter < AppProperties.iterationCount());

        return gError;
    }
    
    /**
     * @param gError
     * @throws Exception 
     * count average error throw iterations
     */
    private void countBP_StepError_average(List<List<Double>> gError) throws Exception {
        List< List<Double>> localError = new ArrayList<List<Double>>();
        localError = backPropagation();

        for (int i = 0; i < localError.size(); i++) {
            double sum = 0.0;
            for (int j = 0; j < localError.get(i).size(); j++) {
                sum += localError.get(i).get(j);
            }
            gError.get(i).add(sum / localError.get(i).size());
        }
    }
    
    private void countBP_StepError_last(List<List<Double>> gError) throws Exception {
        List< List<Double>> localError = new ArrayList<List<Double>>();
        localError = backPropagation();

        for (int i = 0; i < localError.size(); i++) {
            gError.get(i).add(localError.get(i).get(localError.get(i).size()-1));
        }
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
