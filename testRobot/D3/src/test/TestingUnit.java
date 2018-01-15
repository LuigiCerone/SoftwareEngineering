package src.test;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestingUnit extends JFrame {
    ArrayList<src.test.Robot> robots;
    Random random = new Random();
    public static int totalRequestsNumber = 0;
    static long startTime = 0;
    static boolean userWants = true;


    public static void main(String[] args) {
        TestingUnit testingUnit = new TestingUnit();

        JButton STARTButton = new JButton("Start");
        JButton STOPButton = new JButton("Stop");
        final JTextField ipAddress = new JTextField(15);
        final JTextField robotNumber = new JTextField(6);
        final JTextField pauseSize = new JTextField(6);

        final JTextArea stats = new JTextArea();
        stats.setEditable(false);


        JFrame frame = new JFrame("Testing unit");
//        frame.setSize(400, 400);
        frame.setResizable(false);

        Container frameContentPane = frame.getContentPane();

        frameContentPane.setLayout(new BoxLayout(frameContentPane, BoxLayout.Y_AXIS));


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(STARTButton);
        buttonsPanel.add(STOPButton);
        frameContentPane.add(buttonsPanel);

        // ==================================================================0

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        JLabel ipLabel = new JLabel("Server IP: ");
        inputPanel.add(ipLabel);
        inputPanel.add(ipAddress);

        JLabel robotNumberLabel = new JLabel("Robots number: ");
        inputPanel.add(robotNumberLabel);
        inputPanel.add(robotNumber);

        JLabel pauseSizeLabel = new JLabel("Pause size: ");
        inputPanel.add(pauseSizeLabel);
        inputPanel.add(pauseSize);
        frameContentPane.add(inputPanel);

        // ==================================================================

        JPanel logPanel = new JPanel();
        logPanel.setLayout(new FlowLayout());
        JLabel infoLabel = new JLabel("Info: ");
        logPanel.add(infoLabel);

        logPanel.add(stats);

        frameContentPane.add(logPanel);


//        frame.setContentPane(new StartTest().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        STARTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(ipAddress.getText() + " " + robotNumber.getText() + " " + pauseSize.getText());
                userWants = true;

                totalRequestsNumber = 0;
                new Thread() {
                    public void run() {
                        testingUnit.run(ipAddress.getText(), robotNumber.getText(), pauseSize.getText());
                    }
                }.start();
            }
        });

        STOPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long endTime = System.currentTimeMillis();
//                stats.setText("AAA");
                stats.setText(" " + totalRequestsNumber + " requests made in " + (endTime - startTime) + " ms.");
//                System.exit(0);
                userWants = false;
            }
        });
    }

    private void run(String ipAddressText, String robotNumber, String pauseSize) {
        URL url = null;
        try {
            url = new URL("http://" + ipAddressText + ":9000/robots");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            try {
                url = new URL("http://127.0.0.1:9000/robots");
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        }
//        int ROBOTS_NUMBER = 1;
        int ROBOTS_NUMBER;
        try {
            ROBOTS_NUMBER = Integer.parseInt(robotNumber);
        } catch (Exception e) {
            ROBOTS_NUMBER = 1000;
        }
        // Create an array of fake Robots.
        robots = new ArrayList<Robot>(ROBOTS_NUMBER);
        for (int i = 0; i < ROBOTS_NUMBER; i++) {
            robots.add(createFakeRobot());
        }

        System.out.println("Array has been created.");
        startTime = System.currentTimeMillis();
//        Runtime.getRuntime().addShutdownHook(new shutDownHook(startProgram));

        ExecutorService executor = Executors.newFixedThreadPool(4);//creating a pool of 4 threads

        while (userWants) {

            int index = random.nextInt((ROBOTS_NUMBER - 1) + 1);
            Robot currRobot = robots.get(index);

            Runnable worker = new SenderThread(currRobot, url);
            executor.execute(worker); //calling execute method of ExecutorService

//            sendData(currRobot);
            totalRequestsNumber++;
            long pause;
            try {
                pause = Long.parseLong(pauseSize);
            } catch (NumberFormatException e) {
                pause = 1000;
            }
            try {
                Thread.sleep(pause);
//                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    // Create a new robot with random data.
    private Robot createFakeRobot() {
        String robotId = UUID.randomUUID().toString().replace("-", "").substring(0, 17);
        String clusterId = UUID.randomUUID().toString().replace("-", "").substring(0, 17);
        String zoneId = UUID.randomUUID().toString().replace("-", "").substring(0, 17);

        // [0] is not used
        boolean[] signals = new boolean[8];
        for (int j = 1; j < signals.length; j++) {
//            For random value for the signals.
//            signals[j] = random.nextBoolean();

//            All true signals.
            signals[j] = true;
        }
        return new Robot(robotId, clusterId, zoneId, signals);
    }

    // Thread that create a JSON payload and sent it with a POST requests to our system.
    public class SenderThread implements Runnable {
        Robot robot;
        URL url = null;

        public SenderThread(Robot robot, URL url) {
            this.robot = robot;
            this.url = url;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            try {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("robot", robot.getRobotId());

                jsonObject.put("cluster", robot.getClusterId());
                jsonObject.put("zone", robot.getZoneId());

                int randomSignal = random.nextInt((7 - 1) + 1) + 1;
                boolean oldValue = robot.getSignal(randomSignal);
                robot.invertSignal(randomSignal);
                jsonObject.put("signal", randomSignal);
                jsonObject.put("value", !oldValue);

                Timestamp now = new Timestamp(new Date().getTime());
                jsonObject.put("timestamp", now.toString());

                // Uncomment to see the data.
                // System.out.println(jsonObject.toString());

                // Now we create a POST request with jsonObject as data.
                sendPost(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            long end = System.currentTimeMillis();
            System.out.println("One single request took : " + (end - start) + "ms.");

        }

        private void sendPost(JSONObject jsonObject) {

            try {
//                url = new URL("http://127.0.0.1:9000/robots");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(0);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStream os = conn.getOutputStream();

                os.write(jsonObject.toString().getBytes("UTF-8"));
                os.close();
                // Response (do I need this?).
                InputStream in = new BufferedInputStream(conn.getInputStream());
//                System.out.println(in.toString());

                in.close();
                conn.disconnect();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Thread used for display statistics when the Virtual Machine exit() is called.
//    public class shutDownHook extends Thread {
//        long startTime;
//
//        public shutDownHook(long startTime) {
//            this.startTime = startTime;
//        }
//
//        @Override
//        public void run() {
//            long endTime = System.currentTimeMillis();
//            System.out.println("I've run for : " + (endTime - startTime) + "ms and I've done :" + totalRequestsNumber + " requests.");
//        }
//    }
}
