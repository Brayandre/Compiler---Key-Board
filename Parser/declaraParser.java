package parser;
import java.util.List;
import lexer.TipoToken;
import lexer.Token;
public class DeclaraParser extends PrincipalParser {
 
    public DeclaraParser(List<Token> tokens, int startPos) {
        super(tokens, startPos);
    }
 
    // declara -> ‘CAPS’ tipo ID ‘$’ 

    public void declara() {
        consume(TipoToken.CAPS);
        consumeTipo();
        consume(TipoToken.ID);
        consume(TipoToken.CIF);
    }
   
    // declara do for
    public void declaraFor() {
        if (check(TipoToken.CAPS)) {
            consume(TipoToken.CAPS);
        }
        tipo();
        consume(TipoToken.ID);
        consume(TipoToken.CIF);
    }
 
    public void consumeTipo() {
        if (check(TipoToken.NUMINT, TipoToken.NUMDEC, TipoToken.NUMSTR,
                  TipoToken.NUMBOOL, TipoToken.NUMFLOAT)) {
            pos++;
        } else {
            throw new RuntimeException("[ERRO SINTÁTICO] Tipo esperado (numint, numdec, numstr, numbool, numfloat), " +"encontrado: \"" + peek().getLexema() + "\" na posição " + pos);
        }
    }
}