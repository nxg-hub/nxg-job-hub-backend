package core.nxg.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginatedResponse<T> {
    private List<T> content = new ArrayList<>();
    private int currentPage;
    private int pageSize;
    private long totalPages;
    private long totalItems;
    private boolean isFirstPage;
    private boolean isLastPage;

}
