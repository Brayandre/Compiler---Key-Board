package parser;

import java.util.ArrayList;
import java.util.List;
import lexer.TipoToken;
import lexer.Token;

/**
 * Analisador semantico para a linguagem .kb.
 *
 * Faz uma segunda passagem sobre a lista de tokens (independente do Tradutor)
 * e verifica:
 *
 *  1. Declaracao antes do uso.
 *  2. Sem redeclaracao no mesmo escopo.
 *  3. Compatibilidade de tipos em SETCAPS / SET.
 *  4. Operandos numericos em expressoes aritmeticas.
 *  5. Operandos de tipos comparaveis em expressoes relacionais.
 *  6. Variavel de controle do ALTGR deve ser numerica.
 *  7. cmdRecurs opera sobre ID declarado numerico.
 *  8. insert(ID) -- ID deve estar declarado.
 *  9. Conteudo do prt_scr -- IDs devem estar declarados.
 * 10. Escopo aninhado para corpos de ALT/SHIFT/ALTGR.
 * 11. Warning: variaveis declaradas e nunca usadas.
 * 12. Warning: leitura de variavel ainda nao inicializada.
 *
 * Estende PrincipalParser para reaproveitar peek/consume/check/match e a
 * convencao de cursor (campo pos). Erros sao coletados em uma lista; ao
 * final, analisar() retorna a lista. Para problemas catastroficos que
 * impedem continuar (ex.: ausencia de computador_iniciar), e lancada
 * RuntimeException -- mesma convencao do parser.
 */
public class AnalisadorSemantico extends PrincipalParser {

    private final TabelaSimbolos tabela = new TabelaSimbolos();
    private final List<ErroSemantico> erros = new ArrayList<>();
    private final List<ErroSemantico> warnings = new ArrayList<>();

    public AnalisadorSemantico(List<Token> tokens) {
        super(tokens, 0);
    }

    /** Ponto de entrada. Retorna lista de erros (vazia => programa semanticamente valido). */
    public List<ErroSemantico> analisar() {
        prog();
        // Warnings de variaveis declaradas e nao usadas (apenas escopo global no fim)
        for (Simbolo s : tabela.escopoGlobal()) {
            if (!s.isUsado()) {
                warnings.add(new ErroSemantico(
                    ErroSemantico.Categoria.VARIAVEL_DECLARADA_NAO_USADA,
                    "variavel '" + s.getNome() + "' declarada mas nunca usada",
                    s.getPosDecl()));
            }
        }
        return erros;
    }

    public List<ErroSemantico> getWarnings() { return warnings; }
    public TabelaSimbolos getTabela()        { return tabela; }

    // -------------------------- helpers --------------------------

    private void erro(ErroSemantico.Categoria cat, String msg) {
        erros.add(new ErroSemantico(cat, msg, pos));
    }

    private void warn(ErroSemantico.Categoria cat, String msg) {
        warnings.add(new ErroSemantico(cat, msg, pos));
    }

    /** Consome um token de tipo esperado. Igual ao do parser, mas tolera erro. */
    private Token consumeSafe(TipoToken esperado) {
        Token t = peek();
        if (t.getTipo() != esperado) {
            // Erro sintatico re-lancado como semantico apenas para nao quebrar a
            // passagem se quisermos analisar em modo "resiliente". Aqui mantemos
            // o comportamento do parser: erro sintatico = falha dura.
            throw new RuntimeException(
                "[ERRO SINTATICO durante semantica] esperado " + esperado +
                " mas encontrado '" + t.getLexema() + "' (" + t.getTipo() + ") em pos " + pos);
        }
        pos++;
        return t;
    }

    private TipoToken consumirTipoDeclarado() {
        Token t = peek();
        if (!check(TipoToken.NUMINT, TipoToken.NUMDEC, TipoToken.NUMSTR,
                   TipoToken.NUMBOOL, TipoToken.NUMFLOAT)) {
            throw new RuntimeException(
                "[ERRO SINTATICO durante semantica] tipo esperado em pos " + pos +
                ", encontrado '" + t.getLexema() + "'");
        }
        pos++;
        return t.getTipo();
    }

    // -------------------------- producoes --------------------------

    private void prog() {
        consumeSafe(TipoToken.INIT_PROG);
        bloco();
        consumeSafe(TipoToken.TERMINATE_PROG);
        consumeSafe(TipoToken.CIF);
    }

