package gasStation;

import util.Randomizer;

import java.util.ArrayList;

public class GasStation {

    public void sendCarToCheckout(Car car) {
        checkoutSection.receiveCar(car);
    }

    public enum FuelType{
        DIESEL, GAS, PETROL
    }

    private ArrayList<PumpingStation> pumpingStations;
    private Jockey g1;
    private Jockey g2;
    private CheckoutSection checkoutSection;

    public GasStation(){
        pumpingStations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            pumpingStations.add(new PumpingStation(i+1));
        }
        PumpingStation.setGasStation(this);
        checkoutSection = new CheckoutSection();
        g1 = new Jockey();
        g2 = new Jockey();
        Jockey.setGasStation(this);
        g1.start();
        g2.start();
    }

    public synchronized void carArrives(Car car) {
        System.out.println("A car has arrived at the gas station");
        int r = Randomizer.getRandomNumber(0, pumpingStations.size()-1);
        PumpingStation pumpingStation = pumpingStations.get(r);
        pumpingStation.queueUpCar(car);
        notifyAll();
    }

    public void pumpGas(Jockey jockey) throws InterruptedException {
        PumpingStation pumpingStation = null;
        synchronized (this) {
            while (noColumnWaiting()) {
                wait();
            }
            pumpingStation = getNextWaitingColumn();
            pumpingStation.service(jockey);
        }
    }

    private PumpingStation getNextWaitingColumn() {
        for(PumpingStation pumpingStation : pumpingStations){
            if(!pumpingStation.columnQueueIsEmpty() && !pumpingStation.isServicedByJockey()) {
                return pumpingStation;
            }
        }
        return null;
    }

    private boolean noColumnWaiting() {
        for(PumpingStation pumpingStation : pumpingStations){
            if(!pumpingStation.columnQueueIsEmpty() && !pumpingStation.isServicedByJockey()) {
                return false;
            }
        }
        return true;
    }
}
