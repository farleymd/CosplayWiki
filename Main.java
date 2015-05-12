package Marty.company;

import java.io.IOException;

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
