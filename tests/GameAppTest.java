package models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import utils.Utilities;

import static org.junit.jupiter.api.Assertions.*;

public class GameAppTest {

    private GameApp gameAppBelowBoundary, gameAppOnBoundary, gameAppAboveBoundary, gameAppInvalidData;
    private Developer developerLego = new Developer("Lego", "www.lego.com");
    private Developer developerSphero = new Developer("Sphero", "www.sphero.com");

    @BeforeEach
    void setUp() {
        //Validation: appSize(1-1000), appVersion(>=1.0), ageRating (0-18), appCost(>=0), isMultiplayer(true/false).
        gameAppBelowBoundary = new GameApp(developerLego, "WeDo", 1, 1.0, 0, true);
        gameAppOnBoundary = new GameApp(developerLego, "Spike", 1000, 2.0, 1.99, true);
        gameAppAboveBoundary = new GameApp(developerLego, "EV3", 1001, 3.5, 2.99, true);
        gameAppInvalidData = new GameApp(developerLego, "", -1, 0, -1.00, false);
    }

    @AfterEach
    void tearDown() {
        gameAppBelowBoundary = gameAppOnBoundary = gameAppAboveBoundary = gameAppInvalidData = null;
        developerLego = developerSphero = null;
    }

    @Nested
    class Getters {

        @Test
        void getDeveloper() {
            assertEquals(developerLego, gameAppBelowBoundary.getDeveloper());
            assertEquals(developerLego, gameAppOnBoundary.getDeveloper());
            assertEquals(developerLego, gameAppAboveBoundary.getDeveloper());
            assertEquals(developerLego, gameAppInvalidData.getDeveloper());
        }

        @Test
        void getAppName() {
            assertEquals("WeDo", gameAppBelowBoundary.getAppName());
            assertEquals("Spike", gameAppOnBoundary.getAppName());
            assertEquals("EV3", gameAppAboveBoundary.getAppName());
            assertEquals("", gameAppInvalidData.getAppName());
        }

        @Test
        void getAppSize() {
            assertEquals(1, gameAppBelowBoundary.getAppSize());
            assertEquals(1000, gameAppOnBoundary.getAppSize());
            assertEquals(0, gameAppAboveBoundary.getAppSize());
            assertEquals(0, gameAppInvalidData.getAppSize());
        }

        @Test
        void getAppVersion() {
            assertEquals(1.0, gameAppBelowBoundary.getAppVersion());
            assertEquals(2.0, gameAppOnBoundary.getAppVersion());
            assertEquals(3.5, gameAppAboveBoundary.getAppVersion());
            assertEquals(1.0, gameAppInvalidData.getAppVersion());
        }

        @Test
        void getAppCost() {
            assertEquals(0, gameAppBelowBoundary.getAppCost());
            assertEquals(1.99, gameAppOnBoundary.getAppCost());
            assertEquals(2.99, gameAppAboveBoundary.getAppCost());
            assertEquals(0, gameAppInvalidData.getAppCost());
        }

        @Test
        void getMultiplayer() {
            assertTrue(gameAppBelowBoundary.isMultiplayer());
            assertTrue(gameAppOnBoundary.isMultiplayer());
            assertTrue(gameAppAboveBoundary.isMultiplayer());
            assertFalse(gameAppInvalidData.isMultiplayer());
        }
    }

    @Nested
    class Setters {

        @Test
        void setDeveloper() {
            //no validation in models
            assertEquals(developerLego, gameAppBelowBoundary.getDeveloper());
            gameAppBelowBoundary.setDeveloper(developerSphero);
            assertEquals(developerSphero, gameAppBelowBoundary.getDeveloper());
        }

        @Test
        void setAppName() {
            //no validation in models
            assertEquals("WeDo", gameAppBelowBoundary.getAppName());
            gameAppBelowBoundary.setAppName("Mindstorms");
            assertEquals("Mindstorms", gameAppBelowBoundary.getAppName());
        }

        @Test
        void setAppSize() {
            //Validation: appSize(1-1000)
            assertEquals(1, gameAppBelowBoundary.getAppSize());

            gameAppBelowBoundary.setAppSize(1000);
            assertEquals(1000, gameAppBelowBoundary.getAppSize()); //update

            gameAppBelowBoundary.setAppSize(1001);
            assertEquals(1000, gameAppBelowBoundary.getAppSize()); //no update

            gameAppBelowBoundary.setAppSize(2);
            assertEquals(2, gameAppBelowBoundary.getAppSize()); //update

            gameAppBelowBoundary.setAppSize(0);
            assertEquals(2, gameAppBelowBoundary.getAppSize()); //no update
        }

