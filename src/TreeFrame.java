import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by evstropova on 01.02.2016.
 */
public class TreeFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 360;

    JPanel panel;
    JCheckBox showCircles;
    JCheckBox showLines;

    ButtonGroup selectBackground;
    JRadioButton white;
    JRadioButton cyan;
    JRadioButton yellow;
    JRadioButton another; //Потом создать окно для выбора произвольного цвета

    JMenuBar aMenuBar;
    JMenu aFileMenu;
    JMenu anEditMenu;
    JMenu aViewMenu;
    JMenuItem newItem;
    JMenuItem openItem;
    JMenuItem saveItem;
    JRadioButtonMenuItem whiteMenuItem;
    JRadioButtonMenuItem cyanMenuItem;
    JRadioButtonMenuItem yellowMenuItem;
    JMenuItem insertItem;
    JMenuItem findItem;
    JMenuItem deleteItem;

    JButton insert;
    JButton find;
    JButton delete;

    JComponent aTree;
    JPanel aNodeTreePanel;
    NodeTreeComponent aNodeTreeComponent;

    private static Logger logger = Logger.getLogger(TreeFrame.class.getName());
    public static Handler handler;

    public TreeFrame() {
        GridBagLayout layout = new GridBagLayout();
        panel = new JPanel(layout);
        panel.setOpaque(true);
        setContentPane(panel);
        setResizable(false);

        Border mainBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        panel.setBorder(mainBorder);

        setTitle("TreeApp");
        setIconImage(new ImageIcon("tree.gif").getImage());
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        ActionListener colorListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //Протоколируем вход
                logger.log(Level.INFO, "Selecting a color of the background");

                Color color = null;
                if (white.isSelected() || whiteMenuItem.isSelected()) {
                    //Протоколируем выбор
                    logger.log(Level.INFO, "White color is selected");
                    color = Color.WHITE;
                }
                if (cyan.isSelected() || cyanMenuItem.isSelected()) {
                    //Протоколируем выбор
                    logger.log(Level.INFO, "Blue color is selected");
                    color = new Color(224, 255, 255);
                }
                if (yellow.isSelected() || yellowMenuItem.isSelected()) {
                    //Протоколируем выбор
                    logger.log(Level.INFO, "Yellow color is selected");
                    color = new Color(253, 234, 168);
                }
                //Протоколируем установку цвета
                logger.log(Level.INFO, "Background is updated");
                panel.setBackground(color);
            }
        };

        ActionListener insertListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //Протоколируем вызов вставки элемента
                logger.entering(TreeFrame.class.getName(), "The insert dialog is shown");
                String value = JOptionPane.showInputDialog("Enter a value:");
                //Протоколируем значение
                logger.log(Level.INFO, "Insert value: "+value);
                aNodeTreeComponent.insert(Integer.parseInt(value));
            }
        };

        ActionListener findListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //Протоколируем вызов вставки элемента
                logger.entering(TreeFrame.class.getName(), "The find dialog is shown");
                String value = JOptionPane.showInputDialog("Enter a value:");
                //Протоколируем значение
                logger.log(Level.INFO, "Find value: "+value);
                aNodeTreeComponent.find(Integer.parseInt(value));
            }
        };

        ActionListener deleteListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //Протоколируем вызов вставки элемента
                logger.entering(TreeFrame.class.getName(), "The delete dialog is shown");
                String value = JOptionPane.showInputDialog("Enter a value:");
                //Протоколируем значение
                logger.log(Level.INFO, "Delete value: "+value);
                aNodeTreeComponent.delete(Integer.parseInt(value));
            }
        };

        white = new JRadioButton("White");
        white.setSelected(true);
        white.setOpaque(false);
        panel.setBackground(Color.WHITE);
        cyan = new JRadioButton("Cyan");
        cyan.setSelected(false);
        cyan.setOpaque(false);
        yellow = new JRadioButton("Yellow");
        yellow.setSelected(false);
        yellow.setOpaque(false);
        selectBackground = new ButtonGroup();
        selectBackground.add(white);
        selectBackground.add(cyan);
        selectBackground.add(yellow);
        white.addActionListener(colorListener);
        cyan.addActionListener(colorListener);
        yellow.addActionListener(colorListener);
        showCircles = new JCheckBox("Show leaves", true);
        showCircles.setOpaque(false);
        showLines = new JCheckBox("Show lines", false);
        showLines.setOpaque(false);

        showCircles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //Протоколируем вызов
                logger.log(Level.INFO, "Change state show leaves");
                aNodeTreeComponent.displayLeaf = ! aNodeTreeComponent.displayLeaf;
                repaint();
            }
        });

        showLines.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //Протоколируем вызов
                logger.log(Level.INFO, "Change state show branches");
                aNodeTreeComponent.displayBranches = ! aNodeTreeComponent.displayBranches;
                aNodeTreeComponent.redraw(aNodeTreeComponent.root, aNodeTreeComponent.ROOT_X, aNodeTreeComponent.ROOT_Y);
                repaint();
            }
        });

        insert = new JButton("Insert");
        find = new JButton("Find");
        delete = new JButton("Delete");

        insert.addActionListener(insertListener);

        find.addActionListener(findListener);

        delete.addActionListener(deleteListener);

        aTree = new JComponent() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(new ImageIcon("tree.gif").getImage(), new AffineTransform().getScaleInstance(0.4, 0.4), null);
            }
        };
        aMenuBar = new JMenuBar();
        setJMenuBar(aMenuBar);
        aFileMenu = new JMenu("File");
        anEditMenu = new JMenu("Edit");
        aViewMenu = new JMenu("View");
        aMenuBar.add(aFileMenu);
        aMenuBar.add(anEditMenu);
        aMenuBar.add(aViewMenu);
        newItem = new JMenuItem("New", new ImageIcon("New_32x32.png"));
        openItem = new JMenuItem("Open", new ImageIcon("Open_32x32.png"));
        saveItem = new JMenuItem("Save", new ImageIcon("Save_32x32.png"));

        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //разместить код обработки операции создания файла
            }
        });
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //разместить код обработки операции открытия файла
            }
        });
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //разместить код обработки операции сохранения файла
            }
        });

        aFileMenu.add(newItem);
        aFileMenu.add(openItem);
        aFileMenu.add(saveItem);
        ButtonGroup editButtonGroup = new ButtonGroup();
        whiteMenuItem = new JRadioButtonMenuItem("White");
        whiteMenuItem.setSelected(true);
        cyanMenuItem = new JRadioButtonMenuItem("Cyan");
        cyanMenuItem.setSelected(false);
        yellowMenuItem = new JRadioButtonMenuItem("Yellow");
        yellowMenuItem.setSelected(false);
        editButtonGroup.add(whiteMenuItem);
        editButtonGroup.add(cyanMenuItem);
        editButtonGroup.add(yellowMenuItem);
        whiteMenuItem.addActionListener(colorListener);
        cyanMenuItem.addActionListener(colorListener);
        yellowMenuItem.addActionListener(colorListener);
        aViewMenu.add(whiteMenuItem);
        aViewMenu.add(cyanMenuItem);
        aViewMenu.add(yellowMenuItem);
        insertItem = new JMenuItem("Insert", new ImageIcon("Paste_32x32.png"));
        deleteItem = new JMenuItem("Delete", new ImageIcon("Delete_32x32.png"));
        findItem = new JMenuItem("Find", new ImageIcon("Find_32x32.png"));
        insertItem.addActionListener(insertListener);
        deleteItem.addActionListener(deleteListener);
        findItem.addActionListener(findListener);
        anEditMenu.add(insertItem);
        anEditMenu.add(deleteItem);
        anEditMenu.add(findItem);



        aNodeTreeComponent = new NodeTreeComponent();
        aNodeTreePanel = new JPanel();
        aNodeTreePanel.setOpaque(false);
        aNodeTreePanel.setLayout(new BorderLayout());

        Border aNodeTreeBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(165, 165, 165), new Color(75, 75, 75));
        aNodeTreePanel.setBorder(aNodeTreeBorder);
        aNodeTreePanel.add(aNodeTreeComponent, BorderLayout.CENTER);



        panel.add(showCircles, new GBC(0,0,1,1).setWeight(0, 0).setAnchor(GBC.WEST).setInsets(10));
        panel.add(showLines, new GBC(0,1,1,1).setWeight(0, 0).setAnchor(GBC.WEST).setInsets(10));
        panel.add(white, new GBC(0,3,1,1).setWeight(0, 0).setAnchor(GBC.WEST).setInsets(10));
        panel.add(cyan, new GBC(0,4,1,1).setWeight(0, 0).setAnchor(GBC.WEST).setInsets(10));
        panel.add(yellow, new GBC(0,5,1,1).setWeight(0, 0).setAnchor(GBC.WEST).setInsets(10));

        panel.add(aNodeTreePanel, new GBC(1,0,1,6).setWeight(100, 100).setFill(GBC.BOTH).setAnchor(GBC.CENTER).setInsets(10));

        panel.add(insert, new GBC(2,0,1,1).setWeight(0, 0).setAnchor(GBC.CENTER).setInsets(10));
        panel.add(find, new GBC(2,1,1,1).setWeight(0, 0).setAnchor(GBC.CENTER).setInsets(10));
        panel.add(delete, new GBC(2,2,1,1).setWeight(0, 0).setAnchor(GBC.CENTER).setInsets(10));
        panel.add(aTree, new GBC(2,3,1,3).setWeight(20, 0).setInsets(10).setFill(GBC.BOTH).setAnchor(GBC.WEST));
    }

    public static void main(String[] args) {
        //Настраиваем параметры логгера для протоколирования в файл
        try {
            handler = new FileHandler("%h/IdeaProjects/java-projects/Logging/LoggingTreeApp.log", 0, 10);
            logger.addHandler(handler);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Can't create log file handler", e);
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                TreeFrame aTreeFrame = new TreeFrame();
                aTreeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                aTreeFrame.setVisible(true);
            }
        });
    }
}

