import java.text.CharacterIterator;

public class OpArit extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char atual = code.current();

        switch (atual) {
            case '+': code.next(); return new Token(TipoToken.OP_ARIT, "+");
            case '-': code.next(); return new Token(TipoToken.OP_ARIT, "-");
            case '*': code.next(); return new Token(TipoToken.OP_ARIT, "*");
            case '/': code.next(); return new Token(TipoToken.OP_ARIT, "/");
        }

        return null;
    }
}