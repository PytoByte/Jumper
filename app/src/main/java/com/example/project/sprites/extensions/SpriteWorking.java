package com.example.project.sprites.extensions;

import com.example.project.sprites.Sprite;

import java.util.ArrayList;
import java.util.HashMap;

public class SpriteWorking {
    public ArrayList<Sprite> sprites;
    public SpriteWorking(ArrayList<Sprite> sprites) {
        this.sprites = sprites;
    }

    public void setSprites(ArrayList<Sprite> sprites) {
        this.sprites = sprites;
    }

    public ArrayList<Sprite> findCloseSprites(Position pos, String side, String tag) {
        ArrayList<Sprite> closeSprites = new ArrayList<>();
        for (Sprite sprite : sprites) {
            switch (side) {
                case "top":
                    if (sprite.pos.y - pos.y <= 1 && sprite.tag.equals(tag)) {
                        closeSprites.add(sprite);
                    }
                    break;
                case "bottom":
                    if (sprite.pos.y - pos.y >= 1 && sprite.tag.equals(tag)) {
                        closeSprites.add(sprite);
                    }
                    break;
                case "left":
                    if (sprite.pos.x - pos.x <= 1 && sprite.tag.equals(tag)) {
                        closeSprites.add(sprite);
                    }
                    break;
                case "right":
                    if (sprite.pos.x - pos.x >= 1 && sprite.tag.equals(tag)) {
                        closeSprites.add(sprite);
                    }
                    break;
            }
        }
        return closeSprites;
    }

    public ArrayList<Sprite> findCloseSprites(Position pos, String side) throws Exception {
        ArrayList<Sprite> closeSprites = new ArrayList<>();
        if (!side.equals("top") && !side.equals("left") && !side.equals("bottom") && !side.equals("right")) {
            throw new Exception("Unknown tag");
        }
        for (Sprite sprite : sprites) {
            switch (side) {
                case "top":
                    if (sprite.pos.y - pos.y <= 1) {
                        closeSprites.add(sprite);
                    }
                    break;
                case "bottom":
                    if (sprite.pos.y - pos.y >= 1) {
                        closeSprites.add(sprite);
                    }
                    break;
                case "left":
                    if (sprite.pos.x - pos.x <= 1) {
                        closeSprites.add(sprite);
                    }
                    break;
                case "right":
                    if (sprite.pos.x - pos.x >= 1) {
                        closeSprites.add(sprite);
                    }
                    break;
            }
        }
        return closeSprites;
    }

    public ArrayList<Sprite> findCloseSprites(Position pos) {

        ArrayList<Sprite> closeSprites = new ArrayList<>();
        for (Sprite sprite : sprites) {
            if (Math.abs(sprite.pos.x-pos.x)<=1 || Math.abs(sprite.pos.y-pos.y)<=1) {
                closeSprites.add(sprite);
            }
        }
        return closeSprites;
    }

    public HashMap<String, Float> getAngles() {
        HashMap<String, Float> result = new HashMap<>();
        boolean first = true;
        result.put("maxX", 0f);
        result.put("minX", 0f);
        result.put("maxY", 0f);
        result.put("minY", 0f);
        for (Sprite sprite : sprites) {
            float x = sprite.pos.x;
            float y = sprite.pos.y;
            if (first) {
                result.replace("maxX", x);
                result.replace("minX", x);
                result.replace("maxY", y);
                result.replace("minY", y);
                first = false;
            }
            else {
                if (result.get("maxX")<x) {
                    result.replace("maxX", x);
                } else if (result.get("minX")>x) {
                    result.replace("minX", x);
                }

                if (result.get("maxY")>y) {
                    result.replace("maxY", y);
                } else if (result.get("minY")<y) {
                    result.replace("minY", y);
                }
            }
        }
        return result;
    }
}
