package tweeter;

import java.util.*;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/**
 * The core object for the Tweeter library. Provides some simple helper 
 * methods that return primitive types and Strings, as well as exposing 
 * (through the 'twitter4j' instance variable) the full Twitter4j library. 
 * <p>
 * When fetching tweets for a user or a keyword, the default request will
 * return a maximum of 100 items. To get more results, you can supply a higher
 * number as the second argument. Passing -1 as the second argument will cause
 * the function to try to fetch ALL items. However, be warned that a separate 
 * request is issued for every 100 items, so, if you are using the rate-limited API,
 * this may quickly exhaust your supply of queries. 
 * <p>
 * Note: methods starting with an underscore (e.g., <i>myTweeter._search()</i>) 
 * are marked as 'advanced' in the documentation as they either accept or return Twitter4j types,
 * which require the addition of another import statement to your sketch, e.g.,:
 *  <pre>
 *  import twitter4j.*;
 *  </pre> 
 * 
 * @author daniel@rednoise.org
 */
public class Tweeter
{
  protected static final String NO_SEARCH_ERR
    = "You must first perform a search before calling this function";

  /**
   * The current version of the library 
   */
  public static final String VERSION = "0.0.6";
  
  /**
   * Silences all console output from the library 
   * @exclude
   */
  public static final boolean SILENT = false;
  
  static {
    
    if (!SILENT)
      System.out.println("[INFO] Tweeter v"+VERSION);
  }

  protected static final Status[] ES = new Status[0];

  protected static final int DEFAULT_MAX = 100;

  /**
   * Provides access to the Twitter4j library
   */
  public Twitter twitter4j;
  
  /**
   * @exclude
   */
  public List<Status> results; // result from last search
  
  /**
   * @exclude
   */
  public long userId = -1;

  /**
   * Create a Tweeter object with your Twitter authentication keys (obtained from http://dev.twitter.com)
   * @param ckey Twitter consumer key
   * @param csec Twitter consumer secret
   * @param atok Twitter access token
   * @param asec Twitter access token secret
   */
  public Tweeter(String ckey, String csec, String atok, String asec)
  {
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setOAuthConsumerKey(ckey);
    cb.setOAuthConsumerSecret(csec);
    cb.setOAuthAccessToken(atok);
    cb.setOAuthAccessTokenSecret(asec);

    try
    {
      this.twitter4j = new TwitterFactory(cb.build()).getInstance();
    }
    catch (IllegalStateException e)
    {
      throw new TweeterException(e);
    }
  }
  
  /**
   * Posts the specified 'tweet' to Twitter
   */
  public void post(String tweet)
  {
    this._post(tweet);
  }
  
  /**
   * Returns the user-id for this user (that is, the user associated with the set of keys
   * used to create the Tweeter object)
   */
  public long id()
  {
    if (this.userId < 0)
    {
      try
      {
        this.userId = twitter4j.getId();
      }
      catch (IllegalStateException | TwitterException e)
      {
        throw new TweeterException(e);
      }
    }
    return this.userId;
  }

  /**
   * Returns the Text of the tweet at the specified index following a search request
   */
  public String resultText(int index)
  {
    if (results == null || index < results.size() -1)
      throw new TweeterException("No tweet for this index: "+index);
    
    return this.results.get(index).getText();
  }
  
  /**
   * Returns the User-Name associated with the tweet at the specified index following a search request
   */
  public String resultName(int index)
  {
    if (results == null || index < results.size() -1)
      throw new TweeterException("No tweet for this index: "+index);
    
    return this.results.get(index).getUser().getName();
  }

  /**
   * Returns the Creation-Date of the tweet at the specified index following a search request
   */
  public String resultDate(int index)
  {
    if (results == null || index < results.size() -1)
      throw new TweeterException("No tweet for this index: "+index);
    
    return this.results.get(index).getCreatedAt().toString();
  }
  
  /**
   * Returns the number of tweets returned from the last search
   */
  public int resultCount()
  {
    if (results == null)
      throw new TweeterException(NO_SEARCH_ERR);
    
    return this.results.size();
  }


  /**
   * Returns the array of Texts, one for each tweet, following a search request
   */
  public String[] resultTexts()
  {
    if (results == null)
      throw new TweeterException(NO_SEARCH_ERR);
    
    return getTexts();
  }
  
  /**
   * Returns the array of User-Names, one for each tweet, following a search request
   */
  public String[] resultNames()
  {
    if (results == null)
      throw new TweeterException(NO_SEARCH_ERR);
    
    return getNames();
  }
  