class GBC extends GridBagConstraints {
    public GBC(int gridx, int gridy) {
        this.gridx = gridx;
        this.gridy = gridy;
    }

    public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
    }

    public GBC setAnchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    public GBC setFill(int fill) {
        this.fill = fill;
        return this;
    }

    public GBC setWeight(double weigthx, double weighty) {
        this.weightx = weigthx;
        this.weighty = weighty;
        return this;
    }

    public GBC setInsets (int distance) {
        this.insets = new Insets(distance, distance, distance, distance);
        return this;
    }

    public GBC setInsets (int top, int left, int bottom, int right) {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public GBC setIpad (int ipadx, int ipady) {
        this.ipadx = ipadx;
        this.ipady = ipady;
        return this;
    }
}

class NodeTreeComponent extends JComponent {
    LinkedHashMap <Integer, Point2D> map = new LinkedHashMap <Integer, Point2D>();
    LinkedHashMap <Point2D, Double> mapBranches = new LinkedHashMap <Point2D, Double>();
    Image leaf = new ImageIcon("leaves.png").getImage();
    Image branch = new ImageIcon("branch.png").getImage();
    Image iteratorImage = new ImageIcon("pointer.png").getImage();
    Node root = null;
    public static final double ROOT_X = 150;
    public static final double ROOT_Y = 10;
    boolean setPointer = false;
    int valuePointer = -1;
    boolean displayLeaf = true;
    boolean displayBranches = false;
    private Logger logger = Logger.getLogger(NodeTreeComponent.class.getName());

