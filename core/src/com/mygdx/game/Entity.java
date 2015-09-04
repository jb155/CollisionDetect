package com.mygdx.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.MyGdxGame.Direction;
import javafx.scene.shape.Circle;

import java.util.BitSet;

public class Entity extends Sprite {
	public MyGdxGame game;
	public float x;
	public float y;
	public float dx;
	public float dy;
	public int width;
	public int height;  
	public float speed;
	public Texture texture;
	public FileHandle entityFile;
	protected BitSet[] bitSet;

	public float radius;
	protected Circle circleOuterBound;
	public boolean useCircleCollision;

	public Entity(MyGdxGame game, float x, float y, int width, int height, float speed, Texture texture, String fileName) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.texture = texture;

		radius = width / 2;

		circleOuterBound = new Circle();
		circleOuterBound.setCenterX(x);
		circleOuterBound.setCenterY(y);
		circleOuterBound.setRadius(radius);

		useCircleCollision = false;

		entityFile = new FileHandle(fileName);
		bitSet = getBitMask(new Pixmap(entityFile));
		printBitmask();
	}

	public BitSet[] getBitMask(Pixmap pixmap) {
		BitSet[] bitmask = new BitSet[pixmap.getHeight()];
		for (int i = 0; i < bitmask.length; i++) {
			bitmask[i] = new BitSet(pixmap.getWidth());
		}
		for (int y = 0; y < pixmap.getHeight(); y++) {
			for (int x = 0; x < pixmap.getWidth(); x++) {
				if ((pixmap.getPixel(x, y) & 0x000000ff) != 0x00) {
					bitmask[y].set(x);
				}
			}
		}
		return bitmask;
	}

	//Proof things are happening
	private void printBitmask() {
		//System.out.println(texture.);
		for (BitSet set : bitSet) {
			for (int i = 0; i < texture.getWidth(); i++) {
				if (set.get(i)) {
					System.out.print("1 ");
				} else {
					System.out.print("0 ");
				}
			}
			System.out.println("");
		}
	}

	public void update(float delta) {

	}
	
	public void move(float newX, float newY) {
		x = newX;
		y = newY;		
	}
	
	public void render() {
		
	}


	public void tileCollision(int tile, int tileX, int tileY, float newX, float newY, Direction direction) {
		System.out.println("tile collision at: " + tileX + " " + tileY);
		
		if(direction == Direction.U) {
			y = tileY * game.tileSize + game.tileSize;
		}
		else if(direction == Direction.D) {
			y = tileY * game.tileSize - height;
		}
		else if(direction == Direction.L) {
			x = tileX * game.tileSize + game.tileSize;
		}
		else if(direction == Direction.D) {
			x = tileX * game.tileSize - width;
		}		
	}

	public void entityCollision(Entity e2, float newX, float newY, Direction direction) {
		System.out.println("entity collision around: " + newX + " " + newY);
		
		move(newX, newY);
		// could also resolve entity collisions in the same we do tile collision resolution
	}


}