  /**
   * Returns the array of Creation-Dates, one for each tweet, following a search request
   */
  public String[] resultDates()
  {
    if (results == null)
      throw new TweeterException(NO_SEARCH_ERR);
    
    return getDates();
  }


  /**
   * Performs a twitter search for the specified 'query' term 
   * Note: will return a max of 100 items
   */
  public void search(String query)
  {
    this._search(query, DEFAULT_MAX);
  }
  
  /**
   * Performs a twitter search for the specified 'query' term 
   * storing at most 'max' items.
   * 
   * Note: To return ALL results, pass -1 as 'max', however
   * be warned that a new request is issued for every 100 items,
   * so, if you are using the rate-limited API, this may exhaust your supply. 
   */
  public void search(String query, int max)
  {
    this._search(query, max);
  }

  /**
   * Performs a twitter search for the tweets from this user (you) 
   * returning a max of 100
   */
  public void tweets()
  {
    this._tweets();
  }

  /**
   * Performs a twitter search for the tweets from this user (you) 
   * storing at most 'max' items (pass -1 to get ALL tweets)
   */
  public void tweets(int max)
  {
    this._tweets(max);
  }

  /**
   * Performs a twitter search for the tweets from 'userName'
   * returning a max of 100
   */
  public void tweets(String userName)
  {
    this._tweets(userName);
  }

  /**
   * Performs a twitter search for the tweets from 'userName'
   * storing at most 'max' items (pass -1 to get ALL tweets)
   */
  public void tweets(String userName, int max)
  {
    this._tweets(userName, max);
  }
    
  // Twitter4j methods -----------------------------------------

  /**
   * Performs a twitter search for the tweets from this user (you) 
   */
  public Status[] _tweets()
  {
    return _tweets(DEFAULT_MAX);
  }

  /**
   * Performs a twitter search for the tweets from this user (you) 
   * storing at most 'max' items
   */
  public Status[] _tweets(int max)
  {
    try
    {
      this.results = toStatusList(fetchTimeLine(id(), max), max);
      return results.toArray(ES);
    }
    catch (TwitterException e)
    {
      throw new TweeterException(e);
    }
  }

  /**
   * Performs a twitter search for the tweets from 'userName'
   */
  public Status[] _tweets(String userName)
  {
    return _tweets(userName, DEFAULT_MAX);
  }

  /**
   * Performs a twitter search for the tweets from 'userName'
   * storing at most 'max' items
   */
  public Status[] _tweets(String userName, int max)
  {
    try
    {
      List<Status> timeLine = fetchTimeLine(userName, max);
      this.results = toStatusList(timeLine, max);
      return results.toArray(ES);
    }
    catch (TwitterException e)
    {
      throw new TweeterException(e);
    }
  }
  
    /**
   * Performs a twitter search for the specified 'query' term 
   */
  public Status[] _search(String query)
  {
      return this._search(query, DEFAULT_MAX);
  }
  
  /**
   * Performs a twitter search for the specified 'query' term 
   * storing at most 'maxNum' items
   */
  public Status[] _search(String query, int maxNum)
  {
    try
    {
      int perPage = maxNum > 0 && maxNum < DEFAULT_MAX ? maxNum : DEFAULT_MAX;
      return this._search(new Query(query).count(perPage), maxNum);
    }
    catch (TwitterException e)
    {
      throw new TweeterException(e);
    }
  }

  
  /**
   * Performs a twitter search for the twitter4j.Query object
   * and returns the result as an array of twitter4j.Status objects
   * Returns at most 'max' items (pass -1 for no limit)
   * @throws TwitterException 
   */
  public Status[] _search(Query query, int max) throws TwitterException
  {
    long lastId = Long.MAX_VALUE;
    List<Status> tweets = new ArrayList<Status>();
    
    while (true)
    {
      int preCount = tweets.size();
      QueryResult result = twitter4j.search(query);
      tweets.addAll(result.getTweets());
      int postCount = tweets.size();
      
      if (postCount == preCount || (max > 0 && postCount >= max))
        break;

      lastId = getMinId(lastId, tweets);

      query.setMaxId(lastId - 1);
    }
     
    this.results = tweets.subList(0, Math.min(tweets.size(), max));
    
    return results.toArray(ES);
  }

  private long getMinId(long lastId, List<Status> tweets)
  {
    for (Status t : tweets) {
      if (t.getId() < lastId)
        lastId = t.getId();
    }
    return lastId;
  }
  
