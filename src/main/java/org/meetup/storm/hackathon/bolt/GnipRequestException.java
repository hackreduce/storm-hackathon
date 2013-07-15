package org.meetup.storm.hackathon.bolt;

public class GnipRequestException extends Exception {
  public GnipRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
