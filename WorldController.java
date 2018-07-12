package com.liz.game.GameManagers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.liz.game.GameObjects.Bullet;
import com.liz.game.GameObjects.Enemy;
import com.liz.game.GameObjects.EnemyBullet;
import com.liz.game.GameObjects.Floor;
import com.liz.game.GameObjects.Liz;
import com.liz.game.GameObjects.Moon;
import com.liz.game.GameObjects.Snow;
import com.liz.game.GameObjects.VRock;
import com.liz.game.GameUtils.CameraHelper;
import com.liz.game.GameUtils.Constants;

import java.util.Iterator;

import javax.xml.soap.Text;

/**
 * Created by Jeff on 3/8/2018.
 */

public class WorldController extends InputAdapter {

    private TextureAtlas textureAtlas;
    private AssetManager assetManager;

    private TextureRegion lizRunRegion;
    private TextureRegion lizIdleRegion;
    private TextureRegion lizMeleeRegion;
    private TextureRegion lizShootRegion;
    private TextureRegion lizJumpTexture;

    private Texture controlsTexture;
    public Sprite leftArrowSprite;
    public Rectangle leftRec;

    public Sprite rightArrowSprite;
    public Rectangle rightRec;

    public Sprite shootSprite;
    public Rectangle shootRec;

    public Sprite meleeSprite;
    public Rectangle meleeRec;

    public Sprite jumpSprite;
    public Rectangle jumpRec;

    private Texture backTexture;
    public Sprite backSprite;

    private Texture groundTexture;
    //public Sprite groundSprite;

    private Texture treeTexture;
    public Sprite treeSprite;

    public Rectangle leftTriggerRec;
    public Rectangle rightTriggerRec;

    public Array<Floor> floors;
    public Array<Bullet> bullets;
    public Array<Snow> snows;
    public Array<Bullet> enemyBullets;
    public Array<Enemy> enemies;
    public Array<VRock> vRocks;

    private boolean leftTouched;
    private boolean rightTouched;
    private boolean shootTouched;
    private boolean meleeTouched;
    private boolean jumpTouched;

    private static final float BUTTON_SIZE_FACTOR = 860f;
    private static final int NUM_OF_ENEMIES = 10;
    private static final float BULLET_SPEED = 40f;

    public Liz liz;
    public Moon moon;

    public float timep;
    public float timel = 0.15f;

    public float stimep;
    public float stimel = 0.08f;

    public float etimep;
    public float etimel = 0.25f;

    public float rtimep;
    public float rtimel = 2.28f;

    private float shakeDuration;
    private float shakeCounter;
    private boolean isCameraShaking;

    private Vector3 temp;
    private OrthographicCamera camera;
    private OrthographicCamera worldCam;

    public CameraHelper cameraHelper;

    public enum GAME_STATE{Running,Paused};
    public GAME_STATE gameState = GAME_STATE.Running;

    private boolean hit;

    public WorldController() {
        init();
    }

    private void init() {
        assetManager = new AssetManager();
        AssetController.loadAssets(assetManager);

        getAssets();
        initControls();

        shakeDuration = 1f;
        shakeCounter = 0;
        isCameraShaking = false;

        leftTriggerRec = new Rectangle();
        rightTriggerRec = new Rectangle();
        leftTriggerRec.set(28,1.5f,1,1);
        rightTriggerRec.set(40,1.5f,1,1);

        liz = new Liz(lizRunRegion,lizIdleRegion,lizMeleeRegion,lizShootRegion,lizJumpTexture);
        moon = new Moon(10f,10f);
        //enemy = new Enemy();

        temp = new Vector3();
        cameraHelper = new CameraHelper();
        cameraHelper.setTarget(liz.sprite);

        backSprite = new Sprite(backTexture);
        backSprite.setSize(70,10);
        backSprite.setPosition(0,0);

        treeTexture = new Texture("tree.png");
        treeSprite = new Sprite(treeTexture);
        treeSprite.setSize(10,10);
        treeSprite.setPosition(20,0);

        floors = new Array<Floor>();
        for (int i = 0;i < 4;i++) {
            floors.add(new Floor(groundTexture));
        }

        for (int i = 0;i < floors.size;i++) {
            floors.get(i).sprite.setPosition(i * (floors.get(i).sprite.getWidth()-0.2f),0);
        }

        enemies = new Array<Enemy>();
        for(int i = 0;i < NUM_OF_ENEMIES;i++) {
            enemies.add(new Enemy());
        }

        for (int i = 0;i < enemies.size;i++) {
            enemies.get(i).position.set(30+(i*2),1.4f);
        }

        vRocks = new Array<VRock>();
        bullets = new Array<Bullet>();
        enemyBullets = new Array<Bullet>();
        snows = new Array<Snow>();

        Gdx.input.setInputProcessor(this);
    }

