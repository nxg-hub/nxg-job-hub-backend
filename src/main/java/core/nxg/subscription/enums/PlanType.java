package core.nxg.subscription.enums;



import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum PlanType {
    PLATINUM("annually"),

    GOLD("quarterly"),
    SILVER("monthly");

    private final String interval;

}
