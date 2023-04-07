package br.com.infnet.wander.utility;

import br.com.infnet.wander.model.Car;
import br.com.infnet.wander.model.CarStatus;
import br.com.infnet.wander.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



@Component
public class SeedDatabase implements CommandLineRunner {
    
    private CarRepository carRepository;
    
    public SeedDatabase(CarRepository carRepository) {
        this.carRepository = carRepository;
    }
    
    @Override
    public void run(String... args) {
        
        this.carRepository.deleteAll();
//        this.carRepository.save(new Car(
//                CarStatus.AVAILABLE,
//                "Audi",
//                "black",
//                "TT",
//                "Coupe",
//                65000D,
//                CurrencySymbol.USD,
//                "https://www.auto-data.net/en/audi-tt-rs-roadster-8s-facelift-2019-generation-7105#image8"
//        ));
//        this.carRepository.save(new Car(
//                Long.valueOf(0),
//                CarStatus.AVAILABLE,
//                "Audi",
//                "Black",
//                "TT",
//                "Coupé",
//                65000D,
//                CurrencySymbol.USD,
//                "https://www.auto-data.net/en/audi-tt-rs-roadster-8s-facelift-2019-generation-7105#image8"
//
//        ));
        
        this.carRepository.save(new Car(
                Long.valueOf(1),
                CarStatus.AVAILABLE,
                "Alfa Romeo",
                "Black",
                "159 Ti",
                9800D,
                "https://www.auto-data.net/images/f56/Alfa-Romeo-159-Sportwagon.jpg"
        ));
    }
}
