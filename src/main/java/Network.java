
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class Network {

    /**
     * is a reference to genotype can has originate this fenotype
     */
    private final List<Node> inNodes = new ArrayList<Node>();
    private final List<Node> hiddenNodes = new ArrayList<Node>();
    private final List<Node> outNodes = new ArrayList<Node>();

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
                addNode4everyone(l.getIn_node());
                addNode4everyone(l.getOut_node());
                l.getIn_node().removeOutgoingLink(l);
                l.getOut_node().removeIncomingLink(l);
            }
        }
    }

    public void addInNode(Node n) {
        inNodes.add(n);
    }

    public void addOutNode(Node n) {
        n.setAnswer(n.getPotential());
        outNodes.add(n);
    }

    public void addHiddenNode(Node n) {
        hiddenNodes.add(n);
    }

    public int getNumberHidden() {
        return (hiddenNodes.size());
    }

    public void changeOuter() throws Exception {
        Set<Node> doneNodes = new HashSet<Node>();
        Set<Node> openNodes = new HashSet<Node>();
        doneNodes.addAll(inNodes);
        //output Nodes
        for (int i = 0; i < outNodes.size(); i++) {
            calculateNode(outNodes.get(i), doneNodes, openNodes);
        }
    }

    private void calculateNode(Node n, Set<Node> doneNodes, Set<Node> openNodes) throws Exception {
        try {
            if (!openNodes.contains(n)) {
                if (n.getType() != NodeType.INPUT) {
                    openNodes.add(n);
                }
                if (!doneNodes.contains(n)) {
                    for (int i = 0; i < n.getIncomingLinksNumber(); i++) {
                        calculateNode(n.getIncomingLink(i).getIn_node(), doneNodes, openNodes);
                    }
                }
                n.countOut();
                openNodes.remove(n);
                doneNodes.add(n);
            } else {
                throw new Exception("LOOP in calculateNode");
            }
        } catch (Exception exc) {
            System.out.println("==================================");
            System.out.println("ERROR : " + exc.getMessage());
            System.out.println(n.getNodeID());
            System.out.println(n.getType());
            System.out.println("IncomingLinks: ");
            /*for (int i = 0; i < n.getIncomingLinksNumber(); i++) {
             System.out.println("IN Node" + n.getIncomingLink(i).getIn_node().getNodeID() + n.getIncomingLink(i).getIn_node().getType());
             System.out.println("OUT Node" + n.getIncomingLink(i).getOut_node().getNodeID() + n.getIncomingLink(i).getOut_node().getType());
             System.out.println();
             }*/
            System.out.println("==================================");
        }
    }

    public double getError4Organism() {
        double error = 0.0;
        for (int i = 0; i < outNodes.size(); i++) {
            error += outNodes.get(i).error();
        }
        return error/2;
    }
    
    public double getOut4Organism() {
        double out = 0.0;
        for (int i = 0; i < outNodes.size(); i++) {
            out += outNodes.get(i).getPotential();
        }
        return out;
    }

    public void ajustmentWeigth() {
        //errors and weigth adjustment for output layer
        for (int i = 0; i < outNodes.size(); i++) {
            outNodes.get(i).ajErrorOutputNode();
        }
        Set<Node> doneNodes = new HashSet<Node>();
        doneNodes.addAll(outNodes);

        //errors and weigth adjustment for hidden layers
        for (int i = hiddenNodes.size() - 1; i >= 0; i--) {
            calculateMisalignmentWeigth(hiddenNodes.get(i), doneNodes);
        }

    }

    private void calculateMisalignmentWeigth(Node n, Set<Node> doneNodes) {
        if (!doneNodes.contains(n)) {
            for (int i = 0; i < n.getOutgoingLinksNumber(); i++) {
                calculateMisalignmentWeigth(n.getOutgoingLink(i).getOut_node(), doneNodes);
            }
        }
        n.ajErrorHiddenNode();
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

    public void setInputData(List<Double> in, List<Double> out) {
        try {
            if (in.size() != inNodes.size() || out.size() != outNodes.size()) {
                throw new Exception("wrong node number (in or out)");
            } else {
                for (int i = 0; i < inNodes.size(); i++) {
                    inNodes.get(i).setPotential(in.get(i));
                }
                for (int i = 0; i < outNodes.size(); i++) {
                    outNodes.get(i).setAnswer(out.get(i));
                }
            }
        } catch (Exception exc) {
            System.out.println("==================================");
            System.out.println("ERROR : " + exc.getMessage());
            System.out.println("==================================");
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
            //selectCollection.addAll(inNodes);
            selectCollection.addAll(hiddenNodes);
            selectCollection.remove(nodeIn);
            selectCollection.addAll(outNodes);
        }
        int choosenIndex = -1;
        while (choosenIndex == -1 & !selectCollection.isEmpty()) {
            choosenIndex = getIndex4RandomOutNode(selectCollection, nodeIn);
        }
        if (choosenIndex == -1) {
            return nodeIn;
        } else {
            return selectCollection.get(choosenIndex);
        }
    }

    private int getIndex4RandomOutNode(List<Node> selectCollection, Node nodeIn) {
        int Min = 0, Max = selectCollection.size() - 1;
        int selectedIngex = Min + (int) (Math.random() * ((Max - Min) + 1));

        boolean conclusion = true;

        //verify declining hierarchy
        Set<Node> toVerifyNodes = new HashSet<Node>();
        Set<Node> verifiedNodes = new HashSet<Node>();
        verifiedNodes.add(selectCollection.get(selectedIngex));
        toVerifyNodes.add(nodeIn);
        while (!toVerifyNodes.isEmpty()) {
            conclusion = verify_IN_Hierarchy(conclusion, toVerifyNodes, verifiedNodes);
        }

        if (conclusion == true) {
            //verify increase hierarchy
            Set<Node> toVerifyOutNodes = new HashSet<Node>();
            Set<Node> verifiedOutNodes = new HashSet<Node>();
            verifiedOutNodes.add(nodeIn);
            toVerifyOutNodes.add(selectCollection.get(selectedIngex));
            while (!toVerifyOutNodes.isEmpty()) {
                conclusion = verify_OUT_Hierarchy(conclusion, toVerifyOutNodes, verifiedOutNodes);
            }
        }

        if (conclusion == false) {
            selectCollection.remove(selectCollection.get(selectedIngex));
            selectedIngex = -1;
        }
        return selectedIngex;
    }

    private boolean verify_IN_Hierarchy(boolean conclusion, Set<Node> toVerifyNodes, Set<Node> verifiedNodes) {
        final Iterator itr = toVerifyNodes.iterator();
        Node cur = (Node) itr.next();
        toVerifyNodes.remove(cur);

        if (verifiedNodes.contains(cur)) {
            conclusion = false;
        } else {
            for (int i = 0; i < cur.getIncomingLinksNumber(); i++) {
                if (cur.getIncomingLink(i).getIn_node().getType() != NodeType.INPUT) {
                    toVerifyNodes.add(cur.getIncomingLink(i).getIn_node());
                    conclusion = verify_IN_Hierarchy(conclusion, toVerifyNodes, verifiedNodes);
                }
            }
            verifiedNodes.add(cur);
        }
        return conclusion;
    }

    private boolean verify_OUT_Hierarchy(boolean conclusion, Set<Node> toVerifyOutNodes, Set<Node> verifiedOutNodes) {
        final Iterator itr = toVerifyOutNodes.iterator();
        Node cur = (Node) itr.next();
        toVerifyOutNodes.remove(cur);

        if (verifiedOutNodes.contains(cur)) {
            conclusion = false;
        } else {
            for (int i = 0; i < cur.getOutgoingLinksNumber(); i++) {
                if (cur.getOutgoingLink(i).getOut_node().getType() != NodeType.OUTPUT) {
                    toVerifyOutNodes.add(cur.getOutgoingLink(i).getOut_node());
                    conclusion = verify_OUT_Hierarchy(conclusion, toVerifyOutNodes, verifiedOutNodes);
                }
            }
            verifiedOutNodes.add(cur);
        }
        return conclusion;
    }
    /**
     * *********************************************************************
     */
}
