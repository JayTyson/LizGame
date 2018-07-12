package com.liz.game.GameManagers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.liz.game.GameUtils.Constants;

/**
 * Created by Jeff on 3/9/2018.
 */

public class AssetController {

    public AssetController() {

    }

    public static void loadAssets(AssetManager assetManager) {
        assetManager.load(Constants.PACK,TextureAtlas.class);
        assetManager.load(Constants.GROUND_FLOOR,Texture.class);
        assetManager.load(Constants.BACKGROUND,Texture.class);
        assetManager.finishLoading();
    }



}
