package itschool.bluemarble.model.entity.tile.abs;

import itschool.bluemarble.model.entity.tile.ifs.SpecialFunction;
import itschool.bluemarble.model.entity.tile.Tile;
import lombok.Getter;

@Getter
public abstract class SpecialTile extends Tile {
    private SpecialFunction specialFunction;

    public SpecialTile(int index, String name, SpecialFunction specialFunction) {
        super(index,name);
        this.specialFunction = specialFunction;
    }
}
