package pytobyte.game.jumper.batabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SkinEntity {
    @PrimaryKey
    public int skinID;
    public boolean bought;
    public int cost;

    public int getSkinID() {
        return skinID;
    }

    public void setSkinID(int skinID) {
        this.skinID = skinID;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public SkinEntity(int skinID, boolean bought, int cost) {
        this.skinID = skinID;
        this.bought = bought;
        this.cost = cost;
    }

    public SkinEntity() {}
}
