package models;

import models.*;
import utils.Utilities;

import java.util.ArrayList;
import java.util.List;

public abstract class App {

    private Developer developer;
    private String appName = "No app name";
    private double appSize = 0;
    private double appVersion = 1.0;
    private double appCost = 0.0;
    private List<Rating> ratings = new ArrayList<>();

    public App(Developer developer, String appName, double appSize, double appVersion, double appCost) {
        setDeveloper(developer);
        setAppName(appName);
        setAppSize(appSize);
        setAppVersion(appVersion);
        setAppCost(appCost);
    }

    public boolean addRating(Rating rating) {
        return ratings.add(rating);
    }

    public String appSummary() {
        return getAppName() + "(V" + getAppVersion() + ") " + "by " + getDeveloper() + ", " + "€" + getAppCost() + ". " + "Rating: " + calculateRating(); //rating;
    }

    public double calculateRating() {
        if(ratings.isEmpty())
            return 0;
        else {
            int stars = 0;
            for(int i = 0; i < ratings.size(); i++) {
                stars += ratings.get(i).getNumberOfStars();
            }
            return (double)stars / ratings.size();
        }
            
    }

    public Developer getDeveloper() {
        return developer;
    }

    public String getAppName() {
        return appName;
    }

    public double getAppSize() {
        return appSize;
    }

    public double getAppVersion() {
        return appVersion;
    }

    public double getAppCost() {
        return appCost;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public abstract boolean isRecommendedApp();

    public String listRatings() {
        if(ratings.isEmpty())
            return "No ratings added yet";
        else {
            String listOfRatings = "";
            for(int i = 0; i < ratings.size(); i++) {
                listOfRatings += i + ": " + ratings.get(i).toString();
            }
            return listOfRatings;
        }
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public void setAppName(String appName) {this.appName = appName;}

    public void setAppSize(double appSize) {
        if(Utilities.validRange(appSize,1,1000))
            this.appSize = appSize;
    }

    public void setAppVersion(double appVersion) {
        if(appVersion >= 1.0)
            this.appVersion = appVersion;
    }

    public void setAppCost(double appCost) {
        if(appCost >= 0)
            this.appCost = appCost;
    }


    @Override
    public String toString() {
        return "App{" +
                "developer=" + developer +
                ", appName='" + appName + '\'' +
                ", appSize=" + appSize +
                ", appVersion=" + appVersion +
                ", appCost=" + appCost +
                ", ratings=" + ratings +
                '}';
    }
}
