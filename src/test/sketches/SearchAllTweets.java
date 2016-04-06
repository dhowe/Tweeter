package test.sketches;

import java.util.ArrayList;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class SearchAllTweets
{
  public static void main(String[] args)
  {

    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setOAuthConsumerKey("wUyqKA0TfNnhPnSJeudkFGGwi");
    cb.setOAuthConsumerSecret("nw8pq1olZz00lB3pdCsyCyaeiTLzkz2X91ipoCC7YoOxwCqEVP");
    cb.setOAuthAccessToken("2855526444-u183iquum6psPTX5997GSrlsbZYCg4OwItJpasZ");
    cb.setOAuthAccessTokenSecret("xLCjf8P97KrazPdAsXw7f1UydkzEhX1WKw7ek2rW1lDNw");

    Twitter twitter = new TwitterFactory(cb.build()).getInstance();
    searchAll(twitter, "adnauseam");
  }

  private static void searchAll(Twitter twitter, String search)
  {
    Query query = new Query(search);
    query.setCount(100);
    long lastId = Long.MAX_VALUE;
    ArrayList<Status> tweets = new ArrayList<Status>();
    while (true) //tweets.size() < numberOfTweets)
    {
      try
      {
        int count = tweets.size();
        QueryResult result = twitter.search(query);
        tweets.addAll(result.getTweets());
        //System.out.println("Gathered " + tweets.size() + " tweets");
        if (count == tweets.size())
          break;
        
        for (Status t : tweets) {
          if (t.getId() < lastId)
            lastId = t.getId();
        }
        
        query.setMaxId(lastId - 1);
      }
      catch (TwitterException te)
      {
        te.printStackTrace();
      }
    }
  }
}
