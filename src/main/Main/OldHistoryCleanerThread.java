package main.Main;

import main.Model.DAO.HistoryDAO;

public class OldHistoryCleanerThread implements Runnable {
    // This thread is used to clean the histories that our system doesn't need anymore.
    @Override
    public void run() {
        new HistoryDAO().oldHistoriesCleaner();
    }
}
