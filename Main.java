package Marty.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        WikiDB wikiDB = new WikiDB();


            wikiDB.connectDB();
            wikiDB.createDB();
            wikiDB.fillDB();

        wikiDB.insertCharacterFromFile();
        wikiDB.insertImagesFromFile();


        //TODO TRY TO MAKE TINY URL

//        wikiDB.insertImage(characterID, "Marty", "http://cosplayidol.otakuhouse.com/wp-content/uploads/2012/06/s-1-1.jpg");

        try{
            CharacterPage characterPage = new CharacterPage(wikiDB);

        } catch (IOException io){
            io.printStackTrace();
        }
    }
}
