package com.agriculture.mauritanie.exception;

/**
 * Exception lancée lors des violations de contraintes de données
 */
public class DataIntegrityException extends RuntimeException {

  public DataIntegrityException(String message) {
    super(message);
  }

  public DataIntegrityException(String message, Throwable cause) {
    super(message, cause);
  }
}
