import tweeter.*;
import twitter4j.*;

// Note: requires ckey, csec, atok, asec
//   to be defined with your credentials

String query = "art";

int tweetIdx = 0;
Tweeter tweeter;
Status[] tweets;

void setup() 
{
  size(800, 600);
  tweeter = new Tweeter(ckey, csec, atok, asec);
  tweets = tweeter._search(query, 1000);
  System.out.println("Found "+tweets.length);
  //thread("refreshTweets");
}

void draw()
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

void refreshTweets() // every 10 seconds
{
  while (true)
  {
    tweets = tweeter._search(query);
    println("Updated Tweets");
    delay(10000);
  }
}

