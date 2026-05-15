package lexer;

import java.text.CharacterIterator;

public abstract class AFD {

    public abstract Token evaluate(CharacterIterator code);

    // definição dos AFD
    public boolean isTokenSeparator(CharacterIterator code) {
        return code.current() == ' ' ||
                code.current() == '\n' ||
                code.current() == '+' ||
                code.current() == '-' ||
                code.current() == '*' ||
                code.current() == '/' ||
                code.current() == '(' ||
                code.current() == ')' ||
                code.current() == '{' ||
                code.current() == '}' ||
                code.current() == '>' ||
                code.current() == '<' ||
                code.current() == '=' ||
                code.current() == ')' ||
                code.current() == '!' ||
                code.current() == '#' ||
                code.current() == CharacterIterator.DONE;
    }
}
