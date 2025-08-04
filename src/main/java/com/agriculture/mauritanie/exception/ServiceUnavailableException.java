package com.agriculture.mauritanie.exception;

/**
 * Exception lancée quand un service externe n'est pas disponible
 */
public class ServiceUnavailableException extends RuntimeException {

  private final String serviceName;

  public ServiceUnavailableException(String message, String serviceName) {
    super(message);
    this.serviceName = serviceName;
  }

  public ServiceUnavailableException(String message, String serviceName, Throwable cause) {
    super(message, cause);
    this.serviceName = serviceName;
  }

  public String getServiceName() {
    return serviceName;
  }
}