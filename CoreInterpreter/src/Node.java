//Author: Shunkai Guo
//Basic node structure of the parse tree
import java.util.ArrayList;
import java.util.List;

public class Node {
    int value = 0;
    String id = "";
    int altNum;
    int ntNum;
    Node parent;
    List<Node> children;

    public Node() {
        this.altNum = 0;
        this.ntNum = 0;
        this.children = new ArrayList<Node>();
    }

}
