package test.sketches;

import java.util.ArrayList;
import java.util.List;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class GetAllTweetsSO
{
  public static void main(String[] args)
  {
    long userId = 2855126445l;
    
    ConfigurationBuilder cb = new ConfigurationBuilder();

    cb.setOAuthConsumerKey("wUyqKA0TfNnhPnSJeudkFGGwi");
    cb.setOAuthConsumerSecret("nw8pq1olZz00lB3pdCsyCyaeiTLzkz2X91ipoCC7YoOxwCqEVP");
    cb.setOAuthAccessToken("2855526444-u183iquum6psPTX5997GSrlsbZYCg4OwItJpasZ");
    cb.setOAuthAccessTokenSecret("xLCjf8P97KrazPdAsXw7f1UydkzEhX1WKw7ek2rW1lDNw");

    Twitter twitter = new TwitterFactory(cb.build()).getInstance();
    try
    {
 
      User[] friends = getFriends(twitter, userId);

      System.out.println("Found "+friends.length+" friends");

      for (User user : friends)
      {
        Status[] tweets = getAllTweets(twitter, user);
        System.out.println(user.getName() + ": " + tweets.length);
      }
    }
    catch (TwitterException e)
    {
      e.printStackTrace();
    }
  }

  private static User[] getFriends(Twitter twitter, long userId) throws TwitterException
  {
    long cursor = -1;
    PagableResponseList<User> friends;
    do
    {
      friends = twitter.getFriendsList(userId, cursor);
    }
    while ((cursor = friends.getNextCursor()) != 0);
    
    return friends.toArray(new User[0]);
  }

  public static Status[] getAllTweets(Twitter twitter, User user)
  {
    return getAllTweets(twitter, user.getId());
  }
  
  public static Status[] getAllTweets(Twitter twitter, long userId)
  {
    int pageno = 1;
    List statuses = new ArrayList();

    while (true)
    {
      try
      {

        int size = statuses.size();
        Paging page = new Paging(pageno++, 100);
        statuses.addAll(twitter.getUserTimeline(userId, page));
        if (statuses.size() == size)
          break;
      }
      catch (TwitterException e)
      {

        e.printStackTrace();
      }
    }

    return (Status[]) statuses.toArray(new Status[0]);
  }
}
