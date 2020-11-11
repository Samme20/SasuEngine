package com.sasu.game;

import java.awt.event.KeyEvent;

import com.sasu.engine.GameContainer;
import com.sasu.engine.Renderer;
import com.sasu.engine.gfx.ImageTile;

public class Player extends GameObject
{
	private int tileX, tileY;
	private float offX, offY;
	
	private float speed = 300;
	private float fallSpeed = 10;
	private float jump = -3;
	private boolean groundHit = false;
	
	private ImageTile mario_idle;
	float temp = 0;
	
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
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt)
	{
		// Beginning of Left and Right
		mario_idle = new ImageTile("/mario_idle.png", 16, 16);
		
		temp += dt * 3;
		if(temp > 2) temp = 0;
		
		
		// AAAAAAAAAAAAAAAAA
		if(gc.getInput().isKey(KeyEvent.VK_A))
		{
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
		
		// DDDDDDDDDDDDDDDD
		if(gc.getInput().isKey(KeyEvent.VK_D))
		{
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
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) 
	{
		//r.drawFillRect((int)posX, (int)posY, width, height, 0xffff0000);
		r.drawImageTile(mario_idle, (int)posX, (int)posY, (int)temp, 0);
	}

}
