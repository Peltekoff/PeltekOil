package gasStation;

import util.DBConnector;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Logger extends Thread{

    private ArrayList<Stats> statsList;

    Logger(){
        statsList = new ArrayList<>();
    }

    public void addStats(Stats stats){
        statsList.add(stats);
        logIntoDb(stats);
    }

    private void logIntoDb(Stats stats) {
        Connection connection = DBConnector.getInstance().getConnection();
        String sql = "INSERT INTO station_loadings (column_id, fuel_type, fuel_quantity, loading_time) VALUES (?, ?, ?, ?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, stats.getColumnId());
            ps.setString(2, stats.getFuelType().toString());
            ps.setInt(3, stats.getFuelQuantity());
            ps.setTimestamp(4, Timestamp.valueOf(stats.getTimeOfLoading()));
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private TreeMap<Integer, TreeSet<Stats>> selectAllStatsByColumnAndDate(){
        Connection connection = DBConnector.getInstance().getConnection();
        String sql = "SELECT s.column_id, s.fuel_type, s.fuel_quantity, s.loading_time FROM station_loadings AS s\n" +
                "GROUP BY s.column_id, s.loading_time";
        TreeMap<Integer, TreeSet<Stats>> statsCollection = new TreeMap<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                GasStation.FuelType fuelType = getFuelType(rs.getString(2));
                Stats stats = new Stats(
                        rs.getInt(1),
                        fuelType,
                        rs.getInt(3),
                        rs.getTimestamp(4).toLocalDateTime());
                if(!statsCollection.containsKey(stats.getColumnId())){
                    statsCollection.put(stats.getColumnId(), new TreeSet<>());
                }
                statsCollection.get(stats.getColumnId()).add(stats);
                writeToFile(stats.toString(), "fullReportByColumnAndDate");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return statsCollection;
    }

    private GasStation.FuelType getFuelType(String string) {
        switch (string){
            case "DIESEL":
                return GasStation.FuelType.DIESEL;
            case "GAS":
                return GasStation.FuelType.GAS;
            case "PETROL":
                return GasStation.FuelType.PETROL;
            default:
                return null;
        }
    }

    private void selectTotalCarsServicedGroupedByColumn(){
        Connection connection = DBConnector.getInstance().getConnection();
        String sql = "SELECT column_id, COUNT(column_id) FROM station_loadings\n" +
                "GROUP BY column_id";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String fileText = rs.getInt(1) + " | " + rs.getInt(2);
                writeToFile(fileText, "reportOnTotalCarsServicedByColumn");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void selectTotalFuelPumpedByType(){
        Connection connection = DBConnector.getInstance().getConnection();
        String sql = "SELECT fuel_type, SUM(fuel_quantity) FROM station_loadings\n" +
                "GROUP BY fuel_type";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String fileText = rs.getString(1) + " | " + rs.getInt(2);
                writeToFile(fileText, "reportOnTotalFuelPumped");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void selectTotalCashEarnedByFuelType(){
        Connection connection = DBConnector.getInstance().getConnection();
        String sql = "SELECT fuel_type, SUM(fuel_quantity),\n" +
                "       CASE WHEN fuel_type = \"DIESEL\" THEN fuel_quantity*2.40\n" +
                "            WHEN fuel_type = \"GAS\" THEN fuel_quantity*1.60\n" +
                "            WHEN fuel_type = \"PETROL\" THEN fuel_quantity * 2\n" +
                "            END AS total_price\n" +
                "FROM station_loadings\n" +
                "GROUP BY fuel_type";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String fileText = rs.getString(1) + " | " + rs.getInt(2) + " | " + rs.getInt(3);
                writeToFile(fileText, "reportOnTotalCashEarned");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void writeToFile(String text, String fileName){
        File file = new File(fileName + ".txt");
        try(FileWriter fw = new FileWriter(file, true)) {
            fw.append(text + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem with file");
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logData();
        }
    }

    private void logData() {
        selectAllStatsByColumnAndDate();
        selectTotalCarsServicedGroupedByColumn();
        selectTotalFuelPumpedByType();
        selectTotalCashEarnedByFuelType();
    }
}
