package parser;
import java.util.List;
import lexer.TipoToken;
import lexer.Token;

public class CmdParser extends PrincipalParser{

    private DeclaraParser declParser;
    private IoParser ioParser;
    private FlowParser flowParser;
    private ExprParser exprParser;

    public CmdParser(List<Token> tokens, int startPos) {
        super(tokens, startPos);
    }

    // cmd -> decide qual parser chamar baseado no token atual
    public void cmd() {
        if (check(TipoToken.CAPS)) {
            declParser.pos = this.pos;
            declParser.declara();
            this.pos = declParser.pos;

        } else if (check(TipoToken.SETCAPS)) {
            cmdSetLine();

        } else if (check(TipoToken.SET)) {
            cmdExpr();

        } else if (check(TipoToken.INSERT)) {
            ioParser.pos = this.pos;
            ioParser.cmdLeitura();
            this.pos = ioParser.pos;

        } else if (check(TipoToken.PRINT)) {
            ioParser.pos = this.pos;
            ioParser.cmdEscrita();
            this.pos = ioParser.pos;

        } else if (check(TipoToken.ALT)) {
            flowParser.pos = this.pos;
            flowParser.cmdSe();
            this.pos = flowParser.pos;

        } else if (check(TipoToken.SHIFT)) {
            flowParser.pos = this.pos;
            flowParser.cmdWhile();
            this.pos = flowParser.pos;

        } else if (check(TipoToken.ALTGR)) {
            flowParser.pos = this.pos;
            flowParser.cmdFor();
            this.pos = flowParser.pos;

        } else {
            throw new RuntimeException(
                "[ERRO SINTÁTICO] Comando inválido: \"" +
                peek().getLexema() + "\" na posição " + pos
            );
        }
    }

    // cmdExpr -> SET ID --> expr $
    public void cmdExpr() {
        consume(TipoToken.SET);
        consume(TipoToken.ID);
        consume(TipoToken.SETA);
        exprParser.pos = this.pos;
        exprParser.expr();
        this.pos = exprParser.pos;
        consume(TipoToken.CIF);
    }

    // cmdSetLine -> SETCAPS tipo ID --> expr $
    public void cmdSetLine() {
        consume(TipoToken.SETCAPS);
        declParser.pos = this.pos;
        declParser.consumeTipo();
        this.pos = declParser.pos;
        consume(TipoToken.ID);
        consume(TipoToken.SETA);
        exprParser.pos = this.pos;
        exprParser.expr();
        this.pos = exprParser.pos;
        consume(TipoToken.CIF);
    }

    public void injectDeps(DeclaraParser d, IoParser io, 
                           FlowParser flow, ExprParser expr) {
        this.declParser = d;
        this.ioParser = io;
        this.flowParser = flow;
        this.exprParser = expr;
    }

    public void bloco() {
        cmd(); // pelo menos um comando
        // continua enquanto o próximo token puder iniciar um comando
        while (check(TipoToken.CAPS, TipoToken.SET, TipoToken.SETCAPS,
                    TipoToken.INSERT, TipoToken.PRINT,
                    TipoToken.ALT, TipoToken.SHIFT, TipoToken.ALTGR)) {
            cmd();
        }
    }
}