package json.parsor;

import json.parsor.annotation.JsonClass;
import json.parsor.annotation.JsonProperty;
import json.parsor.annotation.Type;

import java.util.ArrayList;

@JsonClass
public class Browser {

    @JsonProperty(name = "browsers.firefox.name", type = Type.STRING)
    private String browserName;

    @JsonProperty(name = "browsers.firefox.pref_url")
    private String browserPrefUrl;

    @JsonProperty(type = Type.ARRAY)
    private ArrayList<Release> releases;

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserPrefUrl() {
        return browserPrefUrl;
    }

    public void setBrowserPrefUrl(String browserPrefUrl) {
        this.browserPrefUrl = browserPrefUrl;
    }

    public ArrayList<Release> getReleases() {
        return releases;
    }

    public void setReleases(ArrayList<Release> releases) {
        this.releases = releases;
    }
}
