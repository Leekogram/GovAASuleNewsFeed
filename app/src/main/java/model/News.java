package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HP-USER on 15-Aug-19.
 */

public class News {

    //@SerializedName(" created_time")
   // @Expose

    private String created_time;
   // @SerializedName("message")
   // @Expose
    private String message;
  //  @SerializedName("id")
  //  @Expose
    private String id;
  //  @SerializedName("full_picture")
   // @Expose
    private String full_picture;
  //  @SerializedName(" picture")
  //  @Expose
    private String picture;



    public News(){

    }

    public News(String created_time, String message, String id, String full_picture, String picture) {
        this.created_time = created_time;
        this.message = message;
        this.id = id;
        this.full_picture = full_picture;
        this.picture = picture;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_picture() {
        return full_picture;
    }

    public void setFull_picture(String full_picture) {
        this.full_picture = full_picture;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
