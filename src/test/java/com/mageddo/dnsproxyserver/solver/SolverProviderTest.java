package com.mageddo.dnsproxyserver.solver;

import com.mageddo.dnsproxyserver.config.application.Configs;
import com.mageddo.dnsproxyserver.quarkus.Instances;
import com.mageddo.dnsproxyserver.solver.Solver;
import com.mageddo.dnsproxyserver.solver.SolverMock;
import com.mageddo.dnsproxyserver.solver.SolverProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class SolverProviderTest {

  @Test
  void mustDisableRemoteSolversWhenNoRemoteServersOptionIsEnabled() {
    // arrange
    final var config = spy(Configs.getInstance());

    doReturn(true)
      .when(config)
      .getNoRemoteServers();

    final var solvers = Instances.<Solver>of(
      new SolverMock("SolverSystem"),
      new SolverMock("SolverDocker"),
      new SolverMock("SolverLocalDB"),
      new SolverMock("SolverCachedRemote")
    );
    final var provider = spy(new SolverProvider(solvers, config));

    // act
    final var names = provider.getSolversNames();

    // assert
    assertEquals("[SolverSystem, SolverDocker, SolverLocalDB]", names.toString());
  }
}
