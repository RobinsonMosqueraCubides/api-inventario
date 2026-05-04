package com.inventory.domain.model;

public enum CategoryStatus {
  ACTIVA,
  INACTIVA;

  public String label() {
    return this == ACTIVA ? "Activa" : "Inactiva";
  }

  public static CategoryStatus fromLabel(String value) {
    return "Inactiva".equalsIgnoreCase(value) || "INACTIVA".equalsIgnoreCase(value) ? INACTIVA : ACTIVA;
  }
}