    private void bloco() {
        cmd();
        while (check(TipoToken.CAPS, TipoToken.SET, TipoToken.SETCAPS,
                     TipoToken.INSERT, TipoToken.PRINT,
                     TipoToken.ALT, TipoToken.SHIFT, TipoToken.ALTGR)) {
            cmd();
        }
    }

    private void cmd() {
        if      (check(TipoToken.CAPS))    cmdDeclara();
        else if (check(TipoToken.SETCAPS)) cmdSetLine();
        else if (check(TipoToken.SET))     cmdSet();
        else if (check(TipoToken.INSERT))  cmdLeitura();
        else if (check(TipoToken.PRINT))   cmdEscrita();
        else if (check(TipoToken.ALT))     cmdSe();
        else if (check(TipoToken.SHIFT))   cmdWhile();
        else if (check(TipoToken.ALTGR))   cmdFor();
        else {
            throw new RuntimeException(
                "[ERRO SINTATICO durante semantica] comando invalido '" +
                peek().getLexema() + "' em pos " + pos);
        }
    }

    // ----- declaracao simples: CAPS tipo ID $
    private void cmdDeclara() {
        consumeSafe(TipoToken.CAPS);
        TipoToken tk = consumirTipoDeclarado();
        Token id = consumeSafe(TipoToken.ID);
        consumeSafe(TipoToken.CIF);

        Tipo t = Tipo.deToken(tk);
        Simbolo s = new Simbolo(id.getLexema(), t, tabela.nivelAtual(), pos, false);
        if (!tabela.declarar(s)) {
            erro(ErroSemantico.Categoria.DECLARACAO_DUPLICADA,
                 "variavel '" + id.getLexema() + "' ja foi declarada neste escopo");
        }
    }

    // ----- declaracao + atribuicao: SETCAPS tipo ID --> expr $
    private void cmdSetLine() {
        consumeSafe(TipoToken.SETCAPS);
        TipoToken tk = consumirTipoDeclarado();
        Token id = consumeSafe(TipoToken.ID);
        consumeSafe(TipoToken.SETA);
        Tipo tExpr = expr();
        consumeSafe(TipoToken.CIF);

        Tipo tDecl = Tipo.deToken(tk);
        Simbolo s = new Simbolo(id.getLexema(), tDecl, tabela.nivelAtual(), pos, true);
        if (!tabela.declarar(s)) {
            erro(ErroSemantico.Categoria.DECLARACAO_DUPLICADA,
                 "variavel '" + id.getLexema() + "' ja foi declarada neste escopo");
        }
        if (!tExpr.compativelCom(tDecl)) {
            erro(ErroSemantico.Categoria.TIPO_INCOMPATIVEL,
                 "atribuicao incompativel: '" + id.getLexema() + "' eh " + tDecl +
                 " mas a expressao eh " + tExpr);
        }
    }

    // ----- atribuicao: SET ID --> expr $
    private void cmdSet() {
        consumeSafe(TipoToken.SET);
        Token id = consumeSafe(TipoToken.ID);
        consumeSafe(TipoToken.SETA);
        Tipo tExpr = expr();
        consumeSafe(TipoToken.CIF);

        Simbolo s = tabela.buscar(id.getLexema());
        if (s == null) {
            erro(ErroSemantico.Categoria.ID_NAO_DECLARADO,
                 "atribuicao a variavel nao declarada '" + id.getLexema() + "'");
            return;
        }
        if (!tExpr.compativelCom(s.getTipo())) {
            erro(ErroSemantico.Categoria.TIPO_INCOMPATIVEL,
                 "atribuicao incompativel: '" + id.getLexema() + "' eh " + s.getTipo() +
                 " mas a expressao eh " + tExpr);
        }
        s.marcarInicializado();
        s.marcarUsado(); // atribuir tambem conta como referenciar
    }

    // ----- insert(ID) $
    private void cmdLeitura() {
        consumeSafe(TipoToken.INSERT);
        consumeSafe(TipoToken.AP);
        Token id = consumeSafe(TipoToken.ID);
        consumeSafe(TipoToken.FP);
        consumeSafe(TipoToken.CIF);

        Simbolo s = tabela.buscar(id.getLexema());
        if (s == null) {
            erro(ErroSemantico.Categoria.ID_NAO_DECLARADO,
                 "insert em variavel nao declarada '" + id.getLexema() + "'");
            return;
        }
        s.marcarInicializado();
        s.marcarUsado();
    }

    // ----- prt_scr(conteudo) $
    private void cmdEscrita() {
        consumeSafe(TipoToken.PRINT);
        consumeSafe(TipoToken.AP);
        conteudo();
        consumeSafe(TipoToken.FP);
        consumeSafe(TipoToken.CIF);
    }

