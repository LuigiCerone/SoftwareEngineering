import org.json.JSONObject;

import java.io.IOException;
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


        // Create an array of fake Robots.
        robots = new ArrayList<Robot>(90000);
        for (int i = 0; i < 90000; i++) {
            robots.add(createFakeRobot());
        }

        System.out.println("Array has been created.");
        startProgram = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new shutDownHook(startProgram));
        ExecutorService executor = Executors.newFixedThreadPool(4);//creating a pool of 4 threads

//        executor.shutdown();
//        while (!executor.isTerminated()) {
//        }

        while (true) {

            int index = random.nextInt((90000 - 0) + 1) + 0;
            Robot currRobot = robots.get(index);

            Runnable worker = new SenderThread(currRobot);
            executor.execute(worker);//calling execute method of ExecutorService

//            sendData(currRobot);
            totalRequestsNumber++;
        }
    }

    private Robot createFakeRobot() {
        String robotId = UUID.randomUUID().toString().replace("-", "").substring(0, 30);
        String clusterId = UUID.randomUUID().toString().replace("-", "").substring(0, 30);
        String zoneId = UUID.randomUUID().toString().replace("-", "").substring(0, 30);

        // [0] is not used
        boolean[] signals = new boolean[8];
        for (int j = 1; j < signals.length; j++) {
            signals[j] = random.nextBoolean();
        }

        return new Robot(robotId, clusterId, zoneId, signals);
    }

    public class SenderThread implements Runnable {
        Robot robot;

        public SenderThread(Robot robot) {
            this.robot = robot;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("robot", robot.getRobotId());
            jsonObject.put("cluster", robot.getClusterId());
            jsonObject.put("zone", robot.getZoneId());

            int randomSignal = random.nextInt((7 - 1) + 1) + 1;
            boolean oldValue = robot.getSignal(randomSignal);
            jsonObject.put("signal", randomSignal);
            jsonObject.put("value", !oldValue);

            Timestamp now = new Timestamp(new Date().getTime());
            jsonObject.put("timestamp", now.toString());

            // Uncomment to see the data.
//        System.out.println(jsonObject.toString());

            // Now we create a POST request with jsonObject as data.
            sendPost(jsonObject);

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
//            InputStream in = new BufferedInputStream(conn.getInputStream());
//            in.close();
                conn.disconnect();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public class shutDownHook extends Thread {
        long startTime;

        public shutDownHook(long startTime) {
            this.startTime = startTime;
        }

        @Override
        public void run() {
            long endTime = System.currentTimeMillis();
//            totalRequestsNumber
            System.out.println("I've run for : " + (endTime - startTime) + "ms and I've done :" + totalRequestsNumber + " requests.");
        }
    }
}
