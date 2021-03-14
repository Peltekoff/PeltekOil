package gasStation;

import java.util.Random;

public class CheckoutSection {

    private Cashier c1;
    private Cashier c2;

    CheckoutSection(){
        Logger logger = new Logger();
        c1 = new Cashier(logger);
        c2 = new Cashier(logger);
        c1.start();
        c2.start();
        logger.start();
    }

    public void receiveCar(Car car) {
        if(new Random().nextBoolean()){
            c1.queueUpCar(car);
        }
        else{
            c2.queueUpCar(car);
        }
    }
}
