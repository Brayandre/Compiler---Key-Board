import java.util.List;

public class FlowParser extends PrincipalParser {

    private CmdParser cmdParser;
    private ExprParser exprParser;
    private DeclaraParser declParser;

    public FlowParser(List<Token> tokens, int startPos) {
        super(tokens, startPos);
    }

    public void injectDeps(CmdParser cmdParser, ExprParser exprParser, DeclaralParser declParser) {
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

    // cmdSe ->  
    // ’ALT’ ’(’ expr op_rel expr ’)’ ’{ bloco }  cmdElif ’ TAB ’{’ bloco ’}’ | 
    // ’ALT’ ’(’ expr op_rel expr ’)’ ’{ bloco }  cmdElif     

    public void cmdSe() {
        consume(TipoToken.ALT);
        consume(TipoToken.AP);
        exprRel();
        consume(TipoToken.FP);
        consume(TipoToken.AC);
        bloco();
        consume(TipoToken.FC);
        cmdElif();
        if (check(TipoToken.TAB)) {
            consume(TipoToken.TAB);
            consume(TipoToken.AC);
            bloco();
            consume(TipoToken.FC);
        }
    }

    // cmdElif -> ’ALT_TAB’ ’(’ expr op_rel expr ’)’  ’{‘ bloco ‘}’ | cmdElif cmdElif | EPS 
    
    public void cmdElif() {
        while (check(TipoToken.ALT_TAB)) {
            consume(TipoToken.ALT_TAB);
            consume(TipoToken.AP);
            exprRel();
            consume(TipoToken.FP);
            consume(TipoToken.AC);
            bloco();
            consume(TipoToken.FC);
        }
    }

    // cmdWhile -> ‘SHIFT’ ’(’ expr op_rel expr ’)’  ’{‘ bloco ‘}’ 

    public void cmdWhile() {
        consume(TipoToken.SHIFT);
        consume(TipoToken.AP);
        exprRel();
        consume(TipoToken.AC);
        consume(TipoToken.AC);
        bloco();
        consume(TipoToken.FC);
    }

    // cmdFor -> ‘ALTGR’ ‘(’ declara : expr op_rel expr ‘$’ :  cmdRecurs’)’  ’{‘ bloco ‘}’ 

    public void cmdFor() {
        consume(TipoToken.ALTGR);
        consume(TipoToken.AP);

        sync();
        DeclaraParser.declaraFor();
        this.pos = declParser.pos;

        consume(TipoToken.TWOP);

        // condição
        exprRel();

        consume(TipoToken.EOF);
        consume(TipoToken.TWOP);

        // incremento
        cmdRecurs();

        consume(TipoToken.FP);
        consume(TipoToken.AC);
        bloco();
        consume(TipoToken.FC);
    }

    // cmdRecurs - > ID op_arit”#” op_arit”#” 

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

    // cmdExpr -> ‘SET’ ID ’-->’ expr ‘$’ 

    private void exprRel() {
        exprParser.pos = this.pos;
        exprParser.expr();
        this.pos = exprParser.pos;

        exprParser.opRel();
        this.pos = exprParser.pos;

        exprParser.expr();
        this.pos = exprParser.pos;  
    }

    // bloco -> cmd bloco | cmd 

    private void bloco() {
        cmdParser.pos = this.pos;
        cmdParser.bloco();
        this.pos = cmdParser.pos;
    }
}