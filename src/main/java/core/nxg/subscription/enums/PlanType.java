package core.nxg.subscription.enums;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PlanType {
    PLATINUM("yearly"),

    GOLD("quarterly"),
    SILVER("monthly");

    private final String interval;

}
