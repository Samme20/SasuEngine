package com.sasu.engine;

public abstract class AbstractGame 
{
	public abstract void update(GameContainer gc, float dt);
	public abstract void renderer(GameContainer gc, Renderer r);
}
