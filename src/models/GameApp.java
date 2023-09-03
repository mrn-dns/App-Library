package models;

import utils.Utilities;

public class GameApp extends App {

    private boolean isMultiplayer = false;

    public GameApp(Developer developer, String appName, double appSize, double appVersion, double appCost, boolean isMultiplayer) {
        super(developer,appName,appSize,appVersion,appCost);
        setMultiplayer(isMultiplayer);
    }

    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public void setMultiplayer(boolean isMultiplayer) {
        this.isMultiplayer = isMultiplayer;
    }

    public boolean isRecommendedApp() {
        return isMultiplayer && (super.calculateRating() >= 4.0);
    }

    public String appSummary() {
        return super.appSummary();
    }

    @Override
    public String toString() {
        return getAppName() +
                "(Version " + getAppVersion() + " " +
                getDeveloper().toString() + " " +
                getAppSize() + "MB" + " " +
                "Cost: " + getAppCost() + " " +
                "Multiplayer: " + Utilities.booleanToYN(isMultiplayer) + " " +
                "Ratings (" + calculateRating() + ")" +
                listRatings();
    }
}
