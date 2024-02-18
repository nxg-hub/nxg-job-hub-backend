package core.nxg.subscription.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum EventType {
    CUSTOMER_IDENTIFICATION_FAILED("customeridentification.failed"),
    CUSTOMER_IDENTIFICATION_SUCCESS("customeridentification.success");

    private final String event;

    EventType(String event) {
        this.event = event;
    }



}
