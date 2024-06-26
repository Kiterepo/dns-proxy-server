package com.mageddo.dnsserver;

import org.xbill.DNS.Message;

public interface RequestHandler {
  /**
   * Delegates a query to available solvers.
   * @param query
   * @param kind tcp, udp
   * @return
   */
  Message handle(Message query, String kind);
}
