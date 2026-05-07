import java.util.List;
public class IoParser extends PrincipalParser {

    public IoParser(List<Token> tokens, int startPos) {
        super(tokens, startPos);
    }


    // cmdLeitura -> ’insert’ ’(’ ID ’)’  ‘$’ 

    public void cmdLeitura() {
        consume(TipoToken.INSERT);
        consume(TipoToken.AP);
        consume(TipoToken.ID);
        consume(TipoToken.FP);
        consume(TipoToken.DOLLAR);
    }

    // cmdEscrita -> ’prt_scr ’(’ conteudo ’)’  ‘$‘

    public void cmdEscrita() {
        consume(TipoToken.PRINT);
        consume(TipoToken.AP);
        conteudo();
        consume(TipoToken.FP);
        consume(TipoToken.DOLLAR);
    }

    // conteudo -> “TEXTO” | ID |“TEXTO“ # ID “TEXTO“ # ID |“TEXTO“ # ID “TEXTO“| 

    private void conteudo() {
        conteudoItem();
        while (check(TipoToken.HASH)) {
            consume(TipoToken.HASH);
            if (check(TipoToken.TEXTO, TipoToken.ID)) {
                conteudoItem();
            }
        }
    }

    private void conteudoItem() {
        if (check(TipoToken.TEXTO)) {
            consume(TipoToken.TEXTO);
        } else if (check(TipoToken.ID)) {
            consume(TipoToken.ID);
        } else {
            throw new RuntimeException(
                "[ERRO SINTÁTICO] Conteúdo de prt_scr deve ser TEXTO ou ID, " +
                "encontrado: \"" + peek().getLexema() + "\" na posição " + pos
            );
        }
    }
}