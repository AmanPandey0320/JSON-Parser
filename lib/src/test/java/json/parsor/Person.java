package json.parsor;

import json.parsor.annotation.JsonClass;
import json.parsor.annotation.JsonProperty;
import json.parsor.annotation.Type;

@JsonClass
public class Person {

    @JsonProperty(type = Type.STRING,name = "naam")
    public String name;
}
