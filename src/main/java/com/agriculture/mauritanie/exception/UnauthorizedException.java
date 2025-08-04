package com.agriculture.mauritanie.exception;

/**
 * Exception lancée quand l'utilisateur n'a pas les permissions nécessaires
 */
public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(String message) {
    super(message);
  }

  public UnauthorizedException(String message, Throwable cause) {
    super(message, cause);
  }
}
