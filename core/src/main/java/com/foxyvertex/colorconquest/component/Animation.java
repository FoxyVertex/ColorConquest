package com.foxyvertex.colorconquest.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.runtime.util.autotable.ATProperty;

import java.util.HashMap;

/**
 * Created by aidan on 2/14/2017.
 */

public class Animation extends Component {
    public enum AnimType {ATLAS, SPRITER, SPINE}
    @ATProperty(fieldName="Animation Type")
    public AnimType animationType;

    public String path;
    public String currentAnimation;
    public Array<String> animationNames = new Array<>();
    public HashMap<String, com.badlogic.gdx.graphics.g2d.Animation<TextureAtlas.AtlasRegion>> animations = new HashMap<>();
    public HashMap<String, Float> animationFrameCounts = new HashMap<>();
    public TextureAtlas textureAtlas;

    public float timer = 0f;
    public boolean loop;

    public String onEnd = "";

    public boolean flipX = false;
    public boolean flipY = false;
}