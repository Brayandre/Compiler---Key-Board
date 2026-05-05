import java.util.List;

public class FlowParser extends ParserBase {

    private CmdParser cmdParser;
    private ExprParser exprParser;
    private DeclParser declParser;

    public FlowParser(List<Token> tokens, int startPos) {
        super(tokens, startPos);
    }

    public void injectDeps(CmdParser cmdParser, ExprParser exprParser, DeclParser declParser) {
        this.cmdParser  = cmdParser;
        this.exprParser = exprParser;
        this.declParser = declParser;
    }

    private void sync() {
        cmdParser.pos  = this.pos;
        exprParser.pos = this.pos;
        declParser.pos = this.pos;
    }

    /** Recupera pos atualizado dos sub-parsers após suas chamadas. */
    private void syncBack() {
        this.pos = cmdParser.pos;
        exprParser.pos = this.pos;
        declParser.pos = this.pos;
    }

    public void cmdSe() {
        consume(TipoToken.ALT);
        consume(TipoToken.LPAREN);
        exprRel();
        consume(TipoToken.RPAREN);
        consume(TipoToken.LBRACE);
        bloco();
        consume(TipoToken.RBRACE);
        cmdElif();
        if (check(TipoToken.TAB)) {
            consume(TipoToken.TAB);
            consume(TipoToken.LBRACE);
            bloco();
            consume(TipoToken.RBRACE);
        }
    }

    public void cmdElif() {
        while (check(TipoToken.ALT_TAB)) {
            consume(TipoToken.ALT_TAB);
            consume(TipoToken.LPAREN);
            exprRel();
            consume(TipoToken.RPAREN);
            consume(TipoToken.LBRACE);
            bloco();
            consume(TipoToken.RBRACE);
        }
    }

    public void cmdWhile() {
        consume(TipoToken.SHIFT);
        consume(TipoToken.LPAREN);
        exprRel();
        consume(TipoToken.RPAREN);
        consume(TipoToken.LBRACE);
        bloco();
        consume(TipoToken.RBRACE);
    }

    public void cmdFor() {
        consume(TipoToken.ALTGR);
        consume(TipoToken.LPAREN);

        sync();
        declParser.declaraFor();
        this.pos = declParser.pos;

        consume(TipoToken.COLON);

        // condição
        exprRel();

        consume(TipoToken.DOLLAR);
        consume(TipoToken.COLON);

        // incremento
        cmdRecurs();

        consume(TipoToken.RPAREN);
        consume(TipoToken.LBRACE);
        bloco();
        consume(TipoToken.RBRACE);
    }

    public void cmdRecurs() {
        consume(TipoToken.ID);
        exprParser.pos = this.pos;
        exprParser.opArit();
        this.pos = exprParser.pos;
        consume(TipoToken.HASH);
        exprParser.pos = this.pos;
        exprParser.opArit();
        this.pos = exprParser.pos;
        consume(TipoToken.HASH);
    }

    private void exprRel() {
        exprParser.pos = this.pos;
        exprParser.expr();
        this.pos = exprParser.pos;

        exprParser.opRel();
        this.pos = exprParser.pos;

        exprParser.expr();
        this.pos = exprParser.pos;
    }

    private void bloco() {
        cmdParser.pos = this.pos;
        cmdParser.bloco();
        this.pos = cmdParser.pos;
    }
}