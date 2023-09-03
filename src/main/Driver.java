package main;

import controllers.AppStoreAPI;
import controllers.DeveloperAPI;
import models.*;
import utils.ScannerInput;
import utils.Utilities;

public class Driver {

    private DeveloperAPI developerAPI = new DeveloperAPI();
    private AppStoreAPI appStoreAPI = new AppStoreAPI();

    public static void main(String[] args) {
        new Driver().start();
    }

    public void start() {
        loadAllData();
        runMainMenu();
    }

    private int mainMenu() {
        System.out.println("""
                 -------------App Store------------
                |  1) Developer - Management MENU  |
                |  2) App - Management MENU        |
                |  3) Reports MENU                 |
                |----------------------------------|
                |  4) Search                       |
                |  5) Sort                         |
                |----------------------------------|
                |  6) Recommended Apps             |
                |  7) Random App of the Day        |
                |  8) Simulate ratings             |
                |----------------------------------|
                |  20) Save all                    |
                |  21) Load all                    |
                |----------------------------------|
                |  0) Exit                         |
                 ----------------------------------""");
        return ScannerInput.validNextInt("==>> ");
    }

    private void runMainMenu() {
        int option = mainMenu();
        while (option != 0) {
            switch (option) {
                case 1 -> runDeveloperMenu();
                case 2 -> runAppMenu();
                case 3 -> runReportsMenu();
                case 4 -> searchAppsBySpecificCriteria();
                case 5 -> appStoreAPI.sortAppsByNameAscending();
                case 6 -> System.out.println(appStoreAPI.listAllRecommendedApps());
                case 7 -> System.out.println("App of the Day is: " + appStoreAPI.randomApp().getAppName() + "!");
                case 8 -> simulateRatings();
                case 20 -> saveAllData();
                case 21 -> loadAllData();
                default -> System.out.println("Invalid option entered: " + option);
            }
            ScannerInput.validNextLine("\n Press the enter key to continue");
            option = mainMenu();
        }
        exitApp();
    }

    private void exitApp() {
        saveAllData();
        System.out.println("Exiting....");
        System.exit(0);
    }

    //--------------------------------------------------
    //  Developer Management - Menu Items
    //--------------------------------------------------
    private int developerMenu() {
        System.out.println("""
                 -------Developer Menu-------
                |   1) Add a developer       |
                |   2) List developer        |
                |   3) Update developer      |
                |   4) Delete developer      |
                |   0) RETURN to main menu   |
                 ----------------------------""");
        return ScannerInput.validNextInt("==>> ");
    }

    private void runDeveloperMenu() {
        int option = developerMenu();
        while (option != 0) {
            switch (option) {
                case 1 -> addDeveloper();
                case 2 -> System.out.println(developerAPI.listDevelopers());
                case 3 -> updateDeveloper();
                case 4 -> deleteDeveloper();
                default -> System.out.println("Invalid option entered" + option);
            }
            ScannerInput.validNextLine("\n Press the enter key to continue");
            option = developerMenu();
        }
    }

    private void addDeveloper() {
        String developerName = ScannerInput.validNextLine("Please enter the developer name: ");
        String developerWebsite = ScannerInput.validNextLine("Please enter the developer website: ");

        if (developerAPI.addDeveloper(new Developer(developerName, developerWebsite))) {
            System.out.println("Add successful");
        } else {
            System.out.println("Add not successful");
        }
    }

    private void updateDeveloper() {
        System.out.println(developerAPI.listDevelopers());
        Developer developer = readValidDeveloperByName();
        if (developer != null) {
            String developerWebsite = ScannerInput.validNextLine("Please enter new website: ");
            if (developerAPI.updateDeveloperWebsite(developer.getDeveloperName(), developerWebsite))
                System.out.println("Developer Website Updated");
            else
                System.out.println("Developer Website NOT Updated");
        } else
            System.out.println("Developer name is NOT valid");
    }

    private void deleteDeveloper() {
        String developerName = ScannerInput.validNextLine("Please enter the developer name: ");
        if (developerAPI.removeDeveloper(developerName) != null) {
            System.out.println("Delete successful");
        } else {
            System.out.println("Delete not successful");
        }
    }

