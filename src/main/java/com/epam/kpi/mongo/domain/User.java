package com.epam.kpi.mongo.domain;

/**
 * @author: Oleg Tsal-Tsalko
 */
public class User {

    private final String name;
    private final Integer tweetsNumber;

    public User(String name, Integer tweetsNumber) {
        this.name = name;
        this.tweetsNumber = tweetsNumber;
    }

    @Override
    public String toString() {
        return "User {\n" +
                "\tname : \"" + name + "\",\n" +
                "\ttweetsNumber : " + tweetsNumber + "\n" +
                "}";
    }
}
