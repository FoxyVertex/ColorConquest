package com.foxyvertex.colorconquest.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.foxyvertex.colorconquest.screens.SplashScreen;
import com.foxyvertex.colorconquest.tools.Utilities;
import com.kotcrab.vis.ui.VisUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 11/26/2016.
 * <p>
 * Manages and houses all of our assets
 */

public class Assets {

    public static Animation<TextureAtlas.AtlasRegion> playerIdleAnim;
    public static Animation<TextureAtlas.AtlasRegion> playerWalkAnim;
    public static Animation<TextureAtlas.AtlasRegion> playerFallAnim;
    public static Animation<TextureAtlas.AtlasRegion> playerJumpStartAnimation;
    public static Animation<TextureAtlas.AtlasRegion> playerJumpLoopAnimation;

    public static Skin         guiSkin;
    public static TextureAtlas mainAtlas;
    public static Sound        clickSound;
    public static List<Image> splashScreenLogos = new ArrayList<Image>();
    public static  Texture      badlogic;
    public static  Texture      blankPixel;
    public static  Texture      slitherikter;
    private static AssetManager manager;

    public static Music menuMusic;
    public static Music gameMusic;

    /**
     * Called on game start. Loads all assets into easily accessible variables.
     */
    public static void load() {
        manager = new AssetManager();

        manager.load("click.wav", Sound.class);
        manager.load("music/menu.wav", Music.class);
        manager.load("music/inGame.mp3", Music.class);

        manager.finishLoading();
        clickSound = manager.get("click.wav", Sound.class);
        menuMusic = manager.get("music/menu.wav", Music.class);
        gameMusic = manager.get("music/inGame.mp3", Music.class);
        Assets.mainAtlas = new TextureAtlas("GreyGuy.pack");

        playerIdleAnim = new Animation<TextureAtlas.AtlasRegion>(1 / 12f, Assets.mainAtlas.findRegions("idle"), Animation.PlayMode.LOOP);
        playerWalkAnim = new Animation<TextureAtlas.AtlasRegion>(1 / 15f, Assets.mainAtlas.findRegions("walk"), Animation.PlayMode.LOOP);
        playerFallAnim = new Animation<TextureAtlas.AtlasRegion>(1 / 4f, Assets.mainAtlas.findRegions("fall"), Animation.PlayMode.LOOP);
        playerJumpStartAnimation = new Animation<TextureAtlas.AtlasRegion>(1 / 4f, Assets.mainAtlas.findRegions("jumpstart"), Animation.PlayMode.NORMAL);
        playerJumpLoopAnimation = new Animation<TextureAtlas.AtlasRegion>(1 / 8f, Assets.mainAtlas.findRegions("jumploop"), Animation.PlayMode.LOOP);

        guiSkin = VisUI.getSkin();

        splashScreenLogos.add(new SplashScreen.SplashLogo("FoxyVertex.png", 0.8f).actorImage);

        badlogic = new Texture("badlogic.jpg");
        blankPixel = new Texture(new Pixmap(1, 1, Pixmap.Format.RGBA8888));
        slitherikter = new Texture("slitherikter.png");
    }

    public static void playSound(Sound sound) {
        if (UserPrefs.isSoundEnabled()) sound.play(1);
    }

    public static void stopPlayingMusic() {
        if (menuMusic.isPlaying()) {
            menuMusic.stop();
        }
    }

    public static void playMusic(Music music) {
        if (menuMusic.isPlaying()) menuMusic.stop();
        if (gameMusic.isPlaying()) gameMusic.stop();
        if (UserPrefs.isMusicEnabled()) music.play();
    }

    public static void dispose() {
        manager.dispose();
        mainAtlas.dispose();
        guiSkin.dispose();
    }

    public static void volumeChanged() {
        menuMusic.setVolume(Utilities.map(UserPrefs.getMusicVolume(), 0, 100, 0, Utilities.map(UserPrefs.getMasterVolume(), 0, 100, 0, 1)));
        gameMusic.setVolume(Utilities.map(UserPrefs.getMusicVolume(), 0, 100, 0, Utilities.map(UserPrefs.getMasterVolume(), 0, 100, 0, 1)));
        clickSound.setVolume(0, Utilities.map(UserPrefs.getSoundVolume(), 0, 100, 0, Utilities.map(UserPrefs.getMasterVolume(), 0, 100, 0, 1)));
    }
}