    private void initControls() {
        controlsTexture = new Texture("contols_btn.png");
        leftArrowSprite = new Sprite(controlsTexture);
        leftArrowSprite.setPosition(50,0);
        leftArrowSprite.setSize(leftArrowSprite.getWidth() * (Gdx.graphics.getWidth()/BUTTON_SIZE_FACTOR),
                leftArrowSprite.getHeight() * (Gdx.graphics.getWidth()/BUTTON_SIZE_FACTOR));
        leftRec = new Rectangle();
        leftRec.set(leftArrowSprite.getX()-30,leftArrowSprite.getY(),leftArrowSprite.getWidth()+60,leftArrowSprite.getHeight()+25);

        rightArrowSprite = new Sprite(controlsTexture);
        rightArrowSprite.setPosition(leftArrowSprite.getX() + leftArrowSprite.getWidth()+60,0);
        rightArrowSprite.setSize(rightArrowSprite.getWidth() * (Gdx.graphics.getWidth()/BUTTON_SIZE_FACTOR),
                rightArrowSprite.getHeight() * (Gdx.graphics.getWidth()/BUTTON_SIZE_FACTOR));
        rightRec = new Rectangle();
        rightRec.set(rightArrowSprite.getX()-30,rightArrowSprite.getY(),rightArrowSprite.getWidth()+60,
                rightArrowSprite.getHeight()+25);

        shootSprite = new Sprite(controlsTexture);
        shootSprite.setPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth()*0.16f),0);
        shootSprite.setSize(shootSprite.getWidth() * (Gdx.graphics.getWidth()/BUTTON_SIZE_FACTOR),
                shootSprite.getHeight() * (Gdx.graphics.getWidth()/BUTTON_SIZE_FACTOR));
        shootRec = new Rectangle();
        shootRec.set(shootSprite.getX(),shootSprite.getY(),shootSprite.getWidth()+60,shootSprite.getHeight()+10);

        meleeSprite = new Sprite(controlsTexture);
        meleeSprite.setPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth()*0.25f),0);
        meleeSprite.setSize(meleeSprite.getWidth() * (Gdx.graphics.getWidth()/BUTTON_SIZE_FACTOR),
                meleeSprite.getHeight() * (Gdx.graphics.getWidth()/BUTTON_SIZE_FACTOR));
        meleeRec = new Rectangle();
        meleeRec.set(meleeSprite.getX()-55,meleeSprite.getY(),meleeSprite.getWidth()+60,meleeSprite.getHeight()+10);

        jumpSprite = new Sprite(controlsTexture);
        jumpSprite.setPosition(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth()*0.21f),Gdx.graphics.getHeight() * 0.15f);
        jumpSprite.setSize(jumpSprite.getWidth() * (Gdx.graphics.getWidth()/BUTTON_SIZE_FACTOR),
                jumpSprite.getHeight() * (Gdx.graphics.getWidth()/BUTTON_SIZE_FACTOR));
        jumpRec = new Rectangle();
        jumpRec.set(jumpSprite.getX()-50,jumpSprite.getY(),jumpSprite.getWidth()+100,jumpSprite.getHeight()+18);
    }

    private void getAssets() {
        textureAtlas = assetManager.get(Constants.PACK);
        lizRunRegion = textureAtlas.findRegion(Constants.LIZ_RUN_SPRITE_SHEET);
        lizIdleRegion = textureAtlas.findRegion(Constants.LIZ_IDLE_SPRITE_SHEET);
        lizMeleeRegion = textureAtlas.findRegion(Constants.LIZ_MELEE_SPRITE_SHEET);
        lizShootRegion = textureAtlas.findRegion(Constants.LIZ_SHOOT_SPRITE_SHEET);
        lizJumpTexture = textureAtlas.findRegion(Constants.LIZ_JUMP_SPRITE);

        groundTexture = assetManager.get(Constants.GROUND_FLOOR);
        backTexture = assetManager.get(Constants.BACKGROUND);
    }

    private void shootBullet(float delta) {
        timep += delta;
        Bullet bullet = new Bullet();
        bullet.bulletSprite.setSize(0.55f,0.145f);
        //liz.sprite.getY()+(liz.sprite.getHeight()*0.455f);
        if(liz.direction == Liz.DIRECTION.Right) {
            bullet.position.set(liz.sprite.getX()+(liz.sprite.getWidth()*0.55f),
                    MathUtils.random(liz.sprite.getY()+(liz.sprite.getHeight()*0.52f),liz.sprite.getY()+(liz.sprite.getHeight()*0.38f)));
            bullet.velocity.x = BULLET_SPEED;
            bullet.isFlipped = false;

        } else {
            bullet.position.set(liz.sprite.getX()+(liz.sprite.getWidth()*0.02f),
                    MathUtils.random(liz.sprite.getY()+(liz.sprite.getHeight()*0.52f),liz.sprite.getY()+(liz.sprite.getHeight()*0.38f)));
            bullet.velocity.x = -BULLET_SPEED;
            bullet.isFlipped = true;
        }

        if(timep > timel) {
            bullets.add(bullet);
            timep = 0;
        }
        //System.out.println(2*0.8f);
    }

    private void shootEnemyBullet(float delta) {
        etimep += delta;
        Bullet bullet = new Bullet();
        bullet.bulletSprite.setSize(0.07f,0.02f);
        for(int i = 0;i < enemies.size;i++) {
            bullet.position.set(enemies.get(i).enemySprite.getX() + 0.4f, enemies.get(i).enemySprite.getY() + (enemies.get(i).enemySprite.getHeight() * 0.33f));
            if (enemies.get(i).direction == Enemy.DIRECTION.Right) {
                bullet.velocity.x = 14f;
            } else {
                bullet.velocity.x = -14f;
            }
        }

        if(etimep > etimel) {
            enemyBullets.add(bullet);
            etimep = 0;
        }
    }

    private void spawnSnow(float delta) {
        stimep += delta;
        Snow snow = new Snow();
        snow.sprite.setSize(MathUtils.random(0.02f,0.12f),MathUtils.random(0.02f,0.09f));
        snow.position.set(MathUtils.random(worldCam.position.x-worldCam.viewportWidth*0.9f,
                worldCam.position.x+worldCam.viewportWidth*0.95f),10);
        snow.sprite.rotate(0.5f);
        snow.velocity.y = -2.05f;

        if(stimep > stimel) {
            snows.add(snow);
            stimep = 0;
        }
    }

    private void dropVRocks(float delta) {
        rtimep += delta;
        VRock vrock = new VRock();
        vrock.rockSprite.setSize(MathUtils.random(2f,4f),2f);
        vrock.position.set(MathUtils.random(worldCam.position.x-worldCam.viewportWidth*0.5f,worldCam.position.x+worldCam.viewportWidth*0.55f),
                worldCam.position.y + (worldCam.viewportHeight));
        vrock.rockSprite.rotate(MathUtils.random(1.0f,3.0f));
        vrock.velocity.y = -2.0f;

        if(rtimep > rtimel) {
            vRocks.add(vrock);
            rtimep = 0;
        }
    }

    public void update(float delta) {
        switch(gameState) {
            case Running:
                gameplayUpdate(delta);
                break;
            case Paused:
                if(Gdx.input.justTouched()) {
                    gameState = GAME_STATE.Running;
                }
                break;
        }
    }

    private void gameplayUpdate(float delta) {
        inputControl(delta);
        //spawnSnow(delta);
        //dropVRocks(delta);

        moon.updateMoon(delta);

       /* for(int i = 0;i < vRocks.size;i++) {
            vRocks.get(i).updateVRock(delta);
            if(vRocks.get(i).rockSprite.getY() < 0) {
                vRocks.removeIndex(i);
            }
        }*/

        for(Enemy e:enemies) {
            e.updateEnemy(delta);
        }

        for(Floor floor:floors) {
            floor.update();
        }
        liz.updateLiz(delta);

        cameraHelper.update();
        if(checkLizFloorCollision()) {
            liz.velocity.y = 0;
            liz.position.y = 1.5f;
            liz.colliding = true;
            liz.lizRec.set(liz.sprite.getX()+liz.sprite.getWidth()*0.25f,liz.sprite.getY(),
                    liz.sprite.getWidth()*0.5f,liz.sprite.getHeight()*0.15f);
        } else {
            liz.colliding = false;
        }


        for(int i = 0;i < enemies.size;i++) {
            if(checkEnemyRightTriggerCollision(i)) {
                enemies.get(i).direction = Enemy.DIRECTION.Left;
                enemies.get(i).velocity.x = -0.02f;
            }
        }


        for(int i = 0;i < enemies.size;i++) {
            if(checkEnemyLeftTriggerCollision(i)) {
                enemies.get(i).direction = Enemy.DIRECTION.Right;
                enemies.get(i).velocity.x = 0.02f;
            }
        }


        /*shootEnemyBullet(delta);
        for (int i = 1;i < enemyBullets.size;i++) {
            enemyBullets.get(i).bulletUpdate(delta);
        }*/

        for(int enemyIndex = 0;enemyIndex < enemies.size;enemyIndex++) {
            for(int bulletIndex = 0;bulletIndex < bullets.size;bulletIndex++) {
                if (checkBulletEnemyCollision(enemyIndex,bulletIndex)) {
                    hit = true;
                    isCameraShaking = true;
                    shakeDuration += 0.05f;
                    enemies.get(enemyIndex).enemyRec.set(0, 0, 0, 0);
                    enemies.get(enemyIndex).isAlive = false;
                    bullets.removeIndex(bulletIndex);

                }
            }
        }

        for (int i = 1; i < bullets.size; i++) {
            bullets.get(i).bulletUpdate(delta);
            if ((bullets.get(i).bulletSprite.getX() > worldCam.position.x + worldCam.viewportWidth*0.5f)
                    || (bullets.get(i).bulletSprite.getX() < worldCam.position.x - worldCam.viewportWidth*0.5f)
                    ) {
                bullets.removeIndex(i);
            }
        }


        /*for (int i = 1;i < snows.size;i++) {
            snows.get(i).updateSnow(delta);
            if (snows.get(i).sprite.getY() < 0) {
                snows.removeIndex(i);
            }
        }*/

        if(Gdx.input.justTouched()) {
            //gameState = GAME_STATE.Paused;
        }

        moonMotion(delta);
    }

    public void inputControl(float delta) {
        if(meleeTouched) {
            liz.isTouched = true;
        } else {
            liz.isTouched = false;
        }

        if(rightTouched) {
            liz.walkRightTouched = true;
        } else {
            liz.walkRightTouched = false;
            moon.velocity.x = 0;
        }

        if(leftTouched) {
            liz.walkLeftTouched = true;

        } else {
            liz.walkLeftTouched = false;
        }

        if(shootTouched) {
            liz.shootTouched = true;
        } else {
            liz.shootTouched = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cameraHelper.setZoom(delta * 5);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cameraHelper.setZoom(delta * -5);
        } else if(Gdx.input.isKeyPressed(Input.Keys.R)) {
            cameraHelper.resetZoom(1.0f);
        }


        if (shootTouched && !rightTouched) {
            if(!leftTouched) {
                if(liz.colliding) {
                    shootBullet(delta);
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if(!liz.colliding) {
                return;
            }
            liz.velocity.y = liz.jumpV;
        }


        if(isCameraShaking) {
            shakeCounter += delta;
            if(shakeCounter <= shakeDuration) {
                cameraHelper.shakeCamera(worldCam);
            } else if(shakeCounter > shakeDuration) {
                shakeCounter = 0;
                isCameraShaking = false;
            }
        }

        //System.out.println(shakeCounter);
        //System.out.println(isCameraShaking);



        //System.out.println(isCameraShaking);

        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            init();
        }
    }

    private void moonMotion(float delta) {
        moon.position.x = cameraHelper.position.x - 5.5f;
    }

    private boolean checkLizFloorCollision() {
        for(Floor floor:floors) {
            if(Intersector.intersectRectangles(liz.lizRec, floor.floorRec, liz.lizRec)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkEnemyRightTriggerCollision(int j) {
        for(int i = 0;i < enemies.size;i++) {
            if (Intersector.intersectRectangles(rightTriggerRec, enemies.get(j).enemyRec, enemies.get(j).enemyRec)) {
                return true;
            }
        }
        return  false;
    }

    private boolean checkEnemyLeftTriggerCollision(int j) {
        for(int i = 0;i < enemies.size;i++) {
            if (Intersector.intersectRectangles(leftTriggerRec, enemies.get(j).enemyRec, enemies.get(j).enemyRec)) {
                return true;
            }
        }
        return  false;
    }

    private boolean checkBulletEnemyCollision(int enemyIndex,int bulletIndex) {
        if (Intersector.intersectRectangles(bullets.get(bulletIndex).bulletRec, enemies.get(enemyIndex).enemyRec,
                enemies.get(enemyIndex).enemyRec)) {
            return true;
        }

        return false;
    }


    public void dispose(){
        assetManager.clear();
        textureAtlas.dispose();
        floors.clear();
    }



    @Override
    public boolean touchDown(int screenX,int screenY,int button,int pointer) {
        temp.set(screenX,screenY,0);
        camera.unproject(temp);
        float x = temp.x;
        float y = temp.y;
        if(isLeftTouched(x,y)) {
            leftTouched = true;
        }

        if(isRightTouched(x,y)) {
            rightTouched = true;
        }

        if(isShootTouched(x,y)) {
            shootTouched = true;
        }

        if(isMeleeTouched(x,y)) {
            meleeTouched = true;
        }
        if(isJumpTouched(x,y)) {
            if(liz.colliding) {
                liz.velocity.y = liz.jumpV;
            }
        }
        return  false;
    }

    @Override
    public boolean touchUp(int screenX,int screenY,int button,int pointer) {
        temp.set(screenX,screenY,0);
        camera.unproject(temp);
        float x = temp.x;
        float y = temp.y;
        if(isLeftTouched(x,y)) {
            leftTouched = false;
        }

        if(isRightTouched(x,y)) {
            rightTouched = false;
        }

        if(isShootTouched(x,y)) {
            shootTouched = false;
        }

        if(isMeleeTouched(x,y)) {
            meleeTouched = false;
        }

        if(isJumpTouched(x,y)) {
            jumpTouched = false;
        }
        return  false;
    }

    @Override
    public boolean keyDown(int key) {
        if(Input.Keys.A == key) {
            leftTouched = true;
        }

        if(Input.Keys.D == key) {
            rightTouched = true;
        }

        if(Input.Keys.L == key) {
            shootTouched = true;
        }

        if(Input.Keys.K == key) {
            meleeTouched = true;
        }
        if(Input.Keys.S == key) {
            isCameraShaking = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int key) {
        if(Input.Keys.A == key) {
            leftTouched = false;
        }

        if(Input.Keys.D == key) {
            rightTouched = false;
        }

        if(Input.Keys.L == key) {
            shootTouched = false;
        }

        if(Input.Keys.K == key) {
            meleeTouched = false;
        }
        return false;
    }

    private boolean isLeftTouched(float x,float y) {
        if((x >= leftRec.getX() && x <= leftRec.getX() + leftRec.getWidth()) &&
                y >= leftRec.getY() && y <= leftRec.getY() + leftRec.getHeight()) {
            return true;
        }
        return false;
    }

    private boolean isRightTouched(float x,float y) {
        if((x >= rightRec.getX() && x <= rightRec.getX() + rightRec.getWidth()) &&
                y >= rightRec.getY() && y <= rightRec.getY() + rightRec.getHeight()) {
            return true;
        }
        return false;
    }

    private boolean isShootTouched(float x,float y) {
        if((x >= shootRec.getX() && x <= shootRec.getX() + shootRec.getWidth()) &&
                (y >= shootRec.getY() && y <= shootRec.getY() + shootRec.getHeight())) {
            return true;
        }
        return false;
    }

    private boolean isMeleeTouched(float x,float y) {
        if((x >= meleeRec.getX() && x <= meleeRec.getX() + meleeRec.getWidth()) &&
                (y >= meleeRec.getY() && y <= meleeRec.getY() + meleeRec.getHeight())) {
            return true;
        }
        return false;
    }

    private boolean isJumpTouched(float x,float y) {
        if((x >= jumpRec.getX() && x <= jumpRec.getX() + jumpRec.getWidth()) &&
                (y >= jumpRec.getY() && y <= jumpRec.getY() + jumpRec.getHeight())) {
            return true;
        }
        return false;
    }

    public void initCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void initWorldCamera(OrthographicCamera camera) {
        this.worldCam = camera;
    }

}