/**
 * 
 */
package org.teamfarce.mirch;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author jacobwunwin
 *
 */
public class MapEntity_Test {
	
	@Test
	public void test_setRoomGetRoom(){
		MapEntity mapEntity = new MapEntity(0, 0, null, null, null);
		
		Room theRoom = new Room(null, null);
		
		mapEntity.setRoom(theRoom);
		
		assertEquals(theRoom, mapEntity.getRoom());
	}
	
	public void test_getName(){
		String input = "Test";
		MapEntity mapEntity = new MapEntity(0, 0, input, null, null);
		assertEquals(input, mapEntity.getName());
		
	}
	
	public void test_genDescription(){
		String input = "Test";
		MapEntity mapEntity = new MapEntity(0, 0, null, input, null);
		assertEquals(input, mapEntity.getDescription());
	}
	
	public void test_getFileName(){
		String input = "Test";
		MapEntity mapEntity = new MapEntity(0, 0, null, null, input);
		assertEquals(input, mapEntity.filename);
	}
	

}