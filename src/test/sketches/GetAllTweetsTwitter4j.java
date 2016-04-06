package test.sketches;

import java.util.ArrayList;
import java.util.List;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class GetAllTweetsTwitter4j
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
    // Status[] allTweets = getAllTweets(twitter, "cnn");
    // System.out.println("Total: " + allTweets.length);
    try
    {
 
      long cursor = -1;
      PagableResponseList<User> friends;
      do
      {
        friends = twitter.getFriendsList(userId, cursor);
      }
      while ((cursor = friends.getNextCursor()) != 0);

      System.out.println("Found "+friends.size()+" friends");

      for (User user : friends)
      {
        Status[] tweets = getAllTweets(twitter, user, 110);
        System.out.println(user.getName() + ": " + tweets.length);
      }
    }
    catch (TwitterException e)
    {
      e.printStackTrace();
    }
  }
  
  public static User[] getFriends(Twitter twitter, long userId) throws TwitterException
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
  
  public static User[] getFollowers(Twitter twitter, long userId) throws TwitterException
  {
    long cursor = -1;
    PagableResponseList<User> friends;
    do
    {
      friends = twitter.getFollowersList(userId, cursor);
    }
    while ((cursor = friends.getNextCursor()) != 0);
    
    return friends.toArray(new User[0]);
  }
  
  public static Status[] getAllTweets(Twitter twitter, Object user)
  {
      return getAllTweets(twitter, user, Integer.MAX_VALUE);
  }
  
  public static Status[] getAllTweets(Twitter twitter, Object user, int maxNum)
  {
    int pageno = 1;
    List statuses = new ArrayList();

    while (true)
    {
      try
      {

        int size = statuses.size();
        Paging page = new Paging(pageno++, 100);
        if (user instanceof User)
          statuses.addAll(twitter.getUserTimeline(((User)user).getId(), page));
        else if (user instanceof String)
          statuses.addAll(twitter.getUserTimeline((String) user, page));
        else if (user instanceof Long)
          statuses.addAll(twitter.getUserTimeline((long) user, page));
        else if (user instanceof Integer)
          statuses.addAll(twitter.getUserTimeline((int) user, page));
        else
        {
          throw new RuntimeException("Unexpected user-type: " + user);
        }
        int ss = statuses.size();
        if (ss >= maxNum || ss == size)
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
