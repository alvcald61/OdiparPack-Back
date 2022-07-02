package com.pucp.odiparpackback.utils;

import com.pucp.odiparpackback.algorithm.AlgorithmService;
import com.pucp.odiparpackback.repository.*;
import com.pucp.odiparpackback.scheduled.FakeScheduledTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ServerInitializer implements ApplicationRunner {

  private static final Logger log = LogManager.getLogger(ServerInitializer.class);
  private final ProductOrderRepository productOrderRepository;
  private final TransportationPlanRepository planRepository;
  private final TruckRepository truckRepository;
  private final AlgorithmService algorithmService;
  private final CityRepository cityRepository;

  private final RouteRepository routeRepository;


  public ServerInitializer(ProductOrderRepository productOrderRepository, TransportationPlanRepository planRepository, TruckRepository truckRepository, AlgorithmService algorithmService, CityRepository cityRepository, RouteRepository routeRepository) {
    this.productOrderRepository = productOrderRepository;
    this.planRepository = planRepository;
    this.truckRepository = truckRepository;
    this.algorithmService = algorithmService;
    this.cityRepository = cityRepository;
    this.routeRepository = routeRepository;
  }

  public static double distance(double lat1,
                                double lat2, double lon1,
                                double lon2) {

    // The math module contains a function
    // named toRadians which converts from
    // degrees to radians.
    lon1 = Math.toRadians(lon1);
    lon2 = Math.toRadians(lon2);
    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);

    // Haversine formula
    double dlon = lon2 - lon1;
    double dlat = lat2 - lat1;
    double a = Math.pow(Math.sin(dlat / 2), 2)
      + Math.cos(lat1) * Math.cos(lat2)
      * Math.pow(Math.sin(dlon / 2), 2);

    double c = 2 * Math.asin(Math.sqrt(a));

    // Radius of earth in kilometers. Use 3956
    // for miles
    double r = 6371;

    // calculate the result
    return (c * r);
  }

  @Override
  public void run(ApplicationArguments applicationArguments) throws Exception {
//    //get file from resources folder
//    File tramos = new File("D:/JavaProjects/OdiparPack-Back/src/main/resources/files/inf226.tramos.v.2.0.txt");
//    File velocidades = new File("D:/JavaProjects/OdiparPack-Back/src/main/resources/files/inf226.velocidades.txt");
//    BufferedReader tramosReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(
//      new FileInputStream(tramos)), StandardCharsets.UTF_8));
//    BufferedReader velocidadesReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(
//      new FileInputStream(velocidades)), StandardCharsets.UTF_8));
//    String line;
//    Map<String, Double> velocidadesMap = new HashMap<>();
//
//    try {
//      while ((line = velocidadesReader.readLine()) != null) {
//        String[] lineSplit = line.split(" = ");
//        log.info(lineSplit[1].trim().split(" ")[0]);
//        velocidadesMap.put(lineSplit[0].trim(), Double.parseDouble(lineSplit[1].trim().split(" ")[0]));
//      }
//    } catch (Exception e) {
//      log.error("Error reading velocidades file", e);
//    }
//
//    try {
//      while ((line = tramosReader.readLine()) != null) {
//        String[] lineSplit = line.split("=>");
//        City start = cityRepository.findByUbigeo(lineSplit[0]);
//        City finish = cityRepository.findByUbigeo(lineSplit[1]);
//        if (Objects.isNull(start) || Objects.isNull(finish)) {
//          continue;
//        }
//        log.trace("{} => {}", start.getUbigeo(), finish.getUbigeo());
//        Double speed = 0.0;
//        if (start.getRegion().equals(Region.COSTA) && finish.getRegion().equals(Region.COSTA)) {
//          speed = velocidadesMap.get("Costa - Costa");
//        }
//        if (start.getRegion().equals(Region.SIERRA) && finish.getRegion().equals(Region.SIERRA)) {
//          speed = velocidadesMap.get("Sierra - Sierra");
//        }
//        if (start.getRegion().equals(Region.SELVA) && finish.getRegion().equals(Region.SELVA)) {
//          speed = velocidadesMap.get("Selva - Selva");
//        }
//        if ((start.getRegion().equals(Region.COSTA) && finish.getRegion().equals(Region.SELVA)) || (start.getRegion().equals(Region.SELVA) && finish.getRegion().equals(Region.COSTA))) {
//          speed = velocidadesMap.get("Costa - Selva");
//        }
//        if ((start.getRegion().equals(Region.COSTA) && finish.getRegion().equals(Region.SIERRA)) || (start.getRegion().equals(Region.SIERRA) && finish.getRegion().equals(Region.COSTA))) {
//          speed = velocidadesMap.get("Costa - Sierra");
//        }
//        if ((start.getRegion().equals(Region.SELVA) && finish.getRegion().equals(Region.SIERRA)) || (start.getRegion().equals(Region.SIERRA) && finish.getRegion().equals(Region.SELVA))) {
//          speed = velocidadesMap.get("Sierra - Selva");
//        }
//        double distance = distance(Double.parseDouble(start.getLatitude()), Double.parseDouble(finish.getLatitude()),
//          Double.parseDouble(start.getLongitude()), Double.parseDouble(finish.getLongitude()));
//        Route route = Route.builder().connected(true).fromCity(start).toCity(finish).speed(speed).distance(distance).build();
//        routeRepository.save(route);
//      }
//    } catch (Exception e) {
//      log.error("Error reading tramos file", e);
//    }
//
//    FakeScheduledTask scheduledTask = new FakeScheduledTask(productOrderRepository,planRepository,truckRepository,algorithmService,cityRepository);
//    scheduledTask.updateTrucks();

  }
}

