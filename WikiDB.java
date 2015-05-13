package Marty.company;

import java.io.*;
import java.lang.*;
import java.sql.*;
import java.util.ArrayList;


/**
 * Created by marty.farley on 5/2/2015.
 *
 * This class is dedicated to the database. Class methods include creating the database,
 * filling the database with some general information, and any searching or updating
 * done to the data by the GUI forms.
 */
public class WikiDB {
    private static String protocol="jdbc:derby:";
    private static String dbName = "wikiDB";

    private static final String USER = "username";
    private static final String PASS = "password";

    Statement statement = null;
    Connection conn = null;
    ResultSet resultSet = null;

    PreparedStatement psInsert = null;


    public void connectDB(){
        try {
            conn = DriverManager.getConnection(protocol + dbName +";create=true",USER,PASS);
            statement = conn.createStatement();

        } catch (SQLException sql){

        }

    }

    public void createDB() {

        try {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet resultSet = meta.getTables(null,null,"CosplayCharacter", null);

            if (resultSet.next()) {
                System.out.println("Tables exist.");
            } else {
                String createCharTableSQL = "CREATE TABLE CosplayCharacter (" +
                        "CharacterID int NOT NULL primary key GENERATED ALWAYS " +
                        "AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                        "name varchar(60) not null," +
                        "uName GENERATED ALWAYS AS (UPPER(name))," +
                        "gender varchar(10)," +
                        "genreID int, " +
                        "universeID int, " +
                        "mediaID int, " +
                        "description varchar(500))";

                String createGenreTableSQL = "CREATE TABLE Genre (GenreID INTEGER NOT NULL GENERATED ALWAYS " +
                        "AS IDENTITY (START WITH 1, INCREMENT BY 1), genreName varchar(60), uGenreName GENERATED ALWAYS AS (UPPER(genreName)))";

                String createUniverseTableSQL = "CREATE TABLE Universe (UniverseID int NOT NULL GENERATED ALWAYS " +
                        "AS IDENTITY (START WITH 1, INCREMENT BY 1), universeName varchar(60), uUniverseName GENERATED ALWAYS " +
                        "AS (UPPER(universeName)))";

                String createMediaTitleTableSQL = "CREATE TABLE Media (MediaID int NOT NULL GENERATED ALWAYS " +
                        "AS IDENTITY (START WITH 1, INCREMENT BY 1), mediaTitle varchar(100)," +
                        "uMediaTitle GENERATED ALWAYS AS (UPPER(mediaTitle))," +
                        "genreID int," +
                        "universeID int," +
                        "createdBy varchar(60)," +
                        "yearReleased int," +
                        "description varchar(60))";

                String createImageTableSQL = "CREATE TABLE Images (ImageID int NOT NULL GENERATED ALWAYS " +
                        "AS IDENTITY (START WITH 1, INCREMENT BY 1), author varchar(60) not null," +
                        "dateUploaded date, location varchar(60), characterID int, url varchar(500)," +
                        "tutorialID int)";

                String createTutorialTableSQL = "CREATE TABLE Tutorials (TutorialID int NOT NULL GENERATED ALWAYS " +
                        "AS IDENTITY (START WITH 1, INCREMENT BY 1), author varchar(60) not null," +
                        "dateCreated date, type varchar(60), characterID int, url varchar(60) not null," +
                        "imageID int)";

                statement.executeUpdate(createCharTableSQL);

                //Character table print out confirmation
                System.out.println("Character table created");

                statement.executeUpdate(createGenreTableSQL);

                //Genre table print out confirmation
                System.out.println("Genre table created");

                statement.executeUpdate(createUniverseTableSQL);

                //Universe table print out confirmation
                System.out.println("Universe table created");

                statement.executeUpdate(createMediaTitleTableSQL);

                //Media title table print out confirmation
                System.out.println("Media table created");

                statement.executeUpdate(createImageTableSQL);

                //Image table print out confirmation
                System.out.println("Image table created");

                statement.executeUpdate(createTutorialTableSQL);

                //Tutorial table print out confirmation
                System.out.println("Tutorial table created");

                resultSet.close();

            }

        } catch (SQLException se) {
            se.printStackTrace();

        }


    }

