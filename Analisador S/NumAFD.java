import java.text.CharacterIterator;

public class NumAFD extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char atual = code.current();

        if (Character.isDigit(atual)) {
            StringBuilder lexema = new StringBuilder();

            while (Character.isDigit(code.current())) {
                lexema.append(code.current());
                code.next();
            }

            return new Token(TipoToken.NUM, lexema.toString());
        }

        return null;
    }
}