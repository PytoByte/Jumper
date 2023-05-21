package pytobyte.game.jumper.sprites.extensions;

import pytobyte.game.jumper.R;

public interface BitmapBank {
    int WALL_0 = R.drawable.wall;
    int WALL_1 = R.drawable.angle;
    int WALL_2 = R.drawable.triple_wall;
    int WALL_3 = R.drawable.tetra_wall;
    int WALL_4 = R.drawable.double_wall;

    int WALL_5 = R.drawable.connector;
    int WALL_6 = R.drawable.triple_connector;
    int WALL_7 = R.drawable.tetra_connector;

    int WALL_8 = R.drawable.wall_connector;

    int WALL_9 = R.drawable.angle_connector;

    int SPIKE_0 = R.drawable.spike;

    int PLAYER_0 = R.drawable.player;
    int PLAYER_1 = R.drawable.player_angry;
    int PLAYER_2 = R.drawable.player_bigeye;
    int PLAYER_3 = R.drawable.player_bored;
    int PLAYER_4 = R.drawable.player_mind;
    int PLAYER_5 = R.drawable.player_raven;
    int PLAYER_6 = R.drawable.player_dollar;


    int POINT_0 = R.drawable.point;

    int FINISH_0 = R.drawable.finish;

    default int getWallTexture(int type) throws Exception {
        switch (type) {
            case 0:
                return WALL_0;
            case 1:
                return WALL_1;
            case 2:
                return WALL_2;
            case 3:
                return WALL_3;
            case 4:
                return WALL_4;
            case 5:
                return WALL_5;
            case 6:
                return WALL_6;
            case 7:
                return WALL_7;
            case 8:
                return WALL_8;
            case 9:
                return WALL_9;
        }
        throw new Exception("Wall type not found");
    }

    default int getSpikeTexture() {
        return SPIKE_0;
    }

    default int getPlayerTexture() {
        return PLAYER_0;
    }

    default int getPointTexture() {
        return POINT_0;
    }

    default int getFinishTexture() {
        return FINISH_0;
    }
}
