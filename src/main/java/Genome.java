
import java.util.List;

/**
 *
 * @author Anastasiia
 */
public class Genome {

    /**
     * Numeric identification for this genotype
     */
    private int genome_id;
    /**
     * Each Gene has a marker telling when it arose historically; Thus, these
     * Genes can be used to speciate the population, and the list of Genes
     * provide an evolutionary history of innovation and link-building
     */
    private List<Gene> genes;

    public Genome(int id, List<Gene> g) {
        genome_id = id;
        genes = g;
    }

    public int getGenome_id() {
        return genome_id;
    }

    public void setGenome_id(int genome_id) {
        this.genome_id = genome_id;
    }

    public List<Gene> getGenes() {
        return genes;
    }

    public void setGenes(List<Gene> genes) {
        this.genes = genes;
    }

    public int getNumberGenes() {
        return (this.genes.size());
    }

    public int lastGeneInovNumber() {
        return (this.genes.get(genes.size() - 1).getInnovation_num());
    }

    public Genome(int id, IOStorage xFile) {
        //TODO
    }

    public void print_to_file(IOStorage xFile) {
        //
        // write to file genome in native format (for re-read)
        //
    }

    public void mutate_addNode(int linkInnovNum) {
        int linkIndex = findNumberByInnovNumber(linkInnovNum);
        genes.get(linkIndex).setEnable(false);  // The old connection is disabled

        Node nodeIn = genes.get(linkIndex).getLink().getIn_node();
        Node nodeOut = genes.get(linkIndex).getLink().getOut_node();

        Node newNode = new Node(NodeType.HIDDEN);

        double weight1 = 1;
        double weight2 = genes.get(linkIndex).getLink().getWeight();

        Link newLink2 = new Link(weight1, newNode, nodeOut);
        Link newLink1 = new Link(weight2, nodeIn, newNode);

        int enableInnovNum1 = OrganismFactory.nextEnabeledInnovetionNumber();
        int enableInnovNum2 = OrganismFactory.nextEnabeledInnovetionNumber();
        boolean enable = true; //genes.get(linkIndex).isEnable();
        Gene newGene1 = new Gene(newLink1, enableInnovNum1, enable);
        Gene newGene2 = new Gene(newLink2, enableInnovNum2, enable);
        //two new connections are added to the genome
        genes.add(newGene1);
        genes.add(newGene2);
    }

    public void mutate_addConnection(Node nodeIn, Node nodeOut, boolean enable) {
        double weight = Math.random() - 0.5; //[-0.5; 0.5];
        Link newLink = new Link(weight, nodeIn, nodeOut);
        int enableInnovNum = OrganismFactory.nextEnabeledInnovetionNumber();
        Gene newGene = new Gene(newLink, enableInnovNum, enable);
        genes.add(newGene);
    }

    private void op_view() {
        // TODO!!
        System.out.print("\n GENOME START   id=" + genome_id);
        System.out.print("\n  genes are :" + genes.size());
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int findNumberByInnovNumber(int linkInnovNum) {
        int number = -1;
        for (int i = 0; i < this.getNumberGenes(); i++) {
            if (genes.get(i).getInnovation_num() == linkInnovNum) {
                number = i;
            }
        }
        return number;
    }
}
