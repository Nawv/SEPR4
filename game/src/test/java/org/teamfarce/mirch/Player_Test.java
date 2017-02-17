package org.teamfarce.mirch;

import org.junit.Before;
import org.junit.Test;
import org.teamfarce.mirch.Entities.Player;
import org.teamfarce.mirch.GameTest;
import org.teamfarce.mirch.Vector2Int;
import org.teamfarce.mirch.map.Room;

import static org.junit.Assert.*;

/**
 * Created by joeshuff on 15/02/2017.
 */
public class Player_Test extends GameTest {

    Player p;

    @Before
    public void start()
    {
        p = new Player("Name", "Desc", "Detective_sprite.png");
    }

    @Test
    public void test_aStar()
    {
        Vector2Int start = new Vector2Int(0, 0);

        Vector2Int dest = new Vector2Int(4, 4);

        p.setTileCoordinates(start);
        p.setRoom(new Room(0, "testMap.tmx", "name"));

        String desiredSolution = "[(0,0), (1,0), (2,0), (3,0), (4,0), (4,1), (4,2), (4,3), (4,4)]";
        assertEquals(desiredSolution, p.aStarPath(dest).toString());
    }

}
