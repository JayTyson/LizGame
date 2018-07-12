package com.liz.game.GameManagers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.liz.game.GameObjects.Bullet;
import com.liz.game.GameObjects.Enemy;
import com.liz.game.GameObjects.EnemyBullet;
import com.liz.game.GameObjects.Floor;
import com.liz.game.GameObjects.Snow;
import com.liz.game.GameObjects.VRock;
import com.liz.game.GameUtils.Constants;

import java.awt.Shape;

/**
 * Created by Jeff on 3/8/2018.
 */

public class WorldRenderer implements Disposable {

    public OrthographicCamera orthographicCamera;
    public OrthographicCamera guiCamera;
    public static SpriteBatch spriteBatch;
    public Viewport viewport;

    BitmapFont font;
    BitmapFont widthFont;
    BitmapFont heightFont;

    public WorldController worldController;

    public ShapeRenderer shapeRenderer;
    public ShapeRenderer shape;
    public ShapeRenderer sRender;


    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    public void init() {
        spriteBatch = new SpriteBatch();
        orthographicCamera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
        orthographicCamera.position.set(orthographicCamera.viewportWidth/2,orthographicCamera.viewportHeight/2,0);
        orthographicCamera.rotate(0);

        shapeRenderer = new ShapeRenderer();
        shape = new ShapeRenderer();
        sRender = new ShapeRenderer();

        //System.out.println(orthographicCamera.viewportWidth + " " + orthographicCamera.viewportHeight);

        guiCamera = new OrthographicCamera();
        guiCamera.setToOrtho(false);
        font = new BitmapFont();
        widthFont = new BitmapFont();
        heightFont = new BitmapFont();
        font.getData().setScale(5,3);
        widthFont.getData().setScale(5,3);
        heightFont.getData().setScale(5,3);
        viewport = new FitViewport(800,480,orthographicCamera);
    }

    public void render() {
        renderWorld();
        renderShapes();
        renderGui();
    }

