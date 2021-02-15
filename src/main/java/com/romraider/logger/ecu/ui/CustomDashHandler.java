package com.romraider.logger.ecu.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JWindow;
import javax.swing.table.TableColumn;

import jdk.internal.org.objectweb.asm.Label;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.romraider.logger.ecu.comms.query.Response;
import com.romraider.logger.ecu.definition.ConvertorUpdateListener;
import com.romraider.logger.ecu.definition.LoggerData;
import com.romraider.logger.ecu.ui.handler.DataUpdateHandler;

public final class CustomDashHandler implements DataUpdateHandler, ConvertorUpdateListener {

    private JLabel gearValue;
    private JLabel coolantValue;
    private JLabel iatValue;
    private JLabel speedValue;
    private JLabel mafValue;
    private JLabel rpmValue;

    private String gearVal = "N";
    private int coolantVal = 0;
    private int iatVal = 0;
    private int speedVal = 0;
    private int mafVal = 0;
    private int rpmVal = 0;
    
    private ImageIcon[] rpmIcons;
    private int[][] speedArr =  {{6,11,16,22,28},{8,15,22,30,37},{11,18,28,37,46},{13,22,33,45,56},{15,26,39,52,65},{17,30,45,60,75},{20,33,50,68,84},{22,37,56,75,93},{24,41,62,83,103},{26,45,67,90,112},{29,48,73,98,121},{31,52,79,105,131},{33,56,84,113,140},{35,60,90,121,150},{37,64,96,128,159},{40,67,101,136,168},{42,71,107,143,178},{44,75,113,151,187},{46,79,118,158,196},{49,82,124,166,206},{51,86,129,173,215},{53,90,135,181,225},{55,94,141,189,234},{58,97,146,196,243},{60,101,152,204,253},{62,105,158,211,262}};
    private int[] rpmArr = {750, 1000, 1250, 1500, 1750, 2000, 2250, 2500, 2750, 3000, 3250, 3500, 3750, 4000, 4250, 4500, 4750, 5000, 5250, 5500, 5750, 6000, 6250, 6500, 6750, 7000};

    public void initDash() {
        rpmIcons = new ImageIcon[18];
        for (int i = 0; i < rpmIcons.length; i++) {
            try {
                // Load the background image
                BufferedImage img = ImageIO.read(new File("dash/tach/" + i + ".jpg"));
                rpmIcons[i] = new ImageIcon(img);
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
        
        JFrame frame = new JFrame("Dash");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(1024, 600));
        panel.setOpaque(true);
        
        JLabel bg = new JLabel();
        try {
            // Load the background image
            BufferedImage img = ImageIO.read(new File("dash/bg.jpg"));
            bg.setIcon(new ImageIcon(img));
            bg.setBounds(0, 0, 1024, 600);
            panel.add(bg);

        } catch (IOException exp) {
            exp.printStackTrace();
        }        

        JLabel coolant = new JLabel("COOLANT");
        coolant.setForeground(java.awt.Color.WHITE);
        coolant.setBounds(100, 200, 200, 60);
        coolant.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        coolant.setFont(new java.awt.Font("Serif Bold", java.awt.Font.PLAIN, 24));
        panel.add(coolant);
        panel.setComponentZOrder(coolant, 0);

        JLabel iat = new JLabel("IAT (°C)");
        iat.setForeground(java.awt.Color.WHITE);
        iat.setBounds(100, 390, 200, 60);
        iat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iat.setFont(new java.awt.Font("Serif Bold", java.awt.Font.PLAIN, 24));
        panel.add(iat);
        panel.setComponentZOrder(iat, 1);

        JLabel speed = new JLabel("SPEED");
        speed.setForeground(java.awt.Color.WHITE);
        speed.setBounds(720, 200, 200, 60);
        speed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        speed.setFont(new java.awt.Font("Serif Bold", java.awt.Font.PLAIN, 24));
        panel.add(speed);
        panel.setComponentZOrder(speed, 2);

        JLabel maf = new JLabel("MAF (g/s)");
        maf.setForeground(java.awt.Color.WHITE);
        maf.setBounds(720, 390, 200, 60);
        maf.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        maf.setFont(new java.awt.Font("Serif Bold", java.awt.Font.PLAIN, 24));
        panel.add(maf);
        panel.setComponentZOrder(maf, 3);

        JLabel gear = new JLabel("GEAR");
        gear.setForeground(java.awt.Color.WHITE);
        gear.setBounds(410, 200, 200, 60);
        gear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gear.setFont(new java.awt.Font("Serif Bold", java.awt.Font.PLAIN, 24));
        panel.add(gear);
        panel.setComponentZOrder(gear, 4);

        coolantValue = new JLabel("-");
        coolantValue.setForeground(java.awt.Color.WHITE);
        coolantValue.setBounds(100, 200, 200, 190);
        coolantValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        coolantValue.setFont(new java.awt.Font("Serif Bold", java.awt.Font.PLAIN, 80));
        panel.add(coolantValue);
        panel.setComponentZOrder(coolantValue, 5);

        iatValue = new JLabel("-");
        iatValue.setForeground(java.awt.Color.WHITE);
        iatValue.setBounds(100, 390, 200, 190);
        iatValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iatValue.setFont(new java.awt.Font("Serif Bold", java.awt.Font.PLAIN, 80));
        panel.add(iatValue);
        panel.setComponentZOrder(iatValue, 6);

        speedValue = new JLabel("-");
        speedValue.setForeground(java.awt.Color.WHITE);
        speedValue.setBounds(720, 200, 200, 190);
        speedValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        speedValue.setFont(new java.awt.Font("Serif Bold", java.awt.Font.PLAIN, 80));
        panel.add(speedValue);
        panel.setComponentZOrder(speedValue, 7);

        mafValue = new JLabel("-");
        mafValue.setForeground(java.awt.Color.WHITE);
        mafValue.setBounds(720, 390, 200, 190);
        mafValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mafValue.setFont(new java.awt.Font("Serif Bold", java.awt.Font.PLAIN, 80));
        panel.add(mafValue);
        panel.setComponentZOrder(mafValue, 8);

        gearValue = new JLabel("-");
        gearValue.setForeground(java.awt.Color.WHITE);
        gearValue.setBounds(410, 200, 200, 380);
        gearValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gearValue.setFont(new java.awt.Font("Serif Bold", java.awt.Font.PLAIN, 260));
        panel.add(gearValue);
        panel.setComponentZOrder(gearValue, 9);

        rpmValue = new JLabel();
        rpmValue.setIcon(rpmIcons[rpmVal]);
        rpmValue.setBounds(39, 50, 946, 94);
        panel.add(rpmValue);
        panel.setComponentZOrder(gearValue, 10);
        
        panel.setComponentZOrder(bg, 11);
        
        frame.add(panel);

        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);

        updateDash();
    }

