package com.mageddo.dnsproxyserver.config.provider.cmdargs.mapper;

import com.mageddo.dnsproxyserver.config.Config;
import com.mageddo.dnsproxyserver.config.Log;
import com.mageddo.dnsproxyserver.config.Server;
import com.mageddo.dnsproxyserver.config.SolverDocker;
import com.mageddo.dnsproxyserver.config.SolverRemote;
import com.mageddo.dnsproxyserver.config.SolverStub;
import com.mageddo.dnsproxyserver.config.SolverSystem;
import com.mageddo.dnsproxyserver.config.mapper.LogLevelMapper;
import com.mageddo.dnsproxyserver.config.provider.cmdargs.vo.ConfigFlag;
import com.mageddo.dnsproxyserver.utils.Booleans;
import com.mageddo.utils.Files;

public class ConfigFlagMapper {
  public static Config toConfig(ConfigFlag config) {
    return Config.builder()
      .server(Server
        .builder()
        .dnsServerNoEntriesResponseCode(config.getNoEntriesResponseCode())
        .webServerPort(config.getWebServerPort())
        .dnsServerPort(config.getDnsServerPort())
        .build()
      )
      .configPath(Files.pathOf(config.getConfigFilePath()))
      .log(Log
        .builder()
        .file(config.getLogToFile())
        .level(LogLevelMapper.mapLogLevelFrom(config.getLogLevel()))
        .build()
      )
      .defaultDns(Config.DefaultDns.builder()
        .active(config.getDefaultDns())
        .resolvConf(Config.DefaultDns.ResolvConf
          .builder()
          .overrideNameServers(config.getResolvConfOverrideNameServers())
          .build()
        )
        .build()
      )
      .solverRemote(SolverRemote
        .builder()
        .active(Booleans.reverseWhenNotNull(config.getNoRemoteServers()))
        .build()
      )
      .solverStub(SolverStub
        .builder()
        .domainName(config.getStubSolverDomainName())
        .build()
      )
      .solverDocker(SolverDocker
        .builder()
        .hostMachineFallback(config.getDockerSolverHostMachineFallbackActive())
        .dpsNetwork(SolverDocker.DpsNetwork
          .builder()
          .autoCreate(config.getDpsNetwork())
          .autoConnect(config.getDpsNetworkAutoConnect())
          .build()
        )
        .dockerDaemonUri(config.getDockerHost())
        .registerContainerNames(config.getRegisterContainerNames())
        .domain(config.getDomain())
        .build()
      )
      .solverSystem(SolverSystem
        .builder()
        .hostMachineHostname(config.getHostMachineHostname())
        .build()
      )
      .source(Config.Source.FLAG)
      .build();
  }
}
