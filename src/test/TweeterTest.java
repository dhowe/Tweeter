package test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import tweeter.Tweeter;
import twitter4j.*;

public class TweeterTest extends TwitterConfig
{
  static final boolean SILENT = true;
      
  Tweeter tweeter;

  @Before
  public void beforeEvery()
  {
    tweeter = new Tweeter(ckey, csec, atok, asec);
  }

  @Test
  public void testTweeter()
  {
    assertTrue(tweeter != null);
  }
    
  @Test
  public void testId()
  {
    long id = tweeter.id();
    cout(id);
    assertEquals(2855526444l, id);
  }

  @Test
  public void testPost()
  {
    String msg = "Test: "+new Date();
    tweeter.post(msg);  
    String status = tweeter._tweets()[0].getText();
    assertEquals(msg, status);
  }
  
  @Test
  public void testTweetsUserName()
  {
    Status[] tweets = tweeter._tweets("cnn");
    cout(tweets.length);
    assertTrue(tweets.length > 0);
    assertTrue(tweets.length <= 100);
    assertTrue(tweets.length == tweeter.results.size());
  }
  
  @Test
  public void testTweetsUserNameInt()
  {
    Status[] tweets = tweeter._tweets("beyonce", 5);
    
    for (int i = 0; i < tweets.length; i++) {
      cout(i+") "+tweets[i].getId());
      cout("   "+tweets[i].getLang());
      cout("   "+tweets[i].getText());
      Place place = tweets[i].getPlace();
      if (place!= null)
        cout("   "+place.getFullName());
      cout("   "+tweets[i].getUser().getName());
      String location = tweets[i].getUser().getLocation();
      if (location!= null && location.length()>0)
        cout("   "+location);
      cout("   "+tweets[i].getUser().getProfileImageURL());
      cout("   "+tweets[i].getCreatedAt());
      URLEntity[] urls = tweets[i].getURLEntities();
      for (int j = 0; j < urls.length; j++)
      {
        cout("   url["+j+"] = "+urls[j]);
      }
      HashtagEntity[] tags = tweets[i].getHashtagEntities();
      cout();
      for (int j = 0; j < tags.length; j++)
      {
        cout("   tag["+j+"] = "+tags[j].getText());
      }
      cout("=====================================");
    }
    assertTrue(tweets.length == 5);
    assertTrue(tweets.length == tweeter.results.size());  
  }

  @Test
  public void testTweets()
  {
    Status[] search = tweeter._tweets();
    cout(search.length);
    assertTrue(search.length > 20);
    assertTrue(search.length == tweeter.results.size());  
  } 
  
  @Test
  public void testTweetsString()
  {
    Status[] search = tweeter._tweets("cnn");
    assertTrue(search.length > 20);
    assertTrue(search.length == tweeter.results.size());  
  } 
  
  @Test
  public void testTweetsInt()
  {
    Status[] search = tweeter._tweets(5);
    assertTrue(search.length == 5);
    assertTrue(search.length == tweeter.results.size());  
  } 
  
  @Test
  public void testTweetsStringInt()
  {
    Status[] search = tweeter._tweets("cnn", 5);
    assertTrue(search.length == 5);
    assertTrue(search.length == tweeter.results.size());  
    
    search = tweeter._tweets("cnn", 205);
    cout(search.length);
    assertTrue(search.length == 205);
    assertTrue(search.length == tweeter.results.size()); 
    
    /*search = tweeter._tweets("cnn", -1); // very slow
    cout(search.length);
    assertTrue(search.length >= 3200);
    assertTrue(search.length == tweeter.results.size()); */ 
  } 
  
  @Test
  public void testSearch()
  {
    Status[] search = tweeter._search("Black Dog");
    cout(search.length);
    assertTrue(search.length == 100);
    assertTrue(search.length == tweeter.results.size());  
  }
  
  @Test
  public void testSearchInt()
  {
    Status[] search = tweeter._search("Black Dog", 5);
    cout(search.length);
    assertTrue(search.length == 5);
    assertTrue(tweeter.resultCount() == search.length);
    assertTrue(search.length == tweeter.results.size());  

    search = tweeter._search("Beyonce", 205);
    cout(search.length);
    assertTrue(search.length == 205);
    assertTrue(tweeter.resultCount() == search.length);
    assertTrue(search.length == tweeter.results.size()); 
  }
  
  @Test
  public void testTwitter4j()
  {
    Twitter twitter4j = tweeter.twitter4j;
    try
    {
      String pp = twitter4j.getPrivacyPolicy();
      //System.out.println(pp);
      assertNotNull(pp);
    }
    catch (TwitterException e)
    {
      e.printStackTrace();
    }
  }
  
  private void cout() {
    cout("");
  }
  private void cout(int i) {
    cout(""+i);
  }  
  private void cout(long i) {
    cout(""+i);
  }
  private void cout(String s) {
    if (!SILENT) System.out.println(s);
  }
  
  public static void main(String[] args)
  {
    TweeterTest tt = new TweeterTest();
    tt.beforeEvery();
    tt.testTweetsString();
  }
}
