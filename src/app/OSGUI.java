package app;

import Sheduler.IOhandler;
import Sheduler.LTS;
import Sheduler.STS;
import computer.CPU;
import computer.MainMemmory;
import java.awt.Color;
import static java.awt.Color.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

/**
 *
 * @author චානක මධුරංග
 */
public class OSGUI extends javax.swing.JFrame {
//take all the buttons, text fields and labels to list. Eazy to access then.

    public int stopwatch = 0;
    JProgressBar[][] jpblist;
    JTextField[] txtlist;
    JLabel[][] queuelist;
    JLabel[] labellist;
    Color[] colo;

    ArrayList<Processes.Process> processListt;

    int factor = 1;//-----------------------------------------------------------------------

    /**
     * Creates new form OSGUI
     */
    @SuppressWarnings("empty-statement")
    public OSGUI() {
        initComponents();

    }

    //Malith - constructor
    public OSGUI(ArrayList<Processes.Process> processList, int timeSlice) {//take process list and few data when initialise
        initComponents();
        processListt = processList;
        jpblist = new JProgressBar[][]{{jpb1_1, jpb1_2, jpb1_3, jpb1_4, jpb1_5, jpb1_6, jpb1_7, jpb1_8, jpb1_9, jpb1_10, jpb1_11, jpb1_12, jpb1_13, jpb1_14, jpb1_15, jpb1_16, jpb1_17, jpb1_18, jpb1_19, jpb1_20, jpb1_21, jpb1_22, jpb1_23, jpb1_24, jpb1_25}, {jpb2_1, jpb2_2, jpb2_3, jpb2_4, jpb2_5, jpb2_6, jpb2_7, jpb2_8, jpb2_9, jpb2_10, jpb2_11, jpb2_12, jpb2_13, jpb2_14, jpb2_15, jpb2_16, jpb2_17, jpb2_18, jpb2_19, jpb2_20, jpb2_21, jpb2_22, jpb2_23, jpb2_24, jpb2_25}, {jpb3_1, jpb3_2, jpb3_3, jpb3_4, jpb3_5, jpb3_6, jpb3_7, jpb3_8, jpb3_9, jpb3_10, jpb3_11, jpb3_12, jpb3_13, jpb3_14, jpb3_15, jpb3_16, jpb3_17, jpb3_18, jpb3_19, jpb3_20, jpb3_21, jpb3_22, jpb3_23, jpb3_24, jpb3_25}, {jpb4_1, jpb4_2, jpb4_3, jpb4_4, jpb4_5, jpb4_6, jpb4_7, jpb4_8, jpb4_9, jpb4_10, jpb4_11, jpb4_12, jpb4_13, jpb4_14, jpb4_15, jpb4_16, jpb4_17, jpb4_18, jpb4_19, jpb4_20, jpb4_21, jpb4_22, jpb4_23, jpb4_24, jpb4_25}, {jpb5_1, jpb5_2, jpb5_3, jpb5_4, jpb5_5, jpb5_6, jpb5_7, jpb5_8, jpb5_9, jpb5_10, jpb5_11, jpb5_12, jpb5_13, jpb5_14, jpb5_15, jpb5_16, jpb5_17, jpb5_18, jpb5_19, jpb5_20, jpb5_21, jpb5_22, jpb5_23, jpb5_24, jpb5_25}};
        txtlist = new JTextField[]{txt1, txt2, txt3, txt4, txt5};
        queuelist = new JLabel[][]{{label1_1, label1_2, label1_3, label1_4, label1_5, label1_6, label1_7, label1_8}, {label2_1, label2_2, label2_3, label2_4, label2_5, label2_6, label2_7, label2_8}, {label3_1, label3_2, label3_3, label3_4, label3_5, label3_6, label3_7, label3_8}};
        labellist = new JLabel[]{jLabel_1, jLabel_2, jLabel_3, jLabel_4, jLabel_5, jLabel_6, jLabel_7, jLabel_8, jLabel_9, jLabel_10, jLabel_11, jLabel_12, jLabel_13, jLabel_14, jLabel_15, jLabel_16, jLabel_17, jLabel_18, jLabel_19, jLabel_20, jLabel_21, jLabel_22, jLabel_23, jLabel_24, jLabel_25};
        colo = new Color[]{blue, yellow, red, white, green};

        for (int er = 0; er < processListt.size(); er++) {//set all the data(process list) to the table

            if (processListt.get(er).getArrivalTime() < 25) {
                jTable.setValueAt(processListt.get(er).getArrivalTime(), er, 0);
            }
            if (processListt.get(er).getServiceTime() < 25) {
                jTable.setValueAt(processListt.get(er).getServiceTime(), er, 1);
            }
            if (processListt.get(er).getRemainingServiceTime() < 25) {
                jTable.setValueAt(processListt.get(er).getRemainingServiceTime(), er, 2);
            }
            if (processListt.get(er).getIORequestTime() < 25) {
                jTable.setValueAt(processListt.get(er).getIORequestTime(), er, 3);
            } else {
                jTable.setValueAt("--", er, 3);//if no value entered
            }
            if (processListt.get(er).getIOCostTime() < 25 & processListt.get(er).getIOCostTime() > 0) {
                jTable.setValueAt(processListt.get(er).getIOCostTime(), er, 4);
            } else {
                jTable.setValueAt("--", er, 4);//if no value entered
            }
        }

        for (int x = 0; x < queuelist.length; x++) {//set the label of all the queues to not visible
            for (int y = 0; y < queuelist[x].length; y++) {
                queuelist[x][y].setVisible(false);
            }
        }

        //Malith
        timeSlice = 1;
        factor = 1;//---------------------------------------------------------------------------------

        CPU cpu = new CPU(this);

        MainMemmory mainMemmory = new MainMemmory(this);

        // start the Long Time Sheduler thread
        Thread lts = new Thread(new LTS(processList, mainMemmory, stopwatch, factor, this));//------------
        Thread sts = new Thread(new STS(processList, cpu, mainMemmory, stopwatch, factor, this));
        Thread ioHandler = new Thread(new IOhandler(mainMemmory, stopwatch, factor));//---------------------

        lts.start();
        sts.start();
        ioHandler.start();

        setLocationRelativeTo(null);  // *** this will center the app ***

        //end of Malith
    }
    // STS sts = new STS(this);
    int column = 0;
    boolean locked = false;//this uses to check a progress bar is running or not

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpb1_1 = new javax.swing.JProgressBar();
        jpb1_2 = new javax.swing.JProgressBar();
        jpb1_3 = new javax.swing.JProgressBar();
        jpb1_4 = new javax.swing.JProgressBar();
        jpb1_5 = new javax.swing.JProgressBar();
        jpb1_6 = new javax.swing.JProgressBar();
        jpb1_7 = new javax.swing.JProgressBar();
        jpb1_8 = new javax.swing.JProgressBar();
        jpb1_9 = new javax.swing.JProgressBar();
        jpb1_10 = new javax.swing.JProgressBar();
        jpb1_11 = new javax.swing.JProgressBar();
        jpb1_13 = new javax.swing.JProgressBar();
        jpb1_14 = new javax.swing.JProgressBar();
        jpb1_12 = new javax.swing.JProgressBar();
        jpb1_15 = new javax.swing.JProgressBar();
        jpb1_16 = new javax.swing.JProgressBar();
        jpb2_1 = new javax.swing.JProgressBar();
        jpb2_2 = new javax.swing.JProgressBar();
        jpb2_3 = new javax.swing.JProgressBar();
        jpb2_4 = new javax.swing.JProgressBar();
        jpb2_5 = new javax.swing.JProgressBar();
        jpb2_6 = new javax.swing.JProgressBar();
        jpb2_7 = new javax.swing.JProgressBar();
        jpb2_8 = new javax.swing.JProgressBar();
        jpb2_9 = new javax.swing.JProgressBar();
        jpb2_10 = new javax.swing.JProgressBar();
        jpb2_11 = new javax.swing.JProgressBar();
        jpb2_12 = new javax.swing.JProgressBar();
        jpb2_13 = new javax.swing.JProgressBar();
        jpb2_14 = new javax.swing.JProgressBar();
        jpb2_15 = new javax.swing.JProgressBar();
        jpb2_16 = new javax.swing.JProgressBar();
        jpb3_1 = new javax.swing.JProgressBar();
        jpb3_2 = new javax.swing.JProgressBar();
        jpb3_3 = new javax.swing.JProgressBar();
        jpb3_4 = new javax.swing.JProgressBar();
        jpb3_5 = new javax.swing.JProgressBar();
        jpb3_6 = new javax.swing.JProgressBar();
        jpb3_7 = new javax.swing.JProgressBar();
        jpb3_8 = new javax.swing.JProgressBar();
        jpb3_9 = new javax.swing.JProgressBar();
        jpb3_10 = new javax.swing.JProgressBar();
        jpb3_11 = new javax.swing.JProgressBar();
        jpb3_12 = new javax.swing.JProgressBar();
        jpb3_13 = new javax.swing.JProgressBar();
        jpb3_14 = new javax.swing.JProgressBar();
        jpb3_15 = new javax.swing.JProgressBar();
        jpb3_16 = new javax.swing.JProgressBar();
        jpb4_1 = new javax.swing.JProgressBar();
        jpb4_3 = new javax.swing.JProgressBar();
        jpb4_2 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        txt1 = new javax.swing.JTextField();
        txt2 = new javax.swing.JTextField();
        txt3 = new javax.swing.JTextField();
        txt4 = new javax.swing.JTextField();
        txt5 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jpb1_17 = new javax.swing.JProgressBar();
        jpb1_18 = new javax.swing.JProgressBar();
        jpb1_19 = new javax.swing.JProgressBar();
        jpb1_20 = new javax.swing.JProgressBar();
        jpb1_21 = new javax.swing.JProgressBar();
        jpb1_22 = new javax.swing.JProgressBar();
        jpb1_23 = new javax.swing.JProgressBar();
        jpb1_24 = new javax.swing.JProgressBar();
        jpb1_25 = new javax.swing.JProgressBar();
        jpb2_17 = new javax.swing.JProgressBar();
        jpb2_18 = new javax.swing.JProgressBar();
        jpb2_19 = new javax.swing.JProgressBar();
        jpb2_20 = new javax.swing.JProgressBar();
        jpb2_21 = new javax.swing.JProgressBar();
        jpb2_22 = new javax.swing.JProgressBar();
        jpb2_23 = new javax.swing.JProgressBar();
        jpb2_24 = new javax.swing.JProgressBar();
        jpb2_25 = new javax.swing.JProgressBar();
        jpb3_17 = new javax.swing.JProgressBar();
        jpb3_18 = new javax.swing.JProgressBar();
        jpb3_19 = new javax.swing.JProgressBar();
        jpb3_20 = new javax.swing.JProgressBar();
        jpb3_21 = new javax.swing.JProgressBar();
        jpb3_22 = new javax.swing.JProgressBar();
        jpb3_23 = new javax.swing.JProgressBar();
        jpb3_24 = new javax.swing.JProgressBar();
        jpb3_25 = new javax.swing.JProgressBar();
        jpb4_4 = new javax.swing.JProgressBar();
        jpb4_5 = new javax.swing.JProgressBar();
        jpb4_6 = new javax.swing.JProgressBar();
        jpb4_7 = new javax.swing.JProgressBar();
        jpb4_8 = new javax.swing.JProgressBar();
        jpb4_9 = new javax.swing.JProgressBar();
        jpb4_10 = new javax.swing.JProgressBar();
        jpb4_11 = new javax.swing.JProgressBar();
        jpb4_12 = new javax.swing.JProgressBar();
        jpb4_13 = new javax.swing.JProgressBar();
        jpb4_14 = new javax.swing.JProgressBar();
        jpb4_15 = new javax.swing.JProgressBar();
        jpb4_16 = new javax.swing.JProgressBar();
        jpb4_17 = new javax.swing.JProgressBar();
        jpb4_18 = new javax.swing.JProgressBar();
        jpb4_19 = new javax.swing.JProgressBar();
        jpb4_20 = new javax.swing.JProgressBar();
        jpb4_21 = new javax.swing.JProgressBar();
        jpb4_22 = new javax.swing.JProgressBar();
        jpb4_23 = new javax.swing.JProgressBar();
        jpb4_24 = new javax.swing.JProgressBar();
        jpb4_25 = new javax.swing.JProgressBar();
        jpb5_1 = new javax.swing.JProgressBar();
        jpb5_2 = new javax.swing.JProgressBar();
        jpb5_3 = new javax.swing.JProgressBar();
        jpb5_4 = new javax.swing.JProgressBar();
        jpb5_5 = new javax.swing.JProgressBar();
        jpb5_6 = new javax.swing.JProgressBar();
        jpb5_7 = new javax.swing.JProgressBar();
        jpb5_8 = new javax.swing.JProgressBar();
        jpb5_9 = new javax.swing.JProgressBar();
        jpb5_10 = new javax.swing.JProgressBar();
        jpb5_11 = new javax.swing.JProgressBar();
        jpb5_12 = new javax.swing.JProgressBar();
        jpb5_13 = new javax.swing.JProgressBar();
        jpb5_14 = new javax.swing.JProgressBar();
        jpb5_15 = new javax.swing.JProgressBar();
        jpb5_16 = new javax.swing.JProgressBar();
        jpb5_17 = new javax.swing.JProgressBar();
        jpb5_18 = new javax.swing.JProgressBar();
        jpb5_19 = new javax.swing.JProgressBar();
        jpb5_20 = new javax.swing.JProgressBar();
        jpb5_21 = new javax.swing.JProgressBar();
        jpb5_22 = new javax.swing.JProgressBar();
        jpb5_23 = new javax.swing.JProgressBar();
        jpb5_24 = new javax.swing.JProgressBar();
        jpb5_25 = new javax.swing.JProgressBar();
        jLabel_1 = new javax.swing.JLabel();
        jLabel_2 = new javax.swing.JLabel();
        jLabel_3 = new javax.swing.JLabel();
        jLabel_4 = new javax.swing.JLabel();
        jLabel_5 = new javax.swing.JLabel();
        jLabel_6 = new javax.swing.JLabel();
        jLabel_7 = new javax.swing.JLabel();
        jLabel_8 = new javax.swing.JLabel();
        jLabel_9 = new javax.swing.JLabel();
        jLabel_10 = new javax.swing.JLabel();
        jLabel_11 = new javax.swing.JLabel();
        jLabel_12 = new javax.swing.JLabel();
        jLabel_13 = new javax.swing.JLabel();
        jLabel_14 = new javax.swing.JLabel();
        jLabel_15 = new javax.swing.JLabel();
        jLabel_16 = new javax.swing.JLabel();
        jLabel_17 = new javax.swing.JLabel();
        jLabel_18 = new javax.swing.JLabel();
        jLabel_19 = new javax.swing.JLabel();
        jLabel_20 = new javax.swing.JLabel();
        jLabel_21 = new javax.swing.JLabel();
        jLabel_22 = new javax.swing.JLabel();
        jLabel_23 = new javax.swing.JLabel();
        jLabel_24 = new javax.swing.JLabel();
        jLabel_25 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        label1_1 = new javax.swing.JLabel();
        label1_2 = new javax.swing.JLabel();
        label1_3 = new javax.swing.JLabel();
        label1_4 = new javax.swing.JLabel();
        label1_5 = new javax.swing.JLabel();
        label1_6 = new javax.swing.JLabel();
        label1_7 = new javax.swing.JLabel();
        label1_8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        label2_1 = new javax.swing.JLabel();
        label2_2 = new javax.swing.JLabel();
        label2_3 = new javax.swing.JLabel();
        label2_4 = new javax.swing.JLabel();
        label2_5 = new javax.swing.JLabel();
        label2_6 = new javax.swing.JLabel();
        label2_7 = new javax.swing.JLabel();
        label2_8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        label3_1 = new javax.swing.JLabel();
        label3_2 = new javax.swing.JLabel();
        label3_3 = new javax.swing.JLabel();
        label3_4 = new javax.swing.JLabel();
        label3_5 = new javax.swing.JLabel();
        label3_6 = new javax.swing.JLabel();
        label3_7 = new javax.swing.JLabel();
        label3_8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableEnd = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CPU Sheduler Simulator 2015");
        setAlwaysOnTop(true);
        setFont(new java.awt.Font("Adobe Arabic", 0, 14)); // NOI18N

