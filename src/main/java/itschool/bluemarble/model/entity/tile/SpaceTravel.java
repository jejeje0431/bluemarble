package itschool.bluemarble.model.entity.tile;

import itschool.bluemarble.model.entity.Player;
import itschool.bluemarble.model.entity.tile.abs.PurchasableTile;
import itschool.bluemarble.model.entity.tile.abs.SpecialTile;
import itschool.bluemarble.model.entity.tile.ifs.SpecialFunction;

public class SpaceTravel extends SpecialTile {
    public SpaceTravel() {
        super(30, "우주여행", (player) -> {
            System.out.println(player.getName() + "님이 우주여행을 시작합니다.");
        });
    }
    public void payFee(Player user, Player owner) {
        if (owner == null) {
            user.payAmountToBank(200_000);
        } else if (!user.equals(owner)) {
            user.payAmountTo(owner, 200_000);
        }
    }
}
