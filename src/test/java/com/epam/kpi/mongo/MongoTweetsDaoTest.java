package com.epam.kpi.mongo;

import com.epam.kpi.mongo.domain.Tweet;
import com.epam.kpi.mongo.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author: Oleg Tsal-Tsalko
 */
public class MongoTweetsDaoTest {

    private TweetsDao tweetsDao;

    @Before
    public void setUp() throws Exception {
        tweetsDao = new MongoTweetsDao();
    }

    @Test
    public void testGetNumberOfTweets() throws Exception {
        System.out.println("Total number of tweets: " + tweetsDao.getNumberOfTweets());
    }

    @Test
    public void testGetOneTweet() throws Exception {
        System.out.println("Random 1 tweet:");
        System.out.println(tweetsDao.getOneTweet());
    }

    @Test
    public void testGetFirstNRandomTweets() throws Exception {
        List<Tweet> tweets = tweetsDao.getFirstNRandomTweets(5);
        System.out.println("Random 5 tweets:");
        tweets.stream().forEach(System.out::println);
    }

    @Test
    public void testGetUserTweets() throws Exception {
        List<Tweet> tweets = tweetsDao.getUserTweets("Suara hatiku.");
        System.out.println("Suara hatiku's tweets:");
        tweets.stream().forEach(System.out::println);
    }

    @Test
    //Long running one because doesn't use any caching and uses MapReduce job under the hood
    public void testGetTopNUsersByNumberOfTweets() throws Exception {
        List<User> tweets = tweetsDao.getTopNUsersByNumberOfTweets(5);
        System.out.println("Top 5 user by number of tweets:");
        tweets.stream().forEach(System.out::println);
    }
}
