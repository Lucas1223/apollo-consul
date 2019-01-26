package com.ctrip.framework.apollo.biz.cosul;


import com.ctrip.framework.apollo.biz.config.BizConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.ConsulProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ApolloConsulClientConfig extends ConsulProperties {

  @Autowired
  private BizConfig bizConfig;

  public ApolloConsulClientConfig() {
  }

  /**
   * Assert only one zone: defaultZone, but multiple environments.
   */
  @Override
  public String getHost() {
    String host = bizConfig.consulServiceHosts();
    return StringUtils.isEmpty(host)?super.getHost():host;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }
}
