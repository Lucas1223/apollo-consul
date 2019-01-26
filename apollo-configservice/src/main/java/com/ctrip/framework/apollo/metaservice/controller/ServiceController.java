package com.ctrip.framework.apollo.metaservice.controller;

import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.metaservice.service.DiscoveryService;
import com.ecwid.consul.v1.health.model.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
public class ServiceController {

  @Autowired
  private DiscoveryService discoveryService;


  @RequestMapping("/meta")
  public List<ServiceDTO> getMetaService() {
    List<HealthService> instances = discoveryService.getMetaServiceInstances();
    List<ServiceDTO> result = instances.stream().map(new Function<HealthService, ServiceDTO>() {

      @Override
      public ServiceDTO apply(HealthService service) {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setAppName(service.getService().getService());
        serviceDTO.setInstanceId(service.getService().getId());
        serviceDTO.setHomepageUrl(getHomePageUrl(service.getService().getAddress(),service.getService().getPort()));
        return serviceDTO;
      }

    }).collect(Collectors.toList());
    return result;
  }

  @RequestMapping("/config")
  public List<ServiceDTO> getConfigService(
      @RequestParam(value = "appId", defaultValue = "") String appId,
      @RequestParam(value = "ip", required = false) String clientIp) {
    List<HealthService> instances = discoveryService.getConfigServiceInstances();
    List<ServiceDTO> result = instances.stream().map(new Function<HealthService, ServiceDTO>() {

      @Override
      public ServiceDTO apply(HealthService service) {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setAppName(service.getService().getService());
        serviceDTO.setInstanceId(service.getService().getId());
        serviceDTO.setHomepageUrl(getHomePageUrl(service.getService().getAddress(),service.getService().getPort()));
        return serviceDTO;
      }

    }).collect(Collectors.toList());
    return result;
  }

  @RequestMapping("/admin")
  public List<ServiceDTO> getAdminService() {
    List<HealthService> instances = discoveryService.getAdminServiceInstances();
    List<ServiceDTO> result = instances.stream().map(new Function<HealthService, ServiceDTO>() {

      @Override
      public ServiceDTO apply(HealthService service) {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setAppName(service.getService().getService());
        serviceDTO.setInstanceId(service.getService().getId());
        serviceDTO.setHomepageUrl(getHomePageUrl(service.getService().getAddress(),service.getService().getPort()));
        return serviceDTO;
      }

    }).collect(Collectors.toList());
    return result;
  }

  private String getHomePageUrl(String serviceAddress,Integer servicePort){
    //http://11.1.98.15:3000/
    StringBuilder url = new StringBuilder();
    url.append("http://");
    url.append(serviceAddress);
    url.append(":"+servicePort+"/");
    return url.toString();
  }
}
