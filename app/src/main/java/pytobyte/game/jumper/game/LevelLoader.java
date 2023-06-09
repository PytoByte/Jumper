package pytobyte.game.jumper.game;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.AssetManager;

import pytobyte.game.jumper.sprites.Finish;
import pytobyte.game.jumper.sprites.Player;
import pytobyte.game.jumper.sprites.Point;
import pytobyte.game.jumper.sprites.Spike;
import pytobyte.game.jumper.sprites.Sprite;
import pytobyte.game.jumper.sprites.Wall;

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
    public int pattern;

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

    public void loadPartLevel(ArrayList<Sprite> levelObjects) throws IOException, JSONException {
        if (gameCore==null) {
            throw new RuntimeException("You forgot give gameCore before load level");
        }

        ArcadeTree arcadeTree = new ArcadeTree();
        ArrayList part = arcadeTree.getNextPart(pattern);
        pattern = (int) part.get(1);
        AssetManager am = gameCore.activity.getAssets();
        InputStream is = am.open("parts/"+ part.get(0) +".json");

        StringBuilder strJSON = new StringBuilder();
        int i;
        while((i=is.read())!=-1){
            strJSON.append((char) i);
        }

        JSONArray ja = new JSONArray(strJSON.toString());

        ArrayList<Sprite> spritesNew = new ArrayList<>();

        for (int j=0; j<ja.length(); j++) {
            JSONObject jo = new JSONObject(ja.get(j).toString());
            jo.put("y", (float)jo.getInt("y"));
            if (jo.get("tag").equals("Player")) {
                continue;
            } else {
                spritesNew.add(convert(jo));
            }
        }

        float maxY1 = findMaxY(levelObjects);
        float maxY2 = findMaxY(spritesNew);
        float dy = maxY1-maxY2;
        float height = findHeight(spritesNew);

        for (Sprite sprite : spritesNew) {
            sprite.pos.y = sprite.pos.y+dy-height-1;
        }
        sprites.addAll(spritesNew);
    }

    public void loadPartLevel(ArrayList<Sprite> levelObjects, String partName) throws IOException, JSONException {
        if (gameCore==null) {
            throw new RuntimeException("You forgot give gameCore before load level");
        }

        AssetManager am = gameCore.activity.getAssets();
        InputStream is = am.open("parts/"+partName+".json");

        StringBuilder strJSON = new StringBuilder();
        int i;
        while((i=is.read())!=-1){
            strJSON.append((char) i);
        }

        JSONArray ja = new JSONArray(strJSON.toString());

        float height = findHeight(levelObjects);

        for (int j=0; j<ja.length(); j++) {
            JSONObject jo = new JSONObject(ja.get(j).toString());
            jo.put("y", (float)jo.getInt("y")-height);
            if (jo.get("tag").equals("Player")) {
                player = (Player) convert(jo);
                sprites.add(player);
            } else {
                sprites.add(convert(jo));
            }
        }
    }

    public float findHeight(ArrayList<Sprite> levelObjects) {
        boolean first = true;
        float minY = 0;
        float maxY = 0;
        for (Sprite o : levelObjects) {
            if (first) {
                minY = o.pos.y;
                maxY = o.pos.y;
                first = false;
            }
            else {
                if (minY<o.pos.y) {
                    minY = o.pos.y;
                } else if (maxY>o.pos.y) {
                    maxY = o.pos.y;
                }
            }

        }
        return Math.abs(minY-maxY);
    }

    public float findMaxY(ArrayList<Sprite> levelObjects) {
        boolean first = true;
        float maxY = 0;
        for (Sprite o : levelObjects) {
            if (first) {
                maxY = o.pos.y;
                first = false;
            }
            else if (maxY>o.pos.y) {
                maxY = o.pos.y;
            }
        }
        return maxY;
    }

    public float findMinY(ArrayList<Sprite> levelObjects) {
        boolean first = true;
        float minY = 0;
        for (Sprite o : levelObjects) {
            if (first) {
                minY = o.pos.y;
                first = false;
            }
            else if (minY<o.pos.y) {
                minY = o.pos.y;
            }
        }
        return minY;
    }

    public float findWidth(ArrayList<Sprite> levelObjects) {
        boolean first = true;
        float minX = 0;
        float maxX = 0;
        for (Sprite o : levelObjects) {
            if (first) {
                minX = o.pos.x;
                maxX = o.pos.x;
                first = false;
            }
            else {
                if (minX>o.pos.x) {
                    minX = o.pos.x;
                } else if (maxX<o.pos.x) {
                    maxX = o.pos.x;
                }
            }

        }
        return Math.abs(maxX-minX);
    }


    public Sprite convert(JSONObject jo) throws JSONException {
        Object tag = jo.get("tag");

        if ("Wall".equals(tag)) {
            Wall wall = new Wall((float) jo.getInt("x"), (float) jo.getInt("y"), gameCore);
            wall.rotate = jo.getInt("r");
            wall.setType(jo.getInt("type"));
            return wall;
        } else if ("Player".equals(tag)) {
            Player player = new Player((float) jo.getInt("x"), (float) jo.getInt("y"), gameCore);
            player.rotate = jo.getInt("r");
            return player;
        } else if ("Spike".equals(tag)) {
            Spike spike = new Spike((float) jo.getInt("x"), (float) jo.getInt("y"), gameCore);
            spike.rotate = jo.getInt("r");
            return spike;
        } else if ("Finish".equals(tag)) {
            Finish finish = new Finish((float) jo.getInt("x"), (float) jo.getInt("y"), gameCore);
            finish.rotate = jo.getInt("r");
            return finish;
        } else if ("Point".equals(tag)) {
            Point point = new Point((float) jo.getInt("x"), (float) jo.getInt("y"), gameCore);
            return point;
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
        if (sharedPreferences.getString("mode", "").equals("company")) {
            String levelName = sharedPreferences.getString("name", null);
            loadLevel(levelName);
        }
        else if (sharedPreferences.getString("mode", "").equals("arcade")) {
            ArcadeTree arcadeTree = new ArcadeTree();
            ArrayList part = arcadeTree.getNextPart(0);
            pattern = (int) part.get(1);
            loadPartLevel(sprites, (String)part.get(0));
        }
    }
}
