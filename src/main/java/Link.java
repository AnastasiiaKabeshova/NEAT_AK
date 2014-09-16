
/*
 * Link is a connection from one node to another with an associated weight; 
 * It can be marked as recurrent;
 * Its parameters are made public for efficiency.
 */
public class Link {

    private int linkID;
    /**
     * a real value of weight of connection(link)
     */
    private double weight;
    /**
     * a reference to an input node
     */
    private Node in_node;
    /**
     * a reference to a output node;
     */
    private Node out_node;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Link) {
            return this.getLinkID() == ((Link) obj).getLinkID();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.linkID;
        return hash;
    }

    public Link(Node nodeIn, Node nodeOut) {
        linkID = OrganismFactory.nextEnabledLinkID();
        weight = Math.random() - 0.5; //random [-0.5; 0.5];
        in_node = nodeIn;
        out_node = nodeOut;
        nodeOut.addIncomingLink(this);
        nodeIn.addOutgoingLink(this);
    }

    public Link(double w, Node nodeIn, Node nodeOut) {
        linkID = OrganismFactory.nextEnabledLinkID();
        weight = w;
        in_node = nodeIn;
        out_node = nodeOut;
        nodeOut.addIncomingLink(this);
        nodeIn.addOutgoingLink(this);
    }
    
    public Link(double w, Link l) {
        linkID = l.getLinkID();
        weight = w;
        in_node = new Node(l.getIn_node(), true);
        out_node = new Node(l.getOut_node(), true);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Node getIn_node() {
        return in_node;
    }

    public void setIn_node(Node in_node) {
        this.in_node = in_node;
    }

    public Node getOut_node() {
        return out_node;
    }

    public void setOut_node(Node out_node) {
        this.out_node = out_node;
    }

    /**
     * @return the linkID
     */
    public int getLinkID() {
        return linkID;
    }

    /**
     * @param linkID the linkID to set
     */
    public void setLinkID(int linkID) {
        this.linkID = linkID;
    }
}