    private Developer readValidDeveloperByName() {
        String developerName = ScannerInput.validNextLine("Please enter the developer's name: ");
        if (developerAPI.isValidDeveloper(developerName)) {
            return developerAPI.getDeveloperByName(developerName);
        } else {
            return null;
        }
    }

    //--------------------------------------------------
    //  App Management - Menu Items
    //--------------------------------------------------
    private int appMenu() {
        System.out.println("""
                 -------App Menu-------
                |   1) Add an app            |
                |   2) List apps             |
                |   3) Update app            |
                |   4) Delete app            |
                |   0) RETURN to main menu   |
                 ----------------------------""");
        return ScannerInput.validNextInt("==>> ");
    }

    private void runAppMenu() {
        int option = appMenu();
        while (option != 0) {
            switch (option) {
                case 1 -> runAddAppMenu();
                case 2 -> System.out.println(appStoreAPI.listAllApps());
                case 3 -> updateApp();
                case 4 -> deleteApp();
                default -> System.out.println("Invalid option entered" + option);
            }
            ScannerInput.validNextLine("\n Press the enter key to continue");
            option = appMenu();
        }
    }

    private int addAppMenu() {
        System.out.println("""
                 -------Add App Menu-------
                |   1) Add a Game App        |
                |   2) Add a Education App   |
                |   3) Add a Productivity App|
                |   0) RETURN to app menu    |
                 ----------------------------""");
        return ScannerInput.validNextInt("==>> ");
    }

    private void runAddAppMenu() {
        int option = addAppMenu();
        while (option != 0) {
            switch (option) {
                case 1 -> addGameApp();
                case 2 -> addEducationApp();
                case 3 -> addProductivityApp();
                default -> System.out.println("Invalid option entered" + option);
            }
            ScannerInput.validNextLine("\n Press the enter key to continue");
            option = addAppMenu();
        }
    }


    private void addGameApp() {

        Developer developer = readValidDeveloperByName();
        String appName = ScannerInput.validNextLine("Please enter the app name: ");
        double appSize = ScannerInput.validNextDouble("Please enter the app size: ");
        double appVersion = ScannerInput.validNextDouble("Please enter the app version: ");
        double appCost = ScannerInput.validNextDouble("Please enter the app cost: ");
        char multiplayer = ScannerInput.validNextChar("Is the GameApp multiplayer? (Y/N)");

        boolean isMultiplayer = Utilities.YNtoBoolean(multiplayer);
        if (appStoreAPI.addApp(new GameApp(developer,appName,appSize,appVersion,appCost,isMultiplayer))) {
            System.out.println("Add successful");
        } else {
            System.out.println("Add not successful");
        }

    }

    private void addEducationApp() {

        Developer developer = readValidDeveloperByName();
        String appName = ScannerInput.validNextLine("Please enter the app name: ");
        double appSize = ScannerInput.validNextDouble("Please enter the app size: ");
        double appVersion = ScannerInput.validNextDouble("Please enter the app version: ");
        double appCost = ScannerInput.validNextDouble("Please enter the app cost: ");
        int level = ScannerInput.validNextInt("Please enter the app level: ");

        if (appStoreAPI.addApp(new EducationApp(developer,appName,appSize,appVersion,appCost,level))) {
            System.out.println("Add successful");
        } else {
            System.out.println("Add not successful");
        }

    }

    private void addProductivityApp() {

        Developer developer = readValidDeveloperByName();
        String appName = ScannerInput.validNextLine("Please enter the app name: ");
        double appSize = ScannerInput.validNextDouble("Please enter the app size: ");
        double appVersion = ScannerInput.validNextDouble("Please enter the app version: ");
        double appCost = ScannerInput.validNextDouble("Please enter the app cost: ");

        if (appStoreAPI.addApp(new ProductivityApp(developer,appName,appSize,appVersion,appCost))) {
            System.out.println("Add successful");
        } else {
            System.out.println("Add not successful");
        }

    }

