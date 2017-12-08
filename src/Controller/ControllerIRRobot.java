package Controller;

import Model.DAO.SignalDAO;
import Model.ReadData;
import Model.Robot;
import Model.Signal;

public class ControllerIRRobot {


    public void updateComponentState(Robot robot, ReadData readData) {

        // Modify the robot counter stat.
        robot.updateComponentState(readData.getValue());


        // Modify the signal data in the DB.
        Signal signal = new Signal(readData.getSignal(), readData.getValue(), readData.getTimestamp(), robot.getRobotId());
        new SignalDAO().update(signal);

        // Check if this is the reading that puts in down the robot and manage it.
    }
}
