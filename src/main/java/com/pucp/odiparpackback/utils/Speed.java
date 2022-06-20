package com.pucp.odiparpackback.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Speed {
    COSTACOSTA(70),
    COSTASIERRA(50),
    COSTASELVA(55),
    SIERRACOSTA(50),
    SIERRASIERRA(60),
    SIERRASELVA(60),
    SELVACOSTA(55),
    SELVASIERRA(60),
    SELVASELVA(65),
    ;

    private final double speed;
}
