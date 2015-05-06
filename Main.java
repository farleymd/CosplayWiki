package Marty.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        WikiDB wikiDB = new WikiDB();



        boolean databaseExist = wikiDB.isDbCreated();

        if (databaseExist == false){
            wikiDB.createDB();
            wikiDB.fillDB();
        }

        wikiDB.insertCharacter("Mary Sue", "Female", "Fanfiction", "Star Trek", "Next Generation", "This will test " +
                "a genre that doesn't exist.");

        wikiDB.insertCharacter("Jedi", "Female", "Movies", "Star Wars", "Star Wars", "This will test " +
                "a universe that doesn't exist.");

        wikiDB.insertCharacter("Sailor Moon", "Female", "Anime", "Sailor Moon", "Sailor Moon SuperS", "This is a test.");
        //wikiDB.searchCharacter("Sailor Moon");

        int characterID = wikiDB.getCharacterID("Sailor Moon");


        //TODO TRUNCATE URL; MAKE TINYURL
        wikiDB.insertImage(characterID, "Marty", "http://cosplayidol.otakuhouse.com/wp-content/uploads/2012/06/s-1-1.jpg");


        try{
            CharacterPage characterPage = new CharacterPage(wikiDB);

        } catch (IOException io){
            io.printStackTrace();
        }



        //wikiDB.deleteDB();




	// write your code here
    }
}
