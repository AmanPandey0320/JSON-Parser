/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package json.parsor;

import json.parsor.builder.ObjectBuilder;
import json.parsor.tokeniser.Token;
import json.parsor.tokeniser.Tokeniser;
import org.junit.Test;

import java.util.HashMap;

public class LibraryTest {
//    @Test public void basicTest() {
//        PSON pson = new PSON<MapperClass>();
//        String jsonString = "{\"name\":\"Aman\",\"age\":24,\"isEducated\":false,\"hobbies\":[\"cricket\",\"badminton\",\"guitar\",\"movies\"],\"class10\":{\"isPassed\":true,\"year\":2016,\"board\":\"CBSE\"},\"class12\":{\"isPassed\":true,\"year\":2018,\"board\":\"CBSE\"},\"UG\":{\"isPassed\":true,\"year\":2023,\"board\":\"NITJSR\"}}";
////        String jsonString = "{unquoted:'andyoucanquotemeonthat',singleQuotes:'Icanuse\"doublequotes\"here',lineBreaks:\"Look,Mom!\\No\\\\n's!\",hexadecimal:0xdecaf,leadingDecimalPoint:.8675309,andTrailing:8675309.,positiveSign:+1,trailingComma:'inobjects',andIn:['arrays',],\"backwardsCompatible\":\"withJSON\",}";
//        Tokeniser tokeniser = new Tokeniser(jsonString);
//        System.out.println("\n\n---------------- TEST CASE 1 --------------------\n\n");
//        for(Token token: tokeniser.getTokens()){
//            System.out.println(token.getTokenType());
//            System.out.println(token.getTokenValue());
//            System.out.println("----------------------------------------------------");
//        }
//    }
//
//    @Test public void hexTest() {
//        PSON pson = new PSON<MapperClass>();
//        String jsonString = "{\"hex_value\":\"\\u004A\"}";
//
//        System.out.println("\n\n---------------- TEST CASE 2 --------------------\n\n");
//
//        Tokeniser tokeniser = new Tokeniser(jsonString);
//        for(Token token: tokeniser.getTokens()){
//            System.out.println(token.getTokenType());
//            System.out.println(token.getTokenValue());
//            System.out.println("----------------------------------------------------");
//        }
//    }

    @Test public void builderTest() {
        PSON pson = new PSON<Person>();
        String jsonString = "{\"name\":\"Aman Kr Pandey\"}";

        System.out.println("\n\n---------------- TEST CASE 3 --------------------\n\n");

        Tokeniser tokeniser = new Tokeniser(jsonString);
        for(Token token: tokeniser.getTokens()){
            System.out.println(token.getTokenType());
            System.out.println(token.getTokenValue());
            System.out.println("----------------------------------------------------");
        }

        ObjectBuilder objectBuilder = new ObjectBuilder(tokeniser.getTokens(),new Person());
        Person person = (Person) objectBuilder.build();
    }
}

