package com.epam.kpi.mongo.domain;

/**
 * @author: Oleg Tsal-Tsalko
 */
public class Tweet {

    public final Long id;
    public final String text;
    public final String user;

    public Tweet(Long id, String text, String user) {
        this.id = id;
        this.text = text;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Tweet {\n" +
                "\tid : " + id + ",\n" +
                "\ttext : \"" + text + "\",\n" +
                "\tuser : \"" + user + "\"\n" +
                "}";
    }

}
