import java.util.Objects;

public class Node {
    private Point position;
    private int g;
    private int h;
    private int f;
    private Node previous;

    public Node(Point position){
        this.position = position;
        this.g = g;
        this.h = h;
        this.f = 0;
        this.previous = null;
    }

    public Node getPrevious(){
        if(previous == null){
            return null;
        }
        return previous;
    }

    public Point getPosition() {return position;}
    public int getH() {return h;}
    public int getG() {return g;}
    public int getF(){return f;}
    public void setG(int g) {this.g = g;}
    public void setH(int h) {this.h = h;}
    public void setF() {this.f = g + h;}
    public void setPrevious(Node node){
        previous = node;
    }

    public int hashCode() {return Objects.hash(position, g, h);}

    public boolean equals(Object o){
        if (o == null) {
            return false;}
        if (o.getClass() != this.getClass()) {
            return false;}
        Node p = (Node) o;
        if (this.position == null && p.position == null){
            return true;
        }
        else if(this.position == null || p.position == null){
            return false;
        }
        else{
            return this.position == p.position;
        }
    }
}
