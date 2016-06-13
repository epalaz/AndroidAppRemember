package com.example.enespc.remembertry3;

import android.location.Location;

/**
 * Created by Enespc on 05.05.2015.
 */
public class BoxSenseCard extends SenseCard {

    Location location;
    String saveNotes;

    public BoxSenseCard(String twitterName, String email, long twitterId, String phoneNumber,String name, Location location, String saveNotes){
        super(twitterName, email, twitterId, phoneNumber, name);

        this.saveNotes = saveNotes;
        this.location = location;

    }

    public Location getLocation(){

        return location;
    }


    public String getSaveNotes(){

        return saveNotes;
    }

    public void setLocation(Location newLoc){

        this.location = newLoc;
    }

    public void setSaveNotes(String newNote){

        this.saveNotes = newNote;
    }
}
