package system_design.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public enum Currency {
    EUR,
    USD,
    RUB,
    ;
}
