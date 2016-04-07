import tweeter.*;

// Note: requires ckey, csec, atok, asec
//   to be defined with your credentials

String text = "Jo sun see mun";
Tweeter tweeter;

public void setup() 
{
  size(400, 200);
  textAlign(CENTER);

  tweeter = new Tweeter(ckey, csec, atok, asec);
  tweeter.post(text);
}

public void draw()
{
  background(40);
  fill(200);
  text(text, 200, 100);
}
