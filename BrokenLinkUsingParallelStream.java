package seleniumConcepts;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BrokenLinksParallelStream {
	
	 public static void main(String[] args) throws InterruptedException {
	        WebDriver driver = new ChromeDriver();
	        driver.get("https://www.snapdeal.com");
	        Thread.sleep(3000);

	        List<WebElement> links = driver.findElements(By.tagName("a"));
	        System.out.println("Total Link count is: " + links.size());

	        Set<String> urlList = new HashSet<>();

	        for (WebElement ele : links) {
	            String href = ele.getAttribute("href");
	            if (href != null && href.startsWith("http")) {
	                urlList.add(href);
	            }
	        }
	        long startTime = System.currentTimeMillis();
	        // Use parallelStream to check links in parallel
	        urlList.parallelStream().map(link -> {
	            checkLink(link);
	            return null;
	        })
	        .collect(java.util.stream.Collectors.toList());
	        long endTime = System.currentTimeMillis();
	        System.out.println("Total time taken: " + (endTime - startTime) + " ms");
	       
	        driver.quit();
	    }

	    private static void checkLink(String link) {
	        try {
	            HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
	            connection.setRequestMethod("HEAD"); //for faster results
	            //connection.setConnectTimeout(5000);
	            connection.connect();
	            int responseCode = connection.getResponseCode();
	            
	            if (responseCode >= 400) {
	            	
	                System.out.println("Broken: " + link + " [Status: " + responseCode + "]");
	            } 
	        } catch (Exception e) {
	            System.out.println("Error: " + link + " [" + e.getMessage() + "]");
	        }
	    }

}
