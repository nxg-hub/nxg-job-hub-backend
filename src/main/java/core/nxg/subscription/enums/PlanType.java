package core.nxg.subscription.enums;



import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum PlanType {
    PLATINUM("annually"),

    GOLD("biannually"),
    SILVER("quarterly");

    private final String interval;

}
