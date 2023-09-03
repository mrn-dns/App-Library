package controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import models.*;
import utils.ISerializer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

import static utils.RatingUtility.generateRandomRating;

public class AppStoreAPI implements ISerializer{

    private List<App> apps = new ArrayList<>();
    private Random randomGenerator = new Random();

    public boolean addApp(App app) {
        return apps.add(app);
    }

    public App deleteAppByIndex(int appToDelete) {
        if (isValidIndex(appToDelete))
            return apps.remove(appToDelete);
        return null;
    }

    public App getAppByIndex(int index) {
        if (isValidIndex(index)) {
            return apps.get(index);
        }
        return null;
    }

    public App getAppByName(String name) {
        for(App app : apps)
            if(isValidAppName(name))
                return app;
        return null;
    }

    /**
     * This method displays all apps, if apps exist.
     *
     *
     * @return A string value representing all apps.
     */
    public String listAllApps() {

        if(apps.isEmpty())
            return "No apps";
        else {
            String listOfApps = "";
            for(int i = 0; i < apps.size(); i++) {
                listOfApps += i + ": " + apps.get(i).appSummary() + '\n';
            }
            return listOfApps;
        }

    }

    public String listSummaryOfAllApps() {

        if(apps.isEmpty())
            return "No apps";
        else {
            String listOfAppSummary = "";
            for(int i = 0; i < apps.size(); i++)
                listOfAppSummary += i + ": " + apps.get(i).appSummary() + '\n';
            return listOfAppSummary;
        }
    }

    public String listAllGameApps() {
        if(apps.isEmpty())
            return "No Apps";
        else {
            String listOfGameApps = "";
            for (int i = 0; i < apps.size(); i++) {
                if (apps.get(i) instanceof GameApp)
                    listOfGameApps += i + ": " + apps.get(i).appSummary() + '\n';
            }
            if (listOfGameApps.isEmpty())
                return "No Game Apps";
            else
                return listOfGameApps;
        }
    }

    public String listAllEducationApps() {
        if (apps.isEmpty())
            return "No Apps";
        else {
            String listOfEducationApps = "";
            for (int i = 0; i < apps.size(); i++) {
                if (apps.get(i) instanceof EducationApp) {
                    listOfEducationApps += i + ": " + apps.get(i).appSummary() + '\n';
                }
            }
            if (listOfEducationApps.isEmpty())
                return "No Education Apps";
            else
                return listOfEducationApps;
        }
    }

    public String listAllProductivityApps() {
        if(apps.isEmpty())
            return "No Apps";
        else {
            String listOfProductivityApps = "";
            for (int i = 0; i < apps.size(); i++) {
                if (apps.get(i) instanceof ProductivityApp) {
                    listOfProductivityApps += i + ": " + apps.get(i).appSummary() + '\n';
                }
            }
            if (listOfProductivityApps.isEmpty())
                return "No Productivity Apps";
            else
                return listOfProductivityApps;
        }
    }

    public String listAllAppsByName(String name) {
        if(apps.isEmpty())
            return "No Apps";
        else {
            String appsByName = "";
            for(App app : apps) {
                if(app.getAppName().toLowerCase().equals(name.toLowerCase()))
                    appsByName += app.getAppName() + '\n';
            }
            if(appsByName.isEmpty())
                return "No apps for name " + name + " exists.";
            else
                return appsByName;
        }
    }

    public String listAllAppsAboveOrEqualAGivenStarRating(int rating) {

        if(apps.isEmpty())
            return "No Apps";
        else {
            String appsWithRating = "";
            for(App app : apps) {
                if(app.calculateRating() >= rating)
                    appsWithRating += app.appSummary();
            }
            if(appsWithRating.isEmpty())
                return "No apps have a rating of " + rating + " or above";
            else
                return appsWithRating;
        }
    }

    public String listAllRecommendedApps() {

        if(apps.isEmpty())
            return "No Apps";
        else {

            String recommendedApps = "";
            for(App app : apps)
                if(app.isRecommendedApp())
                    recommendedApps += app.getAppName() + '\n';

            if(recommendedApps.isEmpty())
                return "No recommended apps";
            else
                return recommendedApps;

        }
    }

    public String listAllAppsByChosenDeveloper(Developer developer) {

        if(apps.isEmpty())
            return "No Apps";
        else {

            String appsByDeveloper = "";
            for(App app : apps)
                if(app.getDeveloper().getDeveloperName().contains(developer.getDeveloperName()))
                    appsByDeveloper += app.getAppName() + '\n';

            if(appsByDeveloper.isEmpty())
                return "No apps for developer: " + developer;
            else
                return appsByDeveloper;

        }
    }

    public int numberOfAppsByChosenDeveloper(Developer developer) {

        int c = 0;
        for(App app : apps)
            if(app.getDeveloper().equals(developer))
                c++;
        return c;

    }

        public App randomApp() {

        if(apps.isEmpty())
            return null;
        else {
            int index = randomGenerator.nextInt(apps.size());
            return apps.get(index);
        }

    }

    public int numberOfApps() {
        return apps.size();
    }


    public void simulateRatings(){
        for (App app :apps) {
            app.addRating(generateRandomRating());
        }
    }

    public boolean isValidIndex(int index) {
        return (index >= 0) && (index < apps.size());
    }

    public boolean isValidAppName(String name) {
        for(App app : apps)
            if(app.getAppName().toLowerCase().equals(name.toLowerCase()))
                return true;
        return false;
    }

    public void sortAppsByNameAscending(){
        for (int i = apps.size() -1; i >= 0; i--) {
            int highestIndex = 0;
            for (int j = 0; j <= i; j++) {
                if (apps.get(j).getAppName().compareTo(
                        apps.get(highestIndex).getAppName()) > 0) {
                    highestIndex = j;
                }
            }
            swapApps(apps, i, highestIndex);
        }
    }



    private void swapApps(List<App> apps, int current, int highest) {
        App smaller = apps.get(current);
        App bigger = apps.get(highest);
        apps.set(current, bigger);
        apps.set(highest,smaller);
    }

    @SuppressWarnings("unchecked")
    public void load() throws Exception {
        //list of classes that you wish to include in the serialisation, separated by a comma
        Class<?>[] classes = new Class[]{App.class, EducationApp.class, GameApp.class, ProductivityApp.class, Rating.class};

        //setting up the xstream object with default security and the above classes
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);

        //doing the actual serialisation to an XML file
        ObjectInputStream in = xstream.createObjectInputStream(new FileReader(fileName()));
        apps = (List<App>) in.readObject();
        in.close();
    }

    public void save() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(fileName()));
        out.writeObject(apps);
        out.close();
    }

    public String fileName(){
        return "apps.xml";
    }
}