    private void conteudo() {
        conteudoItem();
        while (check(TipoToken.HASH)) {
            consumeSafe(TipoToken.HASH);
            if (check(TipoToken.TEXT, TipoToken.ID)) {
                conteudoItem();
            }
        }
    }

    private void conteudoItem() {
        if (check(TipoToken.TEXT)) {
            consumeSafe(TipoToken.TEXT);
        } else if (check(TipoToken.ID)) {
            Token id = consumeSafe(TipoToken.ID);
            Simbolo s = tabela.buscar(id.getLexema());
            if (s == null) {
                erro(ErroSemantico.Categoria.ID_NAO_DECLARADO,
                     "prt_scr usa variavel nao declarada '" + id.getLexema() + "'");
            } else {
                if (!s.isInicializado()) {
                    warn(ErroSemantico.Categoria.USO_ANTES_DE_INICIALIZAR,
                         "prt_scr le '" + id.getLexema() + "' que ainda nao foi inicializado");
                }
                s.marcarUsado();
            }
        } else {
            throw new RuntimeException(
                "[ERRO SINTATICO durante semantica] conteudo invalido em pos " + pos);
        }
    }

    // ----- ALT ( exprRel ) { bloco } (ALT_TAB ( exprRel ) { bloco })* (TAB { bloco })?
    private void cmdSe() {
        consumeSafe(TipoToken.ALT);
        consumeSafe(TipoToken.AP);
        exprRel();
        consumeSafe(TipoToken.FP);
        consumeSafe(TipoToken.AC);
        tabela.abrirEscopo();
        bloco();
        tabela.fecharEscopo();
        consumeSafe(TipoToken.FC);

        while (check(TipoToken.ALT_TAB)) {
            consumeSafe(TipoToken.ALT_TAB);
            consumeSafe(TipoToken.AP);
            exprRel();
            consumeSafe(TipoToken.FP);
            consumeSafe(TipoToken.AC);
            tabela.abrirEscopo();
            bloco();
            tabela.fecharEscopo();
            consumeSafe(TipoToken.FC);
        }

        if (check(TipoToken.TAB)) {
            consumeSafe(TipoToken.TAB);
            consumeSafe(TipoToken.AC);
            tabela.abrirEscopo();
            bloco();
            tabela.fecharEscopo();
            consumeSafe(TipoToken.FC);
        }
    }

    // ----- SHIFT ( exprRel ) { bloco }
    private void cmdWhile() {
        consumeSafe(TipoToken.SHIFT);
        consumeSafe(TipoToken.AP);
        exprRel();
        consumeSafe(TipoToken.FP);
        consumeSafe(TipoToken.AC);
        tabela.abrirEscopo();
        bloco();
        tabela.fecharEscopo();
        consumeSafe(TipoToken.FC);
    }

    // ----- ALTGR ( [CAPS] tipo ID $ : exprRel $ : cmdRecurs ) { bloco }
    private void cmdFor() {
        consumeSafe(TipoToken.ALTGR);
        consumeSafe(TipoToken.AP);

        // O for cria seu proprio escopo para conter a variavel de controle
        tabela.abrirEscopo();

        if (check(TipoToken.CAPS)) consumeSafe(TipoToken.CAPS);
        TipoToken tk = consumirTipoDeclarado();
        Tipo tCtrl = Tipo.deToken(tk);
        if (!tCtrl.ehNumerico()) {
            erro(ErroSemantico.Categoria.OPERACAO_INVALIDA,
                 "variavel de controle do ALTGR deve ser numerica, foi declarada como " + tCtrl);
        }
        Token id = consumeSafe(TipoToken.ID);
        consumeSafe(TipoToken.CIF);

        Simbolo ctrl = new Simbolo(id.getLexema(), tCtrl, tabela.nivelAtual(), pos, true);
        if (!tabela.declarar(ctrl)) {
            erro(ErroSemantico.Categoria.DECLARACAO_DUPLICADA,
                 "variavel '" + id.getLexema() + "' ja existe neste escopo do ALTGR");
        }

        consumeSafe(TipoToken.TWOP);
        exprRel();
        consumeSafe(TipoToken.CIF);
        consumeSafe(TipoToken.TWOP);

        cmdRecurs();

        consumeSafe(TipoToken.FP);
        consumeSafe(TipoToken.AC);
        bloco();
        consumeSafe(TipoToken.FC);

        tabela.fecharEscopo();
    }

