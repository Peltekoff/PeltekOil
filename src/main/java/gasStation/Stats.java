package gasStation;

import java.time.LocalDateTime;

public class Stats implements Comparable<Stats>{

    private int columnId;
    private GasStation.FuelType fuelType;
    private int fuelQuantity;
    private LocalDateTime timeOfLoading;

    public Stats(int columnId, GasStation.FuelType fuelType, int fuelQuantity, LocalDateTime timeOfLoading) {
        this.columnId = columnId;
        this.fuelType = fuelType;
        this.fuelQuantity = fuelQuantity;
        this.timeOfLoading = timeOfLoading;
    }

    public int getColumnId() {
        return columnId;
    }

    public GasStation.FuelType getFuelType() {
        return fuelType;
    }

    public int getFuelQuantity() {
        return fuelQuantity;
    }

    public LocalDateTime getTimeOfLoading() {
        return timeOfLoading;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "columnId=" + columnId +
                ", fuelType=" + fuelType +
                ", fuelQuantity=" + fuelQuantity +
                ", timeOfLoading=" + timeOfLoading +
                '}';
    }

    @Override
    public int compareTo(Stats o) {
        return this.timeOfLoading.compareTo(o.getTimeOfLoading());
    }
}
