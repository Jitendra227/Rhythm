package com.jitendra.rhythm.model;

import java.io.Serializable;

public class AudioItems implements Serializable {
    String audioTitle;
    String audioArtist;
    String duration;
    String audioUri;

    public AudioItems(String audioTitle, String audioArtist, String duration, String audioUri) {
        this.audioTitle = audioTitle;
        this.audioArtist = audioArtist;
        this.duration = duration;
        this.audioUri = audioUri;
    }

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

    public String getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(String audioUri) {
        this.audioUri = audioUri;
    }
}
