package gasStation;

public class Jockey extends Thread{

    private static GasStation gasStation;

    public static void setGasStation(GasStation gasStation){
        Jockey.gasStation = gasStation;
    }

    @Override
    public void run() {
        while(true){
            try {
                gasStation.pumpGas(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
