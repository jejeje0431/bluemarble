package itschool.bluemarble.model.entity.tile;

import itschool.bluemarble.model.entity.Player;
import itschool.bluemarble.model.entity.tile.abs.SpecialTile;
import itschool.bluemarble.model.entity.tile.ifs.SpecialFunction;

public class Island extends SpecialTile {
    public Island(){
        super(10,"무인도", new SpecialFunction() {
            @Override
            public void execute(Player player) {
                System.out.println(player+"님이 무인도에 갇히셨습니다.");
            }
        });
    }
}
