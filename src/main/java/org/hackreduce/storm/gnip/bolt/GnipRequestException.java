package org.hackreduce.storm.gnip.bolt;

public class GnipRequestException extends Exception {
  public GnipRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
