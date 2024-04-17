package org.ntnu.idi.idatt2106.sparesti.sparestibackend.enums;

import lombok.Getter;

@Getter
public enum UserMotivation {
  VERY_HIGH(5), HIGH(4), MIDDLE(3), LOW(2), VERY_LOW(1);

  private final int val;

  UserMotivation(int val) {
    this.val = val;
  }
}
