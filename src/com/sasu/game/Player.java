package com.sasu.game;

import java.awt.event.KeyEvent;

import com.sasu.engine.GameContainer;
import com.sasu.engine.Renderer;
import com.sasu.engine.audio.SoundClip;
import com.sasu.engine.gfx.ImageTile;

public class Player extends GameObject
{
	private int tileX, tileY;
	private float offX, offY;
	
	private float speed = 150;
	private float fallSpeed = 10;
	private float jump = -1.5f;
	private boolean groundHit = false;
	
	private float animSpeed = 6;
	
	private boolean still;
	
	private boolean right;
	private boolean left;
	private boolean right2;
	private boolean left2;
	
	private ImageTile mario;
	float temp = 0;
	
	private SoundClip clip;
	
	private float fallDistance = 0;
	
	public Player(int posX, int posY)
	{
		this.tag = "player";
		this.tileX = posX;
		this.tileY = posY;
		this.offX = 0;
		this.offY = 0;
		this.posX = posX * GameManager.tileSize;
		this.posY = posY * GameManager.tileSize;
		this.width = 16;
		this.height = 16;
		mario = new ImageTile("/mario_idle_right.png", 16, 16);
		clip = new SoundClip("/Audio/test.wav");
		clip.setVolume(-2f);
	}

	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt)
	{
		// Beginning of Left and Right

		temp += dt * animSpeed;
		if(temp > 2) temp = 0;
		
		
		// AAAAAAAAAAAAAAAAA LEFT
		if(gc.getInput().isKey(KeyEvent.VK_A))
		{
			still = false;
			if(!left)
			{
				mario = new ImageTile("/mario_walk_left.png", 16, 16);
				right = false;
				left = true;
				left2 = true;
				right2 = false;
				animSpeed = 9;
			}
			if(gm.getCollision(tileX - 1, tileY) || gm.getCollision(tileX - 1, tileY + (int)Math.signum((int)offY)))
			{
				if(offX > 0)
				{
					offX -= dt *speed;
					if(offX < 0)
					{
						offX = 0;
					}
				}
				else
				{
					offX = 0;
				}
			}
			else
			{
				offX -= dt *speed;
			}
			
		}
		else
		{
			left = false;
		}

						
		// DDDDDDDDDDDDDDDD RIGHT
		if(gc.getInput().isKey(KeyEvent.VK_D))
		{
			still = false;
			if(!right)
			{
				mario = new ImageTile("/mario_walk_right.png", 16, 16);
				left = false;
				right = true;
				right2 = true;
				left2 = false;
				animSpeed = 9;
			}
			if(gm.getCollision(tileX + 1, tileY) || gm.getCollision(tileX + 1, tileY + (int)Math.signum((int)offY)))
			{
				if(offX < 0)
				{
					offX += dt *speed;
					if(offX > 0)
					{
						offX = 0;
					}
				}
				else
				{
					offX = 0;
				}
			}
			else
			{
				offX += dt *speed;
			}
		}
		else
		{
			right = false;
		}
		
		
		if(!gc.getInput().isKey(KeyEvent.VK_D) && !gc.getInput().isKey(KeyEvent.VK_A))
		{
			//NOTHING
		}
		
		//Checks is no input is present and sets still to true	
		if(!right && !left)
		{
			if(!still)
			{
				if(right2)
				{
					mario = new ImageTile("/mario_idle_right.png", 16, 16);
				}
				if(left2)
				{
					mario = new ImageTile("/mario_idle_left.png", 16, 16);
				}
				still = true;
				animSpeed = 4;
			}
			
		}
		
		
		
		// End of Left and Right
		
		
		// Beginning of Jump and gravity
		
		fallDistance += (dt / 2) *fallSpeed; //I changed this to fall slower, my frame time is too fast LUL
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_SPACE) && groundHit)
		{
			fallDistance = jump;
			groundHit = false;
		}
		
		offY += fallDistance;
		
		if(fallDistance < 0)
		{
			if((gm.getCollision(tileX, tileY - 1) || gm.getCollision(tileX + (int)Math.signum((int)offX), tileY - 1)) && offY <= 0)
			{
				fallDistance = 0;
				offY = 0;
			}
		}
		
		
		if(fallDistance > 0)
		{
			if((gm.getCollision(tileX, tileY + 1) || gm.getCollision(tileX + (int)Math.signum((int)offX), tileY + 1)) && offY > 0)
			{
				fallDistance = 0;
				offY = 0;
				groundHit = true;
			}
		}
		
		// End of Jump and gravity
		
		
		// Final Pos
		// Y
		if(offY > GameManager.tileSize / 2)
		{
			tileY++;
			offY -= GameManager.tileSize;
		}
	
		if(offY < -GameManager.tileSize / 2)
		{
			tileY--;
			offY += GameManager.tileSize;
		}
		
		// X
		if(offX > GameManager.tileSize / 2)
		{
			tileX++;
			offX -= GameManager.tileSize;
		}
		
		if(offX < -GameManager.tileSize / 2)
		{
			tileX--;
			offX += GameManager.tileSize;
		}
		
		posY = tileY * GameManager.tileSize + offY;
		posX = tileX * GameManager.tileSize + offX;
		
		if(gc.getInput().isKey(KeyEvent.VK_UP))
		{
			gm.addObject(new Projectile(tileX, tileY, offX + width / 2, offY + height / 2, 3));
			clip.play();
		}
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) 
	{
		//r.drawFillRect((int)posX, (int)posY, width, height, 0xffff0000);
		r.drawImageTile(mario, (int)posX, (int)posY, (int)temp, 0);
	}

}
