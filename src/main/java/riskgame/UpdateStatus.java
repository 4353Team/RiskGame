package riskgame;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class UpdateStatus {

    static String consumerKeyStr = "xxxxxxxxxxxxxxxxxxxxxxxx";
    static String consumerSecretStr = "xxxxxxxxxxxxxxxxxxxxxxxx";
    static String accessTokenStr = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    static String accessTokenSecretStr = "xxxxxxxxxxxxxxxxxxxxxxxx";

    public static void main(String[] args) {

        try
        {

            Twitter twitter = new TwitterFactory().getInstance();

            twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
            AccessToken accessToken = new AccessToken(accessTokenStr,
                    accessTokenSecretStr);

            twitter.setOAuthAccessToken(accessToken);

            twitter.updateStatus("This tweet was sent using Twitter4J!");

            System.out.println("Successfully updated the status in Twitter.");

        }
        catch (TwitterException te) {
            te.printStackTrace();
        }
    }

}