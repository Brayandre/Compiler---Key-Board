import java.util.List;

public class cmdParser extends PrincipalParser{

    private DeclaraParser declParser;
    private IoParser ioParser;
    private FlowParser flowParser;
    private ExprParser exprParser;

    public CmdParser(List<Token> tokens, int startPos) {
        super(tokens, startPos);
    }

    public void injectDeps(DeclaraParser d, IoParser io, 
                           FlowParser flow, ExprParser expr) {
        this.declParser = d;
        this.ioParser      = io;
        this.flowParser    = flow;
        this.exprParser    = expr;
    }
}