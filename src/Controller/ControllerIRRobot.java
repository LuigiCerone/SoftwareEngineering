package Controller;

import Model.DAO.RobotDAO;
import Model.DAO.RobotDAO_Interface;
import Model.DAO.SignalDAO;
import Model.ReadData;
import Model.Robot;
import Model.Signal;

import java.sql.Timestamp;

public class ControllerIRRobot {


    public void updateComponentState(Robot robot, ReadData readData, RobotDAO robotDAO) {
        long downTimeDiff = 0;
        // Modify the robot counter stat.
        int countInefficiencyComponents = robot.updateComponentState(readData.getValue());

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
            if(robot.getStartDownTime() != null)
                downTimeDiff = this.differenceBetweenTimestamps(readData.getTimestamp(), robot.getStartDownTime());
            System.out.println(downTimeDiff);
            // TODO Update the robot count data in the DB.
            // TODO Update the robot downtime in the DB.
        } else {
            // The robot is still down/up.
        }

        // Modify the signal data in the DB.
        Signal signal = new Signal(readData.getSignal(), readData.getValue(), readData.getTimestamp(), robot.getRobotId());
        new SignalDAO().update(signal);
    }

    public long differenceBetweenTimestamps(Timestamp downEnd, Timestamp downStart)
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
