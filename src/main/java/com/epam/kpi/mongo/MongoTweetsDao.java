package com.epam.kpi.mongo;

import com.epam.kpi.mongo.domain.Tweet;
import com.epam.kpi.mongo.domain.User;
import com.mongodb.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: Oleg Tsal-Tsalko
 */
public class MongoTweetsDao implements TweetsDao {

    public static final DBObject ANY = new BasicDBObject();

    private final DB twitter;
    private final DBCollection tweets;

    public MongoTweetsDao() throws UnknownHostException {
        twitter = new Mongo("localhost", 27017).getDB("mongo_lecture");
        tweets = twitter.getCollection("twitter");
    }

    @Override
    public long getNumberOfTweets() {
        return tweets.count();
    }

    @Override
    public Tweet getOneTweet() {
        return toTweetObject(tweets.findOne(ANY, tweetMainFields()));
    }

    @Override
    public List<Tweet> getFirstNRandomTweets(int n) {
        List<Tweet> selectedTweets = new ArrayList<>(n);
        DBCursor cursor = tweets.find(ANY, tweetMainFields()).limit(n);
        for (DBObject tweet : cursor) {
            selectedTweets.add(toTweetObject(tweet));
        }
        return selectedTweets;
    }

    private Tweet toTweetObject(DBObject tweet) {
        return new Tweet(Long.valueOf(tweet.get("id").toString()),
                tweet.get("text").toString(),
                ((DBObject) tweet.get("user")).get("name").toString());
    }

    private DBObject tweetMainFields() {
        DBObject fieldsToFetch = new BasicDBObject();
        fieldsToFetch.put("id", 1);
        fieldsToFetch.put("text", "");
        fieldsToFetch.put("user.name", "");
        return fieldsToFetch;
    }

    @Override
    public List<Tweet> getUserTweets(String userName) {
        List<Tweet> userTweets = new LinkedList<>();
        DBCursor cursor = tweets.find(new BasicDBObject("user.name", userName), tweetMainFields());
        for (DBObject tweet : cursor) {
            userTweets.add(toTweetObject(tweet));
        }
        return userTweets;
    }

    @Override
    public List<User> getTopNUsersByNumberOfTweets(int n) {
        runMapReduceJobToAggregateUsersStatistic();

        List<User> topUsers = new ArrayList<>(n);
        DBCursor cursor = twitter.getCollection("map_reduce_example").find().sort(desc()).limit(n);
        for (DBObject user : cursor) {
            topUsers.add(toUserObject(user));
        }
        return topUsers;
    }

    private User toUserObject(DBObject user) {
        final String userName = (user.get("_id") == null) ? "Anonymous" : user.get("_id").toString();
        return new User(userName, ((Double) user.get("value")).intValue());
    }

    private DBObject desc() {
        return new BasicDBObject("value", -1);
    }

    private void runMapReduceJobToAggregateUsersStatistic() {
        try {
            final String mapFunction = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("mapreduce/map.js"));
            final String reduceFunction = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("mapreduce/reduce.js"));
            {
                long startTime = System.nanoTime();
                tweets.mapReduce(mapFunction, reduceFunction, "map_reduce_example", null);
                System.out.println("MapReduce job run took : "+(System.nanoTime()-startTime)/1e9+"sec");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
