package com.sasu.engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.sasu.engine.gfx.Font;
import com.sasu.engine.gfx.Image;
import com.sasu.engine.gfx.ImageRequest;
import com.sasu.engine.gfx.ImageTile;

public class Renderer 
{
	private Font font = Font.STANDARD;
	private ArrayList<ImageRequest> imageRequest = new ArrayList<ImageRequest>();
	
	private int pW, pH;
	private int[] p;
	private int[] zb;
	
	private int zDepth = 0;
	private boolean processing = false;
	
	private int clearColor = 0;
	

	
	
	public Renderer(GameContainer gc)
	{
		pW = gc.getWidth();
		pH = gc.getHeight();
		p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
		zb = new int[p.length];
		

	}
	
	public void clear()
	{
		for(int i = 0; i < p.length; i++)
		{
			p[i] = clearColor; 
			zb[i] = 0;
		}
			
	}
	
	public void process()
	{
		processing = true;
		
		Collections.sort(imageRequest, new Comparator<ImageRequest>() {

			@Override
			public int compare(ImageRequest i0, ImageRequest i1) 
			{
				if(i0.zDepth < i1.zDepth)
					return -1;
				if(i0.zDepth > i1.zDepth)
					return 1;
				return 0;
				
			}	
		});
		
		for(int i = 0; i < imageRequest.size(); i++)
		{

			ImageRequest ir = imageRequest.get(i);
			System.out.println(ir.zDepth);
			setzDepth(ir.zDepth);
			ir.image.setAlpha(false);
			drawImage(ir.image, ir.offX, ir.offY);
		}
		
		imageRequest.clear();
		processing = false;
	}
	
	
	public void setPixel(int x, int y, int value)
	{
		int alpha = ((value >> 24) & 0xff);
		
		if((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0)
		{
			return;
		}
		
		int index = x + y * pW;
		
		if(zb[x + y * pW] > zDepth)
		{
			return;
		}
		
		zb[index] = zDepth;
		
		if(alpha == 255)
		{
			p[index] = value;
		}
		else
		{
			int pixelColor = p[index];
			
			//Blend red colors
			int newRed = ((pixelColor >> 16) & 0xff) - (int)((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
			//Blend green colors
			int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
			//Blend blue colors
			int newBlue = (pixelColor & 0xff) - (int)(((pixelColor & 0xff) - (value & 0xff)) * (alpha / 255f));
			
			p[index] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue);
		}
		
	}
	
	
	public void drawText(String text, int offX, int offY, int color)
	{
		
		text = text.toUpperCase();
		int offset = 0;
		
		for(int i = 0; i < text.length(); i++)
		{
			int unicode = text.codePointAt(i) - 32;
			
			for(int y = 0; y < font.getFontImage().getH(); y++)
			{
				for(int x = 0; x < font.getWidths()[unicode]; x++)
				{
					if(font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff)
					{
						setPixel(x + offX + offset, y + offY, color);
					}
				}
			}
			
			offset += font.getWidths()[unicode];
		}
		
	}
	
	
	public void drawImage(Image image, int offX, int offY)
	{		
		
		if(image.isAlpha() && !processing)
		{
			imageRequest.add(new ImageRequest(image, zDepth, offX, offY));
			return;
		}
		
		
		//Don't render if off screen
		if(offX < -image.getW()) return;		
		if(offY < -image.getH()) return;
		if(offX >= pW) return;		
		if(offY >= pH) return;
		
		
		
		int newX = 0;
		int newY = 0;
		int newWidth = image.getW();
		int newHeight = image.getH();
		

		
		//Opposite  width !render
		if(offX < 0)
		{
			newX -= offX;
		}	
		
		//Opposite height !render
		if(offY < 0)
		{
			newY -= offY;
		}
	
		//Clips the image width
		if(newWidth + offX >= pW)
		{
			newWidth -= newWidth + offX - pW;
		}
		
		
		//clips the image height
		if(newHeight + offY >= pH)
		{
			newHeight -= newHeight + offY - pH;
		}
		
		
		for(int y = newY; y < newHeight; y++)
		{
			for(int x = newX; x < newWidth; x++)
			{
				setPixel(x + offX, y + offY, image.getP()[x + y * image.getW()]);
			}
		}
	}
	
	public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY)
	{
		
		if(image.isAlpha() && !processing)
		{
			imageRequest.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offX, offY));
			return;
		}
		
		
		//Don't render if off screen
				if(offX < -image.getTileW()) return;		
				if(offY < -image.getTileH()) return;
				if(offX >= pW) return;		
				if(offY >= pH) return;
				
					
				int newX = 0;
				int newY = 0;
				int newWidth = image.getTileW();
				int newHeight = image.getTileH();
				
				
				//Clipping code
				//Opposite  width !render
				if(offX < 0){newX -= offX;}	
				
				//Opposite height !render
				if(offY < 0){newY -= offY;}
			
				//Clips the image width
				if(newWidth + offX >= pW){newWidth -= newWidth + offX - pW;}
				
				//clips the image height
				if(newHeight + offY >= pH){newHeight -= newHeight + offY - pH;}
				
				
				for(int y = newY; y < newHeight; y++)
				{
					for(int x = newX; x < newWidth; x++)
					{
						setPixel(x + offX, y + offY, image.getP()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getW()]);
					}
				}
	}
	
	public void drawRect(int offX, int offY, int width, int height, int color)
	{

		
		
		for(int y = 0; y <= height; y++)
		{
			setPixel(offX, y + offY, color);
			setPixel(offX + width, y + offY, color);
		}
		
		for(int x = 0; x <= width; x++)
		{
			setPixel(x + offX, offY, color);
			setPixel(x + offX, offY + height, color);
		}
	}
	
	public void drawFillRect(int offX, int offY, int width, int height, int color)
	{
		
		//Don't render if off screen
		if(offX < -width) return;		
		if(offY < -height) return;
		if(offX >= pW) return;		
		if(offY >= pH) return;
		
			
		int newX = 0;
		int newY = 0;
		int newWidth = width;
		int newHeight = height;
		
		
		//Clipping code
		//Opposite  width !render
		if(offX < 0){newX -= offX;}	
		
		//Opposite height !render
		if(offY < 0){newY -= offY;}
	
		//Clips the image width
		if(newWidth + offX >= pW){newWidth -= newWidth + offX - pW;}
		
		//clips the image height
		if(newHeight + offY >= pH){newHeight -= newHeight + offY - pH;}
		
		for(int y = newY; y < newHeight; y++)
		{
			for(int x = newX; x < newWidth; x++)
			{
				setPixel(x + offX, y + offY, color);
			}
		}
		
	}

	public int[] getZb() {
		return zb;
	}

	public void setZb(int[] zb) {
		this.zb = zb;
	}

	public int getzDepth() {
		return zDepth;
	}

	public void setzDepth(int zDepth) {
		this.zDepth = zDepth;
	}
	
	
}







