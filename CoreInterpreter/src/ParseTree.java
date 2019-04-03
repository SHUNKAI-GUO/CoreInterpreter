//Author: Shunkai Guo
//Parse tree structure Also containing methods to get access to the value of the node
import java.util.HashMap;
import java.util.Map;

public class ParseTree {
    private Node parseTree;
    private Map<String, Integer> idMap = new HashMap<String, Integer>();

    //most nodes don't require a value
    public ParseTree() {
        this.parseTree = new Node();
    }

    // non-term. at current node
    int currNTNo() {
        return this.parseTree.ntNum;
    }

    // Alternative no. used at current node
    int currAlt() {
        return this.parseTree.altNum;
    }

    void setNTNo(int n) {
        this.parseTree.ntNum = n;
    }

    void setAlt(int a) {
        this.parseTree.altNum = a;
    }

    // move cursor to left child
    void goDownLB() {
        this.parseTree = this.parseTree.children.get(0);
    }

    //move cursor to second child
    void goDownMB() {
        this.parseTree = this.parseTree.children.get(1);
    }

    //move cursor to third child
    void goDownRB() {
        this.parseTree = this.parseTree.children.get(2);
    }

    // move cursor to parent node
    void goUP() {
        this.parseTree = this.parseTree.parent;
    }

    void createLB() {
        Node new_node = new Node();
        new_node.parent = this.parseTree;
        this.parseTree.children.add(new_node);
    }

    void createMB() {
        Node new_node = new Node();
        new_node.parent = this.parseTree;
        this.parseTree.children.add(new_node);
    }

    void createRB() {
        Node new_node = new Node();
        new_node.parent = this.parseTree;
        this.parseTree.children.add(new_node);
    }

    void setId(String id) {
        if (!this.idMap.containsKey(id)) {
            this.idMap.put(id, 0);
        }
        this.parseTree.id = id;
    }

    String getId() {
        return this.parseTree.id;
    }

    boolean exitId(String id) {
        return this.idMap.containsKey(id);
    }

    void setValue(int value) {
        this.parseTree.value = value;
    }

    int getValue() {
        return this.parseTree.value;
    }
}
