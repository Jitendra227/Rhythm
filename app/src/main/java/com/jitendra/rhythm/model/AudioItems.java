package com.jitendra.rhythm.model;

import android.net.Uri;

import java.io.Serializable;

public class AudioItems implements Serializable {
    String audioTitle;
    String audioArtist;
    String duration;
    Uri audioUri;


    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getAudioArtist() {
        return audioArtist;
    }

    public void setAudioArtist(String audioArtist) {
        this.audioArtist = audioArtist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Uri getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }
}
