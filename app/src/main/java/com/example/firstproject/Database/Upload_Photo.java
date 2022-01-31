package com.example.firstproject.Database;

public class Upload_Photo {
    private String mImageUrl;

    public Upload_Photo(){

    }

    public Upload_Photo(String imageUrl){
        mImageUrl = imageUrl;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
