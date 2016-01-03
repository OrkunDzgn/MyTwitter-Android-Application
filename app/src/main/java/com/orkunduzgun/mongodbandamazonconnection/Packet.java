package com.orkunduzgun.mongodbandamazonconnection;


import java.util.List;

public class Packet
{
    public Packet(){
        String Function;
        UserCredential UserCreds;
        User User;
        Tweets Tweets;
        Error Error;
        Following Following;
        Followers Followers;
        Tweet Tweet;
    }

    public class Tweets
    {
        public List<Tweet> tweets;
    }

    public class Tweet
    {
        public String _id;
        public int userid;
        public String tweet;
        public double dateTimePosted;
    }

    public class Error
    {
        public String error;
    }

    public class Following
    {
        public int _id;
        public List<Integer> following;
    }

    public class Followers
    {
        public int _id;
        public List<Integer> followers;
    }

    public class UserCredential
    {
        public int _id;
        public String username;
        public String password;

        void username(String name){
            username = name;
        }
    }

    public class User
    {
        public int _id;
        public String username;
        public String description;
        public double dateJoined;
        public String profilePicture;
    }


}

