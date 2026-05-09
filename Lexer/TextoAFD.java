package lexer;
import java.text.CharacterIterator;

public class TextoAFD extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char atual = code.current();

        //verifica a construção na definição de strings, Ex: "compiladores"

        if (atual == '"') {
            StringBuilder lexema = new StringBuilder();
            code.next(); // pula "

            while (code.current() != '"' && code.current() != CharacterIterator.DONE) {
                lexema.append(code.current());
                code.next();
            }

            if (code.current() == '"') {
                code.next(); // fecha "
                return new Token(TipoToken.TEXT, lexema.toString());
            }
        }

        return null;
    }
}