    public void fillDB(){
        ResultSet resultSet = null;

        String prepGenreInsert = "INSERT INTO Genre(genreName) VALUES (?)";
        String prepUniverseInsert = "INSERT INTO Universe(universeName) VALUES (?)";
        String prepMediaInsert = "INSERT INTO Media(mediaTitle) VALUES (?)";

        try {
            psInsert = conn.prepareStatement(prepGenreInsert);

            //Set values in the genre table
            psInsert.setString(1,"Television");
            psInsert.execute();
            psInsert.setString(1,"Movies");
            psInsert.execute();
            psInsert.setString(1,"Video Games");
            psInsert.execute();
            psInsert.setString(1,"Anime");
            psInsert.execute();
            psInsert.setString(1,"Comic Books");
            psInsert.execute();
            psInsert.setString(1,"Literature");
            psInsert.execute();
            psInsert.setString(1,"Historic");
            psInsert.execute();
            psInsert.setString(1,"Other");
            psInsert.execute();

            //Set values in the Universe table
            psInsert = conn.prepareStatement(prepUniverseInsert);

            psInsert.setString(1,"Hunger Games");
            psInsert.execute();
            psInsert.setString(1,"Marvel");
            psInsert.execute();
            psInsert.setString(1,"Sailor Moon");
            psInsert.execute();
            psInsert.setString(1,"Game of Thrones");
            psInsert.execute();
            psInsert.setString(1,"Disney");
            psInsert.execute();
            psInsert.setString(1,"Other");
            psInsert.execute();

            psInsert = conn.prepareStatement(prepMediaInsert);

            //Set values in the Media table
            psInsert.setString(1,"Frozen");
            psInsert.execute();
            psInsert.setString(1,"The Avengers");
            psInsert.execute();
            psInsert.setString(1,"Buffy the Vampire Slayer");
            psInsert.execute();
            psInsert.setString(1,"Unknown");
            psInsert.execute();


            String fetchAllDataSQL = "SELECT * from Media";

            resultSet = statement.executeQuery(fetchAllDataSQL);
            while (resultSet.next()) {
                int mediaID = resultSet.getInt("mediaID");
                String mediaTitle = resultSet.getString("mediaTitle");

                System.out.println("MediaID:" + mediaID + " Name: " + mediaTitle);

            }

            resultSet.close();


        } catch (SQLException se){
            se.printStackTrace();
        }

    }

    public void insertCharacter(String characterName, String gender,  String genre,
                                String universe, String mediaTitle, String description){
        ResultSet resultSet = null;
        int genreID = getGenreID(genre);
        int universeID = getUniverseID(universe);
        int mediaID = getMediaID(mediaTitle);


        String prepCharacterInsert = "INSERT INTO CosplayCharacter(name, gender, genreID," +
                "universeID, mediaID, description) VALUES (?,?,?,?,?,?)";

        try {
            psInsert = conn.prepareStatement(prepCharacterInsert);

            psInsert.setString(1,characterName);

            psInsert.setString(2,gender);

            psInsert.setInt(3,genreID);

            psInsert.setInt(4,universeID);
            //TODO if universe specified by user doesn't exist, add it

            psInsert.setInt(5, mediaID);
            //TODO if mediaTitle specified by user doesn't exist, add it

            psInsert.setString(6,description);
            psInsert.executeUpdate();


        } catch (SQLException se){
            se.printStackTrace();
        }

    }

    public int getCharacterID(String characterName){
        ResultSet resultSet = null;
        int characterID = 0;

        try{
            String searchGenre = "select * from CosplayCharacter where name = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            recordSearch.setString(1,characterName);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the universe name based on the ID
                characterID = resultSet.getInt("characterID");
            } else{
                System.out.println("Character could not be found.");

            }

            resultSet.close();
        } catch (SQLException se){
            se.printStackTrace();
        }

