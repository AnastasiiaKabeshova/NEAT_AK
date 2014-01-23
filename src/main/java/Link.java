
/*
 * Link is a connection from one node to another with an associated weight; 
 * It can be marked as recurrent;
 * Its parameters are made public for efficiency.
 */
public class Link {
    
    /** a real value of weight of connection(link) */
	  private double weight;
   /** a reference to an  input node */
	  private Node in_node;
   /** a reference to a output node; */
	  private Node out_node;

    public Link(Node nodeIn, Node nodeOut) {
        weight = Math.random() - 0.5; //random [-0.5; 0.5];
        in_node = nodeIn;
        out_node = nodeOut;
        nodeOut.addIncomingLink(this); 
        nodeIn.addOutgoingLink(this);
    }

    public Link(double w, Node nodeIn, Node nodeOut) {
        weight = w;
        in_node = nodeIn;
        out_node = nodeOut;
        nodeOut.addIncomingLink(this);
        nodeIn.addOutgoingLink(this);
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
          
}
