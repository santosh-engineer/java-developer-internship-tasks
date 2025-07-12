import java.util.*;
import java.util.stream.Collectors;

/**
 * AI-Based Recommendation System - Task 4
 * This system uses collaborative filtering to recommend products based on user preferences
 * 
 * @author Santosh
 * @version 1.0
 */
public class RecommendationSystem {
    
    private Map<String, User> users;
    private Map<String, Product> products;
    private Map<String, List<Rating>> userRatings;
    private Map<String, List<Rating>> productRatings;
    
    /**
     * Constructor
     */
    public RecommendationSystem() {
        users = new HashMap<>();
        products = new HashMap<>();
        userRatings = new HashMap<>();
        productRatings = new HashMap<>();
    }
    
    /**
     * Adds a user to the system
     */
    public void addUser(String userId, String name, String[] preferences) {
        User user = new User(userId, name, preferences);
        users.put(userId, user);
        userRatings.put(userId, new ArrayList<>());
    }