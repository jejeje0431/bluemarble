package itschool.bluemarble.model.entity.tile;

import itschool.bluemarble.model.entity.Player;
import itschool.bluemarble.model.entity.tile.abs.PurchasableTile;
import lombok.Getter;

@Getter
public class SpecialVehicle extends FixedTollCity {
    public SpecialVehicle(int index, String name, int price, int toll){
        super(index,name,price,toll);
    }

    void setOwner(Player player){
        this.owner = player;
    }

    @Override
    public int getToll() throws RuntimeException {
        return 0;
    }
}