    private void updateApp() {
        System.out.println(appStoreAPI.listAllApps());
        if(appStoreAPI.numberOfApps() > 0) {
            //only ask the user to choose the note to update if apps exist
            int indexToUpdate = ScannerInput.validNextInt("Enter the index of the app you want to update:");
                if(appStoreAPI.getAppByIndex(indexToUpdate) instanceof GameApp) {
                    GameApp gameAppToUpdate = (GameApp) appStoreAPI.getAppByIndex(indexToUpdate);
                    Developer developer = readValidDeveloperByName();
                    String appName = ScannerInput.validNextLine("Please enter the app name: ");
                    double appSize = ScannerInput.validNextDouble("Please enter the app size: ");
                    double appVersion = ScannerInput.validNextDouble("Please enter the app version: ");
                    double appCost = ScannerInput.validNextDouble("Please enter the app cost: ");
                    char multiplayer = ScannerInput.validNextChar("Is the Game multiplayer? (Y/N)");

                    boolean isMultiplayer = Utilities.YNtoBoolean(multiplayer);
                    gameAppToUpdate.setDeveloper(developer);
                    gameAppToUpdate.setAppName(appName);
                    gameAppToUpdate.setAppSize(appSize);
                    gameAppToUpdate.setAppVersion(appVersion);
                    gameAppToUpdate.setAppCost(appCost);
                    gameAppToUpdate.setMultiplayer(isMultiplayer);
                    System.out.println("Update Successful");
                }
                if(appStoreAPI.getAppByIndex(indexToUpdate) instanceof EducationApp) {
                    EducationApp edAppToUpdate = (EducationApp) appStoreAPI.getAppByIndex(indexToUpdate);
                    Developer developer = readValidDeveloperByName();
                    String appName = ScannerInput.validNextLine("Please enter the app name: ");
                    double appSize = ScannerInput.validNextDouble("Please enter the app size: ");
                    double appVersion = ScannerInput.validNextDouble("Please enter the app version: ");
                    double appCost = ScannerInput.validNextDouble("Please enter the app cost: ");
                    int level = ScannerInput.validNextInt("Please enter the level: ");

                    edAppToUpdate.setDeveloper(developer);
                    edAppToUpdate.setLevel(level);
                    edAppToUpdate.setAppName(appName);
                    edAppToUpdate.setAppSize(appSize);
                    edAppToUpdate.setAppVersion(appVersion);
                    edAppToUpdate.setAppCost(appCost);
                    System.out.println("Update Successful");
                }
                if(appStoreAPI.getAppByIndex(indexToUpdate) instanceof ProductivityApp) {
                    App prodAppToUpdate = appStoreAPI.getAppByIndex(indexToUpdate);
                    Developer developer = readValidDeveloperByName();
                    String appName = ScannerInput.validNextLine("Please enter the app name: ");
                    double appSize = ScannerInput.validNextDouble("Please enter the app size: ");
                    double appVersion = ScannerInput.validNextDouble("Please enter the app version: ");
                    double appCost = ScannerInput.validNextDouble("Please enter the app cost: ");

                    prodAppToUpdate.setDeveloper(developer);
                    prodAppToUpdate.setAppName(appName);
                    prodAppToUpdate.setAppSize(appSize);
                    prodAppToUpdate.setAppVersion(appVersion);
                    prodAppToUpdate.setAppCost(appCost);
                    System.out.println("Update Successful");
                }
            }
        }

    private void deleteApp() {

        if(appStoreAPI.numberOfApps() > 0) {
            System.out.println(appStoreAPI.listAllApps());
            //only ask the user to choose the app to delete if app exists
            int indexToDelete = ScannerInput.validNextInt("Enter the index of the App you wish to delete: ");
            //pass the index of the app to controllers.AppStoreAPI for deleting and check for success.
            if (appStoreAPI.isValidIndex(indexToDelete)) {
                App appToDelete = appStoreAPI.deleteAppByIndex(indexToDelete);
                if (appToDelete != null)
                    System.out.println("Delete Successful! Deleted App: " + appToDelete.getAppName());
                else
                    System.out.println("Delete NOT Successful");
            }
        }
        else System.out.println("No Apps added.");

    }

