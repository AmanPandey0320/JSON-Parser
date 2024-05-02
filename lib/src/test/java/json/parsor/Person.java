package json.parsor;

import json.parsor.annotation.JsonClass;
import json.parsor.annotation.JsonProperty;
import json.parsor.annotation.Type;

import java.util.ArrayList;

@JsonClass
public class Person {

    @JsonProperty(type = Type.STRING,name = "name")
    private String pname;

    @JsonProperty(type = Type.NUMBER)
    private Integer age;

    @JsonProperty(type = Type.BOOLEAN)
    private Boolean isEducated;

    @JsonProperty(type = Type.ARRAY)
    private ArrayList<String> hobbies;

    @JsonProperty(type = Type.OBJECT, nest = Education.class, name = "UG")
    private Education edu;

    public ArrayList<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(ArrayList<String> hobbies) {
        this.hobbies = hobbies;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String name) {
        this.pname = name;
    }

    public Education getEdu() {
        return edu;
    }

    public void setEdu(Education edu) {
        this.edu = edu;
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
