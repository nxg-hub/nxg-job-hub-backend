package core.nxg.subscription;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.*;
import reactor.util.annotation.NonNullApi;

import java.io.IOException;
import java.nio.charset.Charset;

public class APIErrorHandler extends DefaultResponseErrorHandler {

    @Override
    protected void handleError(@Nonnull ClientHttpResponse response,@Nonnull HttpStatusCode code) throws IOException, RestClientException, HttpClientErrorException {

        String message = new String(getResponseBody(response),  getCharset(response));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(message);
        message = rootNode.path("message").asText();

        byte[] body = getResponseBody(response);
        Charset charset = getCharset(response);
        HttpClientErrorException ex = null;
        HttpHeaders headers = response.getHeaders();
        if (response.getStatusCode().is5xxServerError()) {
            ex = HttpClientErrorException.create(message, HttpStatus.INTERNAL_SERVER_ERROR, message, headers, null, null);
            System.out.println("Error occurred. Requesting.... From PayStack: " + code);
            System.out.println("Server error occurred: " + response);

        } else if (response.getStatusCode().is4xxClientError()) {

            ex = HttpClientErrorException.create(message, HttpStatus.BAD_REQUEST, message, headers, null , null);
            System.out.println("Error occurred. Requesting.... From PayStack: " + code);
            System.out.println("Client error occurred: " + response);
        }

        assert ex != null;
        throw ex;
        }



    }



