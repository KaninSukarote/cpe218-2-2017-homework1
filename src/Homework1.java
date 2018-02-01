import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.util.Stack;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.JTree;
import java.awt.Dimension;
import java.awt.GridLayout;

public class Homework1 extends JPanel implements TreeSelectionListener {
        private JEditorPane htmlPane = new JEditorPane();
        private JTree tree;
        static Stack stack = new Stack();

        public Homework1 (Node n) {
            super(new GridLayout(1, 0));

            //Create the nodes.
            DefaultMutableTreeNode top = new DefaultMutableTreeNode("The Java Series");
            //Create a tree that allows one selection at a time.
            tree = new JTree(createNodes(n));
            ImageIcon leafIcon = createImageIcon("ig/middle.gif");
            if (leafIcon != null) {
                DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
                renderer.setOpenIcon(leafIcon);
                renderer.setClosedIcon(leafIcon);
                tree.setCellRenderer(renderer);
            }
            tree.putClientProperty("JTree.lineStyle", "None");
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            //Listen for when the selection changes.
            tree.addTreeSelectionListener(this);
            //Create the scroll pane and add the tree to it.
            JScrollPane treeView = new JScrollPane(tree);

            //Create the HTML viewing pane.
            JScrollPane htmlView = new JScrollPane(htmlPane);

            //Add the scroll panes to a split pane.
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitPane.setTopComponent(treeView);
            splitPane.setBottomComponent(htmlView);

            Dimension minimumSize = new Dimension(100, 50);
            htmlView.setMinimumSize(minimumSize);
            treeView.setMinimumSize(minimumSize);
            splitPane.setDividerLocation(100);
            splitPane.setPreferredSize(new Dimension(500, 300));

            //Add the split pane to this panel.
            add(splitPane);
        }

        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (node == null) return;
            Node root = (Node) stack.pop();
            String token = inorder(root);
            if (root.left!=null) {
                token = token.substring(1,token.length()-1)+ "=" +calculator(root);
                htmlPane.setText(token);
                }
            }
        public ImageIcon createImageIcon(String path) {
            java.net.URL imgURL = Homework1.class.getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("file not found " + path);
                return null;
            }
        }


        public DefaultMutableTreeNode createNodes(Node top) {
            DefaultMutableTreeNode n = new DefaultMutableTreeNode(top);
            if (top.left != null) {
                n.add(createNodes(top.left));
                n.add(createNodes(top.left));
            }
            return n;

        }
        private static void createAndShowGUI(Node n) {
            /*if (true) {
                try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    System.err.println("Couldn't use system look and feel.");
                }
            }*/

            //Create and set up the window.
            JFrame frame = new JFrame("Binary Tree calculator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //Add content to the window.
            frame.add(new Homework1(n));

            //Display the window.
            frame.pack();
            frame.setVisible(true);
        }

        public static void main(String[] args) {

            if (args.length == 0) {
                String input = "251-*32*+";
                for (int i = 0; i < input.length(); i++) {
                    String token = input.substring(i, i + 1);
                    infix(token);
                }
                Node value = (Node) stack.pop();
                String root = inorder(value);
                if (value.left != null) {
                    root = root.substring(1, root.length() - 1) + "=" + calculator(value);
                }
                System.out.println(root);
                Node text = value;

                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        createAndShowGUI(text);


                    }

                });
            }}

        public static  void infix(String x) {
            if (x.matches("[0-9]")) {
                stack.push(new Node(x, null, null));
            } else {
                Node right = (Node) stack.pop();
                Node left = (Node) stack.pop();
                stack.push(new Node(x, left, right));
            }
        }

        public static String inorder(Node x) {
            String value = "";
            if (x.left != null) {
                value += "(" + inorder(x.left);
            }
            value += x.root;
            if (x.right != null) {
                value += inorder(x.right) + ")";
            }
            return value;
        }

        public static int calculator(Node x) {
            if (x.root.matches("[0-9]")) {
                return Integer.parseInt(x.root);
            }
            int value = 0;
            int left = calculator(x.left);
            int right = calculator(x.right);
            String operand = x.root;
            switch (operand) {
                case "+":
                    value = left + right;
                    break;
                case "-":
                    value = left - right;
                    break;
                case "*":
                    value = left * right;
                    break;
                case "/":
                    value = left / right;
                    break;

            }
            return value;


        }
    }




