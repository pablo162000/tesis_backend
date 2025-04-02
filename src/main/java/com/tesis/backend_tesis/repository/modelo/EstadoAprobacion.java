package com.tesis.backend_tesis.repository.modelo;

public enum EstadoAprobacion {
    NO_APROBADO(0),
    EN_REVISON(1),
    APROBADO(2);

    private final int value;

    EstadoAprobacion(int value) {
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
        throw new IllegalArgumentException("Valor inválido para EstadoAprobación: " + i);
    }
}