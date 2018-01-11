import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestingUnit {
    ArrayList<Robot> robots;
    Random random = new Random();
    public static int totalRequestsNumber = 0;
    long startProgram = 0;

    public static void main(String[] args) {
        new TestingUnit().init();
    }

    private void init() {
        int ROBOTS_NUMBER = 1;
        // Create an array of fake Robots.
        robots = new ArrayList<Robot>(ROBOTS_NUMBER);
        for (int i = 0; i < ROBOTS_NUMBER; i++) {
            robots.add(createFakeRobot());
        }

        System.out.println("Array has been created.");
        startProgram = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new shutDownHook(startProgram));


        ExecutorService executor = Executors.newFixedThreadPool(4);//creating a pool of 4 threads

        while (true) {

            int index = random.nextInt(((ROBOTS_NUMBER - 1) - 0) + 1) + 0;
            Robot currRobot = robots.get(index);

            Runnable worker = new SenderThread(currRobot);
            executor.execute(worker); //calling execute method of ExecutorService

//            sendData(currRobot);
            totalRequestsNumber++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    // Create a new robot with random data.
    private Robot createFakeRobot() {
        String robotId = UUID.randomUUID().toString().replace("-", "").substring(0, 30);
        String clusterId = UUID.randomUUID().toString().replace("-", "").substring(0, 30);
        String zoneId = UUID.randomUUID().toString().replace("-", "").substring(0, 30);

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

        public SenderThread(Robot robot) {
            this.robot = robot;
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
            URL url = null;
            try {
                url = new URL("http://127.0.0.1:9000/robots");
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
    public class shutDownHook extends Thread {
        long startTime;

        public shutDownHook(long startTime) {
            this.startTime = startTime;
        }

        @Override
        public void run() {
            long endTime = System.currentTimeMillis();
            System.out.println("I've run for : " + (endTime - startTime) + "ms and I've done :" + totalRequestsNumber + " requests.");
        }
    }
}
