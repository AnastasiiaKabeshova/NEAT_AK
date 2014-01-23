
/**
 * genetic codification of gene;
 */
public class Gene{

    /* reference to object for identify input/output node and featuresn*/
    private Link link;
    /*is historical marking of node*/
    private int innovation_num;
    /*flag: is TRUE the gene is enabled FALSE otherwise.*/
    private boolean enable;
    /*how much mutation has changed the link*/
    private double mutation_num;

    Gene(Link newLink, int innovNum, boolean e) {
        link = newLink;
        innovation_num = innovNum;
        enable = e;
        mutation_num = 0;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link lnk) {
        this.link = lnk;
    }

    public int getInnovation_num() {
        return innovation_num;
    }

    public void setInnovation_num(int innovation_num) {
        this.innovation_num = innovation_num;
    }

    public double getMutation_num() {
        return mutation_num;
    }

    public void setMutation_num(double mutation_num) {
        this.mutation_num = mutation_num;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
