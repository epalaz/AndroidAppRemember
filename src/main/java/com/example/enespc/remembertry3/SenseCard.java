package com.example.enespc.remembertry3;

import java.io.Serializable;

/**
 * Created by Enespc on 01.05.2015.
 */
public class SenseCard implements Serializable {

    String twitterName, email, phoneNumber, name;
    long twitterId;


    public SenseCard(String twitterName, String email, long twitterId, String phoneNumber,String name){

            this.name = name;
            this.email = email;
            this.twitterName = twitterName;
            this.twitterId = twitterId;
            this.phoneNumber= phoneNumber;
    }

    public void setName(String name){

        this.name = name;
    }

    public void setTwitterName(String name){

            this.twitterName = name;
    }

    public void setEmail (String email){

        this.email = email;
    }

    public void setTwitterId(long id){

        this.twitterId = id;
    }

    public void setPhoneNumber(String number){

        this.phoneNumber = number;
    }

    public long getTwitterId(){

        return twitterId;
    }

    public String getTwitterName(){

        return twitterName;
    }

    public String getEmail(){

        return email;
    }

    public String getPhoneNumber(){

        return phoneNumber;
    }

    public String getName(){

      return name;
    }
}
