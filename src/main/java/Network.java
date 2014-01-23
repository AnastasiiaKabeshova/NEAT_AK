
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class Network {

    /**
     * is a reference to genotype can has originate this fenotype
     */
    private List<Node> inNodes = new ArrayList<Node>();
    private List<Node> hiddenNodes = new ArrayList<Node>();
    private List<Node> outNodes = new ArrayList<Node>();

    public Network() {
    }

    public Network(Genome xgenome) {
        for (int i = 0; i < xgenome.getNumberGenes(); i++) {
            Link l = xgenome.getGenes().get(i).getLink();
            addNode4everyone(l.getOut_node());
            addNode4everyone(l.getIn_node());
        }
    }

    public void addInNode(Node h) {
        inNodes.add(h);
    }

    public void addOutNode(Node h) {
        h.setAnswer(h.getPotential());
        outNodes.add(h);
    }

    public void addHiddenNode(Node h) {
        hiddenNodes.add(h);
    }

    public void changeOuter() {
        Set<Node> doneNodes = new HashSet<Node>();
        doneNodes.addAll(inNodes);
        //output Nodes
        for (int i = 0; i < outNodes.size(); i++) {
            calculateNode(outNodes.get(i), doneNodes);
        }
    }

    private void calculateNode(Node n, Set<Node> doneNodes) {
        if (!doneNodes.contains(n)) {
            for (int i = 0; i < n.getIncomingLinksNumber(); i++) {
                calculateNode(n.getLink(i).getIn_node(), doneNodes);
            }
        }
        n.countOut();
        doneNodes.add(n);
    }

    public double getError4Fitness() {
        double error = 0.0;
        for (int i = 0; i < outNodes.size(); i++) {
            error += outNodes.get(i).error();
        }
        return error;
    }

    public void ajustmentWeigthAllLayers() {
        //errors and adjustment for output layer
        for (int i = 0; i < outNodes.size(); i++) {
            outNodes.get(i).ajErrorOutputLayer();
        }
        Set<Node> doneNodes = new HashSet<Node>();
        doneNodes.addAll(outNodes);
        //errors and adjustment for hidden layers
        for (int i = hiddenNodes.size(); i > 0; i--) {
            for (int j = 0; j < allLayers.get(i).getCount(); j++) {
                double sumChildren = 0;
                for (int t = 0; t < allLayers.get(i + 1).getCount(); t++) { // for all neurons on next layer
                    sumChildren += allLayers.get(i + 1).getNeuron(t).getMisalignment() * allLayers.get(i + 1).getNeuron(t).getLink(j).getWeight();
                }
                allLayers.get(i).getNeuron(j).ajErrorHiddenLayer(sumChildren);
            }
        }

        //Adjustment of synaptic weights on the layers
        for (int i = allLayers.size() - 1; i > 0; i--) {
            for (int j = 0; j < allLayers.get(i).getCount(); j++) {
                double lErr = allLayers.get(i).getNeuron(j).getMisalignment();
                for (int t = 0; t < allLayers.get(i).getNeuron(j).getLinkCount(); t++) {
                    double oldWeight = allLayers.get(i).getNeuron(j).getLink(t).getWeight();
                    double newWeight = oldWeight + trainingCoef * lErr * allLayers.get(i).getNeuron(j).incomingLinks.get(t).getNeuron().getPotential();
                    allLayers.get(i).getNeuron(j).getLink(t).setWeight(newWeight);
                }
            }
        }
    }

    private void addNode4everyone(Node n) {
        if (n.getType() == NodeType.INPUT) {
            if (!inNodes.contains(n)) {
                inNodes.add(n);
            }
        } else if (n.getType() == NodeType.HIDDEN) {
            if (!hiddenNodes.contains(n)) {
                hiddenNodes.add(n);
            }
        } else if (n.getType() == NodeType.OUTPUT) {
            if (!outNodes.contains(n)) {
                outNodes.add(n);
            }
        }
    }
}
