package models;

public class ProductivityApp extends App {

    public ProductivityApp(Developer developer, String appName, double appSize, double appVersion, double appCost) {
        super(developer,appName,appSize,appVersion,appCost);
    }

    @Override
    public boolean isRecommendedApp() {
        return (super.getAppCost() >= 1.99) && (super.calculateRating() > 3);
    }

    @Override
    public String toString() {
        return getAppName() +
                "(Version " + getAppVersion() + " " +
                getDeveloper().toString() + " " +
                getAppSize() + "MB" + " " +
                "Cost: " + getAppCost() + " " +
                "Ratings (" + calculateRating() + ")" +
                listRatings();
    }
}
