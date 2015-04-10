package language;


import twitter4j.Status;
import de.twitter4serioussearch.api.TweetListener;
import de.twitter4serioussearch.api.Twitter4Serioussearch;
import de.twitter4serioussearch.api.Twitter4SerioussearchFactory;

/**
 * Hello world!
 *
 */
public class App 
{
	static Twitter4Serioussearch twitter;
	
    public static void main( String[] args )
    {
    	twitter = Twitter4SerioussearchFactory.build();
		twitter.registerQuery("Hallo", "1", new TweetListener() {
			
			@Override
			public void handleNewTweet(Status tweet) {
				System.out.println("listener:" + tweet);
				
			}
		});
		
    }
}