    private void renderShapes() {
        /*shapeRenderer.setProjectionMatrix(orthographicCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(Floor floor:worldController.floors) {
            shapeRenderer.setColor(1,0,0,1);
            shapeRenderer.rect(floor.floorRec.getX(),floor.floorRec.getY(),floor.floorRec.getWidth(),floor.floorRec.getHeight());
        }

        shapeRenderer.setColor(0,1,0,1);
        shapeRenderer.rect(worldController.liz.lizRec.getX(),worldController.liz.lizRec.getY(),
                worldController.liz.lizRec.getWidth(),worldController.liz.lizRec.getHeight());

        shapeRenderer.setColor(0,1,0,1);
        shapeRenderer.rect(worldController.leftTriggerRec.getX(),worldController.leftTriggerRec.getY(),
                worldController.leftTriggerRec.getWidth(),worldController.leftTriggerRec.getHeight());

        shapeRenderer.setColor(0,1,0,1);
        shapeRenderer.rect(worldController.rightTriggerRec.getX(),worldController.rightTriggerRec.getY(),
                worldController.rightTriggerRec.getWidth(),worldController.rightTriggerRec.getHeight());
        for(Enemy enemy:worldController.enemies) {

            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.rect(enemy.enemyRec.getX(), enemy.enemyRec.getY(),
                    enemy.enemyRec.getWidth(), enemy.enemyRec.getHeight());

        }

        for(Bullet bullet:worldController.bullets) {
            shapeRenderer.setColor(1,1,1,1);
            shapeRenderer.rect(bullet.bulletSprite.getX(),bullet.bulletSprite.getY(),
                    bullet.bulletSprite.getWidth(),bullet.bulletSprite.getHeight());

        }

        for(Bullet bullet:worldController.enemyBullets) {
            shapeRenderer.setColor(1,0.5f,1,1);
            shapeRenderer.rect(bullet.bulletSprite.getX(),bullet.bulletSprite.getY(),
                    bullet.bulletSprite.getWidth(),bullet.bulletSprite.getHeight());
        }

        shapeRenderer.setColor(0.5f,0.4f,0.8f,1);
        shapeRenderer.rect(orthographicCamera.position.x-orthographicCamera.viewportWidth/2,orthographicCamera.position.y-orthographicCamera.viewportHeight/2,
                orthographicCamera.viewportWidth,orthographicCamera.viewportHeight);

        shapeRenderer.setColor(1f,1,1,1);
        shapeRenderer.rect(MathUtils.random(orthographicCamera.position.x-orthographicCamera.viewportWidth/2,
                orthographicCamera.position.x+orthographicCamera.viewportWidth/2),
                0,
                0.08f,0.05f);



        shapeRenderer.end();*/

        shape.setProjectionMatrix(orthographicCamera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        /*for (Snow snow:worldController.snows) {
            shape.setColor(1,1,1,1);
            shape.rect(snow.sprite.getX(),snow.sprite.getY(),
                    snow.sprite.getWidth(),snow.sprite.getHeight());
        }*/

        shape.end();

        //GUI Rectangles code
        /*sRender.setProjectionMatrix(guiCamera.combined);
        sRender.begin(ShapeRenderer.ShapeType.Line);

        sRender.setColor(1,1,1,1);
        sRender.rect(worldController.leftRec.getX(),worldController.leftRec.getY(),
                worldController.leftRec.getWidth(),worldController.leftRec.getHeight());

        sRender.setColor(1,1,1,1);
        sRender.rect(worldController.rightRec.getX(),worldController.rightRec.getY(),
                worldController.rightRec.getWidth(),worldController.rightRec.getHeight());

        sRender.setColor(1,1,1,1);
        sRender.rect(worldController.shootRec.getX(),worldController.shootRec.getY(),
                worldController.shootRec.getWidth(),worldController.shootRec.getHeight());

        sRender.setColor(1,1,1,1);
        sRender.rect(worldController.meleeRec.getX(),worldController.meleeRec.getY(),
                worldController.meleeRec.getWidth(),worldController.meleeRec.getHeight());

        sRender.setColor(1,1,1,1);
        sRender.rect(worldController.jumpRec.getX(),worldController.jumpRec.getY(),
                worldController.jumpRec.getWidth(),worldController.jumpRec.getHeight());
        sRender.end();*/

    }

    private void renderWorld() {
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        worldController.cameraHelper.applyTo(orthographicCamera);
        spriteBatch.begin();
        //worldController.backSprite.draw(spriteBatch);
        worldController.moon.renderMoon(spriteBatch);
        worldController.treeSprite.draw(spriteBatch);
        for(Bullet bullet:worldController.bullets) {
            bullet.renderBullet(spriteBatch);
        }
        worldController.liz.renderLiz(spriteBatch);

        for(Enemy e:worldController.enemies) {
            e.renderEnemy(spriteBatch);
        }

//        for(VRock rock:worldController.vRocks) {
//            rock.renderVRock(spriteBatch);
//        }

        for(Floor floor:worldController.floors) {
            floor.renderFloor(spriteBatch);
        }
        spriteBatch.end();
    }

    private void renderGui() {
        spriteBatch.setProjectionMatrix(guiCamera.combined);
        spriteBatch.begin();

        font.draw(spriteBatch,"FPS: " + Gdx.graphics.getFramesPerSecond(),0,Gdx.graphics.getHeight()*0.99f);
        widthFont.draw(spriteBatch,"WIDTH: " + Gdx.graphics.getWidth(),0,Gdx.graphics.getHeight()*0.9f);
        heightFont.draw(spriteBatch,"HEIGHT: " + Gdx.graphics.getHeight(),0,Gdx.graphics.getHeight()*0.8f);

        worldController.leftArrowSprite.draw(spriteBatch);
        worldController.rightArrowSprite.draw(spriteBatch);
        worldController.shootSprite.draw(spriteBatch);
        worldController.meleeSprite.draw(spriteBatch);
        worldController.jumpSprite.draw(spriteBatch);

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        worldController.dispose();
        font.dispose();
        widthFont.dispose();
        heightFont.dispose();
    }

    public static SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

}
