package com.liz.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.liz.game.GameManagers.WorldController;
import com.liz.game.GameManagers.WorldRenderer;

public class LizGame extends ApplicationAdapter {

	WorldController worldController;
	WorldRenderer worldRenderer;

	FPSLogger logger;

	@Override
	public void create () {
		worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController);
        worldController.initCamera(worldRenderer.guiCamera);
        worldController.initWorldCamera(worldRenderer.orthographicCamera);

        logger = new FPSLogger();

	}

	@Override
	public void render () {
	    float delta = Gdx.graphics.getDeltaTime();
	    worldController.update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//logger.log();
		worldRenderer.render();

	}
	
	@Override
	public void dispose () {
		worldRenderer.dispose();
	}
}
