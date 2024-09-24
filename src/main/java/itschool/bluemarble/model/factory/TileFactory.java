package itschool.bluemarble.model.factory;

import itschool.bluemarble.dao.TileDao;
import itschool.bluemarble.model.entity.goldenKey.GoldenKeyTile;
import itschool.bluemarble.model.entity.tile.*;
import itschool.bluemarble.model.entity.tile.abs.City;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class TileFactory {
    private static ArrayList<Tile> TILES = new ArrayList<Tile>();
    private static GoldenKeyTile goldenKeyTile;
    private static TileDao tileDao = new TileDao();

    static {

        //소스 추가 필요
        TILES.addAll(Arrays.asList(
                new Tile(0, "시작"),           // index 0, Special
                new Island(),                               // index 10, Special
                new SpaceTravel(),
                new GiveDonation(),                         // index 20, Special
                DonationParty.getInstance(),                // index 38, Special
                new GoldenKeyTile(2),                           // index 2
                new GoldenKeyTile(7),                           // index 7
                new GoldenKeyTile(12),                          // index 12
                new GoldenKeyTile(17),                          // index 17
                new GoldenKeyTile(22),                          // index 22
                new GoldenKeyTile(35)                           // index 35
        ));

        // 도시 추가
        TILES.addAll(tileDao.selectCities());

        // 통행료 고정, FixedTollCity + Special vehicle
        TILES.addAll(tileDao.selectFixedTollTiles());

        TILES.sort(((o1, o2) -> o1.getIndex() - o2.getIndex()));
    }

    public static ArrayList<Tile> getTiles() {
        return TILES;
    }
}
