package core.nxg.serviceImpl;

import core.nxg.dto.Geoname;
import core.nxg.dto.GeonamesNearbyResponse;
import core.nxg.dto.GeonamesSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeonamesService {

@Value("${geonames.username}")
private String GEONAMES_USERNAME;

    @Autowired
    private RestTemplate restTemplate;

    public List<String> getNearbyCities(String city) {
        String url = UriComponentsBuilder.fromHttpUrl("http://api.geonames.org/searchJSON")
                .queryParam("q", city)
                .queryParam("maxRows", 1)
                .queryParam("username", GEONAMES_USERNAME)
                .toUriString();

        GeonamesSearchResponse response = restTemplate.getForObject(url, GeonamesSearchResponse.class);

        if (response != null && !response.getGeonames().isEmpty()) {
            Geoname cityDetails = response.getGeonames().get(0);
            return findNearbyPlaces(cityDetails.getLat(), cityDetails.getLng());
        }

        return List.of();
    }

    private List<String> findNearbyPlaces(double lat, double lng) {
        String url = UriComponentsBuilder.fromHttpUrl("http://api.geonames.org/findNearbyPlaceNameJSON")
                .queryParam("lat", lat)
                .queryParam("lng", lng)
                .queryParam("radius", 50) // Radius in kilometers
                .queryParam("username", GEONAMES_USERNAME)
                .toUriString();

        GeonamesNearbyResponse response = restTemplate.getForObject(url, GeonamesNearbyResponse.class);

        if (response != null) {
            return response.getGeonames().stream()
                    .map(Geoname::getName)
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
