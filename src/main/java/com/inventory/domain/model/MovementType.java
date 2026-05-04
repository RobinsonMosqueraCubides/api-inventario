package com.inventory.domain.model;

public enum MovementType {
  ENTRADA,
  SALIDA,
  AJUSTE;

  public String label() {
    return switch (this) {
      case ENTRADA -> "Entrada";
      case SALIDA -> "Salida";
      case AJUSTE -> "Ajuste";
    };
  }

  public static MovementType fromLabel(String value) {
    return switch (value == null ? "" : value.toLowerCase()) {
      case "entrada" -> ENTRADA;
      case "salida" -> SALIDA;
      case "ajuste" -> AJUSTE;
      default -> throw new IllegalArgumentException("Tipo de movimiento invalido.");
    };
  }
}
