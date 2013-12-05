package com.epam.kpi.mongo;

import com.epam.kpi.mongo.domain.Tweet;
import com.epam.kpi.mongo.domain.User;

import java.util.List;

/**
 * @author: Oleg Tsal-Tsalko
 */
public interface TweetsDao {

    long getNumberOfTweets();

    Tweet getOneTweet();

    List<Tweet> getFirstNRandomTweets(int n);

    List<Tweet> getUserTweets(String userName);

    List<User> getTopNUsersByNumberOfTweets(int n);

}
