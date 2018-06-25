package com.example.gagan.maymay_test1;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by GAGAN on 24-05-2018.
 */

public class memepost extends memepostid{

    public String user, image_url, description;
    public Date timestamp;

    private memepost(){}

    public memepost(String user,String image_url,String description,Date timestamp){

        this.user = user;
        this.image_url = image_url;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }



}
