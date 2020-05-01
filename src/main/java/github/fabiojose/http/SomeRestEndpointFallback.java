package github.fabiojose.http;

import javax.validation.Valid;

import org.springframework.stereotype.Component;

/**
 * @author fabiojose
 */
@Component
public class SomeRestEndpointFallback implements SomeRestEndpointClient {

    @Override
    public void post(@Valid SomeReq request) {

    }
    
}