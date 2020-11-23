package com.sasu.game;

import com.sasu.engine.GameContainer;
import com.sasu.engine.Renderer;

public class Projectile extends GameObject
{
	private int tileX, tileY;
	private float offX, offY;
	
	private float speed = 200;
	private int direction;
	
	private long timeNow;
	
	boolean groundHit;
	
	public Projectile(int tileX, int tileY, float offX, float offY, int direction)
	{
		this.direction = direction;
		this.tileX = tileX;
		this.tileY = tileY;
		this.offX = offX;
		this.offY = offY;
		posY = tileY * GameManager.tileSize + offY;
		posX = tileX * GameManager.tileSize + offX;
	}
	@Override
	public void update(GameContainer gc, GameManager gm, float dt)
	{		
		switch(direction)
		{
		case 0: offY += speed *dt; break; //DOWN
		case 1: offX += speed *dt; break; //LEFT
		case 2: offY -= speed *dt; break; //UP
		case 3: offX -= speed *dt; break; //RIGHT
		case 4:
			offX -= speed *dt;
			groundHit = false;
			System.out.println(timeNow - (System.currentTimeMillis() - 2000));
			if((gm.getCollision(tileX, tileY) && !groundHit || gm.getCollision(tileX + (int)Math.signum((int)offX), tileY)) && offY > 0 && !groundHit)
			{
				groundHit = true;
				timeNow = System.currentTimeMillis();
			}
			else
			{
				if(timeNow - (System.currentTimeMillis() - 2000) < 0)
				{
					offY += speed *dt;
				}
			}
			
			if(groundHit)
			{
				if(timeNow - (System.currentTimeMillis() - 2000) < 0)
				{
					offY -= speed *dt;
					timeNow = 0;
					//groundHit = false;
				}
			}
		}
		
		// Final Pos
		// Y
		if(offY > GameManager.tileSize)
		{
			tileY++;
			offY -= GameManager.tileSize;
		}
		
		if(offY < 0)
		{
			tileY--;
			offY += GameManager.tileSize;
		}
		
		// X
		if(offX > GameManager.tileSize)
		{
			tileX++;
			offX -= GameManager.tileSize;
		}
		
		if(offX < 0)
		{
			tileX--;
			offX += GameManager.tileSize;
		}
		
		/*if(gm.getCollision(tileX, tileY))
		{
			this.dead = true;
		}*/
		
		
		posY = tileY * GameManager.tileSize + offY;
		posX = tileX * GameManager.tileSize + offX;
	}

	@Override
	public void render(GameContainer gc, Renderer r) 
	{
		r.drawFillRect((int)posX, (int)posY, 4, 4, 0xffff0000);
	}

}
