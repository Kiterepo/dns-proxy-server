package com.mageddo.dnsproxyserver.config.provider.prop;

import com.mageddo.dnsproxyserver.config.dataprovider.VersionDAO;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Properties;

import static com.mageddo.commons.io.IoUtils.loadPropertiesFromResources;

@Singleton
@NoArgsConstructor(onConstructor_ = @Inject)
public class VersionDAOProp implements VersionDAO {

  private static final Properties resources = loadPropertiesFromResources("/application.properties");

  public String findVersion(){
    return resources.getProperty("version", "unknown");
  }

}
