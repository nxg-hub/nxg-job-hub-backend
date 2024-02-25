package core.nxg.subscription.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum TransactionStatus {
    PENDING,
    SUCCESSFUL,
    FAILED
}
