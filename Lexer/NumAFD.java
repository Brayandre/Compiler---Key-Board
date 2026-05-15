package lexer;

import java.text.CharacterIterator;

public class NumAFD extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        if (!Character.isDigit(code.current()))
            return null;

        StringBuilder lexema = new StringBuilder();

        // verifica a presença de numeros, podendo ser decimal
        while (Character.isDigit(code.current())) {
            lexema.append(code.current());
            code.next();
        }

        if (code.current() == '.') {
            char proximo = code.next();
            if (Character.isDigit(proximo)) {
                lexema.append('.');
                while (Character.isDigit(code.current())) {
                    lexema.append(code.current());
                    code.next();
                }
            } else {
                code.previous();
            }
        }

        return new Token(TipoToken.NUM, lexema.toString());
    }
}