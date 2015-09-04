package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.BitSet;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	int screenWidth;
	int screenHeight;
    
	// 1 = block
	// 0 = empty
	// the x and y coordinate system is not what it seems
	// visually x goes down and y across
	// this will make more sense when you compare it to what is drawn
	int[][] map = {
		{1,1,1,1,1,0,0,0,1,1,1,1,1,1,1},
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,1,1,0,0,0,0,0,0,0,0,0,0,0},
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,1,1,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,1,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
		{1,1,1,1,1,1,0,0,1,1,1,1,1,1,1},
	};
	int mapWidth = 15;
	int mapHeight = 15;
	int tileSize = 20;
	Texture tileTexture;
	
	ArrayList<Entity> entities = new ArrayList<Entity>();
	
	enum Axis { X, Y };
	enum Direction { U, D, L, R };

  @Override
  //***********************************************************
  //Create
  //Create all textures, entities and sprite batches
  //***********************************************************
  public void create () {
	  batch = new SpriteBatch();
	  tileTexture = new Texture("block.png");  
	  screenWidth = Gdx.graphics.getWidth();
	  screenHeight = Gdx.graphics.getHeight();
	 	  
	  // add some entities including a player
	  entities.add(new Player(this, 100, 150, 20, 20, 120.0f, new Texture("player.png"),"player.png"));
	  entities.add(new Entity(this, 50, 150, 20, 20, 120.0f, new Texture("enemy.png"),"enemy.png"));
	  entities.add(new Entity(this, 200, 200, 20, 20, 120.0f, new Texture("enemy.png"), "enemy.png"));
	  entities.add(new Entity(this, 180, 50, 20, 20, 120.0f, new Texture("enemy.png"), "enemy.png"));
  }
	//***********************************************************
	//Move entity
	//Move entity to new position
	//***********************************************************
  public void moveEntity(Entity e, float newX, float newY) {
	  // just check x collisions keep y the same
	  moveEntityInAxis(e, Axis.X, newX, e.y);
	  // just check y collisions keep x the same
	  moveEntityInAxis(e, Axis.Y, e.x, newY);
  }
	//***********************************************************
	//Move entity in axis
	//Move entity in determined direction
	//***********************************************************
  public void moveEntityInAxis(Entity e, Axis axis, float newX, float newY) {
	  Direction direction;
	  
	  // determine axis direction
	  if(axis == Axis.Y) {
		  if(newY - e.y < 0) direction = Direction.U;
		  else direction = Direction.D;
	  }
	  else {
		  if(newX - e.x < 0) direction = Direction.L;
		  else direction = Direction.R;
	  }


	  if(!tileCollision(e, direction, newX, newY) && !entityCollision(e, direction, newX, newY)) {
		  // full move with no collision
		  e.move(newX, newY);
	  }else{
		  e.hit();
	  }
	  // else collision with wither tile or entity occurred
  }
	//***********************************************************
	//Tile Collision
	//checks if the objects is withing the game world
	//Checks if entity collides with game world
	//***********************************************************
  public boolean tileCollision(Entity e, Direction direction, float newX, float newY) {
	  boolean collision = false;

	  // determine affected tiles
	  int x1 = (int) Math.floor(Math.min(e.x, newX) / tileSize);
	  int y1 = (int) Math.floor(Math.min(e.y, newY) / tileSize);
	  int x2 = (int) Math.floor((Math.max(e.x, newX) + e.width - 0.1f) / tileSize);
	  int y2 = (int) Math.floor((Math.max(e.y, newY) + e.height - 0.1f) / tileSize);
	  
	  // todo: add boundary checks...
	  if (newX < 0) {
		  e.x = 0;
		  e.getCircleOuterBound().setCenterX(e.y);
		  return true;
	  }else if (newX > tileSize * (map.length-1)) {
		  e.x = tileSize * (map.length - 1);
		  e.getCircleOuterBound().setCenterX(e.y);
		  return true;
	  }
	  if (newY < 0) {
		  e.y = 0;
		  e.getCircleOuterBound().setCenterY(e.y);
		  return true;
	  }else if (newY > tileSize * (map.length-1)) {
		  e.y = tileSize * (map.length - 1);
		  e.getCircleOuterBound().setCenterY(e.y);
		  return true;
	  }

	  if(!collision) {
		  // tile checks
		  for (int x = x1; x <= x2; x++) {
			  for (int y = y1; y <= y2; y++) {
				  if (map[x][y] == 1) {
					  collision = true;

					  e.tileCollision(map[x][y], x, y, newX, newY, direction);
				  }
			  }
		  }
	  }
	  return collision;
  }
	//***********************************************************
	//entity Collision
	//Tires to move entity in new pos
	//Checks for bounding shape collision
	//If bounding collision, do bitwise collision check
	//***********************************************************
  public boolean entityCollision(Entity e1, Direction direction, float newX, float newY) {
	  boolean collision = false;

	  for(int i = 0; i < entities.size(); i++) {
		  Entity e2 = entities.get(i);

		  // we don't want to check for collisions between the same entity
		  if(e1 != e2) {
			  //distance between 2 entities
			  Vector2 dist = new Vector2(Math.abs(e1.x-e2.x),Math.abs(e1.y-e2.y));
			  float rad = e1.radius+e2.radius;

			  if(dist.len()<rad) {

				  int startX = (int) Math.max(newX, e2.x);
				  int startY = (int) Math.max(newY, e2.y);

				  int endX = ((int) Math.min(newX + e1.width, e2.x + e2.width));
				  int endY = ((int) Math.min(newY + e1.height, e2.y + e2.height));

				  for (int j = 0; j < Math.abs(endY - startY); j++) {
					  int y_test1 = Math.abs(((int) (startY - newY)) + j);
					  int y_test2 = Math.abs(((int) (startY - e2.y))) + j;
					  int x_test1 = Math.abs(((int) (startX - newX)));
					  int x_test2 = Math.abs(((int) (startX - e2.x)));
					  if((y_test1<e1.height)&&(y_test2<e2.height)) {
						  BitSet overlayEntity = e2.bitSet[y_test2].get(x_test2, x_test2 + 1 + Math.abs(endX - startX));
						  BitSet overlayPlayer = e1.bitSet[y_test1].get(x_test1, x_test1 + Math.abs(endX - startX));
						  overlayPlayer.and(overlayEntity);
						  if (overlayPlayer.cardinality() != 0) {
							  collision = true;
							  break;
						  }
					  }else{
						  continue;
					  }
				  }
			  }
		  }
	  }

	  return collision;
  }

	@Override
  public void render () {
	  
	  // update
	  // ---
	  
	  
	  float delta = Gdx.graphics.getDeltaTime();
	  
	  // update all entities
	  for(int i = entities.size() - 1; i >= 0; i--) {
		  Entity e = entities.get(i);
		  // update entity based on input/ai/physics etc
		  // this is where we determine the change in position
		  e.update(delta);
		  // now we try move the entity on the map and check for collisions
		  moveEntity(e, e.x + e.dx, e.y + e.dy);
	  }	  
	  
	  
	  // draw
	  // ---

	  
	  // to offset where your map and entities are drawn change the viewport
	  // see libgdx documentation
	  
	  Gdx.gl.glClearColor(0, 0, 0, 1);
	  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.enableBlending();
	  batch.begin();
    
	  // draw tile map
	  // go over each row bottom to top
	  for(int y = 0; y < mapHeight; y++) {
		  // go over each column left to right		
		  for(int x = 0; x < mapWidth; x++) {
			  // tile
			  if(map[x][y] == 1) {
				  batch.draw(tileTexture, x * tileSize, y * tileSize);
			  }
			  // draw other types here...
		  }
	  }
    
	  // draw all entities
	  for(int i = entities.size() - 1; i >= 0; i--) {
		  Entity e = entities.get(i);
		  batch.draw(e.texture, e.x, e.y);
	  }
	  
	  batch.end();
  }
}
