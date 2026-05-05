import java.util.List;
 
/**
 * Regras:
 *   expr   -> fator (op_arit fator)*
 *   fator  -> NUM | ID | '(' expr ')'
 *   op_rel -> '<' | '>' | '<=' | '>=' | '!=' | '=='
 *   op_arit -> '+' | '-' | '*' | '/'
 */
public class ExprParser extends ParserBase {
 
    public ExprParser(List<Token> tokens, int startPos) {
        super(tokens, startPos);
    }
 
    /**
     * expr -> fator (op_arit fator)*
     */
    public void expr() {
        fator();
        while (check(TipoToken.OP_ARIT)) {
            opArit();
            fator();
        }
    }
 
    /**
     * fator -> NUM | ID | '(' expr ')'
     */
    public void fator() {
        if (check(TipoToken.NUM)) {
            consume(TipoToken.NUM);
        } else if (check(TipoToken.ID)) {
            consume(TipoToken.ID);
        } else if (check(TipoToken.LPAREN)) {
            consume(TipoToken.LPAREN);
            expr();
            consume(TipoToken.RPAREN);
        } else {
            throw new RuntimeException(
                "[ERRO SINTÁTICO] Fator esperado (NUM, ID ou expressão entre parênteses), " +
                "encontrado: \"" + peek().getLexema() + "\" (" + tipo() + ") na posição " + pos
            );
        }
    }
 
    /**
     * op_rel -> '<' | '>' | '<=' | '>=' | '!=' | '=='
     */
    public void opRel() {
        if (check(TipoToken.OP_REL)) {
            pos++;
        } else {
            throw new RuntimeException(
                "[ERRO SINTÁTICO] Operador relacional esperado (<, >, <=, >=, !=, ==), " +
                "encontrado: \"" + peek().getLexema() + "\" na posição " + pos
            );
        }
    }
 
    /**
     * op_arit -> '+' | '-' | '*' | '/'
     */
    public void opArit() {
        if (check(TipoToken.OP_ARIT)) {
            pos++;
        } else {
            throw new RuntimeException(
                "[ERRO SINTÁTICO] Operador aritmético esperado (+, -, *, /), " +
                "encontrado: \"" + peek().getLexema() + "\" na posição " + pos
            );
        }
    }
}