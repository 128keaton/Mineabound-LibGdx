package com.tycoon177.mineabound.screens;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.tycoon177.mineabound.MineaboundLauncher;
import com.tycoon177.mineabound.entities.Player;
import com.tycoon177.mineabound.utils.MineaboundInputProcessor;
import com.tycoon177.mineabound.world.ChunkHandler;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


public class GameWorld implements Screen {
	private static OrthographicCamera camera;
	private SpriteBatch renderer;
	public float colorBlueDay;
	public float colorRedDay;
	public float colorGreenDay;
	public float colorDuration;
	public float finalGreen;
	public float finalRed;
	public float finalBlue;
	public float colorRedNight;
	public float colorBlueNight;
	public float colorGreenNight;
	public ArrayList<Integer> canMoveToNight;
	public boolean shouldMoveToNight;
	private ShapeRenderer debugRenderer;
	public static Player player;
	private ChunkHandler chunkHandler;
	private MineaboundInputProcessor inputProcessor;
	public static GameWorld world;
	public static final float VIEWPORT_SIZE = 20;
	private Music song;

	@Override
	public void show() {

		song = Gdx.audio.newMusic(Gdx.files.internal("C418 - Atempause.mp3"));
		if (song != null) {
			song.setVolume(.1f);
			song.setLooping(true);
			song.play();
		}

		GameWorld.world = this;
		renderer = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 10, 10);
		player = new Player();
		canMoveToNight = new ArrayList<Integer>();

		colorRedDay = 0.110f;
		colorGreenDay = 0.60f;
		colorBlueDay = 0.80f;
		colorRedNight = 0.0f;
		colorGreenNight = 0.0f;
		colorBlueNight = 0.100f;
		player.setPosition(0, 100);
		colorDuration = 0.128f;
		shouldMoveToNight = false;
		createInputProcessor();
		chunkHandler = new ChunkHandler();
		debugRenderer = new ShapeRenderer();
		
	

	}








	private void createInputProcessor() {
		Gdx.input
				.setInputProcessor((inputProcessor = new MineaboundInputProcessor(
						this)));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(finalRed, finalGreen, finalBlue, 1);
		// Gdx.gl.glClearColor(0.0f,0.0f,0.100f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		removeAllBodies();
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin();
		chunkHandler.render(renderer);
		player.draw(renderer, player.getDirection());
		renderer.end();

		if (MineaboundLauncher.IS_DEBUG_RENDERING) {
			debugRenderer.setProjectionMatrix(camera.combined);
			// Gdx.gl.glLineWidth(2f);
			debugRenderer.begin(ShapeType.Line);
			debugRenderer
					.setColor(MineaboundLauncher.PLAYER_BOUNDING_BOX_COLOR);
			player.debugRender(debugRenderer);
			debugRenderer.setColor(MineaboundLauncher.BLOCK_BOUNDING_BOX_COLOR);
			chunkHandler.debugRender(debugRenderer);
			debugRenderer.end();
		}
		update(delta);
	}

	 public void handleClock() {

	
			java.awt.EventQueue.invokeLater(new Runnable() {

				public void run() {

					if (shouldMoveToNight == false) {
						if (finalBlue < colorBlueDay) {
							finalBlue = finalBlue + 0.04f;
						} else {
							canMoveToNight.add(0);
						}
						if (finalRed < colorRedDay) {
							finalRed = finalRed + 0.06f;
						} else {
							canMoveToNight.add(1);
						}
						if (finalGreen < colorGreenDay) {
							finalGreen = finalGreen + 0.04f;
						} else {
							canMoveToNight.add(2);
						}

					} else if (shouldMoveToNight == true) {
						if (finalBlue > colorBlueNight) {
							finalBlue = finalBlue - 0.0001f;
							
						}else {
							canMoveToNight.add(0);

						}
						
						if (finalRed > colorRedNight) {
							finalRed = finalRed - 0.0001f;
							
						}else {
							canMoveToNight.add(1);

						}
						
						if (finalGreen > colorGreenNight) {

							finalGreen = finalGreen - 0.0001f;
						} else {
							canMoveToNight.add(2);

						}

					}
					System.out.println(canMoveToNight.size());

					if (canMoveToNight.size() > 10) {
						if (shouldMoveToNight == false) {
							canMoveToNight.removeAll(canMoveToNight);
							
							shouldMoveToNight = true;
							
							System.out.println("Should go to night");
						} else if (shouldMoveToNight == true) {
							canMoveToNight.removeAll(canMoveToNight);
							shouldMoveToNight = false;
							System.out.println("Should go to day");
						}
						canMoveToNight.removeAll(canMoveToNight);
					}

					System.out.println(finalBlue);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			
	}

	private void removeAllBodies() {
		// chunkHandler.removeAllChunksFromWorld();
	}

	private void update(float delta) {
		player.update(delta);
		camera.position.set(player.getPosition(), 0);
		chunkHandler.update();
		handleClock();
		

		// cameraZoom();
	}

	private void cameraZoom() {
		if (Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)) {
			camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DEL)) {
			camera.rotate(-.5f, 0, 0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.END)) {
			camera.rotate(.5f, 0, 0, 1);
		}

		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f,
				100 / camera.viewportWidth);

	}

	@Override
	public void resize(int width, int height) {
		// 80x80 = 1x1
		// camera.setToOrtho(false, width / 80, height / 80);
		camera.viewportWidth = VIEWPORT_SIZE;
		camera.viewportHeight = VIEWPORT_SIZE * height / width;

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		renderer.dispose();
		song.stop();
		song.dispose();
	}

	public OrthographicCamera getCamera() {

		return camera;
	}

	public Player getPlayer() {

		return player;
	}

	public MineaboundInputProcessor getInputProcessor() {
		return inputProcessor;
	}

	public ChunkHandler getChunkHandler() {
		return chunkHandler;
	}
}
