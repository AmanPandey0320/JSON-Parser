package json;

import json.annotation.JsonClass;
import json.annotation.JsonProperty;
import json.annotation.Type;

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
    private Boolean isGa;

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getEngine_version() {
        return engine_version;
    }

    public void setEngine_version(String engine_version) {
        this.engine_version = engine_version;
    }

    public Boolean getIsGa() {
        return isGa;
    }

    public void setIsGa(Boolean ga) {
        isGa = ga;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @JsonProperty(type = Type.ARRAY)
    private ArrayList<String> tags;
}
