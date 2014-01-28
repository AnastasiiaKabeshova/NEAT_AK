
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
    List<Link> incomingLinks; // All inputs of neuron
    List<Link> outgoingLinks; // All outs of neuron

    public Node() {
        incomingLinks = new ArrayList<Link>();
        outgoingLinks = new ArrayList<Link>();
    }
    
    public Node(NodeType node_type) {
        incomingLinks = new ArrayList<Link>();
        outgoingLinks = new ArrayList<Link>();
        type = node_type;
    }

    public Node(double newPotential, NodeType node_type) {
        this.setPotential(newPotential);
        incomingLinks = new ArrayList<Link>();
        outgoingLinks = new ArrayList<Link>();
        type = node_type;
    }

    public Link getIncomingLink(int index) {
        return incomingLinks.get(index);
    }
    
    public Link getOutgoingLink(int index) {
        return outgoingLinks.get(index);
    }

    public int getIncomingLinksNumber() {
        return incomingLinks.size();
    }

    public int getOutgoingLinksNumber() {
        return outgoingLinks.size();
    }

    public void addIncomingLink(Link l) {
        incomingLinks.add(l);
    }

    public void addOutgoingLink(Link l) {
        outgoingLinks.add(l);
    }
    
    public void removeIncomingLink (Link l) {
        Iterator<Link> iter = incomingLinks.iterator();
        while (iter.hasNext()) {
            if (iter.next().getLinkID() == l.getLinkID()) {
                iter.remove();
            }
	}
    }
    
    public void removeOutgoingLink (Link l) {
        Iterator<Link> iter = outgoingLinks.iterator();
        while (iter.hasNext()) {
            if (iter.next().getLinkID() == l.getLinkID()) {
                iter.remove();
            }
	}
    }

    //modified sigmoidal transfer function
    public double activationF(double x) {
        double f = 1 / (1 + Math.exp(-x * NeatClass.p_alpha_activationFunction));
        return f;
    }

    public void countOut() {
        if (this.type != NodeType.INPUT) {
            double out = 0;
            for (int i = 0; i < incomingLinks.size(); i++) {
                out += incomingLinks.get(i).getWeight() * incomingLinks.get(i).getIn_node().getPotential();
            }
            double newPower = activationF(out);  // activation function  
            this.setPotential(newPower);
        }
    }

    //simple error (real output - output we wait) 
    // ONLY for output layer
    public double error() {
        return Math.pow(this.answer - getPotential(), 2);
    }
    // actual error for neurons on output layer

    public void ajErrorOutputLayer() {
        setMisalignment(this.getPotential() * (1 - this.getPotential()) * (this.getAnswer() - this.getPotential()));
    }
    // for other layers

    public void ajErrorHiddenLayer() {
        double sumChildren = 0.0;
        for (int t = 0; t < getOutgoingLinksNumber(); t++) { // for all neurons on next layer
            sumChildren += this.getMisalignment() * this.getOutgoingLink(t).getWeight();
        }
        this.setMisalignment(getPotential() * (1 - getPotential()) * sumChildren); //для скрытых слоев - по невязке предыдущего слоя
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
}
