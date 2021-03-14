package gasStation;

import java.util.LinkedList;
import java.util.Queue;

public class Cashier extends Thread{

    private Queue<Car> carQueue;
    private static Logger logger;

    Cashier(Logger logger){
        Cashier.logger = logger;
        carQueue = new LinkedList<>();
    }

    public void queueUpCar(Car car) {
        synchronized (this){
            carQueue.add(car);
            notifyAll();
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                serviceCar();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void serviceCar() throws InterruptedException {
        synchronized (this){
            if(carQueue.isEmpty()){
                wait();
            }
            Car car = carQueue.poll();
            Thread.sleep(5000);
            Stats stats = car.getStats();
            logger.addStats(stats);
        }
    }
}
