package com.tycoon177.mineabound.utils;

import java.util.Date;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.tycoon177.mineabound.entities.Player;
import com.tycoon177.mineabound.screens.GameWorld;
import com.tycoon177.mineabound.world.Block;
import com.tycoon177.mineabound.world.BlockType;

public class MineaboundInputProcessor implements InputProcessor {

	private GameWorld world;
	private final float forceX = .1f;
	private Vector2 lastTouchedPoint;
	public BlockType blockType;
	public int finalScroll;
	public MineaboundInputProcessor(GameWorld world) {
		this.world = world;
	//	blockType = new BlockType();
		blockType = BlockType.SLIME;
		lastTouchedPoint = new Vector2();
		finalScroll = 0;
	}

	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Keys.A:
				world.getPlayer().setXVelocity(-forceX);
				world.getPlayer().setDirection(Player.LEFT);
				break;
			case Keys.D:
				world.getPlayer().setXVelocity(forceX);
				world.getPlayer().setDirection(Player.RIGHT);
				break;
			case Keys.W:
			case Keys.SPACE:
				if (world.getPlayer().canJump())
					world.getPlayer().jump();// characterMovement.y = forceY;
				break;
			case Keys.S:
				// characterMovement.y = -forceY;
				break;
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Keys.A:
			case Keys.D:
				world.getPlayer().setXVelocity(0.5f);
				long start = new Date().getTime();
				while(new Date().getTime() - start < 50L){	world.getPlayer().setXVelocity(0);}

				
			
				break;
			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		final Vector3 touchLocation = new Vector3(screenX + .5f, screenY + .5f, 0);
		Vector3 onScreenLoc = world.getCamera().unproject(touchLocation); // In world point
		this.lastTouchedPoint.set(new Vector2(onScreenLoc.x, onScreenLoc.y));
		if (this.world.getPlayer().getDistanceFromPoint(onScreenLoc.x, onScreenLoc.y) < 5) {
			if (button == Buttons.LEFT){
				
				for(Block border : this.world.getChunkHandler().getVisibleBlocks()){
						if(border.getBlockType() == BlockType.BEDROCK){
							
						}else{
							this.world.getChunkHandler().removeBlock(touchLocation);
						}
				}
				
				
		}
			else
				if (button == Buttons.RIGHT) {
					this.world.getChunkHandler().addBlock(onScreenLoc, blockType);
					System.out.print("place");
				}
		}
		
		System.out.println(this.world.getPlayer().getDistanceFromPoint(lastTouchedPoint));
		return true;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		final Vector3 touchLocation = new Vector3(screenX + .5f, screenY + .5f, 0);
		Vector3 onScreenLoc = world.getCamera().unproject(touchLocation); // In world point
	//	this.lastTouchedPoint.set(new Vector2(onScreenLoc.x, onScreenLoc.y));
		
		
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {


			if (finalScroll > -1 && finalScroll < 10) {
				finalScroll = finalScroll + amount;
				
				
		
				System.out.println("UP");
			}else{
				finalScroll = 0;
			}
			
	
	
		
		  switch (finalScroll) {
		  case 0: blockType = BlockType.SLIME;
		  	break;
		  case 1: blockType = BlockType.STONE;
		  	break;
		  case 2: blockType = BlockType.DIRT;
		  	break;
		  case 3: blockType = BlockType.GRASS;
		  	break;
		  case 4: blockType = BlockType.BEDROCK;
		  	break;
		  
		  	
		  
		  }
			System.out.println(finalScroll);
		
			
		return true;
	}

	public Vector2 getLastTouchedPoint() {
		return lastTouchedPoint;
	}

}
