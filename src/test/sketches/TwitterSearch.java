package test.sketches;

import processing.core.PApplet;
import test.TwitterConfig;
import tweeter.Tweeter;
import twitter4j.Status;

public class TwitterSearch extends TwitterConfig
{
  String query = "art";
  
  int tweetIdx = 0;
  Tweeter tweeter;
  Status[] tweets;
  
  public void settings()
  {
    size(800, 600);
  }
  
  public void setup() 
  {
    tweeter = new Tweeter(ckey, csec, atok, asec);
    tweets = tweeter._search(query,1000);
    System.out.println("Found "+tweets.length);
    //thread("refreshTweets");
  }

  public void draw()
  {
    fill(0, 40);
    rect(0, 0, width, height);

    if (++tweetIdx >= tweets.length)
      tweetIdx = 0;

    Status status = tweets[tweetIdx];

    fill(200);
    text(status.getText(), random(width), random(height), 300, 200);

    delay(250);
  }
  
  public void refreshTweets() // every 10 seconds
  {
    while (true)
    {
      tweets = tweeter._search(query);

      println("Updated Tweets");

      delay(10000);
    }
  }
  
  public static void main(String[] args) {
  	
		PApplet.main(new String[]{ TwitterSearch.class.getName() });
	}
  
}