  /**
   * Posts the specified 'tweet' to Twitter
   * @return a twitter4j.Status object
   */
  public Status _post(String tweet)
  {
    try
    {
      return twitter4j.updateStatus(tweet);
    }
    catch (TwitterException te)
    {
      throw new TweeterException(te);
    }
  }
  
  // Helper methods -----------------------------------------
 
  protected String[] getNames()
  {
    int i = 0;
    String[] s = new String[results.size()];
    for (Iterator it = results.iterator(); it.hasNext();)
      s[i++] = ((Status) it.next()).getUser().getName();
    return s;
  }
  
  protected String[] getDates()
  {
    int i = 0;
    String[] s = new String[results.size()];
    for (Iterator it = results.iterator(); it.hasNext();)
      s[i++] = ((Status) it.next()).getCreatedAt().toString();
    return s;
  }   
  
  protected String[] getTexts()
  {
    int i = 0;
    String[] s = new String[results.size()];
    for (Iterator it = results.iterator(); it.hasNext();)
      s[i++] = ((Status) it.next()).getText();
    return s;
  }
  
  protected User[] getFriends(Twitter twitter, long uid) 
  {
    long cursor = -1;
    PagableResponseList<User> friends = null;
    try
    {
      do
      {
        friends = twitter.getFriendsList(uid, cursor);
      }
      while ((cursor = friends.getNextCursor()) != 0);
    }
    catch (TwitterException e)
    {
      throw new TweeterException(e);
    }
    
    return friends.toArray(new User[0]);
  }
  
  protected User[] getFollowers(Twitter twitter, long uid)
  {
    long cursor = -1;
    PagableResponseList<User> friends = null;
    try
    {
      do
      {
        friends = twitter.getFollowersList(uid, cursor);
      }
      while ((cursor = friends.getNextCursor()) != 0);
    }
    catch (TwitterException e)
    {
      throw new TweeterException(e);
    }
    
    return friends.toArray(new User[0]);
  }
  
  protected List<Status> fetchTimeLine(Object user) throws TwitterException
  {
    return fetchTimeLine(user, DEFAULT_MAX);
  }
  
  protected List<Status> fetchTimeLine(Object user, int maxNum) throws TwitterException
  {
    int pageno = 1;
    int perPage = maxNum > 0 && maxNum < DEFAULT_MAX ? maxNum : DEFAULT_MAX;

    List<Status> statuses = new ArrayList<Status>();
    while (true)
    {
      int preCount = statuses.size();
      callFetch(user, statuses, new Paging(pageno++, perPage));
      
      int postCount = statuses.size();
      if ((maxNum > 0 && postCount >= maxNum) || postCount == preCount)
        break;
    }

    return statuses;
  }

  private void callFetch(Object user, List<Status> statuses, Paging page) throws TwitterException
  {
    if (user instanceof User)
      statuses.addAll(twitter4j.getUserTimeline(((User) user).getId(), page));
    else if (user instanceof String)
      statuses.addAll(twitter4j.getUserTimeline((String) user, page));
    else if (user instanceof Long)
      statuses.addAll(twitter4j.getUserTimeline((long) user, page));
    else if (user instanceof Integer)
      statuses.addAll(twitter4j.getUserTimeline((int) user, page));
    else
      throw new RuntimeException("Unexpected user-type: " + user);
  }


  protected List<Status> toStatusList(List<Status> tl, int max)
  {
    int i = 0;
    this.results = new ArrayList<Status>();
    for (Iterator it = tl.iterator(); it.hasNext();)
    {
      this.results.add((Status) it.next());
      if (++i == max) break;
    }
    return results;
  }

/*  protected List<Status> fetchTimeLine(int max) throws TwitterException
  {
    return getAllTweets(twitter4j, id(), max);
  }

  protected List<Status> fetchTimeLine(Object user, int max) throws TwitterException
  {
    return getAllTweets(twitter4j, user, max); 
        //twitter4j.getUserTimeline(userName);
  }*/
  
  protected static String getFullMessage(String msg, Throwable e)
  {
    String s = "";
    if (e instanceof TwitterException)
    {
      TwitterException tw = (TwitterException) e;
      s += tw.getErrorMessage()+" (code=" + tw.getErrorCode() + ") \n";
    }
    s += e.getMessage();
    return msg != null ? msg + "\n" + s : s;
  }
  
  class TweeterException extends RuntimeException
  {
    public TweeterException(String message, Throwable cause)
    {
      super(getFullMessage(message, cause), cause);
    }

    public TweeterException(String message)
    {
      super(message);
    }

    public TweeterException(Throwable cause)
    {
      super(getFullMessage("", cause));
    }
  }

}
