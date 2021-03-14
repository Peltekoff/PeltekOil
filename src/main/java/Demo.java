import gasStation.Car;
import gasStation.CarProducer;
import gasStation.GasStation;
import util.DBConnector;

import java.sql.Connection;

public class Demo {

    public static void main(String[] args) {

        CarProducer producer = new CarProducer();
        GasStation gasStation = new GasStation();
        CarProducer.setGasStation(gasStation);
        producer.start();
    }
}