        jpb1_1.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_2.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_3.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_4.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_5.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_6.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_7.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_8.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_9.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_10.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_11.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_13.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_14.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_12.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_15.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_16.setForeground(new java.awt.Color(51, 51, 255));

        jpb2_1.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_2.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_3.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_4.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_5.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_6.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_7.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_8.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_9.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_10.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_11.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_12.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_13.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_14.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_15.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_16.setForeground(new java.awt.Color(255, 255, 51));

        jpb3_1.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_2.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_3.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_4.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_5.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_6.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_7.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_8.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_9.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_10.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_11.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_12.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_13.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_14.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_15.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_16.setForeground(new java.awt.Color(255, 51, 51));

        jpb4_1.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_3.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 10)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Job Name");

        txt1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        txt2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        txt3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        txt4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        txt5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setToolTipText("");
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTable.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "<HTML>Arrival<br>    Time</html>", "<HTML>Process <br>Time</HTML>", "<HTML>Remaining<br>Time</HTML>", "<HTML>IO Request<br>Time</HTML>", "<HTML>IO Duration<br>Time</HTML>"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.setName("Details"); // NOI18N
        jTable.setRowHeight(20);
        jScrollPane1.setViewportView(jTable);
        if (jTable.getColumnModel().getColumnCount() > 0) {
            jTable.getColumnModel().getColumn(0).setResizable(false);
            jTable.getColumnModel().getColumn(1).setResizable(false);
            jTable.getColumnModel().getColumn(2).setResizable(false);
            jTable.getColumnModel().getColumn(3).setResizable(false);
            jTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Labels/labels/RQ.png"))); // NOI18N

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Labels/labels/AQ.png"))); // NOI18N

        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Labels/labels/IQ.png"))); // NOI18N

        jpb1_17.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_18.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_19.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_20.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_21.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_22.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_23.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_24.setForeground(new java.awt.Color(51, 51, 255));

        jpb1_25.setForeground(new java.awt.Color(51, 51, 255));

        jpb2_17.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_18.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_19.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_20.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_21.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_22.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_23.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_24.setForeground(new java.awt.Color(255, 255, 51));

        jpb2_25.setForeground(new java.awt.Color(255, 255, 51));

        jpb3_17.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_18.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_19.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_20.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_21.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_22.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_23.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_24.setForeground(new java.awt.Color(255, 51, 51));

        jpb3_25.setForeground(new java.awt.Color(255, 51, 51));

        jpb4_4.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_5.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_6.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_7.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_8.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_9.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_10.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_11.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_12.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_13.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_14.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_15.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_16.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_17.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_18.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_19.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_20.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_21.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_22.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_23.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_24.setForeground(new java.awt.Color(255, 255, 255));

        jpb4_25.setForeground(new java.awt.Color(255, 255, 255));

        jpb5_1.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_2.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_3.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_4.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_5.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_6.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_7.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_8.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_9.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_10.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_11.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_12.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_13.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_14.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_15.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_16.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_17.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_18.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_19.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_20.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_21.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_22.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_23.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_24.setForeground(new java.awt.Color(51, 255, 51));

        jpb5_25.setForeground(new java.awt.Color(51, 255, 51));

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        label1_1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "01", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label1_1.setIconTextGap(0);
        label1_1.setOpaque(true);
        jPanel2.add(label1_1);

        label1_2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "02", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label1_2.setIconTextGap(0);
        label1_2.setOpaque(true);
        jPanel2.add(label1_2);

        label1_3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "03", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label1_3.setIconTextGap(0);
        label1_3.setOpaque(true);
        jPanel2.add(label1_3);

        label1_4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "04", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label1_4.setIconTextGap(0);
        label1_4.setOpaque(true);
        jPanel2.add(label1_4);

        label1_5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "05", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label1_5.setIconTextGap(0);
        label1_5.setOpaque(true);
        jPanel2.add(label1_5);

        label1_6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "06", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label1_6.setIconTextGap(0);
        label1_6.setOpaque(true);
        jPanel2.add(label1_6);

        label1_7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "07", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label1_7.setIconTextGap(0);
        label1_7.setOpaque(true);
        jPanel2.add(label1_7);

        label1_8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "08", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label1_8.setIconTextGap(0);
        label1_8.setOpaque(true);
        jPanel2.add(label1_8);

        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        label2_1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "01", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label2_1.setIconTextGap(0);
        label2_1.setOpaque(true);
        jPanel4.add(label2_1);

        label2_2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "02", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label2_2.setIconTextGap(0);
        label2_2.setOpaque(true);
        jPanel4.add(label2_2);

        label2_3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "03", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label2_3.setIconTextGap(0);
        label2_3.setOpaque(true);
        jPanel4.add(label2_3);

        label2_4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "04", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label2_4.setIconTextGap(0);
        label2_4.setOpaque(true);
        jPanel4.add(label2_4);

        label2_5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "05", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label2_5.setIconTextGap(0);
        label2_5.setOpaque(true);
        jPanel4.add(label2_5);

        label2_6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "06", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label2_6.setIconTextGap(0);
        label2_6.setOpaque(true);
        jPanel4.add(label2_6);

        label2_7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "07", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label2_7.setIconTextGap(0);
        label2_7.setOpaque(true);
        jPanel4.add(label2_7);

        label2_8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "08", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label2_8.setIconTextGap(0);
        label2_8.setOpaque(true);
        jPanel4.add(label2_8);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        label3_1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "01", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label3_1.setIconTextGap(0);
        label3_1.setOpaque(true);
        jPanel1.add(label3_1);

        label3_2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "02", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label3_2.setIconTextGap(0);
        label3_2.setOpaque(true);
        jPanel1.add(label3_2);

        label3_3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "03", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label3_3.setIconTextGap(0);
        label3_3.setOpaque(true);
        jPanel1.add(label3_3);

        label3_4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "04", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label3_4.setIconTextGap(0);
        label3_4.setOpaque(true);
        jPanel1.add(label3_4);

        label3_5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "05", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label3_5.setIconTextGap(0);
        label3_5.setOpaque(true);
        jPanel1.add(label3_5);

        label3_6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "06", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label3_6.setIconTextGap(0);
        label3_6.setOpaque(true);
        jPanel1.add(label3_6);

        label3_7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "07", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label3_7.setIconTextGap(0);
        label3_7.setOpaque(true);
        jPanel1.add(label3_7);

        label3_8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "08", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 8))); // NOI18N
        label3_8.setIconTextGap(0);
        label3_8.setOpaque(true);
        jPanel1.add(label3_8);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Labels/labels/Layer.png"))); // NOI18N

        jTableEnd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Job Name", "Turn Arround Time", "Waiting Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableEnd);
        if (jTableEnd.getColumnModel().getColumnCount() > 0) {
            jTableEnd.getColumnModel().getColumn(0).setResizable(false);
            jTableEnd.getColumnModel().getColumn(1).setResizable(false);
            jTableEnd.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt1)
                            .addComponent(txt2)
                            .addComponent(txt3)
                            .addComponent(txt4)
                            .addComponent(txt5, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel_1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_13, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_14, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_15, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_16, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_18, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_19, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_20, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_21, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_22, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_23, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_24, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel_25, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jpb4_1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_13, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_14, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_15, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_16, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_18, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_19, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_20, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_21, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_22, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_23, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_24, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb4_25, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jpb2_1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_13, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_14, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_15, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_16, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_18, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_19, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_20, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_21, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_22, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_23, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_24, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb2_25, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jpb1_1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_13, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_14, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_15, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_16, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_18, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_19, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_20, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_21, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_22, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_23, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_24, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb1_25, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jpb3_1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_13, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_14, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_15, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_16, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_18, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_19, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_20, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_21, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_22, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_23, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_24, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jpb3_25, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jpb5_1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_13, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_14, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_15, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_16, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_18, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_19, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_20, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_21, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_22, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_23, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_24, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jpb5_25, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel64)
                            .addComponent(jLabel43))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 726, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_14, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_16, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_17, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_18, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_19, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_20, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_21, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_22, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_23, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_24, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_25, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jpb1_10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_22, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_24, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb1_25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jpb2_10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_22, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_24, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb2_25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jpb3_10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_22, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_24, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb3_25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jpb4_1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_22, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_24, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb4_25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_22, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_24, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb5_25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel22))
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel43))
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel64)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    void fillProgressBar(int raw, int runTime) {//fill given progress bars nicely

        new Thread() {
            public void run() {
                for (int ti = 0; ti < runTime; ti++) {
                    labellist[column].setText(Integer.toString(column));//visible the running time on the top
                    for (int i = 1; i < 101; i++) {
                        jpblist[raw][column].setValue(i);
                        try {
                            Thread.sleep(10 * factor - 3);//--------------------------------------------------------------------
                        } catch (InterruptedException ex) {
                            Logger.getLogger(OSGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    column++;
                }
                locked = false;
            }
        }.start();

    }

    public void skip() {
        if (column < 25) {
            labellist[column].setText(Integer.toString(column));//visible the running time on the top
        }
        column++;

    }

    public void enqueue(int whichQueue, int index, int remainingTime, int totalTime) {//0=ready, 1=auxiliary, 2= io
        //enqueue a queue
        for (int y = 0; y < 7; y++) {
            if ("".equals(queuelist[whichQueue][y].getText())) {
                queuelist[whichQueue][y].setText("Job_" + Integer.toString(index + 1) + "  (" + Integer.toString(remainingTime) + "/" + Integer.toString(totalTime) + ")");
                queuelist[whichQueue][y].setBackground(colo[index]);
                queuelist[whichQueue][y].setVisible(true);
                break;
            }
        }
    }

    public void dequeue(int whichQueue, int index) {
//dequeue a queue
        for (int y = 0; y < 7; y++) {
            try {
                queuelist[whichQueue][y].setBackground(colo[(Integer.parseInt(queuelist[whichQueue][y + 1].getText().substring(4, 5))) - 1]);
            } catch (Exception e) {
                queuelist[whichQueue][y].setBackground(null);
            }
            queuelist[whichQueue][y].setText(queuelist[whichQueue][y + 1].getText());
        }

        for (int y = 0; y < 7; y++) {//set empty lables to not visible
            if ("".equals(queuelist[whichQueue][y].getText())) {
                queuelist[whichQueue][y].setVisible(false);
            }
        }

    }

    public void display(int index, int runTime, int arrivalTime, int proccessTime, int remainingTime) {
//the function which the processer uses to display the processes
        if (!locked) {
            locked = true;
            txtlist[index].setText("Job_" + Integer.toString(index + 1));//set datas to the table while running
            jTable.setValueAt(arrivalTime, index, 0);
            jTable.setValueAt(proccessTime, index, 1);
            jTable.setValueAt(remainingTime, index, 2);

            fillProgressBar(index, runTime);
        }
    }

    public void FinalResultDisplay(int[][] processDetails) {//show final results(turn arround time,waiting time) on the bottom table
        for (int sd = 0; sd < processDetails.length; sd++) {
            jTableEnd.setValueAt("Job_" + Integer.toString(sd + 1), sd, 0);
            jTableEnd.setValueAt(processDetails[sd][0], sd, 1);
            jTableEnd.setValueAt(processDetails[sd][1], sd, 2);

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OSGUI().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel_1;
    private javax.swing.JLabel jLabel_10;
    private javax.swing.JLabel jLabel_11;
    private javax.swing.JLabel jLabel_12;
    private javax.swing.JLabel jLabel_13;
    private javax.swing.JLabel jLabel_14;
    private javax.swing.JLabel jLabel_15;
    private javax.swing.JLabel jLabel_16;
    private javax.swing.JLabel jLabel_17;
    private javax.swing.JLabel jLabel_18;
    private javax.swing.JLabel jLabel_19;
    private javax.swing.JLabel jLabel_2;
    private javax.swing.JLabel jLabel_20;
    private javax.swing.JLabel jLabel_21;
    private javax.swing.JLabel jLabel_22;
    private javax.swing.JLabel jLabel_23;
    private javax.swing.JLabel jLabel_24;
    private javax.swing.JLabel jLabel_25;
    private javax.swing.JLabel jLabel_3;
    private javax.swing.JLabel jLabel_4;
    private javax.swing.JLabel jLabel_5;
    private javax.swing.JLabel jLabel_6;
    private javax.swing.JLabel jLabel_7;
    private javax.swing.JLabel jLabel_8;
    private javax.swing.JLabel jLabel_9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable;
    private javax.swing.JTable jTableEnd;
    private javax.swing.JProgressBar jpb1_1;
    private javax.swing.JProgressBar jpb1_10;
    private javax.swing.JProgressBar jpb1_11;
    private javax.swing.JProgressBar jpb1_12;
    private javax.swing.JProgressBar jpb1_13;
    private javax.swing.JProgressBar jpb1_14;
    private javax.swing.JProgressBar jpb1_15;
    private javax.swing.JProgressBar jpb1_16;
    private javax.swing.JProgressBar jpb1_17;
    private javax.swing.JProgressBar jpb1_18;
    private javax.swing.JProgressBar jpb1_19;
    private javax.swing.JProgressBar jpb1_2;
    private javax.swing.JProgressBar jpb1_20;
    private javax.swing.JProgressBar jpb1_21;
    private javax.swing.JProgressBar jpb1_22;
    private javax.swing.JProgressBar jpb1_23;
    private javax.swing.JProgressBar jpb1_24;
    private javax.swing.JProgressBar jpb1_25;
    private javax.swing.JProgressBar jpb1_3;
    private javax.swing.JProgressBar jpb1_4;
    private javax.swing.JProgressBar jpb1_5;
    private javax.swing.JProgressBar jpb1_6;
    private javax.swing.JProgressBar jpb1_7;
    private javax.swing.JProgressBar jpb1_8;
    private javax.swing.JProgressBar jpb1_9;
    private javax.swing.JProgressBar jpb2_1;
    private javax.swing.JProgressBar jpb2_10;
    private javax.swing.JProgressBar jpb2_11;
    private javax.swing.JProgressBar jpb2_12;
    private javax.swing.JProgressBar jpb2_13;
    private javax.swing.JProgressBar jpb2_14;
    private javax.swing.JProgressBar jpb2_15;
    private javax.swing.JProgressBar jpb2_16;
    private javax.swing.JProgressBar jpb2_17;
    private javax.swing.JProgressBar jpb2_18;
    private javax.swing.JProgressBar jpb2_19;
    private javax.swing.JProgressBar jpb2_2;
    private javax.swing.JProgressBar jpb2_20;
    private javax.swing.JProgressBar jpb2_21;
    private javax.swing.JProgressBar jpb2_22;
    private javax.swing.JProgressBar jpb2_23;
    private javax.swing.JProgressBar jpb2_24;
    private javax.swing.JProgressBar jpb2_25;
    private javax.swing.JProgressBar jpb2_3;
    private javax.swing.JProgressBar jpb2_4;
    private javax.swing.JProgressBar jpb2_5;
    private javax.swing.JProgressBar jpb2_6;
    private javax.swing.JProgressBar jpb2_7;
    private javax.swing.JProgressBar jpb2_8;
    private javax.swing.JProgressBar jpb2_9;
    private javax.swing.JProgressBar jpb3_1;
    private javax.swing.JProgressBar jpb3_10;
    private javax.swing.JProgressBar jpb3_11;
    private javax.swing.JProgressBar jpb3_12;
    private javax.swing.JProgressBar jpb3_13;
    private javax.swing.JProgressBar jpb3_14;
    private javax.swing.JProgressBar jpb3_15;
    private javax.swing.JProgressBar jpb3_16;
    private javax.swing.JProgressBar jpb3_17;
    private javax.swing.JProgressBar jpb3_18;
    private javax.swing.JProgressBar jpb3_19;
    private javax.swing.JProgressBar jpb3_2;
    private javax.swing.JProgressBar jpb3_20;
    private javax.swing.JProgressBar jpb3_21;
    private javax.swing.JProgressBar jpb3_22;
    private javax.swing.JProgressBar jpb3_23;
    private javax.swing.JProgressBar jpb3_24;
    private javax.swing.JProgressBar jpb3_25;
    private javax.swing.JProgressBar jpb3_3;
    private javax.swing.JProgressBar jpb3_4;
    private javax.swing.JProgressBar jpb3_5;
    private javax.swing.JProgressBar jpb3_6;
    private javax.swing.JProgressBar jpb3_7;
    private javax.swing.JProgressBar jpb3_8;
    private javax.swing.JProgressBar jpb3_9;
    private javax.swing.JProgressBar jpb4_1;
    private javax.swing.JProgressBar jpb4_10;
    private javax.swing.JProgressBar jpb4_11;
    private javax.swing.JProgressBar jpb4_12;
    private javax.swing.JProgressBar jpb4_13;
    private javax.swing.JProgressBar jpb4_14;
    private javax.swing.JProgressBar jpb4_15;
    private javax.swing.JProgressBar jpb4_16;
    private javax.swing.JProgressBar jpb4_17;
    private javax.swing.JProgressBar jpb4_18;
    private javax.swing.JProgressBar jpb4_19;
    private javax.swing.JProgressBar jpb4_2;
    private javax.swing.JProgressBar jpb4_20;
    private javax.swing.JProgressBar jpb4_21;
    private javax.swing.JProgressBar jpb4_22;
    private javax.swing.JProgressBar jpb4_23;
    private javax.swing.JProgressBar jpb4_24;
    private javax.swing.JProgressBar jpb4_25;
    private javax.swing.JProgressBar jpb4_3;
    private javax.swing.JProgressBar jpb4_4;
    private javax.swing.JProgressBar jpb4_5;
    private javax.swing.JProgressBar jpb4_6;
    private javax.swing.JProgressBar jpb4_7;
    private javax.swing.JProgressBar jpb4_8;
    private javax.swing.JProgressBar jpb4_9;
    private javax.swing.JProgressBar jpb5_1;
    private javax.swing.JProgressBar jpb5_10;
    private javax.swing.JProgressBar jpb5_11;
    private javax.swing.JProgressBar jpb5_12;
    private javax.swing.JProgressBar jpb5_13;
    private javax.swing.JProgressBar jpb5_14;
    private javax.swing.JProgressBar jpb5_15;
    private javax.swing.JProgressBar jpb5_16;
    private javax.swing.JProgressBar jpb5_17;
    private javax.swing.JProgressBar jpb5_18;
    private javax.swing.JProgressBar jpb5_19;
    private javax.swing.JProgressBar jpb5_2;
    private javax.swing.JProgressBar jpb5_20;
    private javax.swing.JProgressBar jpb5_21;
    private javax.swing.JProgressBar jpb5_22;
    private javax.swing.JProgressBar jpb5_23;
    private javax.swing.JProgressBar jpb5_24;
    private javax.swing.JProgressBar jpb5_25;
    private javax.swing.JProgressBar jpb5_3;
    private javax.swing.JProgressBar jpb5_4;
    private javax.swing.JProgressBar jpb5_5;
    private javax.swing.JProgressBar jpb5_6;
    private javax.swing.JProgressBar jpb5_7;
    private javax.swing.JProgressBar jpb5_8;
    private javax.swing.JProgressBar jpb5_9;
    private javax.swing.JLabel label1_1;
    private javax.swing.JLabel label1_2;
    private javax.swing.JLabel label1_3;
    private javax.swing.JLabel label1_4;
    private javax.swing.JLabel label1_5;
    private javax.swing.JLabel label1_6;
    private javax.swing.JLabel label1_7;
    private javax.swing.JLabel label1_8;
    private javax.swing.JLabel label2_1;
    private javax.swing.JLabel label2_2;
    private javax.swing.JLabel label2_3;
    private javax.swing.JLabel label2_4;
    private javax.swing.JLabel label2_5;
    private javax.swing.JLabel label2_6;
    private javax.swing.JLabel label2_7;
    private javax.swing.JLabel label2_8;
    private javax.swing.JLabel label3_1;
    private javax.swing.JLabel label3_2;
    private javax.swing.JLabel label3_3;
    private javax.swing.JLabel label3_4;
    private javax.swing.JLabel label3_5;
    private javax.swing.JLabel label3_6;
    private javax.swing.JLabel label3_7;
    private javax.swing.JLabel label3_8;
    private javax.swing.JTextField txt1;
    private javax.swing.JTextField txt2;
    private javax.swing.JTextField txt3;
    private javax.swing.JTextField txt4;
    private javax.swing.JTextField txt5;
    // End of variables declaration//GEN-END:variables
}
