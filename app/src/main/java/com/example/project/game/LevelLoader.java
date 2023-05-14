package com.example.project.game;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.example.project.sprites.Finish;
import com.example.project.sprites.Player;
import com.example.project.sprites.Spike;
import com.example.project.sprites.Sprite;
import com.example.project.sprites.Wall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LevelLoader {
    public String name;
    public ArrayList<Sprite> sprites = new ArrayList<>();
    public Player player;
    public GameCore gameCore;

    public LevelLoader() {}

    public LevelLoader(GameCore gameCore) {
        this.gameCore = gameCore;
    }

    public void joinToCore() {
        gameCore.setLevel(this);
    }

    public void initDefaultLevel() {
        name = "-1";

        for (int i = -100; i<100; i+=1) {
            sprites.add(new Wall(i, 100, gameCore));
            sprites.add(new Wall(i, -100, gameCore));
            sprites.add(new Wall(100, i, gameCore));
            sprites.add(new Wall(-100, i, gameCore));
            sprites.add(new Wall(0, i+2, gameCore));
        }

        player = new Player(2, 0, gameCore);

        sprites.add(player);

        sprites.add(new Wall(0, 99, gameCore));
        sprites.add(new Wall(0, 98, gameCore));
        sprites.add(new Wall(-99, 99, gameCore));
        sprites.add(new Finish(99, 99, gameCore));
    }

    public void loadLevel(String name) throws IOException, JSONException {
        if (gameCore==null) {
            throw new RuntimeException("You forgot give gameCore before load level");
        }

        AssetManager am = gameCore.activity.getAssets();
        InputStream is = am.open("levels/"+name+".json");

        StringBuilder strJSON = new StringBuilder();
        int i;
        while((i=is.read())!=-1){
            strJSON.append((char) i);
        }

        JSONArray ja = new JSONArray(strJSON.toString());

        for (int j=0; j<ja.length(); j++) {
            JSONObject jo = new JSONObject(ja.get(j).toString());

            if (jo.get("tag").equals("Player")) {
                player = (Player) convert(jo);
                sprites.add(player);
            } else {
                sprites.add(convert(jo));
            }
        }

    }


    public Sprite convert(JSONObject jo) throws JSONException {
        Object tag = jo.get("tag");

        if ("Wall".equals(tag)) {
            return new Wall((float)(int) jo.get("x"), (float)(int) jo.get("y"), gameCore);
        } else if ("Player".equals(tag)) {
            return new Player((float)(int) jo.get("x"), (float)(int) jo.get("y"), gameCore);
        } else if ("Spike".equals(tag)) {
            return new Spike((float)(int) jo.get("x"), (float)(int) jo.get("y"), gameCore);
        } else if ("Finish".equals(tag)) {
            return new Finish((float)(int) jo.get("x"), (float)(int) jo.get("y"), gameCore);
        }

        throw new UnknownError("No tag in tags list");
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(ArrayList<Sprite> sprites) {
        this.sprites = sprites;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameCore getGameCore() {
        return gameCore;
    }

    public void setGameCore(GameCore gameCore) {
        this.gameCore = gameCore;
    }

    public void loadFromPreference() throws JSONException, IOException {
        SharedPreferences sharedPreferences = gameCore.activity.getSharedPreferences("level", MODE_PRIVATE);
        String levelName = sharedPreferences.getString("name", null);

        loadLevel(levelName);
    }
}
