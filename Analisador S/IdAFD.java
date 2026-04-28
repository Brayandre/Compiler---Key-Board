import java.text.CharacterIterator;

public class IdAFD extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char atual = code.current();

        if (Character.isLetter(atual)) {
            StringBuilder lexemaBuilder = new StringBuilder();

            while (Character.isLetterOrDigit(code.current())) {
                lexemaBuilder.append(code.current());
                code.next();
            }

            String lexema = lexemaBuilder.toString();

            // palavras reservadas
            switch (lexema) {
                case "CAPS": return new Token(TipoToken.CAPS, lexema);
                case "SET": return new Token(TipoToken.SET, lexema);
                case "ALT": return new Token(TipoToken.ALT, lexema);
                case "SHIFT": return new Token(TipoToken.SHIFT, lexema);
                case "ALTGR": return new Token(TipoToken.ALTGR, lexema);
                case "insert": return new Token(TipoToken.INSERT, lexema);
                case "prt_scr": return new Token(TipoToken.PRINT, lexema);
                default: return new Token(TipoToken.ID, lexema);
            }
        }

        return null;
    }
}