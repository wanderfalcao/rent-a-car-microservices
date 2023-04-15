package br.com.infnet.wander.utility;

import br.com.infnet.wander.model.Car;
import br.com.infnet.wander.model.CarStatus;
import br.com.infnet.wander.repository.CarRepository;
import br.com.infnet.wander.service.SequenceGeneratorCarService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



@Component
public class SeedDatabase implements CommandLineRunner {
    
    private CarRepository carRepository;
    private final SequenceGeneratorCarService sequenceGeneratorCarService;
    
    public SeedDatabase(CarRepository carRepository, SequenceGeneratorCarService sequenceGeneratorCarService) {
        this.carRepository = carRepository;
        this.sequenceGeneratorCarService = sequenceGeneratorCarService;
    }
    
    @Override
    public void run(String... args) {
        
        this.carRepository.deleteAll();
        
        this.carRepository.save(new Car(
                sequenceGeneratorCarService.generateSequence(Car.SEQUENCE_NAME),
                CarStatus.AVAILABLE,
                "Alfa Romeo",
                "Black",
                "159 Ti",
                9800D,
                "https://www.auto-data.net/images/f56/Alfa-Romeo-159-Sportwagon.jpg"
        ));
    }
}
