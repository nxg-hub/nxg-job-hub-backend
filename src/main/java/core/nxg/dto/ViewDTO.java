package core.nxg.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ViewDTO {

    private Long viewId;
    private int viewCount;
    
}
