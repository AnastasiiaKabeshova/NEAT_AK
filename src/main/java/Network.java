
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

            if (xgenome.getGenes().get(i).isEnable()) {
                addNode4everyone(l.getIn_node());
                addNode4everyone(l.getOut_node());
            } else {
                // if Link is disable -> delete this Link from input and output
                l.getIn_node().removeOutgoingLink(l);
                l.getOut_node().removeIncomingLink(l);
            }
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
    
    public int getNumberHidden() {
        return (hiddenNodes.size());
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
                calculateNode(n.getIncomingLink(i).getIn_node(), doneNodes);
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

    public void ajustmentWeigth() {
        //errors and adjustment for output layer
        for (int i = 0; i < outNodes.size(); i++) {
            outNodes.get(i).ajErrorOutputLayer();
        }
        Set<Node> doneNodes = new HashSet<Node>();
        doneNodes.addAll(outNodes);
        //errors and adjustment for hidden layers
        for (int i = hiddenNodes.size(); i > 0; i--) {
            calculateMisalignment(hiddenNodes.get(i), doneNodes);
        }

        //Adjustment of synaptic weights on the layers
        for (Node nodeVar : doneNodes) {
            double lErr = nodeVar.getMisalignment();
            for (int t = 0; t < nodeVar.getIncomingLinksNumber(); t++) {
                double oldWeight = nodeVar.getIncomingLink(t).getWeight();
                double potential = nodeVar.getIncomingLink(t).getOut_node().getPotential();
                double newWeight = oldWeight + NeatClass.p_training_coefficient * lErr * potential;
                nodeVar.getIncomingLink(t).setWeight(newWeight);
            }
        }
    }

    private void calculateMisalignment(Node n, Set<Node> doneNodes) {
        if (!doneNodes.contains(n)) {
            for (int i = 0; i < n.getOutgoingLinksNumber(); i++) {
                calculateNode(n.getOutgoingLink(i).getOut_node(), doneNodes);
            }
        }
        n.ajErrorHiddenLayer();
        doneNodes.add(n);
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

    /**
     * *********************************************************************
     */
    //tool for mutation AddConnection
    public Node selectRandomNode(Node nodeIn) {
        List<Node> selectCollection = new ArrayList<Node>();
        if (nodeIn.getType() == NodeType.INPUT) {
            selectCollection.addAll(outNodes);
            selectCollection.addAll(hiddenNodes);
        } else if (nodeIn.getType() == NodeType.OUTPUT) {
            selectCollection.addAll(inNodes);
            selectCollection.addAll(hiddenNodes);
        } else if (nodeIn.getType() == NodeType.HIDDEN) {
            selectCollection.addAll(inNodes);
            selectCollection.addAll(hiddenNodes);
            selectCollection.remove(nodeIn);
            selectCollection.addAll(outNodes);
        }
        int choosenIndex = -1;
        while (choosenIndex == -1) {
            choosenIndex = getIndex4RandomNode(selectCollection, nodeIn);
        }
        return selectCollection.get(choosenIndex);
    }

    private int getIndex4RandomNode(List<Node> selectCollection, Node nodeIn) {
        int Min = 0, Max = selectCollection.size()-1;
        int randomIndex = Min + (int) (Math.random() * ((Max - Min) + 1));
        for (int i = 0; i < selectCollection.get(randomIndex).getIncomingLinksNumber(); i++) {
            if (selectCollection.get(randomIndex).getIncomingLink(i).getIn_node() == nodeIn) {
                randomIndex = -1;
            }
        }
        return randomIndex;
    }
    /**
     * *********************************************************************
     */
}