    public NodeTreeComponent() {
        logger.setLevel(Level.INFO);
        logger.addHandler(TreeFrame.handler);
    }

    public void insert(int value) {
        //Протоколируем вход
        logger.entering(NodeTreeComponent.class.getName(), "insert");
        Node newNode = new Node();
        newNode.value = value;

        if (!find(value)) {
            if (root == null) {
                newNode.x = ROOT_X;
                newNode.y = ROOT_Y;
                root = newNode;
                root.leftChild = null;
                root.rightChild = null;
                //Протоколируем вставку корня
                logger.log(Level.INFO, "Insert root, value: "+value);
                map.put(value, new Point2D.Double(newNode.x, newNode.y));
                repaint();
            } else {
                Node current = root;
                Node parent;
                while (true) {
                    parent = current;
                    if (value < current.value) {
                        current = current.leftChild;
                        if (current == null) {
                            if (parent.x > 0 && parent.x < 280 && parent.y < 240) {
                                newNode.x = parent.x - 45;
                                newNode.y = parent.y + 45;
                                parent.leftChild = newNode;
                                //Протоколируем вставку корня
                                logger.log(Level.INFO, "Insert a new left child node, value: "+value);
                                map.put(value, new Point2D.Double(newNode.x, newNode.y));
                                redraw(root, ROOT_X, ROOT_Y);
                                repaint();
                            }
                            else {
                                //Протоколируем выход за пределы окна
                                logger.log(Level.INFO, "Insert a new left child node is not possible, value: "+value);
                                JOptionPane.showMessageDialog(null, "You have exceeded an input window!");
                            }
                            return;
                        }
                    } else {
                        current = current.rightChild;
                        if (current == null) {
                            if (parent.x > 0 && parent.x < 280 && parent.y < 240) {
                                newNode.x = parent.x + 45;
                                newNode.y = parent.y + 45;
                                parent.rightChild = newNode;
                                //Протоколируем вставку корня
                                logger.log(Level.INFO, "Insert a new right child node, value: "+value);
                                map.put(value, new Point2D.Double(newNode.x, newNode.y));
                                redraw(root, ROOT_X, ROOT_Y);
                                repaint();
                            }
                            else {
                                //Протоколируем выход за пределы окна
                                logger.log(Level.INFO, "Insert a new right child node is not possible, value: "+value);
                                JOptionPane.showMessageDialog(null, "You have exceeded an input window!");
                            }
                            return;
                        }
                    }
                }
            }
        }
        else {
            //Протоколируем наличие узла с заданным значением
            logger.log(Level.INFO, "The value "+value+" is already present in the tree");
            JOptionPane.showMessageDialog(null, "The value "+value+" is already present in the tree");
        }
    }

