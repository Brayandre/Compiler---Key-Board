import java.util.List;

public class DeclaraParser extends ParserBase {
 
    public DeclaraParser(List<Token> tokens, int startPos) {
        super(tokens, startPos);
    }
 
    public void declara() {
        consume(TipoToken.CAPS);
        tipo();
        consume(TipoToken.ID);
        consume(TipoToken.DOLLAR);
    }
 
    public void declaraFor() {
        if (check(TipoToken.CAPS)) {
            consume(TipoToken.CAPS);
        }
        tipo();
        consume(TipoToken.ID);
        consume(TipoToken.DOLLAR);
    }
 
    public void tipo() {
        if (check(TipoToken.NUMINT, TipoToken.NUMDEC, TipoToken.NUMSTR,
                  TipoToken.NUMBOOL, TipoToken.NUMFLOAT)) {
            pos++;
        } else {
            throw new RuntimeException(
                "[ERRO SINTÁTICO] Tipo esperado (numint, numdec, numstr, numbool, numfloat), " +
                "encontrado: \"" + peek().getLexema() + "\" na posição " + pos
            );
        }
    }
}