package json.util;

import json.parsor.JsonTokens;

public class TokenUtil {
    public static boolean isDigit(char ch){
        if('0' <= ch && ch <= '9'){
            return true;
        }
        return false;
    }

    public static boolean isWhiteSpace(char ch) {
        if (ch == ' ') {
            return true;
        } else if (ch == '\t') {
            return true;
        } else if (ch == '\n') {
            return true;
        } else if (ch == '\r') {
            return true;
        }
        return false;
    }

    public static boolean isHex(char ch) {
        if (ch >= '0' && ch <= '9') {
            return true;
        } else if (ch >= 'a' && ch <= 'f') {
            return true;
        } else if (ch >= 'A' && ch <= 'F') {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isLetter(char ch) {
        if (ch >= 'a' && ch <= 'z') {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEndofString(char ch){
        if(ch == JsonTokens.DOUBLE_QUOTE) return true;
        return false;
    }


    /**
     *
     * @description checks for escape character in the json string
     * */
    public static char getEscape(char c){
        switch(c){
            case '"':
                return '"';
            case '\\':
                return '\\';
            case '/':
                return '/';
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            case 'u':
                return 'u';
            default:
                return JsonTokens.NULL_CHAR;
        }
    }
}
