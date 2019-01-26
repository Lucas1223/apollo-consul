package com.ctrip.framework.apollo.metaservice.service;

import com.ctrip.framework.apollo.core.ServiceNameConsts;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.health.model.Check;
import com.ecwid.consul.v1.health.model.HealthService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DiscoveryService {

  @Autowired
  private ConsulClient consulClient;

  public List<HealthService> getConfigServiceInstances() {
    removeService();
    Response<List<HealthService>> services = consulClient.getHealthServices(ServiceNameConsts.APOLLO_CONFIGSERVICE,true,null);
    if (services == null) {
      Tracer.logEvent("Apollo.ConsulDiscovery.NotFound", ServiceNameConsts.APOLLO_CONFIGSERVICE);
    }
    return services != null ? services.getValue() : Collections.emptyList();
  }

  public List<HealthService> getMetaServiceInstances() {
    removeService();
    Response<List<HealthService>> services = consulClient.getHealthServices(ServiceNameConsts.APOLLO_METASERVICE,true,null);
    if (services == null) {
      Tracer.logEvent("Apollo.EurekaDiscovery.NotFound", ServiceNameConsts.APOLLO_METASERVICE);
    }
    return services != null ? services.getValue() : Collections.emptyList();
  }

  public List<HealthService> getAdminServiceInstances() {
    removeService();
    Response<List<HealthService>> services = consulClient.getHealthServices(ServiceNameConsts.APOLLO_ADMINSERVICE,true,null);
    if (services == null) {
      Tracer.logEvent("Apollo.EurekaDiscovery.NotFound", ServiceNameConsts.APOLLO_ADMINSERVICE);
    }
    return services != null ? services.getValue() : Collections.emptyList();
  }

  @Async
  public void removeService() {
    Response<List<Check>> response = consulClient.getHealthChecksState(null);
    if (response == null) {
      return;
    }
    List<Check> checkList = response.getValue();
    if (checkList == null) {
      return;
    }
    for (Check check : checkList) {
      if (!Check.CheckStatus.PASSING.equals(check.getStatus())) {
        System.out.println(">>>>>>>>>>>>>>清除consul上无效服务：" + new Gson().toJson(check));
        consulClient.agentServiceDeregister(check.getServiceId());
      }
    }
  }
}
