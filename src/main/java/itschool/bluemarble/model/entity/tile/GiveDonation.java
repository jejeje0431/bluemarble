package itschool.bluemarble.model.entity.tile;

import itschool.bluemarble.model.entity.Player;
import itschool.bluemarble.model.entity.tile.abs.SpecialTile;
import itschool.bluemarble.model.entity.tile.ifs.SpecialFunction;

public class GiveDonation extends SpecialTile {
    public GiveDonation(){
        super(20,"사회복지기금접수처", new SpecialFunction() {
            @Override
            public void execute(Player player) throws RuntimeException{
                try {
                    System.out.println(player.getName() + "님의 사회복지기금이 접수되었습니다.");
                    // player.showAmount();
                } catch(Exception e){
                    throw e;
                }
            }
        });
    }
}
