import java.util.List;

public class Tree {

    public Node createNode(List<int[][]> value) {
        return new Node(value);
    }

    public Node insert(Node n, List<int[][]> value) {
        if (n == null) {
            return createNode(value);
        } else if (n.left == null) {
            n.left = insert(n.left, value);
        } else if (n.right == null) {
            n.right = insert(n.right, value);
        } else if (n.midd1 == null) {
            n.midd1 = insert(n.midd1, value);
        } else {
            n.midd2 = insert(n.midd2, value);
        }

        return n;
    }

    public void traversePreorder(Node root, int position) {
        if (root != null) {
            System.out.println("now?");
            //root.printData();
            //System.out.println("traverse" + data);
            traversePreorder(root.left, position - 2);
            traversePreorder(root.right, position - 1);
            traversePreorder(root.midd1, position);
            traversePreorder(root.midd2, position + 1);

        }
    }
}
