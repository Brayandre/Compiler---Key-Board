import java.text.CharacterIterator;

public class DelimitadorAFD extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char atual = code.current();

        switch (atual) {
            case '$': 
                code.next(); 
                return new Token(TipoToken.EOF,  "$");

            case '#': 
                code.next(); 
                return new Token(TipoToken.HASH,    "#");
            case ':': 
                code.next(); 
                return new Token(TipoToken.TP,   ":");
            case '(': 
                code.next(); 
                return new Token(TipoToken.LPAREN,  "(");
            case ')': 
                code.next(); 
                return new Token(TipoToken.RPAREN,  ")");
            case '{': 
                code.next(); 
                return new Token(TipoToken.LBRACE,  "{");
            case '}': 
                code.next(); 
                return new Token(TipoToken.RBRACE,  "}");
        }

        return null;
    }
}