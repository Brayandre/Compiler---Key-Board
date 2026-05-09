package lexer;
import java.text.CharacterIterator;

public class DelimitadorAFD extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char atual = code.current();

        // verifica se o token pego faz parte da gramatica dos delimitadores
        switch (atual) {
            case '$': 
                code.next(); 
                return new Token(TipoToken.CIF,  "$");
            case '#': 
                code.next(); 
                return new Token(TipoToken.HASH,    "#");
            case ':': 
                code.next(); 
                return new Token(TipoToken.TWOP,   ":");
            case '(': 
                code.next(); 
                return new Token(TipoToken.AP,  "(");
            case ')': 
                code.next(); 
                return new Token(TipoToken.FP,  ")");
            case '{': 
                code.next(); 
                return new Token(TipoToken.AC,  "{");
            case '}': 
                code.next(); 
                return new Token(TipoToken.FC,  "}");
        }

        return null;
    }
}