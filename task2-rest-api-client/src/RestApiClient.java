import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * REST API Client - Task 2
 * This application consumes public REST APIs and displays data in structured format
 * Example: Weather API, JSONPlaceholder API
 * 
 * Note: This example uses basic Java HTTP client. For production, consider using
 * libraries like OkHttp, Apache HttpClient, or Spring RestTemplate
 * 
 * @author Santosh
 * @version 1.0
 */
public class RestApiClient {
    
    /**
     * Makes a GET request to the specified URL
     * @param urlString URL to make request to
     * @return Response as String
     */
    public static String makeGetRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Java REST Client");
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                System.err.println("HTTP Error: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error making request: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Fetches and displays weather data (mock implementation)
     * Note: Replace with actual weather API key and endpoint
     */
    public static void fetchWeatherData() {
        System.out.println("=== Weather Data Demo ===");
        
        // Mock weather data since we don't have API key
        String mockWeatherData = "{\n" +
            "  \"name\": \"London\",\n" +
            "  \"main\": {\n" +
            "    \"temp\": 15.5,\n" +
            "    \"humidity\": 65,\n" +
            "    \"pressure\": 1013\n" +
            "  },\n" +
            "  \"weather\": [{\n" +
            "    \"main\": \"Clouds\",\n" +
            "    \"description\": \"overcast clouds\"\n" +
            "  }]\n" +
            "}";
        
        try {
            JSONObject weather = new JSONObject(mockWeatherData);
            String city = weather.getString("name");
            JSONObject main = weather.getJSONObject("main");
            JSONArray weatherArray = weather.getJSONArray("weather");
            JSONObject weatherInfo = weatherArray.getJSONObject(0);
            
            System.out.println("City: " + city);
            System.out.println("Temperature: " + main.getDouble("temp") + "°C");
            System.out.println("Humidity: " + main.getInt("humidity") + "%");
            System.out.println("Pressure: " + main.getInt("pressure") + " hPa");
            System.out.println("Weather: " + weatherInfo.getString("main"));
            System.out.println("Description: " + weatherInfo.getString("description"));
            
        } catch (Exception e) {
            System.err.println("Error parsing weather data: " + e.getMessage());
        }
    }
    
    /**
     * Fetches and displays posts from JSONPlaceholder API
     */
    public static void fetchPosts() {
        System.out.println("\n=== Fetching Posts from JSONPlaceholder ===");
        
        String response = makeGetRequest("https://jsonplaceholder.typicode.com/posts?_limit=5");
        
        if (response != null) {
            try {
                JSONArray posts = new JSONArray(response);
                
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.getJSONObject(i);
                    System.out.println("\n--- Post " + (i + 1) + " ---");
                    System.out.println("ID: " + post.getInt("id"));
                    System.out.println("User ID: " + post.getInt("userId"));
                    System.out.println("Title: " + post.getString("title"));
                    System.out.println("Body: " + post.getString("body").substring(0, 
                        Math.min(100, post.getString("body").length())) + "...");
                }
            } catch (Exception e) {
                System.err.println("Error parsing posts: " + e.getMessage());
            }
        }
    }
    
    /**
     * Fetches and displays user information
     */
    public static void fetchUserInfo(int userId) {
        System.out.println("\n=== Fetching User Information ===");
        
        String response = makeGetRequest("https://jsonplaceholder.typicode.com/users/" + userId);
        
        if (response != null) {
            try {
                JSONObject user = new JSONObject(response);
                JSONObject address = user.getJSONObject("address");
                JSONObject geo = address.getJSONObject("geo");
                JSONObject company = user.getJSONObject("company");
                
                System.out.println("User ID: " + user.getInt("id"));
                System.out.println("Name: " + user.getString("name"));
                System.out.println("Username: " + user.getString("username"));
                System.out.println("Email: " + user.getString("email"));
                System.out.println("Phone: " + user.getString("phone"));
                System.out.println("Website: " + user.getString("website"));
                System.out.println("City: " + address.getString("city"));
                System.out.println("Zipcode: " + address.getString("zipcode"));
                System.out.println("Company: " + company.getString("name"));
                System.out.println("Company Motto: " + company.getString("catchPhrase"));
                
            } catch (Exception e) {
                System.err.println("Error parsing user info: " + e.getMessage());
            }
        }
    }
    
    /**
     * Fetches and displays todos for a user
     */
    public static void fetchUserTodos(int userId) {
        System.out.println("\n=== Fetching User Todos ===");
        
        String response = makeGetRequest("https://jsonplaceholder.typicode.com/todos?userId=" + userId + "&_limit=5");
        
        if (response != null) {
            try {
                JSONArray todos = new JSONArray(response);
                
                for (int i = 0; i < todos.length(); i++) {
                    JSONObject todo = todos.getJSONObject(i);
                    System.out.println("\n--- Todo " + (i + 1) + " ---");
                    System.out.println("ID: " + todo.getInt("id"));
                    System.out.println("Title: " + todo.getString("title"));
                    System.out.println("Completed: " + (todo.getBoolean("completed") ? "✓" : "✗"));
                }
            } catch (Exception e) {
                System.err.println("Error parsing todos: " + e.getMessage());
            }
        }
    }
    
    /**
     * Interactive menu for API operations
     */
    public static void showMenu() {
        System.out.println("\n=== REST API Client Menu ===");
        System.out.println("1. Fetch Posts");
        System.out.println("2. Fetch User Info");
        System.out.println("3. Fetch User Todos");
        System.out.println("4. Show Weather Data (Demo)");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }
    
    /**
     * Main method - demonstrates REST API consumption
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== REST API Client Demo ===");
        System.out.println("This application demonstrates consuming REST APIs");
        System.out.println("Using JSONPlaceholder (https://jsonplaceholder.typicode.com/)");
        
        while (true) {
            showMenu();
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                switch (choice) {
                    case 1:
                        fetchPosts();
                        break;
                    case 2:
                        System.out.print("Enter user ID (1-10): ");
                        int userId = scanner.nextInt();
                        scanner.nextLine();
                        fetchUserInfo(userId);
                        break;
                    case 3:
                        System.out.print("Enter user ID (1-10): ");
                        int todoUserId = scanner.nextInt();
                        scanner.nextLine();
                        fetchUserTodos(todoUserId);
                        break;
                    case 4:
                        fetchWeatherData();
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
                
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                scanner.nextLine(); // clear invalid input
            }
        }
    }
}

// Note: You'll need to add JSON library dependency
// For Maven, add this to pom.xml:
/*
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20230618</version>
</dependency>
*/