/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package json.parsor;

import json.parsor.builder.ObjectBuilder;
import json.parsor.tokeniser.Token;
import json.parsor.tokeniser.Tokeniser;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class LibraryTest {
    @Test public void basicTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PSON pson = new PSON<Person>();
        String jsonString = "{\"name\":\"Aman\",\"age\":24,\"isEducated\":false,\"hobbies\":[\"cricket\",\"badminton\",\"guitar\",\"movies\"],\"class10\":{\"isPassed\":true,\"year\":2016,\"board\":\"CBSE\"},\"class12\":{\"isPassed\":true,\"year\":2018,\"board\":\"CBSE\"},\"UG\":{\"isPassed\":true,\"year\":2023,\"board\":\"NITJSR\"}}";
//        String jsonString = "{unquoted:'andyoucanquotemeonthat',singleQuotes:'Icanuse\"doublequotes\"here',lineBreaks:\"Look,Mom!\\No\\\\n's!\",hexadecimal:0xdecaf,leadingDecimalPoint:.8675309,andTrailing:8675309.,positiveSign:+1,trailingComma:'inobjects',andIn:['arrays',],\"backwardsCompatible\":\"withJSON\",}";
        System.out.println("\n\n---------------- TEST CASE 1 --------------------\n\n");

        Tokeniser tokeniser = new Tokeniser(jsonString);

        ObjectBuilder objectBuilder = new ObjectBuilder(tokeniser.getTokens(),new Person());
        Person person = (Person) objectBuilder.build();
    }

    @Test public void hexTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PSON pson = new PSON<Person>();
        String jsonString = "{\"hex_value\":\"\\u004A\"}";

        System.out.println("\n\n---------------- TEST CASE 2 --------------------\n\n");

        Tokeniser tokeniser = new Tokeniser(jsonString);

        ObjectBuilder objectBuilder = new ObjectBuilder(tokeniser.getTokens(),new Person());
        Person person = (Person) objectBuilder.build();
    }

    @Test public void builderTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PSON pson = new PSON<Browser>();
        String jsonString = "{\"browsers\":{\"firefox\":{\"name\":\"Firefox\",\"pref_url\":\"about:config\",\"releases\":[{\"release_date\":\"2004-11-09\",\"status\":\"retired\",\"engine\":\"Gecko\",\"engine_version\":\"1.7\",\"tags\":[\"json\",\"parser\"],\"isGa\":1}]}}}";
        System.out.println("\n\n---------------- TEST CASE 3 --------------------\n\n");

        Tokeniser tokeniser = new Tokeniser(jsonString);
        Browser browser = new Browser();
        ObjectBuilder objectBuilder = new ObjectBuilder(tokeniser.getTokens(),browser);
        browser = (Browser) objectBuilder.build();

    }
}

