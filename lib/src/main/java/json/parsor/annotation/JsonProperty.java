package json.parsor.annotation;

public @interface JsonProperty {
    String name();
    Type.Property type() default Type.Property.STRING;
    Type.Property arrayType() default Type.Property.NONE;
}
