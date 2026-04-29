import java.text.CharacterIterator;

public class IdAFD extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char atual = code.current();

        if (Character.isLetter(atual)) {
            StringBuilder lexemaBuilder = new StringBuilder();

            while (Character.isLetterOrDigit(code.current()) || code.current() == '_') {
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
                case "computador_iniciar":  return new Token(TipoToken.COMPUTADOR_INICIAR, lexema);
                case "computador_encerrar": return new Token(TipoToken.COMPUTADOR_ENCERRAR, lexema);
                case "SETCAPS": return new Token(TipoToken.SETCAPS, lexema);
                case "TAB":     return new Token(TipoToken.TAB, lexema);
                case "ALT_TAB": return new Token(TipoToken.ALT_TAB, lexema);
                case "numint":   return new Token(TipoToken.NUMINT, lexema);
                case "numdec":   return new Token(TipoToken.NUMDEC, lexema);
                case "numstr":   return new Token(TipoToken.NUMSTR, lexema);
                case "numbool":  return new Token(TipoToken.NUMBOOL, lexema);
                case "numfloat": return new Token(TipoToken.NUMFLOAT, lexema);
                default: return new Token(TipoToken.ID, lexema);

            }
        }

        return null;
    }
}