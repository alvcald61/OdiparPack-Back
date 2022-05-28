package com.pucp.odiparpackback.config;

import com.pucp.odiparpackback.repository.CityRepository;
import com.pucp.odiparpackback.repository.DepotRepository;
import com.pucp.odiparpackback.repository.TruckRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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

  @Override
  public void run(String... args) throws Exception {
//    BufferedReader reader;
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
//        city.setLongitude(split[3]);
//        city.setLatitude(split[4]);
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
  }

}
