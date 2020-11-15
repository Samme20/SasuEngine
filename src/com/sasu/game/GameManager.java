package com.sasu.game;

import java.util.ArrayList;

import com.sasu.engine.AbstractGame;
import com.sasu.engine.GameContainer;
import com.sasu.engine.Renderer;
import com.sasu.engine.gfx.Image;

public class GameManager extends AbstractGame {
	public static final int tileSize = 16; // Size of the game tiles :)

	private boolean[] collision;
	private int levelW, levelH;
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	
	private Camera camera;

	public GameManager() {
		objects.add(new Player(25, 10));
		loadLevel("/map.png");
		camera = new Camera("player");
	}

	@Override
	public void init(GameContainer gc) {
		gc.getRenderer().setAmbientColor(-1);
	}

	@Override
	public void update(GameContainer gc, float dt) {
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).update(gc, this, dt);
			if (objects.get(i).isDead()) {
				objects.remove(i);
				i--;
			}
		}
		camera.update(gc, this, dt);
	}
	float temp = 0;

	@Override
	public void renderer(GameContainer gc, Renderer r) 
	{
		camera.render(r);
		
		for (int y = 0; y < levelH; y++) {
			for (int x = 0; x < levelW; x++) {
				if (collision[x + y * levelW]) {
					r.drawFillRect(x * tileSize, y * tileSize, tileSize, tileSize, 0xff000000);
				} else {
					r.drawFillRect(x * tileSize, y * tileSize, tileSize, tileSize, 0xfff9f9f9);
				}
			}
		}

		for (GameObject obj : objects) {
			obj.render(gc, r);
		}
	}

	public void loadLevel(String path) {
		Image levelImage = new Image(path);

		levelW = levelImage.getW();
		levelH = levelImage.getH();
		collision = new boolean[levelW * levelH];

		for (int y = 0; y < levelH; y++) {
			for (int x = 0; x < levelW; x++) {
				if (levelImage.getP()[x + y * levelImage.getW()] == 0xff000000) {
					collision[x + y * levelImage.getW()] = true;
				} else {
					collision[x + y * levelImage.getW()] = false;
				}
			}
		}
	}
	
	public void addObject(GameObject object)
	{
		objects.add(object);
	}
	
	public GameObject getObject(String tag)
	{
		for(int i = 0; i < objects.size(); i++)
		{
			if(objects.get(i).getTag().equals(tag))
			{
				return objects.get(i);
			}
		}
		return null;
	}

	public boolean getCollision(int x, int y) {
		if (x < 0 || x >= levelW || y < 0 || y >= levelH) {
			return true;
		}
		return collision [(x + y * levelW)];
	}

	public static void main(String args[]) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.start();

	}

}
