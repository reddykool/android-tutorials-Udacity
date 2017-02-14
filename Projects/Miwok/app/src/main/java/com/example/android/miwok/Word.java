package com.example.android.miwok;

/**
 * Created by Reddyz on 15-01-2017.
 */
/**
 * {@link Word} represents a single Android platform release.
 * Each object has 2 properties: default translation and Miwok translation
 */
public class Word {
    //miwok translation word
    private String mMiwokTranslation;

    //default translation word
    private String mDefaultTranslation;

    //Image Resource Id
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    //Audio Resource id
    private int mAudioResourceId;

    /** Constant value that represents no image was provided for this word */
    private static final int NO_IMAGE_PROVIDED = -1;

    /*
    * Create a new Word object.
    *
    * @param audioResourceId is the corresponding Miwok pronounciation audio resource file
    * @param defaultTranslation is the default word(e.g. one/two/three...)
    * @param miwokTranslation is the corresponding Miwok translated word (e.g.  lutti/otiiko/tolookosu...)
    * */
    public Word(int audioResourceId, String defaultTranslation, String miwokTranslation){
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }

    /*
    * Create a new Word object.
    *
    * @param audioResourceId is the corresponding Miwok pronounciation audio resource file
    * @param defaultTranslation is the default word(e.g. one/two/three...)
    * @param miwokTranslation is the corresponding Miwok translated word (e.g.  lutti/otiiko/tolookosu...)
    * @param imageResourceId is the corresponding image(resource Id) to display in view later
    * */
    public Word(int audioResourceId, String defaultTranslation, String miwokTranslation, int imageResourceId){
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }


    /**
     * Get the default word
     */
    public String getDefaultTranslation(){
        return mDefaultTranslation;
    }

    /**
     * Get the miwok word
     */
    public String getMiwokTranslation(){
        return mMiwokTranslation;
    }

    /**
     * Get the image resource id
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

    /**
     * Returns whether word has image or not?
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    /**
     * Get the audio resource id
     */
    public int getAudioResourceId() {
        return mAudioResourceId;
    }

    @Override
    public String toString() {
        return "Word{" +
                "mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                ", mAudioResourceId=" + mAudioResourceId +
                '}';
    }
}
