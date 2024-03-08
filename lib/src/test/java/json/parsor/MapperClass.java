package json.parsor;

import json.parsor.annotation.JsonProperty;
import json.parsor.annotation.Type;

public class MapperClass {

    @JsonProperty(name = "key",type = Type.Property.STRING)
    public String key;
}
