package gasStation;

import util.Randomizer;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PumpingStation {

    private Queue<Car> carQueue;
    private int columnId;
    private boolean isServicedByJockey = false;
    private static GasStation gasStation;

    PumpingStation(int columnId){
        this.columnId = columnId;
        carQueue = new ConcurrentLinkedQueue<>();
    }

    public static void setGasStation(GasStation gasStation){
        PumpingStation.gasStation = gasStation;
    }

    public void queueUpCar(Car car) {
        System.out.println("A car has queued up at column number " + columnId);
        carQueue.offer(car);
    }

    public boolean columnQueueIsEmpty(){
        return carQueue.isEmpty();
    }

    public boolean isServicedByJockey(){
        return isServicedByJockey;
    }

    public void service(Jockey jockey) {
        isServicedByJockey = true;
        System.out.println("Pumping fuel into car at column " + columnId);
        Car car = carQueue.poll();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int randomFuelType = Randomizer.getRandomNumber(0, 2);
        int randomFuelQuantity = Randomizer.getRandomNumber(10, 40);
        GasStation.FuelType fuelType = GasStation.FuelType.values()[randomFuelType];
        car.setStats(new Stats(this.columnId, fuelType, randomFuelQuantity, LocalDateTime.now()));
        System.out.println("Car pumping is complete at column " + columnId + " sending to checkout.");
        gasStation.sendCarToCheckout(car);
        isServicedByJockey = false;
    }
}
