package com.inventory.domain.model;

public enum Status {
  ACTIVO,
  INACTIVO;

  public String label() {
    return this == ACTIVO ? "Activo" : "Inactivo";
  }

  public static Status fromLabel(String value) {
    return "Inactivo".equalsIgnoreCase(value) || "INACTIVO".equalsIgnoreCase(value) ? INACTIVO : ACTIVO;
  }
}
