package itschool.bluemarble.model.entity.tile.abs;

import itschool.bluemarble.exception.violation.PlayerHasNoMoneyViolation;
import itschool.bluemarble.model.entity.Player;
import itschool.bluemarble.model.entity.construction.Building;
import itschool.bluemarble.model.entity.construction.Hotel;
import itschool.bluemarble.model.entity.construction.Villa;
import itschool.bluemarble.model.enumclass.Color;
import itschool.bluemarble.model.enumclass.ConstructionType;
import itschool.bluemarble.progress.GameByConsole;
import lombok.Getter;

@Getter
public class City extends PurchasableTile {

    protected ConstructionType constructionType = null; // 현재 건물
    protected Villa villa = new Villa();
    protected Building building = new Building();
    protected Hotel hotel = new Hotel();
    protected Color color;
    
    // 생성자
    public City(int index, String name, Color color, int price, int toll, int villaToll, int buildingToll, int hotelToll) {
        super(index, name);
        this.color = color;
        this.price = price;
        this.toll = toll;
        this.villa.setToll(villaToll);
        this.building.setToll(buildingToll);
        this.hotel.setToll(hotelToll);
    }

        /*// 건물 가격 정의 및 객체 생성
        villa = new Villa(color.getVillaPrice());
        building = new Building(color.getBuildingPrice());
        hotel = new Hotel(color.getHotelPrice());*/



    // isCity() 메소드에서 활용
    // VILLA 구매
    public void buyVilla(Player player) throws PlayerHasNoMoneyViolation {
        player.payAmountToBank(color.getVillaPrice());
        constructionType = ConstructionType.VILLA;
    }

    // BUILDING 구매
    public void buyBuilding(Player player) throws PlayerHasNoMoneyViolation {
        player.payAmountToBank(color.getBuildingPrice());
        constructionType = ConstructionType.BUILDING;
    }

    // HOTEL 구매
    public void buyHotel(Player player) throws PlayerHasNoMoneyViolation {
        player.payAmountToBank(color.getHotelPrice());
        constructionType = ConstructionType.HOTEL;
    }


    @Override
    public int getToll() throws RuntimeException {
        if(constructionType == ConstructionType.VILLA) {
            return villa.getToll();
        } else if(constructionType == ConstructionType.BUILDING) {
            return building.getToll();
        } else if(constructionType == ConstructionType.HOTEL) {
            return hotel.getToll();
        }

        return toll;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + "(");
        sb.append((constructionType != null)? constructionType.name() + "有/" : "");
        sb.append(GameByConsole.formatWithCommas(price) + ", " + GameByConsole.formatWithCommas(toll) + ")");

        return sb.toString();
    }
}