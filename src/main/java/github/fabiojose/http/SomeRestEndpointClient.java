package github.fabiojose.http;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author fabiojose
 */
@FeignClient(
    name = "SomeRestClientHTTP",
    url = "${app.some.http.endpoint.url}",
    fallback = SomeRestEndpointFallback.class
)
public interface SomeRestEndpointClient {

    @PostMapping(
        value = "/some/v1",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    void post(
        @Valid
        @RequestBody(required = true) SomeReq request);

}