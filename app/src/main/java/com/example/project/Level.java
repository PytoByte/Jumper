package com.example.project;

import com.example.project.sprites.Finish;
import com.example.project.sprites.Player;
import com.example.project.sprites.Sprite;
import com.example.project.sprites.Wall;

import java.util.ArrayList;

public class Level {
    public String name;
    public ArrayList<Sprite> sprites = new ArrayList<>();
    public Player player;
    public GameCore gameCore;

    public Level() {}

    public Level(GameCore gameCore) {
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
}
