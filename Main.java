package Marty.company;

import Marty.company.CharacterPage;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        WikiDB wikiDB = new WikiDB();



        boolean databaseExist = wikiDB.isDbCreated();

        if (databaseExist == false){
            wikiDB.connectDB();
            wikiDB.createDB();
            wikiDB.fillDB();
        }

        wikiDB.insertCharacter("Sailor Moon", "Female", "Anime", "Sailor Moon", "Sailor Moon SuperS", "This is a test.");
        //wikiDB.searchCharacter("Sailor Moon");

        wikiDB.insertCharacter("Sailor Mars", "Female", "Anime", "Sailor Moon", "Sailor Moon SuperS", "Champion of Mars");

        wikiDB.insertCharacter("Sailor Star Fighter", "Female", "Anime", "Sailor Moon", "Sailor Moon Stars", "Leader of Sailor Starlights");

        wikiDB.insertCharacter("Goku", "Male", "Anime", "Dragonball Z", "Dragon Ball Z", "Protector of Earth");

        wikiDB.insertCharacter("Spike Spiegel", "Male", "Anime", "Cowboy Bebop", "Cowboy Bebop", "He is a bounty hunter on a spaceship " +
                "called the Bebop and travels space with his crew Jet, Faye, Ed & Ein.");

        wikiDB.insertCharacter("Yoko Littner", "Female", "Anime", "Gurren Lagann", "Tengen Toppa Gurren Lagann", "A member of a small resistance against the beastmen");

        wikiDB.insertCharacter("Queen Elsa", "Female", "Movies", "Disney", "Frozen", "Has magical ice powers.");

        wikiDB.insertCharacter("10th Doctor", "Male", "Television", "Doctor Who", "Doctor Who", "Allons-y!");

        wikiDB.insertCharacter("Iron Man", "Male", "Movies", "Marvel", "The Avengers", "Billionare genius Tony Stark");

        wikiDB.insertCharacter("Cloud Strike", "Male", "Video Games", "Final Fantasy", "Final Fantasy 7", "Ex-Soldier, Big Sword");

        int characterID = wikiDB.getCharacterID("Sailor Moon");


        //TODO TRUNCATE URL; MAKE TINYURL
        wikiDB.insertImage(characterID, "Marty", "http://cosplayidol.otakuhouse.com/wp-content/uploads/2012/06/s-1-1.jpg");
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
