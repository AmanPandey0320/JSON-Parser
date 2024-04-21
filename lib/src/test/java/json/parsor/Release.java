package json.parsor;

import json.parsor.annotation.JsonClass;
import json.parsor.annotation.JsonProperty;
import json.parsor.annotation.Type;

import java.util.ArrayList;

@JsonClass
public class Release {

    @JsonProperty
    private String release_date;

    @JsonProperty
    private String status;

    @JsonProperty
    private String engine;

    @JsonProperty
    private String engine_version;

    @JsonProperty(type = Type.BOOLEAN)
    private boolean isGa;

    @JsonProperty(type = Type.ARRAY)
    private ArrayList<String> tags;
}
