package github.fabiojose.http;

import java.util.UUID;

import lombok.Data;

/**
 * @author fabiojose
 */
@Data
public class SomeReq {
   
    private UUID id;
    private String name;

}