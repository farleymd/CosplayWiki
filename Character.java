package Marty.company;

import java.util.StringTokenizer;

/**
 * Created by marty.farley on 5/3/2015
 *
 * This class is used primarily as a reference tool and ArrayList wrapper. Class has 2 constructor methods;
 * second constructor method is blanked, and used sparringly.
 */
public class Character {
    private int characterID;
    private String characterName;
    private String gender;
    private int genreID;
    private int universeID;
    private int mediaID;
    private String description;

    public Character(int characterID, String characterName, String gender, int genreID, int universeID,
                     int mediaID, String description){
        this.characterID = characterID;
        this.characterName = characterName;
        this.gender = gender;
        this.genreID = genreID;
        this.universeID = universeID;
        this.mediaID = mediaID;
        this.description = description;
    }

    public Character(){

    }


    public int getCharacterID(){
        return characterID;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getGender(){
        return gender;
    }

    public int getGenreID(){
        return genreID;
    }


    public int getUniverseID(){
        return universeID;
    }

    public int getMediaID(){
        return mediaID;
    }


    public String getDescription(){
        return description;
    }

    public String toString(){
        return(this.characterName + "\n");
    }

    public String toString(int characterInts){
        return(this.characterName + " = " + "\n" +
                this.gender + " = " + "\n" +
                this.genreID + " = " + "\n" +
                this.universeID + " = " + "\n" +
                this.mediaID + " = " + "\n" +
                this.description);
    }

}
