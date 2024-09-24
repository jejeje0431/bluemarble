package itschool.bluemarble.model.entity.tile;

import itschool.bluemarble.model.entity.tile.abs.PurchasableTile;

public class FixedTollCity extends PurchasableTile {

    public FixedTollCity(int index, String name, int price, int toll) {
        super(index, name);
        this.price = price;
        this.toll = toll;
    }


    @Override
    public int getToll() throws RuntimeException {
        if(!isPurchasable()) {
            return toll;
        }  else {
            throw new RuntimeException("현재 주인이 없으므로 통행료 지불 의무가 없습니다.");
        }
    }
}
