package itschool.bluemarble.dao;

import itschool.bluemarble.dao.base.CommomDao;
import itschool.bluemarble.model.entity.tile.FixedTollCity;
import itschool.bluemarble.model.entity.tile.abs.City;
import itschool.bluemarble.model.enumclass.Color;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TileDao extends CommomDao {
    public List<City> selectCities() {
        List<City> resultList = new ArrayList<>();
        try (Connection dbconn = connect();
             Statement st = dbconn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM city")){

            while(rs.next()){
                // City city = new City("타이베이", Color.PINK, 50_000, 2_000, 10_000, 90_000, 250_000);
                // Index 컬럼도 추가 필요
                int index = rs.getInt("index");
                String name = rs.getString("name");
                Color color = Color.valueOf(rs.getString("color"));
                int price = rs.getInt("price");
                int toll =rs.getInt("toll");
                int villaToll = rs.getInt("villa_toll");
                int buildingToll = rs.getInt("building_toll");
                int hotelToll =rs.getInt("hotel_toll");

                City city = new City(index, name, color, price, toll, villaToll, buildingToll, hotelToll);
                resultList.add(city);
            }
            return resultList;
        } catch (SQLException e){
            throw new RuntimeException("selectCities 오류 발생\n", e);
        }
    }

    public List<FixedTollCity> selectFixedTollTiles()  {
        List<FixedTollCity> resultList = new ArrayList<>();
        try (Connection dbconn = connect();
             Statement st = dbconn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM fixed_toll_tile")){

            while(rs.next()){
                int index = rs.getInt("index");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                int toll =rs.getInt("toll");

                FixedTollCity fixedTollCity = new FixedTollCity(index,name,price,toll);
                resultList.add(fixedTollCity);
            }
            return resultList;
        } catch (SQLException e){
            throw new RuntimeException("selectFixedTollTiles 오류 발생\n", e);
        }
    }
}
