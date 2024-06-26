package core.nxg.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class GeonamesNearbyResponse {
    private List<Geoname> geonames;

}
