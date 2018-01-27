package main.Main;

import main.HttpServer.DashboardIRThread;
import main.HttpServer.HttpServerInit;
import main.HttpServer.OldHistoryCleanerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends JFrame {
    static JTextPane hostIP;

    public static void main(String[] args) throws Exception {

        // JFrame stuff========================================================
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");

        JFrame frame = new JFrame("SEProject");
        frame.setResizable(false);
        frame.setSize(300, 100);
        Container frameContentPane = frame.getContentPane();
        frameContentPane.setLayout(new FlowLayout());
        frameContentPane.add(startButton);
        frameContentPane.add(stopButton);
        frameContentPane.add(new JLabel("IP for the webSocket is: "));

        hostIP = new JTextPane();
        hostIP.setEditable(false); // as before
        hostIP.setBackground(null); // this is the same as a JLabel
        hostIP.setBorder(null); // remove the border


        frameContentPane.add(hostIP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
        frame.setVisible(true);
        //====================================================================

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {

                        try {
                            new HttpServerInit();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        ScheduledExecutorService scheduler =
                                Executors.newSingleThreadScheduledExecutor();

                        // Every 5 mins system calculate the ir.
                        scheduler.scheduleAtFixedRate(new DashboardIRThread(), 1, 5, TimeUnit.MINUTES);
                        // Every 90 mins system cleans the old unused histories.
                        scheduler.scheduleAtFixedRate(new OldHistoryCleanerThread(), 30, 90, TimeUnit.MINUTES);
                        System.out.println("The system is running!");
                    }
                }.start();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


    }

    public static void setIP(String IP) {
        hostIP.setText("" + IP);
    }
}
