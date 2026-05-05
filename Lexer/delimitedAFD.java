import java.text.CharacterIterator;

public class delimitedAFD extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char atual = code.current();

        switch (atual) {
            case '$': code.next(); return new Token(TipoToken.DOLLAR,  "$");
            case '#': code.next(); return new Token(TipoToken.HASH,    "#");
            case ':': code.next(); return new Token(TipoToken.COLON,   ":");
            case '(': code.next(); return new Token(TipoToken.LPAREN,  "(");
            case ')': code.next(); return new Token(TipoToken.RPAREN,  ")");
            case '{': code.next(); return new Token(TipoToken.LBRACE,  "{");
            case '}': code.next(); return new Token(TipoToken.RBRACE,  "}");
        }

        return null;
    }
}
}
