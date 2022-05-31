package com.pucp.odiparpackback.config;

import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.repository.CityRepository;
import com.pucp.odiparpackback.repository.DepotRepository;
import com.pucp.odiparpackback.repository.RouteRepository;
import com.pucp.odiparpackback.repository.TruckRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.apache.lucene.util.SloppyMath.haversinMeters;

@Component
@Order(1)
class FileLoader implements CommandLineRunner {

  private static final Logger logger = LogManager.getLogger(FileLoader.class);

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private DepotRepository depotRepository;

  @Autowired
  private TruckRepository truckRepository;

  @Autowired
  private RouteRepository routeRepository;

  public double calculateDistanceInMeters(City city1, City city2) {
    return haversinMeters(Double.parseDouble(city1.getLatitude()), Double.parseDouble(city1.getLongitude()),
      Double.parseDouble(city2.getLatitude()), Double.parseDouble(city2.getLongitude()));
  }

  @Override
  public void run(String... args) throws Exception {
//
//    BufferedReader reader;
//
//    logger.info("Cargando archivos de datos de ciudades");
//    try {
//      reader = new BufferedReader(new FileReader("D:/JavaProjects/OdiparPack-Back/src/main/resources/files/inf226.oficinas.txt"));
//      do {
//        String line = reader.readLine();
//        if (line == null) {
//          break;
//        }
//        logger.info("{}", line);
//        String[] split = line.split(",");
//        City city = new City();
//        city.setUbigeo(split[0]);
//        city.setName(split[2]);
//        city.setLatitude(split[3]);
//        city.setLongitude(split[4]);
//        if (split[5].equals("SELVA")) {
//          city.setRegion(Region.JUNGLE);
//        }
//        if (split[5].equals("COSTA")) {
//          city.setRegion(Region.COST);
//        }
//        if (split[5].equals("SIERRA")) {
//          city.setRegion(Region.HIGHLAND);
//        }
//        cityRepository.save(city);
//        logger.info("Cities loaded");
//      } while (true);
//    } catch (Exception e) {
//      logger.error("Error al cargar los archivos de datos", e);
//    }
//    logger.info("loading depots");
//    Depot depot = new Depot();
//    depot.setCity(cityRepository.findByUbigeo("040101"));
//    depot.setName("Almacén de Arequipa");
//    depotRepository.save(depot);
//
//    Depot depot2 = new Depot();
//    depot2.setCity(cityRepository.findByUbigeo("150101"));
//    depot2.setName("Almacén de Arequipa");
//    depotRepository.save(depot2);
//
//    Depot depot3 = new Depot();
//    depot3.setCity(cityRepository.findByUbigeo("130101"));
//    depot3.setName("Almacén de Arequipa");
//    depotRepository.save(depot3);
//    logger.info("depots loaded");
//    logger.trace("loading Routes");
//
//
//  /*
//    try {
//      reader = new BufferedReader(new FileReader("D:/JavaProjects/OdiparPack-Back/src/main/resources/files/inf226.tramos.v.2.0.txt"));
//      do {
//        Route route = new Route();
//        String line = reader.readLine();
//        if (line == null) {
//          break;
//        }
//        String[] split = line.split("=>");
//        logger.info("{}", split);
//        logger.info("{}", cityRepository.findByUbigeo(split[0]));
//        logger.info("{}", cityRepository.findByUbigeo(split[1]));
//        route.setFromCity(cityRepository.findByUbigeo(split[0]));
//        route.setToCity(cityRepository.findByUbigeo(split[1]));
//        route.setDistance(calculateDistanceInMeters(route.getFromCity(), route.getToCity()));
//        route.setSpeed(0.0);
//        route.setConnected(true);
//        routeRepository.save(route);
//      } while (true);
//    } catch (Exception e) {
//      logger.error("Error al cargar los archivos de datos", e);
//    }
//    logger.trace("Routes loaded");
//    
//    */
//    Truck truck = new Truck();
//    truck.setCapacity(90D);
//    truck.setPlate("123456");
//    truck.setAvailable(true);
//    truck.setCurrentCity(cityRepository.findByName("LIMA"));
//    for (int i = 0 ; i < 4 ; i++) {
//      truck = new Truck();
//      truck.setCapacity(90D);
//      truck.setPlate("123456");
//      truck.setAvailable(true);
//      truck.setCurrentCity(cityRepository.findByName("LIMA"));
//      truckRepository.save(truck);
//    }
//
//    for (int i = 0 ; i < 7 ; i++) {
//      truck = new Truck();
//      truck.setCapacity(45D);
//      truck.setPlate("123456");
//      truck.setAvailable(true);
//      truck.setCurrentCity(cityRepository.findByName("LIMA"));
//      truckRepository.save(truck);
//    }
//
//    for (int i = 0 ; i < 10 ; i++) {
//      truck = new Truck();
//      truck.setCapacity(30D);
//      truck.setPlate("123456");
//      truck.setAvailable(true);
//      truck.setCurrentCity(cityRepository.findByName("LIMA"));
//      truckRepository.save(truck);
//    }
//
//    truck = new Truck();
//    truck.setCapacity(90D);
//    truck.setPlate("123456");
//    truck.setAvailable(true);
//    truck.setCurrentCity(cityRepository.findByName("TRUJILLO"));
//    truck.setCapacity(45D);
//    truckRepository.save(truck);
//    for (int i = 0 ; i < 3 ; i++) {
//      truck = new Truck();
//      truck.setCapacity(45D);
//      truck.setPlate("123456");
//      truck.setAvailable(true);
//      truck.setCurrentCity(cityRepository.findByName("TRUJILLO"));
//      truckRepository.save(truck);
//    }
//
//    truckRepository.save(truck);
//    for (int i = 0 ; i < 6 ; i++) {
//      truck = new Truck();
//      truck.setCapacity(30D);
//      truck.setPlate("123456");
//      truck.setAvailable(true);
//      truck.setCurrentCity(cityRepository.findByName("TRUJILLO"));
//      truckRepository.save(truck);
//    }
//
//    truck = new Truck();
//    truck.setCapacity(90D);
//    truck.setPlate("123456");
//    truck.setAvailable(true);
//    truck.setCurrentCity(cityRepository.findByName("AREQUIPA"));
//    truckRepository.save(truck);
//
//    truck.setCapacity(45D);
//    for (int i = 0 ; i < 5 ; i++) {
//      truck = new Truck();
//      truck.setCapacity(45D);
//      truck.setPlate("123456");
//      truck.setAvailable(true);
//      truck.setCurrentCity(cityRepository.findByName("AREQUIPA"));
//      truckRepository.save(truck);
//    }
//
//    for (int i = 0 ; i < 8 ; i++) {
//      truck = new Truck();
//      truck.setCapacity(30D);
//      truck.setPlate("123456");
//      truck.setAvailable(true);
//      truck.setCurrentCity(cityRepository.findByName("AREQUIPA"));
//      truckRepository.save(truck);
//    }
  }

}
