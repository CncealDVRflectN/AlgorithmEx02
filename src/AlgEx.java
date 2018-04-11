import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class AlgEx implements Runnable {
    private static class Vertex {
        int key;

        Vertex right;
        Vertex left;

        public Vertex() {
            key = 0;
            right = null;
            left = null;
        }

        public Vertex(int num) {
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

        public boolean add(int num) {
            Vertex vertex = new Vertex(num);
            Vertex iter = root;

            if (root == null) {
                this.root = vertex;
                return true;
            }

            while (iter != null) {
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

        public void directLeftRound(Vertex vertex, PrintWriter writer) throws Exception {
            if (vertex != null) {
                writer.write(vertex.key + "\n");
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
            } else if (iter.right == null && iter.left == null) {
                deleteLeaf(iter, parent);
            } else if (!(iter.right != null && iter.left != null)) {
                deleteOneChildVertex(iter, parent);
            } else {
                deleteTwoChildVertex(iter, parent);
            }

            return true;
        }

        private void deleteLeaf(Vertex leaf, Vertex parent) {
            if (parent != null) {
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

            if (parent != null) {
                if (parent.key < vertex.key) {
                    parent.right = next;
                } else {
                    parent.left = next;
                }
            } else {
                if (vertex.right != null) {
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

            if (vertex.right != next) {
                next.right = vertex.right;
            } else {
                next.right = null;
            }

            if (parent != null) {
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

    @Override
    public void run() {
        BufferedReader reader;
        PrintWriter writer = null;
        Tree tree;
        String line;
        int value;

        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            writer = new PrintWriter("output.txt");
            tree = new Tree();

            line = reader.readLine();
            value = Integer.valueOf(line);

            reader.readLine();
            while ((line = reader.readLine()) != null) {
                tree.add(Integer.valueOf(line));
            }
            reader.close();

            tree.rightDelete(value);
            tree.directLeftRound(tree.root, writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(null, new AlgEx(), "", 128 * 1024 * 1024).start();
    }
}
