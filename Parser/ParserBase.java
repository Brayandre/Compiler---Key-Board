import java.util.List;
 
public abstract class ParserBase {
 
    protected List<Token> tokens;
    protected int pos;
 
    public ParserBase(List<Token> tokens, int startPos) {
        this.tokens = tokens;
        this.pos = startPos;
    }
 
    protected Token peek() {
        return tokens.get(pos);
    }
 
    /** Tipo do token atual. */
    protected TipoToken tipo() {
        return peek().getTipo();
    }
 
    protected Token consume(TipoToken esperado) {
        Token t = peek();
        if (t.getTipo() != esperado) {
            throw new RuntimeException(
                "[ERRO SINTÁTICO] Esperado '" + esperado +
                "' mas encontrado \"" + t.getLexema() +
                "\" (" + t.getTipo() + ") na posição " + pos
            );
        }
        pos++;
        return t;
    }

    protected boolean check(TipoToken... tipos) {
        for (TipoToken t : tipos) {
            if (tipo() == t) return true;
        }
        return false;
    }
 
    protected boolean match(TipoToken t) {
        if (tipo() == t) { pos++; return true; }
        return false;
    }
}