package com.example.patrick.complexgui.info;

/**
 * Created by Patrick on 2017-12-24.
 */

public class MusicFile {
    //Instance variables containing song info
    private int image;
    private int song;
    private String artist;
    private String title;
    private String album;

    //Assign value to instance variable upon creation of an object
    public MusicFile(int image, int song, String artist, String title){
        this.image = image;
        this.artist = artist;
        this.title = title;
        this.song = song;
    }

    //Returns the artist
    public String getArtist() {
        return artist;
    }

    //Returns the image path
    public int getImage() {
        return image;
    }

    //Returns the title
    public String getTitle() {
        return title;
    }

    public int getSong() {
        return song;
    }

    //Returns Artist and title if toString on
    // MusicFile object is called
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(artist);
        sb.append(" - ");
        sb.append(title);
        return sb.toString();
    }
}