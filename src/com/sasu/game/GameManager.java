package com.sasu.game;

import java.awt.event.KeyEvent;

import com.sasu.engine.AbstractGame;
import com.sasu.engine.GameContainer;
import com.sasu.engine.Renderer;
import com.sasu.engine.audio.SoundClip;
import com.sasu.engine.gfx.Image;
import com.sasu.engine.gfx.ImageTile;

public class GameManager extends AbstractGame
{
	public int multi = 5;
	
	private ImageTile image;
	private ImageTile image2;
	private ImageTile image3;
	private SoundClip clip;

	public GameManager()
	{
		image = new ImageTile("/test.png", 16, 16);
		image2 = new ImageTile("/space.png", 32, 32);
		image3 = new ImageTile("/spaceT.png", 64, 64);
		image3.setAlpha(true);
		
	}
	
	@Override
	public void update(GameContainer gc, float dt) 
	{
		if(gc.getInput().isKeyDown(KeyEvent.VK_A))
		{
			clip = new SoundClip("/Audio/test.wav");
			clip.setVolume(-20f);
			
			System.out.println("A trycktes ned!");
			clip.play();
		}
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_S))
		{
			clip = new SoundClip("/Audio/test2.wav");
			clip.setVolume(-20f);
			
			System.out.println("S trycktes ned!");
			clip.play(); 
		}
		
		

		
		temp += dt * multi;
		
		if(temp >  4)
		{
			temp = 0;
		}
	}
	
	float temp = 0;

	@Override
	public void renderer(GameContainer gc, Renderer r)
	{
		r.drawImage(image2, 10, 10);
		
		//r.drawImage(image3, gc.getInput().getMouseX(), gc.getInput().getMouseY());
		r.drawImageTile(image, gc.getInput().getMouseX() - 8, gc.getInput().getMouseY() - 16, (int)temp, 0);
		
		
	}
	
	public static void main(String args[])
	{
		GameContainer gc = new GameContainer(new GameManager());
		gc.start();
	}

}
