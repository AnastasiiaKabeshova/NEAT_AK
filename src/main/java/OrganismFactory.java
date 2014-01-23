
import java.util.ArrayList;
import java.util.List;

public class OrganismFactory {

    private static int enabledNodeID = 1;
    private static int enabledGenomeID = 1;
    private static int enabledInnovetionNumber = 1;

    public static int nextEnabeledGenomeID() {
        return enabledGenomeID++;
    }

    public static int nextEnabeledInnovetionNumber() {
        return enabledInnovetionNumber++;
    }

    public static void resetIndexes() {
        enabledNodeID = 1;
        enabledInnovetionNumber = 1;
        enabledGenomeID = 1;
    }

    public void setEnabeledInnovetionNumber(int num) {
        enabledInnovetionNumber = num;
    }

    public static int nextEnabledNodeID() {
        return enabledNodeID++;
    }

    public Organism createOrganism(List<Double> inNodes, List<Double> outNodes) {
        //create initial genome in -> out
        List<Gene> genes = new ArrayList<Gene>();
        Network net = new Network();
        for (int i = 0; i < outNodes.size(); i++) {
            Node newOutNode = new Node(outNodes.get(i), NodeType.OUTPUT);
            newOutNode.setNodeID(nextEnabledNodeID());
            for (int j = 0; j < inNodes.size(); j++) {
                Node newInNode = new Node(inNodes.get(j), NodeType.INPUT);
                newInNode.setNodeID(nextEnabledNodeID());
                net.addInNode(newInNode);

                Link newLink = new Link(newInNode, newOutNode);  // newOutNode.addIncomingLink(newLink); - Link constructior do this
                genes.add(new Gene(newLink, nextEnabeledInnovetionNumber(), true));
            }
            net.addOutNode(newOutNode);
        }

        Genome newGenome = new Genome(nextEnabeledGenomeID(), genes);
        return (new Organism(newGenome, net));
    }

    public Organism createOrganism(List<Gene> genes) {
        Genome newGenome = new Genome(nextEnabeledGenomeID(), genes);
        return (new Organism(newGenome));
    }

    public Organism dublicate(Organism oldOrganism) {
        Organism newOrganism = new Organism();
        newOrganism = oldOrganism;
        newOrganism.getGenome().setGenome_id(nextEnabeledGenomeID());
        return (newOrganism);
    }
}
