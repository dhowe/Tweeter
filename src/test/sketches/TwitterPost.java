package test.sketches;

import java.util.Date;

import processing.core.PApplet;
import test.TwitterConfig;
import tweeter.Tweeter;

public class TwitterPost extends TwitterConfig
{
  String text = "This is a test tweet @" + new Date();
  Tweeter tweeter;

  public void settings()
  {
    size(400,200);
  }
  
  public void setup() 
  {
    textAlign(CENTER);
    
    tweeter = new Tweeter(ckey, csec, atok, asec);
    tweeter.post(text);
  }

  public void draw()
  {
    fill(0, 40);
    rect(0, 0, width, height);


    fill(200);
    text(text, 200, 100);
  }
  
  public static void main(String[] args) {
  	
		PApplet.main(new String[]{ TwitterPost.class.getName() });
	}
  
  
}