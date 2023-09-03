package controllers;

import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class AppStoreAPITest {

    private EducationApp edAppBelowBoundary, edAppOnBoundary, edAppAboveBoundary, edAppInvalidData;
    private ProductivityApp prodAppBelowBoundary, prodAppOnBoundary, prodAppAboveBoundary, prodAppInvalidData;
    private GameApp gameAppBelowBoundary, gameAppOnBoundary, gameAppAboveBoundary, gameAppInvalidData;

    private Developer developerLego = new Developer("Lego", "www.lego.com");
    private Developer developerSphero = new Developer("Sphero", "www.sphero.com");
    private Developer developerEAGames = new Developer("EA Games", "www.eagames.com");
    private Developer developerKoolGames = new Developer("Kool Games", "www.koolgames.com");
    private Developer developerApple = new Developer("Apple", "www.apple.com");
    private Developer developerMicrosoft = new Developer("Microsoft", "www.microsoft.com");

    private AppStoreAPI appStore = new AppStoreAPI();
    private AppStoreAPI emptyAppStore = new AppStoreAPI();

    @BeforeEach
    void setUp() {

        //Validation: appSize(1-1000), appVersion(>=1.0), ageRating (0-18), appCost(>=0), level(1-10).
        edAppBelowBoundary = new EducationApp(developerLego, "WeDo", 1, 1.0, 0,  1);

        edAppOnBoundary = new EducationApp(developerLego, "Spike", 1000, 2.0,
                1.99, 10);

        edAppAboveBoundary = new EducationApp(developerLego, "EV3", 1001, 3.5,  2.99,  11);

        edAppInvalidData = new EducationApp(developerLego, "", -1, 0, -1.00,  0);


        //Validation: appSize(1-1000), appVersion(>=1.0), ageRating (0-18), appCost(>=0),
        prodAppBelowBoundary = new ProductivityApp(developerApple, "NoteKeeper", 1, 1.0, 0.0);

        prodAppOnBoundary = new ProductivityApp(developerMicrosoft, "Outlook", 1000, 2.0, 1.99);

        prodAppAboveBoundary = new ProductivityApp(developerApple, "Pages", 1001, 3.5, 2.99);

        prodAppInvalidData = new ProductivityApp(developerMicrosoft, "", -1, 0, -1.00);


        //Validation: appSize(1-1000), appVersion(>=1.0), ageRating (0-18), appCost(>=0),
        gameAppBelowBoundary = new GameApp(developerEAGames, "Tetris", 1, 1.0, 0.0,  false);

        gameAppOnBoundary = new GameApp(developerKoolGames, "CookOff", 1000, 2.0, 1.99,  true);

        gameAppAboveBoundary = new GameApp(developerEAGames, "Empires", 1001, 3.5,  2.99, false);

        gameAppInvalidData = new GameApp(developerKoolGames, "", -1, 0,  -1.00,  true);

        //not included - edAppOnBoundary, edAppInvalidData, prodAppBelowBoundary, gameAppBelowBoundary, gameAppInvalidData.
        appStore.addApp(edAppBelowBoundary);
        appStore.addApp(prodAppOnBoundary);
        appStore.addApp(gameAppAboveBoundary);
        appStore.addApp(prodAppBelowBoundary);
        appStore.addApp(edAppAboveBoundary);
        appStore.addApp(prodAppInvalidData);
        appStore.addApp(gameAppOnBoundary);
    }

    @AfterEach
    void tearDown() {
        edAppBelowBoundary = edAppOnBoundary = edAppAboveBoundary = edAppInvalidData = null;
        gameAppBelowBoundary = gameAppOnBoundary = gameAppAboveBoundary = gameAppInvalidData = null;
        prodAppBelowBoundary = prodAppOnBoundary = prodAppAboveBoundary = prodAppInvalidData = null;
        developerApple = developerEAGames = developerKoolGames = developerLego = developerMicrosoft = null;
        appStore = emptyAppStore = null;
    }

    @Nested
    class Getters {

        @Test
        void getAppByIndexWorks() {
            assertEquals(edAppBelowBoundary, appStore.getAppByIndex(0));
            assertNull(appStore.getAppByIndex(8));
        }

        @Test
        void getAppByName() {
            assertEquals(edAppBelowBoundary, appStore.getAppByName("wedo"));
            assertNull(appStore.getAppByName("wfawf"));
        }

    }

    @Nested
    class CRUDMethods {

        @Test
        void addAppWorksCorrectly() {
            assertEquals(7, appStore.numberOfApps());
            appStore.addApp(edAppOnBoundary);
            assertEquals(8,appStore.numberOfApps());
            assertEquals(edAppOnBoundary, appStore.getAppByIndex(7));
        }

        @Test
        void deleteAppWorksCorrectly() {
            assertEquals(7, appStore.numberOfApps());
            assertEquals(prodAppOnBoundary, appStore.getAppByIndex(1));
            appStore.deleteAppByIndex(1);
            assertEquals(6, appStore.numberOfApps());

            assertEquals(0, emptyAppStore.numberOfApps());
            assertNull(emptyAppStore.deleteAppByIndex(0));
        }

    }

    @Nested
    class ListingMethods {

        @Test
        void listAllAppsReturnsNoAppsStoredWhenArrayListIsEmpty() {
            assertEquals(0, emptyAppStore.numberOfApps());
            assertTrue(emptyAppStore.listAllApps().toLowerCase().contains("no apps"));
        }

        @Test
        void listAllAppsReturnsAppsStoredWhenArrayListHasAppsStored() {
            assertEquals(7, appStore.numberOfApps());
            String apps = appStore.listAllApps();
            //checks for objects in the string
            assertTrue(apps.contains("WeDo"));
            assertTrue(apps.contains("Outlook"));
            assertTrue(apps.contains("Empires"));
            assertTrue(apps.contains("NoteKeeper"));
            assertTrue(apps.contains("EV3"));
            assertTrue(apps.contains("CookOff"));
        }

        @Test
        void listRecommendedAppsReturnsNoAppsWhenRecommendedAppsDoNotExist() {
            assertEquals(7, appStore.numberOfApps());

            String apps = appStore.listAllRecommendedApps();
            //checks for the three objects in the string
            assertTrue(apps.contains("No recommended apps"));
        }

        @Test
        void listRecommendedAppsReturnsNoAppsWhenRecommendedDoNotExist() {
            assertEquals(0, emptyAppStore.numberOfApps());

            assertTrue(emptyAppStore.listAllRecommendedApps().toLowerCase().contains("no apps"));
        }

        @Test
        void listRecommendedAppsReturnsRecommendedAppsWhenTheyExist() {
            assertEquals(7, appStore.numberOfApps());

            //adding recommended apps to the list
            appStore.addApp(setupGameAppWithRating(5,4));
            appStore.addApp(setupEducationAppWithRating(3,4));
            appStore.addApp(setupProductivityAppWithRating(3,4));
            assertEquals(10, appStore.numberOfApps());

            String apps = appStore.listAllRecommendedApps();
            System.out.println(apps);
            //checks for the three objects in the string
            assertTrue(apps.contains("MazeRunner"));
            assertTrue(apps.contains("Evernote"));
            assertTrue(apps.contains("WeDo"));
        }

        @Test
        void listSummaryOfAllAppsReturnsNoAppsWhenNoAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());
            assertTrue(emptyAppStore.listSummaryOfAllApps().toLowerCase().contains("no apps"));
        }

        @Test
        void listSummaryOfAllAppsReturnsSummaryOfAllAppsWhenAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());

            emptyAppStore.addApp(edAppOnBoundary);
            emptyAppStore.addApp(prodAppBelowBoundary);

            assertEquals(2, emptyAppStore.numberOfApps());

            assertTrue(emptyAppStore.listSummaryOfAllApps().toLowerCase().contains(edAppOnBoundary.appSummary().toLowerCase()));
            assertTrue(emptyAppStore.listSummaryOfAllApps().toLowerCase().contains(prodAppBelowBoundary.appSummary().toLowerCase()));
        }

        @Test
        void listAllGameAppsReturnsNoAppsWhenNoGameAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());
            assertTrue(emptyAppStore.listAllGameApps().toLowerCase().contains("no apps"));
        }

        @Test
        void listAllGameAppsReturnsNoGameAppsWhenNoGameAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());

            emptyAppStore.addApp(edAppAboveBoundary);

            assertTrue(emptyAppStore.listAllGameApps().toLowerCase().contains("no game apps"));
        }

        @Test
        void listAllGameAppsReturnsAllGameAppsWhenGameAppsExist() {
            assertEquals(7, appStore.numberOfApps());

            assertTrue(appStore.listAllGameApps().toLowerCase().contains(gameAppOnBoundary.appSummary().toLowerCase()));
            assertFalse(appStore.listAllGameApps().toLowerCase().contains(gameAppBelowBoundary.appSummary().toLowerCase()));
            assertTrue(appStore.listAllGameApps().toLowerCase().contains(gameAppAboveBoundary.appSummary().toLowerCase()));
        }

        @Test
        void listAllEducationAppsReturnsNoAppsWhenNoEducationAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());
            assertTrue(emptyAppStore.listAllEducationApps().toLowerCase().contains("no apps"));
        }

        @Test
        void listAllEducationAppsReturnsNoEducationAppsWhenNoEducationAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());

            emptyAppStore.addApp(gameAppOnBoundary);

            assertTrue(emptyAppStore.listAllEducationApps().toLowerCase().contains("no education apps"));
        }

        @Test
        void listAllEducationAppsReturnsAllEducationAppsWhenEducationAppsExist() {
            assertEquals(7, appStore.numberOfApps());

            assertFalse(appStore.listAllEducationApps().toLowerCase().contains(edAppOnBoundary.appSummary().toLowerCase()));
            assertTrue(appStore.listAllEducationApps().toLowerCase().contains(edAppBelowBoundary.appSummary().toLowerCase()));
            assertTrue(appStore.listAllEducationApps().toLowerCase().contains(edAppAboveBoundary.appSummary().toLowerCase()));
        }

        @Test
        void listAllProductivityAppsReturnsNoAppsWhenNoProductivityAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());
            assertTrue(emptyAppStore.listAllProductivityApps().toLowerCase().contains("no apps"));
        }

        @Test
        void listAllProductivityAppsReturnsNoProductivityAppsWhenNoProductivityAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());

            emptyAppStore.addApp(gameAppOnBoundary);

            assertTrue(emptyAppStore.listAllProductivityApps().toLowerCase().contains("no productivity apps"));
        }

        @Test
        void listAllProductivityAppsReturnsAllProductivityAppsWhenProductivityAppsExist() {
            assertEquals(7, appStore.numberOfApps());

            assertTrue(appStore.listAllProductivityApps().toLowerCase().contains(prodAppOnBoundary.appSummary().toLowerCase()));
            assertTrue(appStore.listAllProductivityApps().toLowerCase().contains(prodAppBelowBoundary.appSummary().toLowerCase()));
            assertFalse(appStore.listAllProductivityApps().toLowerCase().contains(prodAppAboveBoundary.appSummary().toLowerCase()));
        }

        @Test
        void listAllAppsByNameReturnsNoAppsWhenNoAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());
            assertTrue(emptyAppStore.listAllAppsByName("name").toLowerCase().contains("no apps"));
        }

        @Test
        void listAllAppsByNameReturnsNoAppsWhenNoAppsWithNameExist() {
            assertEquals(0, emptyAppStore.numberOfApps());

            emptyAppStore.addApp(gameAppOnBoundary);

            assertTrue(emptyAppStore.listAllAppsByName(prodAppBelowBoundary.getAppName()).contains("No apps for name "));
        }

        @Test
        void listAllAppsByNameReturnsAppsByNameWhenAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());

            emptyAppStore.addApp(prodAppBelowBoundary);
            emptyAppStore.addApp(prodAppOnBoundary);

            assertTrue(emptyAppStore.listAllAppsByName(prodAppBelowBoundary.getAppName()).contains("NoteKeeper"));
            assertTrue(emptyAppStore.listAllAppsByName(prodAppOnBoundary.getAppName()).contains("Outlook"));
        }

        @Test
        void fileName() {
            assertTrue(appStore.fileName().contains("apps.xml"));
        }


    }

    @Nested
    class ReportingMethods {



        @Test
        void numberOfAppsByChosenDeveloperReturnsAppsWhenAppsExist() {
            assertEquals(7, appStore.numberOfApps());

            assertEquals(1, appStore.numberOfAppsByChosenDeveloper(developerEAGames));
            assertEquals(1, appStore.numberOfAppsByChosenDeveloper(developerApple));
            assertEquals(2, appStore.numberOfAppsByChosenDeveloper(developerLego));
            assertEquals(0, appStore.numberOfAppsByChosenDeveloper(developerSphero));
            assertEquals(2, appStore.numberOfAppsByChosenDeveloper(developerMicrosoft));
            assertEquals(1, appStore.numberOfAppsByChosenDeveloper(developerKoolGames));
        }

    }

    @Nested
    class SearchingMethods {

        @Test
        void listAllAppsByDeveloperWorksCorrectly() {
            assertEquals(0, emptyAppStore.numberOfApps());

            emptyAppStore.addApp(edAppOnBoundary);
            emptyAppStore.addApp(prodAppBelowBoundary);

            assertEquals(2, emptyAppStore.numberOfApps());

            assertTrue(emptyAppStore.listAllAppsByChosenDeveloper(developerLego).contains("Spike"));
            assertTrue(emptyAppStore.listAllAppsByChosenDeveloper(developerApple).contains("NoteKeeper"));
        }

        @Test
        void listAllAppsAboveOrEqualAGivenStarRatingWorksCorrectly() {
            appStore.addApp(setupEducationAppWithRating(3,4));
            appStore.addApp(setupGameAppWithRating(4,4));

            assertTrue(appStore.listAllAppsAboveOrEqualAGivenStarRating(3).contains("WeDo"));
            assertTrue(appStore.listAllAppsAboveOrEqualAGivenStarRating(3).contains("MazeRunner"));
        }

        @Test
        void listAllAppsAboveOrEqualAGivenStarRatingReturnsNoAppsWhenNoAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());

            assertTrue(emptyAppStore.listAllAppsAboveOrEqualAGivenStarRating(3).contains("No Apps"));
        }

        @Test
        void listAllAppsAboveOrEqualAGivenStarRatingReturnsNoAppsWhenNoAppsAboveOrEqualExist() {
            assertEquals(7, appStore.numberOfApps());

            assertTrue(appStore.listAllAppsAboveOrEqualAGivenStarRating(2).contains("No apps have a rating of "));
        }

        @Test
        void randomAppReturnsNoRandomAppWhenNoAppsExist() {
            assertEquals(0, emptyAppStore.numberOfApps());
            assertNull(emptyAppStore.randomApp());
        }

        @Test
        void randomAppReturnsRandomAppWhenAppsExist() {
        }

    }

    @Nested
    class SortingMethods {

        @Test
        void sortByNameAscendingReOrdersList() {
            assertEquals(7, appStore.numberOfApps());
            //checks the order of the objects in the list
            assertEquals(edAppBelowBoundary, appStore.getAppByIndex(0));
            assertEquals(prodAppOnBoundary, appStore.getAppByIndex(1));
            assertEquals(gameAppAboveBoundary, appStore.getAppByIndex(2));
            assertEquals(prodAppBelowBoundary, appStore.getAppByIndex(3));
            assertEquals(edAppAboveBoundary, appStore.getAppByIndex(4));
            assertEquals(prodAppInvalidData, appStore.getAppByIndex(5));
            assertEquals(gameAppOnBoundary, appStore.getAppByIndex(6));

            appStore.sortAppsByNameAscending();
            assertEquals(prodAppInvalidData, appStore.getAppByIndex(0));
            assertEquals(gameAppOnBoundary, appStore.getAppByIndex(1));
            assertEquals(edAppAboveBoundary, appStore.getAppByIndex(2));
            assertEquals(gameAppAboveBoundary, appStore.getAppByIndex(3));
            assertEquals(prodAppBelowBoundary, appStore.getAppByIndex(4));
            assertEquals(prodAppOnBoundary, appStore.getAppByIndex(5));
            assertEquals(edAppBelowBoundary, appStore.getAppByIndex(6));
        }

        @Test
        void sortByNameAscendingDoesntCrashWhenListIsEmpty() {
            assertEquals(0,emptyAppStore.numberOfApps());
            emptyAppStore.sortAppsByNameAscending();
        }

    }

    @Test
    void simulateRatingsAddsRatings() {
        appStore.simulateRatings();
        assertTrue(edAppAboveBoundary.addRating(new Rating(3, "John Doe", "Very Good")));
    }

    //--------------------------------------------
    // Helper Methods
    //--------------------------------------------
    EducationApp setupEducationAppWithRating(int rating1, int rating2) {
        //setting all conditions to true
        EducationApp edApp = new EducationApp(developerLego, "WeDo", 1,
                1.0, 1.00, 3);
        edApp.addRating(new Rating(rating1, "John Doe", "Very Good"));
        edApp.addRating(new Rating(rating2, "Jane Doe", "Excellent"));

        return edApp;
    }

    GameApp setupGameAppWithRating(int rating1, int rating2) {
        GameApp gameApp = new GameApp(developerEAGames, "MazeRunner", 1,
                1.0, 1.00, true);
        gameApp.addRating(new Rating(rating1, "John Soap", "Exciting Game"));
        gameApp.addRating(new Rating(rating2, "Jane Soap", "Nice Game"));
        return gameApp;
    }

    ProductivityApp setupProductivityAppWithRating(int rating1, int rating2) {
        ProductivityApp productivityApp = new ProductivityApp(developerApple, "Evernote", 1,
                1.0, 1.99);

        productivityApp.addRating(new Rating(rating1, "John101", "So easy to add a note"));
        productivityApp.addRating(new Rating(rating2, "Jane202", "So useful"));
        return productivityApp;
    }

}