    public void delete(int value) {
        //Протоколируем вход
        logger.entering(NodeTreeComponent.class.getName(), "delete");
        Node current = root;
        Node parent = root;
        boolean isLeftChild = true;

        while (current.value != value) {
            parent = current;
            if (value < current.value) {
                isLeftChild = true;
                current = current.leftChild;
            } else {
                isLeftChild = false;
                current = current.rightChild;
            }
            if (current == null) {
                //Протоколируем отсутствие узла для удаления
                logger.log(Level.INFO, "The value "+value+" is not present in the tree");
                return;
            }
        }
        // Листовой узел
        if(current.rightChild == null && current.leftChild == null) {
            if (current == root) root = null;
            else {
                if (isLeftChild) parent.leftChild = null;
                else parent.rightChild = null;
            }
        }
        // Узел с одним потомком
        else
        if(current.rightChild == null)
            if (current == root) root = current.leftChild;
            else
            if (isLeftChild) parent.leftChild = current.leftChild;
            else parent.rightChild = current.leftChild;
        else
        if(current.leftChild == null)
            if (current == root) root = current.rightChild;
            else
            if (isLeftChild) parent.leftChild = current.rightChild;
            else parent.rightChild = current.rightChild;
        // Узел с двумя потомками
        else {
            Node successor = getSuccessor(current);
            successor.leftChild = current.leftChild;
            if (current == root) root = successor;
            else
                if(isLeftChild) parent.leftChild = successor;
                else parent.rightChild = successor;
        }
        //Протоколируем выход
        logger.exiting(NodeTreeComponent.class.getName(), "delete");
        map.clear();
        mapBranches.clear();
        redraw(root, ROOT_X, ROOT_Y);
        repaint();
    }

    public boolean find(int value) {
        //Протоколируем вход
        logger.entering(NodeTreeComponent.class.getName(), "find");
        Node current = root;
        while (current != null) {
            if (value == current.value) {
                setPointer = true;
                valuePointer = value;
                repaint();
                //Протоколируем нахождение корня
                logger.log(Level.INFO, "The value "+value+" is found");
                return true;
            }
            if (value < current.value)
                current = current.leftChild;
            else current = current.rightChild;
        }
        //Протоколируем нахождение корня
        logger.log(Level.INFO, "The value "+value+" is not found");
        return false;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for(Map.Entry<Integer, Point2D> entry: map.entrySet()) {
            g2.drawString(Integer.toString(entry.getKey()), (int) entry.getValue().getX(),(int) entry.getValue().getY());
            if (displayLeaf) {
                g2.drawImage(leaf, (int) entry.getValue().getX(), (int) entry.getValue().getY(), 23, 20, null);
            }
            if (setPointer && entry.getKey() == valuePointer) {
                g2.drawImage(iteratorImage, (int) entry.getValue().getX(), (int) entry.getValue().getY() + 30, 24, 24, null);
                setPointer = false;
                valuePointer = -1;
            }
        }

        for(Map.Entry<Point2D, Double> entry: mapBranches.entrySet()) {
            if (displayBranches) {
                AffineTransform old = g2.getTransform();
                g2.rotate(entry.getValue(), (int) entry.getKey().getX(), (int) entry.getKey().getY());
                g2.drawImage(branch, (int) entry.getKey().getX(), (int) entry.getKey().getY(), 32, 16, null);
                g2.setTransform(old);
            }
        }
    }

    public void redraw(Node node, double x, double y) {
        if (node != null) {
            node.x = x;
            node.y = y;
            map.put(node.value, new Point2D.Double(node.x, node.y));
            if(node.leftChild != null) mapBranches.put(new Point2D.Double(node.x, node.y + 25), 3*Math.PI/4);
            if(node.rightChild != null) mapBranches.put(new Point2D.Double(node.x + 25, node.y + 15), Math.PI/4);
            redraw(node.leftChild, x - 45, y + 45);
            redraw(node.rightChild, x + 45, y + 45);
        }
    }

    public Node getSuccessor(Node delNode) {
        //Протоколируем вход
        logger.entering(NodeTreeComponent.class.getName(), "getSuccessor");
        Node current = delNode.rightChild;
        Node successorParent = delNode;
        Node successor = delNode;
        while (current != null) {
            successorParent = successor;
            successor = current;
            current = current.leftChild;
        }
        if(successor != delNode.rightChild) {
            successorParent.leftChild = successor.rightChild;
            successor.rightChild = delNode.rightChild;
        }
        //Протоколируем значение преемника
        logger.log(Level.INFO, "The value of successor: "+successor);
        return successor;
    }
}

class Node {
    public int value;
    public Node leftChild;
    public Node rightChild;
    public double x;
    public double y;
}