    private void testVals() {
        coolantVal = 90;
        iatVal = 35;
        speedVal = 62;
        mafVal = 167;
        rpmVal = 2100;

        updateDash();
    }

    private void calculateGear() {
        int rpmIndex = findNearest(rpmArr, rpmVal);
        int speedIndex = findNearest(speedArr[rpmIndex], Integer.valueOf(speedVal));
        
        gearVal = String.valueOf(speedIndex + 1);
    }

    public void updateDash() {

        if (coolantVal > 0) {
            coolantValue.setText(String.valueOf(coolantVal));
        } else {
            coolantValue.setText("-");
        }
        if (iatVal > 0) {
            iatValue.setText(String.valueOf(iatVal));
        } else {
            iatValue.setText("-");
        }
        if (speedVal > 0) {
            speedValue.setText(String.valueOf(speedVal));
            calculateGear();
            gearValue.setText(String.valueOf(gearVal));
        } else {
            speedValue.setText("-");
            gearValue.setText("N");
        }
        if (mafVal > 0) {
            mafValue.setText(String.valueOf(mafVal));
        } else {
            mafValue.setText("-");
        }

        rpmValue.setIcon(rpmIcons[getRpmTachIndex(rpmVal)]);
    }

    private int findNearest(int[] tarray, int value) {
        int idx = 0;
        int distance = Math.abs(tarray[0] - value);
        for (int c = 0; c < tarray.length; c++) {
            int cdistance = Math.abs(tarray[c] - value);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        return idx;
    }

    private int getRpmTachIndex(int rpm) {
        double divider = 6800 / rpmIcons.length;
        int rpmTach = (int) Math.floor(rpm / divider);
        if (rpmTach > rpmIcons.length - 1) {
            rpmTach = rpmIcons.length - 1;
        }
        return rpmTach;
    }

    public synchronized void registerData(final LoggerData loggerData) {
        System.out.print(loggerData.getName());
    }

    public synchronized void handleDataUpdate(Response response) {
        for (LoggerData loggerData : response.getData()) {
            String lId = loggerData.getId();
            if (lId.equals("P2")) {
                coolantVal = (int)response.getDataValue(loggerData);
            } else if (lId.equals("P12")) {
                mafVal = (int)response.getDataValue(loggerData);
            } else if (lId.equals("P11")) {
                iatVal = (int)response.getDataValue(loggerData);
            } else if (lId.equals("P9")) {
                speedVal = (int)response.getDataValue(loggerData);
            } else if (lId.equals("P8")) {
                rpmVal = (int)response.getDataValue(loggerData);             
            }
        }
        updateDash();
    }

    public synchronized void deregisterData(LoggerData loggerData) {
    }

    public synchronized void cleanUp() {
    }

    public synchronized void reset() {
    }

    public synchronized void notifyConvertorUpdate(LoggerData updatedLoggerData) {
    }
}