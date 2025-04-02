package com.tesis.backend_tesis.repository.modelo;

public enum EstadoValidacion {
    NO_VALIDADO(0),
    NO_REVISADO(1),
    VALIDADO(2);

    private final int value;

    EstadoValidacion(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EstadoValidacion fromInt(int i) {
        for (EstadoValidacion estado : EstadoValidacion.values()) {
            if (estado.getValue() == i) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Valor inválido para EstadoValidacion: " + i);
    }
}