    // ----- ID op_arit # op_arit #
    private void cmdRecurs() {
        Token id = consumeSafe(TipoToken.ID);
        Simbolo s = tabela.buscar(id.getLexema());
        if (s == null) {
            erro(ErroSemantico.Categoria.ID_NAO_DECLARADO,
                 "incremento usa variavel nao declarada '" + id.getLexema() + "'");
        } else {
            if (!s.getTipo().ehNumerico()) {
                erro(ErroSemantico.Categoria.OPERACAO_INVALIDA,
                     "incremento exige variavel numerica, '" + id.getLexema() +
                     "' eh " + s.getTipo());
            }
            s.marcarUsado();
            s.marcarInicializado();
        }
        consumeSafe(TipoToken.OP_ARIT);
        consumeSafe(TipoToken.HASH);
        consumeSafe(TipoToken.OP_ARIT);
        consumeSafe(TipoToken.HASH);
    }

    // ----- expr op_rel expr -- retorna BOOL
    private Tipo exprRel() {
        Tipo esq = expr();
        Token op = consumeSafe(TipoToken.OP_REL);
        Tipo dir = expr();

        if (esq == Tipo.ERRO || dir == Tipo.ERRO) {
            return Tipo.BOOL; // silencia cascata
        }

        // == e != aceitam tipos iguais ou ambos numericos. Outros operadores so numerico.
        String lex = op.getLexema();
        boolean igualdade = lex.equals("==") || lex.equals("!=");

        boolean ambosNum = esq.ehNumerico() && dir.ehNumerico();
        if (ambosNum) return Tipo.BOOL;

        if (igualdade && esq == dir) return Tipo.BOOL;

        erro(ErroSemantico.Categoria.OPERACAO_INVALIDA,
             "operador relacional '" + lex + "' entre tipos incompativeis: " + esq + " " + lex + " " + dir);
        return Tipo.BOOL;
    }

    // ----- expr -> fator (op_arit fator)*
    private Tipo expr() {
        Tipo acc = fator();
        while (check(TipoToken.OP_ARIT)) {
            Token op = consumeSafe(TipoToken.OP_ARIT);
            Tipo dir = fator();

            // soma com strings nao e suportada pelo Go por concatenacao via "+"
            // (na verdade Go aceita), mas a .kb nao suporta operacoes mistas.
            if (acc == Tipo.ERRO || dir == Tipo.ERRO) {
                acc = Tipo.ERRO;
                continue;
            }
            if (!acc.ehNumerico() || !dir.ehNumerico()) {
                erro(ErroSemantico.Categoria.OPERACAO_INVALIDA,
                     "operador aritmetico '" + op.getLexema() +
                     "' exige operandos numericos, recebeu " + acc + " e " + dir);
                acc = Tipo.ERRO;
                continue;
            }
            // promocao: se algum lado for FLOAT, resultado e FLOAT
            acc = (acc == Tipo.FLOAT || dir == Tipo.FLOAT) ? Tipo.FLOAT : Tipo.INT;
        }
        return acc;
    }

    // ----- fator -> NUM | ID | ( expr )
    private Tipo fator() {
        if (check(TipoToken.NUM)) {
            Token n = consumeSafe(TipoToken.NUM);
            // se contem '.', e FLOAT; senao INT.
            return n.getLexema().contains(".") ? Tipo.FLOAT : Tipo.INT;
        }
        if (check(TipoToken.ID)) {
            Token id = consumeSafe(TipoToken.ID);
            Simbolo s = tabela.buscar(id.getLexema());
            if (s == null) {
                erro(ErroSemantico.Categoria.ID_NAO_DECLARADO,
                     "uso de variavel nao declarada '" + id.getLexema() + "'");
                return Tipo.ERRO;
            }
            if (!s.isInicializado()) {
                warn(ErroSemantico.Categoria.USO_ANTES_DE_INICIALIZAR,
                     "uso da variavel '" + id.getLexema() + "' antes de inicializar");
            }
            s.marcarUsado();
            return s.getTipo();
        }
        if (check(TipoToken.AP)) {
            consumeSafe(TipoToken.AP);
            Tipo t = expr();
            consumeSafe(TipoToken.FP);
            return t;
        }
        // fator invalido -- registra como erro semantico e tenta seguir
        erro(ErroSemantico.Categoria.OPERACAO_INVALIDA,
             "fator invalido (esperado NUM, ID ou '('), encontrado '" + peek().getLexema() + "'");
        // consome o token para nao travar
        pos++;
        return Tipo.ERRO;
    }
}
