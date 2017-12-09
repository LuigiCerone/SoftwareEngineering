package main.Controller;

import main.Model.DAO.RobotDAO;
import main.Model.DAO.SignalDAO;
import main.Model.ReadData;
import main.Model.Robot;
import main.Model.Signal;

import java.sql.Timestamp;

public class ControllerIRRobot {


    public void updateComponentState(Robot robot, ReadData readData, RobotDAO robotDAO) {
        long downTimeDiff = 0;
        // Modify the robot counter stat.
        int countInefficiencyComponents = robot.updateComponentState(readData.getValue());
        System.out.println(countInefficiencyComponents);
        // The robot has become down with this reading.
        if (countInefficiencyComponents == -1) {
            // Start counting for down time.
            // Save in the DB the timestamp in witch the robot has gone down, call this Y.
            robotDAO.updateCountAndStartDown(robot, readData);
        }

        // The robot has become up with this reading.
        else if (countInefficiencyComponents == 0) {
            // Stop counting for downtime, calculate it and add to robot.downTime.
            // Get Y and calculate the time.
            downTimeDiff = this.differenceBetweenTimestamps(readData.getTimestamp(), robot.getStartDownTime());
            System.out.println(downTimeDiff + " con count : " + countInefficiencyComponents + "==" + robot.getCountInefficiencyComponents());

            robotDAO.updateCountAndStopDown(robot, readData, downTimeDiff);
        } else {
            // The robot is still down/up.
            robotDAO.updateCount(robot);
        }

        // Modify the signal data in the DB.
        Signal signal = new Signal(readData.getSignal(), readData.getValue(), readData.getTimestamp(), robot.getRobotId());
        new SignalDAO().update(signal);
    }

    private long differenceBetweenTimestamps(Timestamp downEnd, Timestamp downStart)
    {
        long milliseconds1 = downStart.getTime();
        long milliseconds2 = downEnd.getTime();

        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
//        long diffMinutes = diff / (60 * 1000);
//        long diffHours = diff / (60 * 60 * 1000);
//        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffSeconds;
    }
}
