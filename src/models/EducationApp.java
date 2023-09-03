package models;

import utils.Utilities;

import java.util.ArrayList;
import java.util.List;

public class EducationApp extends App {

    private int level = 0;

    public EducationApp(Developer developer, String appName, double appSize, double appVersion, double appCost, int level) {
        super(developer,appName,appSize,appVersion,appCost);
        setLevel(level);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if(Utilities.validRange(level,0,10))
            this.level = level;
    }

    public boolean isRecommendedApp() {
        return (super.getAppCost() > 0.99) && (super.calculateRating() >= 3.5) && (level >= 3);
    }

    public String appSummary() {
        return "level " + getLevel() + " " + super.appSummary();
    }

    @Override
    public String toString() {
        return getAppName() +
                "(Version " + getAppVersion() + " " +
                getDeveloper().toString() + " " +
                getAppSize() + "MB" + " " +
                "Cost: " + getAppCost() + " " +
                "Level: " + getLevel() + " " +
                "Ratings (" + calculateRating() + ")" +
                listRatings();
    }
}
