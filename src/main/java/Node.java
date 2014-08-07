
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A NODE is either a NEURON or a SENSOR. If it's a sensor, it can be loaded
 * with a value for output If it's a neuron, it has a list of its incoming input
 * signals Use an activation count to avoid flushing
 */
public final class Node extends NeatClass {

    private int nodeID;
    /**
     * Used for genetic marking of nodes. are 4 type of node :
     * input,bias,hidden,output
     */
    private NodeType type;
    private double potential; // Neuron accumulated power
    private double answer; // only for output layer!!!
    private double misalignment; //невязка сети
    private final List<Link> incomingLinks = new ArrayList<Link>(); // All inputs of neuron
    private final List<Link> outgoingLinks = new ArrayList<Link>(); // All outs of neuron

    public Node() {
    }

    public Node(NodeType node_type) {
        type = node_type;
        this.setNodeID(OrganismFactory.nextEnabledNodeID());
    }

    public Node(double newPotential, NodeType node_type) {
        this.setPotential(newPotential);
        type = node_type;
        this.setNodeID(OrganismFactory.nextEnabledNodeID());
    }

    public Node(Node n) {
        this.setNodeID(n.getNodeID());
        this.setType(n.getType());
        this.setPotential(n.getPotential());
        this.setAnswer(n.getAnswer());
        this.setMisalignment(n.getMisalignment());
        this.incomingLinks.addAll(n.getIncomingLinks());
        this.outgoingLinks.addAll(n.getOutgoingLinks());
    }

    public Link getIncomingLink(int index) {
        return getIncomingLinks().get(index);
    }

    public Link getOutgoingLink(int index) {
        return getOutgoingLinks().get(index);
    }

    public int getIncomingLinksNumber() {
        return getIncomingLinks().size();
    }

    public int getOutgoingLinksNumber() {
        return getOutgoingLinks().size();
    }

    public void addIncomingLink(Link l) {
        getIncomingLinks().add(l);
    }

    public void addOutgoingLink(Link l) {
        getOutgoingLinks().add(l);
    }

    public void removeIncomingLink(Link l) {
        Iterator<Link> iter = getIncomingLinks().iterator();
        while (iter.hasNext()) {
            if (iter.next().getLinkID() == l.getLinkID()) {
                iter.remove();
            }
        }
    }

    public void removeOutgoingLink(Link l) {
        Iterator<Link> iter = getOutgoingLinks().iterator();
        while (iter.hasNext()) {
            if (iter.next().getLinkID() == l.getLinkID()) {
                iter.remove();
            }
        }
    }

    //modified sigmoidal transfer function
    private double activationF(double x) {
        double f = 1 / (1 + Math.exp(-x * NeatClass.p_alpha_activationFunction));
        return f;
    }

    //Функция Ферми (экспоненциальная сигмоида):
    private double activationFfermi(double x) {
        double f = 1 / (1 + Math.exp(x * (-2) * NeatClass.p_alpha_activationFunction));
        return f;
    }

    public void countOut() {
        if (this.type != NodeType.INPUT) {
            double out = 0.0;
            for (int i = 0; i < getIncomingLinks().size(); i++) {
                out += getIncomingLinks().get(i).getWeight() * getIncomingLinks().get(i).getIn_node().getPotential();
            }
            double newPower = activationFfermi(out);  // activation function  
            this.setPotential(newPower);
        }
    }

    //simple error (real output - output we wait) 
    // ONLY for output layer
    public double error() {
        return Math.pow(this.answer - this.getPotential(), 2);
    }

    // actual error for neurons on output layer
    public void ajErrorOutputNode() {
        setMisalignment(this.getPotential() * (1 - this.getPotential()) * (this.getAnswer() - this.getPotential()));

        //Adjustment of synaptic weights on the output layer
        double lErr = this.getMisalignment();
        for (int t = 0; t < this.getIncomingLinksNumber(); t++) {
            double oldWeight = this.getIncomingLink(t).getWeight();
            double out = this.getIncomingLink(t).getIn_node().getPotential();
            double newWeight = oldWeight + NeatClass.p_training_coefficient * lErr * out;
            this.getIncomingLink(t).setWeight(newWeight);
        }

    }

    // for other layers
    public void ajErrorHiddenNode() {
        double sumChildren = 0.0;
        for (int t = 0; t < getOutgoingLinksNumber(); t++) { // for all neurons on next layer
            sumChildren += this.getOutgoingLink(t).getOut_node().getMisalignment() * this.getOutgoingLink(t).getWeight();
        }
        this.setMisalignment(this.getPotential() * (1 - this.getPotential()) * sumChildren); //для скрытых слоев - по невязке предыдущего слоя

        //Adjustment of synaptic weights on the layers
        double lErr = this.getMisalignment();
        for (int t = 0; t < this.getIncomingLinksNumber(); t++) {
            double oldWeight = this.getIncomingLink(t).getWeight();
            double out = this.getIncomingLink(t).getIn_node().getPotential();
            double newWeight = oldWeight + NeatClass.p_training_coefficient * lErr * out;
            this.getIncomingLink(t).setWeight(newWeight);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            return this.getNodeID() == ((Node) obj).getNodeID();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.nodeID;
        return hash;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public double getPotential() {
        return potential;
    }

    public void setPotential(double potent) {
        this.potential = potent;
    }

    /**
     * @return the misalignment
     */
    public double getMisalignment() {
        return misalignment;
    }

    /**
     * @param misalignment the misalignment to set
     */
    public void setMisalignment(double misalignment) {
        this.misalignment = misalignment;
    }

    /**
     * @return the answer
     */
    public double getAnswer() {
        return answer;
    }

    /**
     * @param answer the answer to set
     */
    public void setAnswer(double answer) {
        this.answer = answer;
    }

    /**
     * @return the incomingLinks
     */
    public List<Link> getIncomingLinks() {
        return incomingLinks;
    }

    /**
     * @return the outgoingLinks
     */
    public List<Link> getOutgoingLinks() {
        return outgoingLinks;
    }
}