        return characterID;
    }

    public ArrayList<Character> searchAllCharacters(){
        ResultSet resultSet = null;
        ArrayList<Character> allCharacters = new ArrayList<Character>();

        try {
            String fetchAllDataSQL = "SELECT * from CosplayCharacter";

            resultSet = statement.executeQuery(fetchAllDataSQL);
            while (resultSet.next()) {
                int charID = resultSet.getInt("characterID");
                String name = resultSet.getString("name");
                String genderChar = resultSet.getString("gender");
                int genreCharID = resultSet.getInt("genreID");
                String genreCharName = getGenreName(genreCharID);
                int universeCharID = resultSet.getInt("universeID");
                String universeCharName = getUniverseName(universeCharID);
                int mediaCharID = resultSet.getInt("mediaID");
                String mediaCharName = getMediaTile(mediaCharID);
                String charDesc = resultSet.getString("description");
                String uName = resultSet.getString("uName");

                System.out.println("Character Name : " + name +
                        "Upper Case Name : " + uName +
                        " GenreID : " + genreCharID +
                        " Genre Name : " + genreCharName +
                        " UniverseID : " + universeCharID +
                        " Universe Name : " + universeCharName +
                        " MediaID:" + mediaCharID +
                        " Media Title : " + mediaCharName);

                Character character = new Character(charID, name, genderChar, genreCharID,
                        universeCharID, mediaCharID, charDesc);
                allCharacters.add(character);

                try {
                    FileWriter openWriter = new FileWriter("addCharacters.txt");
                    final BufferedWriter openBufWriter = new BufferedWriter(openWriter);

                    addCharactersToFile(character, openBufWriter);

                } catch (IOException io){

                }

            }
            resultSet.close();

        } catch (SQLException sql){
            sql.printStackTrace();
        }

        return allCharacters;
    }

    public void editCharacterName(int characterID, String characterName){
        int saveStatus = 0;

        try{
            String updateName = "UPDATE CosplayCharacter set name = (?) where characterID = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(updateName);
            recordSearch.setString(1,characterName);
            recordSearch.setInt(2,characterID);
            saveStatus = recordSearch.executeUpdate();

        } catch (SQLException se){
            se.printStackTrace();
        }
    }

    public void editCharacterGender(int characterID, String gender){
        int saveStatus = 0;

        try{
            String updateName = "UPDATE CosplayCharacter set gender = (?) where characterID = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(updateName);
            recordSearch.setString(1,gender);
            recordSearch.setInt(2,characterID);
            saveStatus = recordSearch.executeUpdate();

        } catch (SQLException se){
            se.printStackTrace();
        }
    }

    public void editCharacterGenre(int characterID, String genre){
        int genreID = getGenreID(genre);
        int saveStatus = 0;

        try{
            String updateName = "UPDATE CosplayCharacter set genreID = (?) where characterID = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(updateName);
            recordSearch.setInt(1, genreID);
            recordSearch.setInt(2,characterID);
            saveStatus = recordSearch.executeUpdate();

        } catch (SQLException se){
            se.printStackTrace();
        }
    }

    public void editCharacterUniverse(int characterID, String universe){
        int universeID = getUniverseID(universe);
        int saveStatus = 0;

        try{
            String updateName = "UPDATE CosplayCharacter set universeID = (?) where characterID = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(updateName);
            recordSearch.setInt(1,universeID);
            recordSearch.setInt(2,characterID);
            saveStatus = recordSearch.executeUpdate();

        } catch (SQLException se){
            se.printStackTrace();
        }
    }

    public void editCharacterMedia(int characterID, String media){
        int mediaID = getMediaID(media);
        int saveStatus = 0;

        try{
            String updateName = "UPDATE CosplayCharacter set mediaID = (?) where characterID = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(updateName);
            recordSearch.setInt(1,mediaID);
            recordSearch.setInt(2,characterID);
            saveStatus = recordSearch.executeUpdate();

        } catch (SQLException se){
            se.printStackTrace();
        }

    }

    public void editCharacterDesc (int characterID, String desc){
        int saveStatus = 0;

        try{
            String updateName = "UPDATE CosplayCharacter set description = (?) where characterID = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(updateName);
            recordSearch.setString(1,desc);
            recordSearch.setInt(2,characterID);
            saveStatus = recordSearch.executeUpdate();

        } catch (SQLException se){
            se.printStackTrace();
        }

    }

    public void insertCharacterFromFile(){
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader("addCharacters.txt"));
            String line;

            while ((line = bufReader.readLine()) != null) {
                String[] split = line.split(" = ");
                    String fileCharacterName = split[0];
                    String fileGender = split[1];
                    String fileGenre = split[2];
                    String fileUniverse = split[3];
                    String fileMedia = split[4];
                    String fileDesc = split[5];

                int genreID = getGenreID(fileGenre);
                int universeID = getUniverseID(fileUniverse);
                int mediaID = getMediaID(fileMedia);


                String prepCharacterInsert = "INSERT INTO CosplayCharacter(name, gender, genreID," +
                            "universeID, mediaID, description) VALUES (?,?,?,?,?,?)";

                    try {
                        psInsert = conn.prepareStatement(prepCharacterInsert);

                        psInsert.setString(1, fileCharacterName);

                        psInsert.setString(2, fileGender);

                        psInsert.setInt(3, genreID);

                        psInsert.setInt(4, universeID);

                        psInsert.setInt(5, mediaID);

                        psInsert.setString(6, fileDesc);
                        psInsert.executeUpdate();
                    } catch (SQLException sql){
                        sql.printStackTrace();
                    }

                split = null;
                }
            return;
        } catch (FileNotFoundException ffe){
            System.out.println(ffe);
            System.out.println("There are no files by that name in the directory.");
            return;
        } catch (IOException io){

        }
    }

    public void insertImagesFromFile(){
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader("addCharacterImages.txt"));
            String line;

            while ((line = bufReader.readLine()) != null) {
                String[] split = line.split(" = ");
                String fileCharacterName = split[0];
                String fileAuthor = split[1];
                String fileURL = split[2];

                int characterID = getCharacterID(fileCharacterName);

                String prepImageInsert = "INSERT INTO Images(author, characterID, url) VALUES (?,?,?)";

                try {
                    psInsert = conn.prepareStatement(prepImageInsert);


                    psInsert.setString(1, fileAuthor);
                    psInsert.setInt(2, characterID);
                    psInsert.setString(3, fileURL);
                    psInsert.executeUpdate();


                } catch (SQLException se){
                    se.printStackTrace();
                }

                split = null;
            }
            return;
        } catch (FileNotFoundException ffe){
            System.out.println(ffe);
            System.out.println("There are no files by that name in the directory.");
            return;
        } catch (IOException io){

        }
    }

    public ArrayList<Character> searchCharacter(String characterName){
        ResultSet resultSet = null;
        ArrayList<Character> characterDetails = new ArrayList<Character>();
        String strongCharacterName = characterName.toUpperCase();

        //TODO SET LOGIC IF CHARACTER NAME NOT FOUND
        String fetchAllDataSQL = "SELECT * from CosplayCharacter where uName like (?)";

        try{
            psInsert = conn.prepareStatement(fetchAllDataSQL);
            psInsert.setString(1, "%" + strongCharacterName + "%");
            String universeName = "";
            resultSet = psInsert.executeQuery();
            while (resultSet.next()) {
                characterName = resultSet.getString("name");
                int characterID = resultSet.getInt("characterID");
                String gender = resultSet.getString("gender");
                int genreID = resultSet.getInt("genreID");
                String genreName = getGenreName(genreID);
                    int universeID = resultSet.getInt("universeID");
                    universeName = getUniverseName(universeID);
                int mediaID = resultSet.getInt("mediaID");
                String mediaTitle = getMediaTile(mediaID);
                String description = resultSet.getString("description");

                characterDetails.add(new Character(characterID, characterName, gender, genreID,
                        universeID, mediaID, description));


            }

            resultSet.close();

        } catch (SQLException se){
            se.printStackTrace();
        }

        return characterDetails;

    }

    public ArrayList searchCharacterID(int characterID){
        ResultSet resultSet = null;
        ArrayList<Character> characterDetails = new ArrayList<Character>();



        String fetchAllDataSQL = "SELECT * from CosplayCharacter where characterID = (?)";

        try{
            psInsert = conn.prepareStatement(fetchAllDataSQL);
            psInsert.setInt(1,characterID);
            String universeName = "";
            resultSet = psInsert.executeQuery();
            while (resultSet.next()) {
                String characterName = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                int genreID = resultSet.getInt("genreID");
                String genreName = getGenreName(genreID);
                int universeID = resultSet.getInt("universeID");
                universeName = getUniverseName(universeID);
                int mediaID = resultSet.getInt("mediaID");
                String mediaTitle = getMediaTile(mediaID);
                String description = resultSet.getString("description");

                characterDetails.add(new Character(characterID, characterName, gender, genreID,
                        universeID, mediaID, description));


                System.out.println("CharacterID: " + characterID + "Character Name:" + characterName + " Gender: " + gender +
                        " Genre: " + genreName + " Universe: " + universeName + "Title of Series: " +
                        mediaTitle + "Character Description: " + description);


            }

            resultSet.close();

        } catch (SQLException se){
            se.printStackTrace();
        }

        return characterDetails;

    }

    public Character returnCharacter(int characterID){
        ResultSet resultSet = null;
        ArrayList<Character> characterDetails = new ArrayList<Character>();
        Character character = new Character();


        String fetchAllDataSQL = "SELECT * from CosplayCharacter where characterID = (?)";

        try{
            psInsert = conn.prepareStatement(fetchAllDataSQL);
            psInsert.setInt(1,characterID);
            String universeName = "";
            resultSet = psInsert.executeQuery();
            while (resultSet.next()) {
                String characterName = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                int genreID = resultSet.getInt("genreID");
                String genreName = getGenreName(genreID);
                int universeID = resultSet.getInt("universeID");
                universeName = getUniverseName(universeID);
                int mediaID = resultSet.getInt("mediaID");
                String mediaTitle = getMediaTile(mediaID);
                String description = resultSet.getString("description");

                character = new Character(characterID, characterName, gender, genreID,
                        universeID, mediaID, description);


                System.out.println("CharacterID: " + characterID + "Character Name:" + characterName + " Gender: " + gender +
                        " Genre: " + genreName + " Universe: " + universeName + "Title of Series: " +
                        mediaTitle + "Character Description: " + description);


            }

            resultSet.close();

        } catch (SQLException se){
            se.printStackTrace();
        }

        return character;

    }

    public int getGenreID(String genre){
        ResultSet resultSet = null;
        ResultSet secondResultSet = null;
        int genreID = 0;

        try{
            String searchGenre = "select * from Genre where genreName = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            recordSearch.setString(1,genre);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the genreID of the genre entered by the user for this character
                genreID = resultSet.getInt("genreID");
            } else{
                //if that genre doesn't exist, set the genre to Other's genreID
                String otherSearch ="select * from Genre where genreName = 'Other'";
                PreparedStatement otherDecision = conn.prepareStatement(otherSearch);
                secondResultSet = otherDecision.executeQuery();

                if (secondResultSet.next()){
                    genreID = secondResultSet.getInt("genreID");
                }

                secondResultSet.close();

            }

            resultSet.close();

        } catch (SQLException se){
            se.printStackTrace();
        }

        return genreID;
    }

    public ArrayList<Integer> searchGenre(String genreName){
        ResultSet resultSet = null;
        int genreID = 0;
        ArrayList<Integer> genreCharacters = new ArrayList<Integer>();

        if (!genreName.equals("")) {
            try {
                String searchGenre = "select * from Genre where genreName = (?)";
                PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
                recordSearch.setString(1, genreName);
                resultSet = recordSearch.executeQuery();

                if (resultSet.next()) {
                    ResultSet secondResultSet = null;
                    genreID = resultSet.getInt("genreID");

                    String searchForGenreCharacters = "select * from CosplayCharacter where genreID = (?) order by name";
                    PreparedStatement characterSearch = conn.prepareStatement(searchForGenreCharacters);
                    characterSearch.setInt(1, genreID);
                    secondResultSet = characterSearch.executeQuery();

                    while (secondResultSet.next()) {
                        int characterID = secondResultSet.getInt("characterID");
                        genreCharacters.add(characterID);
                    }

                    secondResultSet.close();
                }

                resultSet.close();

            } catch (SQLException se) {
                se.printStackTrace();
            }
        } else {
            try {
                String searchGenre = "select * from Genre";
                PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
                resultSet = recordSearch.executeQuery();

                while (resultSet.next()) {
                    ResultSet secondResultSet = null;
                    genreID = resultSet.getInt("genreID");

                    String searchForGenreCharacters = "select * from CosplayCharacter where genreID IN (?) order by name";
                    PreparedStatement characterSearch = conn.prepareStatement(searchForGenreCharacters);
                    characterSearch.setInt(1, genreID);
                    secondResultSet = characterSearch.executeQuery();

                    while (secondResultSet.next()) {
                        int characterID = secondResultSet.getInt("characterID");
                        genreCharacters.add(characterID);
                    }

                    secondResultSet.close();
                }

                resultSet.close();

            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return genreCharacters;
    }

    public String getGenreName(int genreID){
        ResultSet resultSet = null;
        String genreName = "";

        try{
            String searchGenre = "select * from Genre where genreID = (?)" ;
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            recordSearch.setInt(1,genreID);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the genreID of the genre entered by the user for this character
                genreName = resultSet.getString("genreName");
            } else{
                //if that genre doesn't exist, set the genre to Other's genreID
                genreName = "Other";

            }

            resultSet.close();
        } catch (SQLException se){
            se.printStackTrace();
        }

        return genreName;
    }

    public int getUniverseID(String universe){
        ResultSet resultSet = null;
        ResultSet secondResultSet = null;
        int universeID = 0;

        try{
            String searchUniverse = "select * from Universe where universeName = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(searchUniverse);
            recordSearch.setString(1,universe);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the genreID of the genre entered by the user for this character
                universeID = resultSet.getInt("universeID");
            } else{
                //if that genre doesn't exist, set the genre to Other's genreID
                String addUniverse = "INSERT INTO Universe(universeName) VALUES(?)";
                psInsert = conn.prepareStatement(addUniverse);

                psInsert.setString(1, universe);
                psInsert.execute();

                String otherSearch = "select * from Universe where universeName = (?)";
                PreparedStatement otherDecision= conn.prepareStatement(otherSearch);
                otherDecision.setString(1,universe);
                secondResultSet = otherDecision.executeQuery();

                if (secondResultSet.next()){
                    universeID = secondResultSet.getInt("universeID");
                }

                secondResultSet.close();
            }

            resultSet.close();


        } catch (SQLException se){
            se.printStackTrace();
        }

        return universeID;
    }

    public String getUniverseName(int universeID){
        ResultSet resultSet = null;
        String universeName = "";

        try{
            String searchGenre = "select * from Universe where universeID = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            recordSearch.setInt(1,universeID);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the universe name based on the ID
                universeName = resultSet.getString("universeName");
            } else{
                //if that universe doesn't exist, set the universe to Other
                universeName = "Unknown";

            }

            resultSet.close();
        } catch (SQLException se){
            se.printStackTrace();
        }

        return universeName;

    }

    public ArrayList<Integer> searchUniverse(String universeName){
        ResultSet resultSet = null;
        int universeID = 0;
        ArrayList<Integer> universeCharacters = new ArrayList<Integer>();


        if (!universeName.equals("")){
            try {
                String searchUniverse = "select * from Universe where universeName = (?)";
                PreparedStatement recordSearch = conn.prepareStatement(searchUniverse);
                recordSearch.setString(1, universeName);
                resultSet = recordSearch.executeQuery();

                if (resultSet.next()){
                    ResultSet secondResultSet = null;
                    universeID = resultSet.getInt("universeID");

                    String searchForUniverseCharacters = "select * from CosplayCharacter where universeID = (?)";
                    PreparedStatement characterSearch = conn.prepareStatement(searchForUniverseCharacters);
                    characterSearch.setInt(1,universeID);
                    secondResultSet = characterSearch.executeQuery();

                    while (secondResultSet.next()){
                        int characterID = secondResultSet.getInt("characterID");
                        universeCharacters.add(characterID);
                    }

                    secondResultSet.close();
                }

                resultSet.close();

            } catch (SQLException se){
                se.printStackTrace();
            }
        } else {
            try {
                String searchUniverse = "select * from Universe";
                PreparedStatement recordSearch = conn.prepareStatement(searchUniverse);
                resultSet = recordSearch.executeQuery();

                while (resultSet.next()) {
                    ResultSet secondResultSet = null;
                    universeID = resultSet.getInt("universeID");

                    String searchForGenreCharacters = "select * from CosplayCharacter where universeID IN (?) order by name";
                    PreparedStatement characterSearch = conn.prepareStatement(searchForGenreCharacters);
                    characterSearch.setInt(1, universeID);
                    secondResultSet = characterSearch.executeQuery();

                    while (secondResultSet.next()) {
                        int characterID = secondResultSet.getInt("characterID");
                        universeCharacters.add(characterID);
                    }

                    secondResultSet.close();
                }

                resultSet.close();

            } catch (SQLException se) {
                se.printStackTrace();
            }
        }


        return universeCharacters;
    }

    public int getMediaID(String mediaTitle){
        ResultSet resultSet = null;
        ResultSet secondResultSet = null;
        int mediaID = 0;

        try{
            String searchGenre = "select * from Media where mediaTitle = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            recordSearch.setString(1,mediaTitle);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the genreID of the genre entered by the user for this character
                mediaID = resultSet.getInt("mediaID");
            } else{
                String addMediaTitle = "INSERT INTO Media(mediaTitle) VALUES (?)";
                psInsert = conn.prepareStatement(addMediaTitle);

                psInsert.setString(1,mediaTitle);
                psInsert.execute();


                String otherSearch ="select * from Media where mediaTitle = '" + mediaTitle + "'";
                PreparedStatement otherDecision = conn.prepareStatement(otherSearch);
                secondResultSet = otherDecision.executeQuery();

                if (secondResultSet.next()){
                    mediaID = secondResultSet.getInt("mediaID");
                }

                secondResultSet.close();
            }

            resultSet.close();


        } catch (SQLException se){
            se.printStackTrace();
        }


        return mediaID;
    }

    public String getMediaTile(int mediaID){
        ResultSet resultSet = null;
        String mediaTitle = "";

        try{
            String searchGenre = "select * from Media where mediaID = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            recordSearch.setInt(1,mediaID);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the universe name based on the ID
                mediaTitle = resultSet.getString("mediaTitle");
            } else{
                //if that universe doesn't exist, set the media title to Unknown
                mediaTitle = "Unknown";

            }

            resultSet.close();

        } catch (SQLException se){
            se.printStackTrace();
        }

        return mediaTitle;
    }

    public ArrayList<Integer> searchMediaTitle(String mediaTitle){
        ResultSet resultSet = null;
        int mediaID = 0;
        ArrayList<Integer> mediaCharacters = new ArrayList<Integer>();

        try{
            String searchMedia ="select * from Media where mediaTitle = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(searchMedia);
            recordSearch.setString(1,mediaTitle);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()){
                ResultSet secondResultSet = null;
                mediaID = resultSet.getInt("mediaID");

                String searchForMediaCharacters = "select * from CosplayCharacter where mediaID = (?)";
                PreparedStatement characterSearch = conn.prepareStatement(searchForMediaCharacters);
                characterSearch.setInt(1,mediaID);
                secondResultSet = characterSearch.executeQuery();

                while (secondResultSet.next()){
                    int characterID = secondResultSet.getInt("characterID");
                    mediaCharacters.add(characterID);
                }

                secondResultSet.close();
            }

            resultSet.close();

        } catch (SQLException se){
            se.printStackTrace();
        }


        return mediaCharacters;
    }

    public void insertImage (int characterID, String author, String url){

        String prepImageInsert = "INSERT INTO Images(author, characterID, url) VALUES (?,?,?)";

        try {
            psInsert = conn.prepareStatement(prepImageInsert);


            psInsert.setString(1, author);
            psInsert.setInt(2, characterID);
            psInsert.setString(3, url);
            psInsert.executeUpdate();


        } catch (SQLException se){
            se.printStackTrace();
        }
    }

    public ArrayList searchImages (int characterID){
        ResultSet resultSet = null;
        ArrayList<String> characterImages = new ArrayList<String>();

        try {
            String imageSearch ="select * from Images where characterID = (?)";
            PreparedStatement recordSearch = conn.prepareStatement(imageSearch);
            recordSearch.setInt(1,characterID);
            resultSet = recordSearch.executeQuery();

            while (resultSet.next()) {
                String imageURL ="";
                imageURL = resultSet.getString("url");

                characterImages.add(imageURL);
            }

            resultSet.close();

        } catch (SQLException se){
            se.printStackTrace();
        }

        return characterImages;
    }

    private void addCharactersToFile(Character character, BufferedWriter openBufWriter) {
        try{
            //Strings must be inserted into the file, and read from the file, because if the
            //int of the genre, media, or universe doesn't exist, the whole character will be
            //rejected. If the String of the genre, media or universe doesn't exist, the
            //database will insert it automatically.

            String genreName = getGenreName(character.getGenreID());
            String universeName = getUniverseName(character.getUniverseID());
            String mediaTitle= getMediaTile(character.getMediaID());

            openBufWriter.write(character.getCharacterName() + " = ");
            openBufWriter.write(character.getGender() + " = ");
            openBufWriter.write(genreName + " = ");
            openBufWriter.write(universeName + " = ");
            openBufWriter.write(mediaTitle + " = ");
            openBufWriter.write(character.getDescription() + " = ");
            openBufWriter.newLine();

        } catch (IOException io){
            io.printStackTrace();
        }
    }

    public void deleteDB(){
        String deleteCharSQL = "DROP TABLE CosplayCharacter";
        String deleteGenreSQL = "DROP TABLE Genre";
        String deleteImagesSQL = "DROP TABLE Images";
        String deleteUniverseSQL = "DROP TABLE Universe";
        String deleteTutorialsSQL = "DROP TABLE Tutorials";
        String deleteMediaSQL = "DROP TABLE Media";
        try {
            statement.executeUpdate(deleteCharSQL);
            statement.executeUpdate(deleteGenreSQL);
            statement.executeUpdate(deleteImagesSQL);
            statement.executeUpdate(deleteUniverseSQL);
            statement.executeUpdate(deleteTutorialsSQL);
            statement.executeUpdate(deleteMediaSQL);
        } catch (SQLException se){
            se.printStackTrace();
        }

    }

    public void closeDB(){
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed.");
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
