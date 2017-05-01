package org.teamfarce.mirch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import org.teamfarce.mirch.ScenarioBuilder.ScenarioBuilderException;
import org.teamfarce.mirch.dialogue.Dialogue;
import org.teamfarce.mirch.entities.Player;
import org.teamfarce.mirch.entities.Suspect;
import org.teamfarce.mirch.map.Room;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * MIRCH is used to generate all graphics in the program. It initialises the scenario generator and game state
 * and provides all interactions with the back end of the program.
 *
 * Lorem Ipsum executable file: http://lihq.me/Downloads/Assessment3/Game.zip
 *
 * @author jacobwunwin
 */
public class MIRCH extends Game {
    public static MIRCH me;
    
    // Each game state
    public GameSnapshot game1Snapshot;
    public GameSnapshot game2Snapshot;
    // Pointer to current game state
    public GameSnapshot gameSnapshot;
    
    public GUIController guiController;
    
    // Each game's rooms
    public ArrayList<Room> game1Rooms;
    public ArrayList<Room> game2Rooms;
    // Pointer to current game's rooms
    public ArrayList<Room> rooms;

    // Each game's characters
    public ArrayList<Suspect> game1Characters;
    public ArrayList<Suspect> game2Characters;
    // Pointer to current game's characters
    public ArrayList<Suspect> characters;

    public int step; //stores the current loop number

    public Player player1, player2;
    // Pointer to current player
    public Player player;
    
    public boolean game1 = true;

    /**
     * Initialises all variables in the game and sets up the game for play.
     */
    @Override
    public void create() {

        me = this;
        Assets.load();

        step = 0; //initialise the step variable

        ScenarioBuilderDatabase database;
        try {
            database = new ScenarioBuilderDatabase("db.db");

            try {
                game1Snapshot = ScenarioBuilder.generateGame(this, database, new Random());
                game2Snapshot = ScenarioBuilder.generateGame(this, database, new Random());
            } catch (ScenarioBuilderException e) {
                e.printStackTrace();
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        //generate RenderItems from each room
        game1Rooms = new ArrayList<>();
        for (Room room : game1Snapshot.getRooms()) {
        	game1Rooms.add(room);
        }
        game2Rooms = new ArrayList<>();
        for (Room room : game2Snapshot.getRooms()) {
        	game2Rooms.add(room);
        }

        //generate RenderItems for each suspect
        game1Characters = new ArrayList<>();
        for (Suspect suspect : game1Snapshot.getSuspects()) {
        	game1Characters.add(suspect);
        }
        //generate RenderItems for each suspect
        game2Characters = new ArrayList<>();
        for (Suspect suspect : game2Snapshot.getSuspects()) {
        	game2Characters.add(suspect);
        }

        // Initialize gameSnapshot pointer
        gameSnapshot = game1Snapshot;

        game1Snapshot.map.placeNPCsInRooms(game1Characters);
        game2Snapshot.map.placeNPCsInRooms(game2Characters);

        // Prepare the list of suspects to be shown on the CCTV screen
        // Added by Alex - Team Jaapan
        game1Snapshot.prepCCTVSuspects();
        game2Snapshot.prepCCTVSuspects();
        
        System.out.println("Murderer: " + gameSnapshot.murderer.getName());

        //initialise the player sprite
        Dialogue playerDialogue = null;
        try {
            playerDialogue = new Dialogue("Player.JSON", true);
        } catch (Dialogue.InvalidDialogueException e) {
            System.out.print(e.getMessage());
            System.exit(0);
        }
        player1 = new Player(this, "Bob", "The player to beat all players", "Detective_sprite.png", playerDialogue);
        player1.setTileCoordinates(7, 10);
        player1.setRoom(game1Rooms.get(0));
        
        player2 = new Player(this, "Bob", "The player to beat all players", "Detective_sprite.png", playerDialogue);
        player2.setTileCoordinates(7, 10);
        player2.setRoom(game2Rooms.get(0));

        // Initialize pointers
        rooms = game1Rooms;
        characters = game1Characters;
        player = player1;

        game2Snapshot.setState(GameState.map);

        //Setup screens
        guiController = new GUIController(this);
        guiController.initScreens();
    }

    /**
     * The render function deals with all game logic. It receives inputs from the input controller,
     * carries out logic and pushes outputs to the screen through the GUIController
     */
    @Override
    public void render() {
        this.guiController.update();
        super.render();

        step++; //increment the step counter
    }

    @Override
    public void dispose() {

    }
}
