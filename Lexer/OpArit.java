package lexer;
import java.text.CharacterIterator;

public class OpArit extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char atual = code.current();

        // verifica se faz parte dos operadores aritmeticos
        switch (atual) {
            case '+': 
                code.next(); 
                return new Token(TipoToken.OP_ARIT, "+");
            case '-': 
                code.next(); 
                return new Token(TipoToken.OP_ARIT, "-");
            case '*': 
                code.next(); 
                return new Token(TipoToken.OP_ARIT, "*");
            case '/': 
                code.next(); 
                return new Token(TipoToken.OP_ARIT, "/");
        }

        return null;
    }
}