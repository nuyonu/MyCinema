package app.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Database {

    private static Logger logger = LoggerFactory.getLogger(Database.class);

    private Database() {
    }

    public static void create() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
        try {
            mongoDatabase.createCollection("Users");
        } catch (MongoCommandException e) {
            logger.info("Collection Users already exist.");
        }
        try {
            mongoDatabase.createCollection("Movies");
        } catch (MongoCommandException e) {
            logger.info("Collection Movies already exist.");
        }
        try {
            mongoDatabase.createCollection("CinemaRooms");
        } catch (MongoCommandException e) {
            logger.info("Collection CinemaRooms already exist.");
        }
        try {
            mongoDatabase.createCollection("Reservation");
        } catch (MongoCommandException e) {
            logger.info("Collection Reservation already exist.");
        }
        try {
            mongoDatabase.createCollection("Reset");
        } catch (MongoCommandException e) {
            logger.info("Collection Reset already exist.");
        }
        try {
            mongoDatabase.createCollection("Screening");
        } catch (MongoCommandException e) {
            logger.info("Collection Screening already exist.");
        }
        try {
            mongoDatabase.createCollection("Statistics");
        } catch (MongoCommandException e) {
            logger.info("Collection Statistics already exist.");
        }
    }
}
