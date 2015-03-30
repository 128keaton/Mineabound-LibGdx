package com.tycoon177.mineabound.entities;

import com.badlogic.gdx.math.Vector2;
import com.tycoon177.mineabound.utils.LoadedTextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Player extends LivingEntity {
	/** The default player size **/
	private Vector2 playerSize = new Vector2(.8f, 1.8f);

	public ShapeRenderer shapeRenderer;
	

	public Player() {
		
		
		super();
		shapeRenderer = new ShapeRenderer();
		 
		 shapeRenderer.begin(ShapeType.Filled);
		 shapeRenderer.setColor(0, 1, 0, 1);
		 shapeRenderer.rect(10, 5, 16, 20);

		 shapeRenderer.end();
		 
		setSize(playerSize.x -.2f, playerSize.y);
		this.setSprite(LoadedTextureAtlas.blockAtlas.createSprite("playerStill"));
	}
}
