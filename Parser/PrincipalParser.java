package parser;

import java.util.List;
import lexer.TipoToken;
import lexer.Token;

public abstract class PrincipalParser {

    protected List<Token> tokens;
    protected int pos;

    // chamas os tokens e definem ele

    public PrincipalParser(List<Token> tokens, int startPos) {
        this.tokens = tokens;
        this.pos = startPos;
    }

    protected Token peek() {
        return tokens.get(pos);
    }

    protected TipoToken tipo() {
        return peek().getTipo();
    }

    // verifica a compatibilidade dos token, dado e esperado
    protected Token consume(TipoToken esperado) {
        Token t = peek();
        if (t.getTipo() != esperado) {
            throw new RuntimeException(
                    "[ERRO SINTÁTICO] Esperado '" + esperado + "' mas encontrado \"" + t.getLexema() + "\" ("
                            + t.getTipo() + ") na posição " + pos);
        }
        pos++;
        return t;
    }

    // verifica os tokens da gramatica
    protected boolean check(TipoToken... tipos) {
        for (TipoToken t : tipos) {
            if (tipo() == t)
                return true;
        }
        return false;
    }

    // verifica se a posicção deles esta certa
    protected boolean match(TipoToken t) {
        if (tipo() == t) {
            pos++;
            return true;
        }
        return false;
    }
}