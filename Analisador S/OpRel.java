import java.text.CharacterIterator;

public class OpRel extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char atual = code.current();

        if (atual == '!') {
            code.next();
            if (code.current() == '=') {
                code.next();
                return new Token(TipoToken.OP_REL, "!=");
            }
            return null;
        }

        if (atual == '<' || atual == '>' || atual == '=') {
            String lexema = String.valueOf(atual);
            code.next();

            if (code.current() == '=') {
                lexema += "="; 
                code.next();  
            }

            return new Token(TipoToken.OP_REL, lexema);
        }

        return null; 
    }
}