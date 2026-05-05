import java.text.CharacterIterator;

public class ArrowAFD extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        if (code.current() != '-') return null;

        char c1 = code.next();
        if (c1 != '-') { code.previous(); return null; }

        char c2 = code.next();
        if (c2 != '>') { code.previous(); code.previous(); return null; }

        code.next(); // avança após o >
        return new Token(TipoToken.ARROW, "-->");
    }
}