package itschool.bluemarble.model.entity.tile;

import itschool.bluemarble.model.entity.Player;
import itschool.bluemarble.model.entity.ifs.Payable;

public class DonationParty extends Tile {
    private int amount=0;

    // 싱글톤 시작
    final private static DonationParty instance = new DonationParty();

    public static DonationParty getInstance() {
        return instance;
    }

    private DonationParty() {
        super(38,"사회복지기금수령처");
        this.amount = 0;
    }
    // 싱글톤 끝

    public void payAmountTo(Payable receiver, int amount) {
        receiver.plusAmount(this.amount);
        this.amount = 0;
    }
    public int getAmount(){ return this.amount; }
    public void plusAmount(int amount) {
        this.amount += amount;
    }
}
