package itschool.bluemarble.model.entity.tile.abs;

import itschool.bluemarble.exception.violation.PlayerHasNoMoneyViolation;
import itschool.bluemarble.model.entity.Player;
import itschool.bluemarble.model.entity.tile.Tile;
import itschool.bluemarble.progress.GameByConsole;
import lombok.Getter;

@Getter
public abstract class PurchasableTile extends Tile {
    protected Player owner;
    protected int price;
    protected int toll;

    public PurchasableTile(int index, String name) {
        super(index, name);
    }

    public boolean isPurchasable(){
        return (owner == null)? true:false;
    }

    // 도시 구매 (주인이 없는 지 체크 먼저)
    public void purchaseTile(Player player) throws PlayerHasNoMoneyViolation {
        if(isPurchasable()) {
            player.payAmountToBank(price);
            player.plusAsset(price);
            this.owner = player;
        } else {
            throw new RuntimeException("이미 주인이 있는 땅입니다.");
        }
    }

    // 통행료가 얼마인지 반환
    public abstract int getToll() throws RuntimeException;

    public boolean shouldPay(Player player) {
        if(player.equals(owner) || null == owner) { // 땅의 주인이거나 땅에 주인이 없거나
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return name + '(' + GameByConsole.formatWithCommas(price) + " / " + GameByConsole.formatWithCommas(toll) + ')';
    }
}