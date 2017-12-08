package Controller;

import Model.DAO.SignalDAO;
import Model.ReadData;
import Model.Robot;
import Model.Signal;

public class ControllerIRRobot {


    public void updateComponentState(Robot robot, ReadData readData) {

        // Modify the robot counter stat.
        int countInefficiencyComponents = robot.updateComponentState(readData.getValue());

        // The robot has become down with this reading.
        if (countInefficiencyComponents == -1) {
            // Start counting for down time.
            // Save in the DB the timestamp in witch the robot has gone down, call this Y.

        }
        // The robot has become up with this reading.
        else if (countInefficiencyComponents == 0) {
            // Stop counting for downtime, calculate it and add to robot.downTime.
            // Get Y and calculate the time.
        } else {
            // The robot is still down/up.
        }

        // TODO Update the robot data in the DB.

        // Modify the signal data in the DB.
        Signal signal = new Signal(readData.getSignal(), readData.getValue(), readData.getTimestamp(), robot.getRobotId());
        new SignalDAO().update(signal);

        // Check if this is the reading that puts in down the robot and manage it.
    }
}
