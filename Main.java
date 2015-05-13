package Marty.company;

import java.io.IOException;

/**
 * Created by marty.farley on 5/2/2015.
 *
 * This class begins the program. Database is connected, created, and filled with
 * general data information. Main then created the CharacterPage, which is the
 * primary GUI for the program.
 */

public class Main {

    public static void main(String[] args) {

        WikiDB wikiDB = new WikiDB();


            wikiDB.connectDB();
            wikiDB.createDB();
            wikiDB.fillDB();

        wikiDB.insertCharacterFromFile();
        wikiDB.insertImagesFromFile();

        try{
            CharacterPage characterPage = new CharacterPage(wikiDB);

        } catch (IOException io){
            io.printStackTrace();
        }
    }
}
