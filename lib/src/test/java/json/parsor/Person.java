package json.parsor;

import json.parsor.annotation.JsonArray;
import json.parsor.annotation.JsonClass;
import json.parsor.annotation.JsonProperty;
import json.parsor.annotation.Type;

import java.util.ArrayList;

@JsonClass
public class Person {

    @JsonProperty(type = Type.STRING,name = "name")
    private String name;

    @JsonProperty(type = Type.NUMBER)
    private Integer age;

    @JsonProperty(type = Type.BOOLEAN)
    private Boolean isEducated;

    @JsonProperty(type = Type.ARRAY)
    @JsonArray(type = String.class)
    private ArrayList<String> hobbies;

    public ArrayList<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(ArrayList<String> hobbies) {
        this.hobbies = hobbies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getIsEducated() {
        return isEducated;
    }

    public void setIsEducated(Boolean educated) {
        isEducated = educated;
    }
}
