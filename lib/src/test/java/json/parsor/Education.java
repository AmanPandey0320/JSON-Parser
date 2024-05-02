package json.parsor;

import json.parsor.annotation.JsonClass;
import json.parsor.annotation.JsonProperty;
import json.parsor.annotation.Type;

@JsonClass
public class Education {

    @JsonProperty(type = Type.BOOLEAN)
    private Boolean isPassed;

    @JsonProperty(type = Type.NUMBER)
    private Integer year;

    @JsonProperty(type = Type.STRING)
    private String board;

    public Boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(Boolean passed) {
        isPassed = passed;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }
}
