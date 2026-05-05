import java.util.List;

/**
 * Sub-parser responsável pelos comandos de entrada e saída.
 * Regras:
 * cmdLeitura -> 'insert' '(' ID ')' '$'
 * cmdEscrita -> 'prt_scr' '(' conteudo ')' '$'
 * conteudo   -> (TEXTO | ID) ('#' (TEXTO | ID))*
 */
public class IoParser extends ParserBase {

    public IoParser(List<Token> tokens, int startPos) {
        super(tokens, startPos);
    }

    /**
     * cmdLeitura -> 'insert' '(' ID ')' '$'
     */
    public void cmdLeitura() {
        consume(TipoToken.INSERT);
        consume(TipoToken.LPAREN);
        consume(TipoToken.ID);
        consume(TipoToken.RPAREN);
        consume(TipoToken.DOLLAR);
    }

    /**
     * cmdEscrita -> 'prt_scr' '(' conteudo ')' '$'
     */
    public void cmdEscrita() {
        consume(TipoToken.PRINT);
        consume(TipoToken.LPAREN);
        conteudo();
        consume(TipoToken.RPAREN);
        consume(TipoToken.DOLLAR);
    }

    /**
     * conteudo -> (TEXTO | ID) ('#' (TEXTO | ID))*
     * Permite intercalar textos e IDs separados por '#', ex:
     *   "Digite um número:" # x
     *   n1 # "é menor que" # n2 #
     */
    private void conteudo() {
        conteudoItem();
        while (check(TipoToken.HASH)) {
            consume(TipoToken.HASH);
            // '#' pode aparecer no final sem item subsequente (conforme exemplo da GLC)
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