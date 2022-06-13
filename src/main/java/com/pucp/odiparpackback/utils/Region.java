package com.pucp.odiparpackback.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Region {
  COSTA(24),
  SIERRA(48),
  SELVA(72);

  private final double maxHours;
}
