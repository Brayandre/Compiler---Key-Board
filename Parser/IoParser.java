package parser;
import java.util.List;
import lexer.TipoToken;
import lexer.Token;
public class IoParser extends PrincipalParser {

    //DECLARAÇÕES UNICAS

    public IoParser(List<Token> tokens, int startPos) {
        super(tokens, startPos);
    }


    // cmdLeitura -> ’insert’ ’(’ ID ’)’  ‘$’ 

    public void cmdLeitura() {
        consume(TipoToken.INSERT);
        consume(TipoToken.AP);
        consume(TipoToken.ID);
        consume(TipoToken.FP);
        consume(TipoToken.CIF);
    }

    // cmdEscrita -> ’prt_scr ’(’ conteudo ’)’  ‘$‘

    public void cmdEscrita() {
        consume(TipoToken.PRINT);
        consume(TipoToken.AP);
        conteudo();
        consume(TipoToken.FP);
        consume(TipoToken.CIF);
    }

    // conteudo -> “TEXTO” | ID |“TEXTO“ # ID “TEXTO“ # ID |“TEXTO“ # ID “TEXTO“| 

    private void conteudo() {
        conteudoItem();
        while (check(TipoToken.HASH)) {
            consume(TipoToken.HASH);
            if (check(TipoToken.TEXT, TipoToken.ID)) {
                conteudoItem();
            }
        }
    }

    private void conteudoItem() {
        if (check(TipoToken.TEXT)) {
            consume(TipoToken.TEXT);
        } else if (check(TipoToken.ID)) {
            consume(TipoToken.ID);
        } else {
            throw new RuntimeException("[ERRO SINTÁTICO] Conteúdo de prt_scr deve ser TEXTO ou ID, " + "encontrado: \"" + peek().getLexema() + "\" na posição " + pos);
        }
    }
}