        @Test
        void setAppVersion() {
            //Validation: appVersion(>=1.0)
            assertEquals(1.0, gameAppBelowBoundary.getAppVersion());

            gameAppBelowBoundary.setAppVersion(2.0);
            assertEquals(2.0, gameAppBelowBoundary.getAppVersion()); //update

            gameAppBelowBoundary.setAppVersion(0.0);
            assertEquals(2.0, gameAppBelowBoundary.getAppVersion()); //no update

            gameAppBelowBoundary.setAppVersion(1.0);
            assertEquals(1.0, gameAppBelowBoundary.getAppVersion()); //update
        }

        @Test
        void setAppCost() {
            //Validation: appCost(>=0)
            assertEquals(0.0, gameAppBelowBoundary.getAppCost());

            gameAppBelowBoundary.setAppCost(1.0);
            assertEquals(1.0, gameAppBelowBoundary.getAppCost()); //update

            gameAppBelowBoundary.setAppCost(-1);
            assertEquals(1.0, gameAppBelowBoundary.getAppCost()); //no update

            gameAppBelowBoundary.setAppCost(0.0);
            assertEquals(0.0, gameAppBelowBoundary.getAppCost()); //update
        }

        @Test
        void setMultiplayer() {
            //Validation: isMultiplayer(true/false)
            assertTrue(gameAppBelowBoundary.isMultiplayer());

            gameAppBelowBoundary.setMultiplayer(false); //update
            assertFalse(gameAppBelowBoundary.isMultiplayer());
        }

    }

    @Nested
    class ObjectStateMethods {

        @Test
        void appSummaryReturnsCorrectString() {
            GameApp gameApp = setupGameAppWithRating(3, 4);
            String stringContents = gameApp.appSummary();

            assertTrue(stringContents.contains(gameApp.getAppName() + "(V" + gameApp.getAppVersion()));
            assertTrue(stringContents.contains(gameApp.getDeveloper().toString()));
            assertTrue(stringContents.contains("€" + gameApp.getAppCost()));
            assertTrue(stringContents.contains("Rating: " + gameApp.calculateRating()));
        }

        @Test
        void toStringReturnsCorrectString() {
            GameApp gameApp = setupGameAppWithRating(3, 4);
            String stringContents = gameApp.toString();

            assertTrue(stringContents.contains(gameApp.getAppName()));
            assertTrue(stringContents.contains("(Version " + gameApp.getAppVersion()));
            assertTrue(stringContents.contains(gameApp.getDeveloper().toString()));
            assertTrue(stringContents.contains(gameApp.getAppSize() + "MB"));
            assertTrue(stringContents.contains("Cost: " + gameApp.getAppCost()));
            assertTrue(stringContents.contains("Multiplayer: " + Utilities.booleanToYN(gameApp.isMultiplayer())));
            assertTrue(stringContents.contains("Ratings (" + gameApp.calculateRating()));

            //contains list of ratings too
            assertTrue(stringContents.contains("John Doe"));
            assertTrue(stringContents.contains("Very Good"));
            assertTrue(stringContents.contains("Jane Doe"));
            assertTrue(stringContents.contains("Excellent"));
        }

    }

    @Nested
    class RecommendedApp {

        @Test
        void appIsNotRecommendedWhenRatingIsLessThan4() {
            //setting all conditions to true with ratings of 3 and 3 (i.e. 3.0)
            GameApp gameApp = setupGameAppWithRating(3, 3);
            //verifying recommended app returns false (rating not high enough)
            assertFalse(gameApp.isRecommendedApp());
        }

        @Test
        void appIsNotRecommendedWhenNoRatingsExist() {
            //setting all conditions to true with no ratings
            GameApp gameApp = new GameApp(developerLego, "WeDo", 1,
                    1.0, 1.00,  true);
            //verifying recommended app returns true
            assertFalse(gameApp.isRecommendedApp());
        }

        @Test
        void appIsRecommendedWhenAllOfTheConditionsAreTrue() {
            //setting all conditions to true with ratings of 4 and 4 (i.e. 4.0)
            GameApp gameApp = setupGameAppWithRating(4, 4);

            //verifying recommended app returns true
            assertTrue(gameApp.isRecommendedApp());
        }

    }

    GameApp setupGameAppWithRating(int rating1, int rating2) {
        //setting all conditions to true
        GameApp gameApp = new GameApp(developerLego, "WeDo", 1,
                1.0, 1.00,  true);
        gameApp.addRating(new Rating(rating1, "John Doe", "Very Good"));
        gameApp.addRating(new Rating(rating2, "Jane Doe", "Excellent"));

        //verifying all conditions are true for a recommended educational app]
        assertEquals(2, gameApp.getRatings().size());  //two ratings are added
        assertEquals(((rating1 + rating2) / 2.0), gameApp.calculateRating(), 0.01);
        assertTrue(gameApp.isMultiplayer());

        return gameApp;
    }

}

