import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

public class AlgEx {
    private static class Vertex {
        Integer key;
        Vertex right;
        Vertex left;

        public Vertex() {
            key = null;
            right = null;
            left = null;
        }

        public Vertex(Integer num) {
            key = num;
            right = null;
            left = null;
        }
    }

    private static class Tree {
        Vertex root;

        public Tree() {
            root = null;
        }

        public Tree(Vertex vertex) {
            root = vertex;
        }

        public boolean add(Vertex vertex) {
            Vertex iter = root;
            if (root == null) {
                this.root = vertex;
                return true;
            }
            while (iter != null && iter.key != vertex.key) {
                if (vertex.key > iter.key) {
                    if (iter.right == null) {
                        iter.right = vertex;
                        return true;
                    } else {
                        iter = iter.right;
                    }
                } else if (vertex.key < iter.key) {
                    if (iter.left == null) {
                        iter.left = vertex;
                        return true;
                    } else {
                        iter = iter.left;
                    }
                } else if (vertex.key == iter.key) {
                    return false;
                }
            }
            return true;
        }

        public void directLeftRound(Vertex vertex, PrintStream writer) throws Exception {
            if (vertex != null) {
                writer.println(vertex.key);
                directLeftRound(vertex.left, writer);
                directLeftRound(vertex.right, writer);
            }
        }

        public boolean rightDelete(Integer value) {
            Vertex iter = root;
            Vertex parent = null;
            while (iter != null && iter.key != value) {
                parent = iter;
                if (value > iter.key) {
                    iter = iter.right;
                } else if (value < iter.key) {
                    iter = iter.left;
                } else if (value == iter.key) {
                    break;
                }
            }
            if (iter == null) {
                return false;
            } /*else if (parent == null) {
                root = null;
            }*/ else if (iter.right == null && iter.left == null) {
                deleteLeaf(iter, parent);
            } else if (!(iter.right != null && iter.left != null)) {
                deleteOneChildVertex(iter, parent);
            } else {
                deleteTwoChildVertex(iter, parent);
            }
            return true;
        }

        private void deleteLeaf(Vertex leaf, Vertex parent) {
            if(parent != null) {
                if (parent.key < leaf.key) {
                    parent.right = null;
                } else {
                    parent.left = null;
                }
            } else {
                root = null;
            }
        }

        private void deleteOneChildVertex(Vertex vertex, Vertex parent) {
            Vertex next;
            if (vertex.right != null) {
                next = vertex.right;
            } else {
                next = vertex.left;
            }
            if(parent != null) {
                if (parent.key < vertex.key) {
                    parent.right = next;
                } else {
                    parent.left = next;
                }
            } else {
                if(vertex.right != null) {
                    root = vertex.right;
                } else {
                    root = vertex.left;
                }
            }
        }

        private void deleteTwoChildVertex(Vertex vertex, Vertex parent) {
            Vertex next;
            Vertex nextParent;
            next = vertex.right;
            nextParent = vertex;
            while (next.left != null) {
                nextParent = next;
                next = next.left;
            }
            if (next.right != null) {
                deleteOneChildVertex(next, nextParent);
            } else {
                deleteLeaf(next, nextParent);
            }
            next.left = vertex.left;
            if(vertex.right != next) {
                next.right = vertex.right;
            } else {
                next.right = null;
            }
            if(parent != null) {
                if (parent.key < next.key) {
                    parent.right = next;
                } else {
                    parent.left = next;
                }
            } else {
                root = next;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(new File("input.txt"));
        Tree tree = new Tree();
        PrintStream writer = new PrintStream("output.txt");
        Integer value;
        value = scanner.nextInt();
        scanner.nextLine();
        while (scanner.hasNextInt()) {
            tree.add(new Vertex(scanner.nextInt()));
        }
        tree.rightDelete(value);
        tree.directLeftRound(tree.root, writer);
        writer.close();
        scanner.close();
    }
}