    private int reportsMenu() {
        System.out.println("""
                 -------Reports Menu-------
                |   1) Apps Overview         |
                |   2) Developers Overview   |
                |   0) RETURN to main menu   |
                 ----------------------------""");
        return ScannerInput.validNextInt("==>> ");
    }

    private void runReportsMenu() {
        int option = reportsMenu();
        while (option != 0) {
            switch (option) {
                case 1 -> runAppOverviewMenu();
                case 2 -> System.out.println("There is a total of " + developerAPI.numberOfDevelopers() + " developers:" + '\n' + developerAPI.listDevelopers());
                default -> System.out.println("Invalid option entered" + option);
            }
            ScannerInput.validNextLine("\n Press the enter key to continue");
            option = reportsMenu();
        }
    }

    private int appsOverviewMenu() {
        System.out.println("""
                 -------App Overview Menu-------
                |   1) List All Game Apps        |
                |   2) List All Education Apps   |
                |   3) List All Productivity Apps|
                |   0) Return to Reports Menu    |
                 ----------------------------""");
        return ScannerInput.validNextInt("==>> ");
    }

    private void runAppOverviewMenu() {
        int option = appsOverviewMenu();
        while (option != 0) {
            switch (option) {
                case 1 -> System.out.println("Game Apps:" + '\n' + appStoreAPI.listAllGameApps());
                case 2 -> System.out.println("Education Apps:" + '\n' + appStoreAPI.listAllEducationApps());
                case 3 -> System.out.println("Productivity Apps:" + '\n' + appStoreAPI.listAllProductivityApps());
                default -> System.out.println("Invalid option entered" + option);
            }
            ScannerInput.validNextLine("\n Press the enter key to continue");
            option = appsOverviewMenu();
        }
    }

    private void searchAppsBySpecificCriteria() {
        System.out.println("""
                What criteria would you like to search apps by:
                  1) App Name
                  2) Developer Name
                  3) Rating (all apps of that rating or above)""");
        int option = ScannerInput.validNextInt("==>> ");
        switch (option) {
            case 1 -> searchAppsByName();
            case 2 -> searchAppsByDeveloper();
            case 3 -> searchAppsEqualOrAboveAStarRating();
            default -> System.out.println("Invalid option");
        }
    }

    private void searchAppsByName() {

        if (appStoreAPI.numberOfApps() > 0) {
            //only ask the user to enter the name if apps exist
            String appName = ScannerInput.validNextLine("Enter the name to search by: ");
            System.out.println(appStoreAPI.listAllAppsByName(appName));
        } else System.out.println("No apps.");

    }

    private void searchAppsByDeveloper() {

        if (appStoreAPI.numberOfApps() > 0) {
            //only ask the user to enter the name if apps exist
            System.out.println(appStoreAPI.listAllAppsByChosenDeveloper(readValidDeveloperByName()));
        } else System.out.println("No apps.");

    }

    private void searchAppsEqualOrAboveAStarRating() {
        if (appStoreAPI.numberOfApps() > 0) {
            //only ask the user to enter the rating if apps exist
            int rating = ScannerInput.validNextInt("Enter the rating to search by: ");
            System.out.println(appStoreAPI.listAllAppsAboveOrEqualAGivenStarRating(rating));
        } else System.out.println("No apps.");
    }

    private void simulateRatings() {
        // simulate random ratings for all apps (to give data for recommended apps and reports etc).
        if (appStoreAPI.numberOfApps() > 0) {
            System.out.println("Simulating ratings...");
            appStoreAPI.simulateRatings();
           System.out.println(appStoreAPI.listSummaryOfAllApps());
        } else {
            System.out.println("No apps");
        }
    }

    //--------------------------------------------------
    //  Persistence Menu Items
    //--------------------------------------------------

    private void saveAllData() {
        try {
            System.out.println("Saving to file: " + appStoreAPI.fileName());
            appStoreAPI.save();
        } catch (Exception e) {
            System.err.println("Error writing to file: " + e);
        }
    }

    private void loadAllData() {
        try {
            System.out.println("Loading from file: " + appStoreAPI.fileName());
            appStoreAPI.load();
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e);
        }
    }

}