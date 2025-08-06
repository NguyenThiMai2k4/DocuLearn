package com.doculearn.pojo;

import java.io.Serializable;

public class Section implements Serializable {
    private String heading;
    private String content;

    // Constructors
    public Section() {
    }

    public Section(String heading, String content) {
        this.heading = heading;
        this.content = content;
    }

    // Getters and Setters
    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Optional: toString() for debugging
    @Override
    public String toString() {
        return "Section{" +
                "heading='" + heading + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

