package gasStation;

public class CarProducer extends Thread{

    private static GasStation gasStation;

    public static void setGasStation(GasStation gasStation){
        CarProducer.gasStation = gasStation;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
                gasStation.carArrives(new Car());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
