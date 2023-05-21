package com.example.project.UI.items.shop;

import com.example.project.R;
import com.example.project.sprites.extensions.BitmapBank;

public interface Prices extends BitmapBank {
    static int getPrice(int id) {
        switch (id){
            case PLAYER_0:
                return 0;
            case PLAYER_1:
                return 10;
            case PLAYER_2:
                return 25;
            case PLAYER_3:
                return 50;
            case PLAYER_4:
                return 75;
            case PLAYER_5:
                return 100;
            case PLAYER_6:
                return 1000;
        }

        return 0;
    }
}
