package parser;
import java.util.List;
import lexer.TipoToken;
import lexer.Token;

public class Tradutor extends PrincipalParser { 

    private final StringBuilder saida = new StringBuilder();
    private int indent = 0;
    // variavel responsavel por incluir pachage q libera o uso de inser e print no go 
    private boolean usaFmt = false;

    public Tradutor(List<Token> tokens) {
        super(tokens, 0);
    }

    public String traduzir() {
        prog();
        String cabecalho = "package main\n\n";
        if (usaFmt) {
            cabecalho += "import \"fmt\"\n\n";
        }
        return cabecalho + saida.toString();
    }

    private void traduz(String texto) {
        saida.append(recuo()).append(texto).append("\n");
    }

    private String recuo() {
        return "\t".repeat(indent);
    }

    // altera os tipo da .kb para a .go
    private String mapTipo(TipoToken t) {
        switch(t) {
            case NUMINT:   return "int";
            case NUMDEC:   return "float64";  
            case NUMFLOAT: return "float64";
            case NUMSTR:   return "string";
            case NUMBOOL:  return "bool";
            default: throw new RuntimeException("[TRADUTOR] Tipo desconhecido: " + t);
        }
    }

    //Constroi o codigo principal
    private void prog() {
        consume(TipoToken.INIT_PROG);
        traduz("func main() {");
        indent++;
        bloco();
        indent--;
        traduz("}");
        consume(TipoToken.TERMINATE_PROG);
        consume(TipoToken.CIF);
    }

    private void bloco() {
        cmd();
        while (check(TipoToken.CAPS, TipoToken.SET, TipoToken.SETCAPS,
                     TipoToken.INSERT, TipoToken.PRINT,
                     TipoToken.ALT, TipoToken.SHIFT, TipoToken.ALTGR)) {
            cmd();
        }
    }

    // define o cada palavra é em GO
    private void cmd() {
        if (check(TipoToken.CAPS))    { cmdDeclara();  }
        else if (check(TipoToken.SETCAPS)) { cmdSetLine();  }
        else if (check(TipoToken.SET))     { cmdExpr();     }
        else if (check(TipoToken.INSERT))  { cmdLeitura();  }
        else if (check(TipoToken.PRINT))   { cmdEscrita();  }
        else if (check(TipoToken.ALT))     { cmdSe();       }
        else if (check(TipoToken.SHIFT))   { cmdWhile();    }
        else if (check(TipoToken.ALTGR))   { cmdFor();      }
        else {
            throw new RuntimeException(
                "[ERRO SINTÁTICO] Comando inválido: \"" + peek().getLexema() + "\" na posição " + pos);
        }
    }

    //----------------------------------------- GERAÇÃO DO CODIGO ---------------------------------------//

    private void cmdDeclara() {
        consume(TipoToken.CAPS);
        String goTipo = mapTipo(peek().getTipo());
        consumeTipo();
        String nome = consume(TipoToken.ID).getLexema();
        consume(TipoToken.CIF);
        traduz("var " + nome + " " + goTipo);
    }

    private void cmdSetLine() {
        consume(TipoToken.SETCAPS);
        String goTipo = mapTipo(peek().getTipo());
        consumeTipo();
        String nome = consume(TipoToken.ID).getLexema();
        consume(TipoToken.SETA);
        String valor = expr();
        consume(TipoToken.CIF);
        traduz("var " + nome + " " + goTipo + " = " + valor);
    }

    private void cmdExpr() {
        consume(TipoToken.SET);
        String nome = consume(TipoToken.ID).getLexema();
        consume(TipoToken.SETA);
        String valor = expr();
        consume(TipoToken.CIF);
        traduz(nome + " = " + valor);
    }

    private void cmdLeitura() {
        usaFmt = true;
        consume(TipoToken.INSERT);
        consume(TipoToken.AP);
        String nome = consume(TipoToken.ID).getLexema();
        consume(TipoToken.FP);
        consume(TipoToken.CIF);
        traduz("fmt.Scan(&" + nome + ")");
    }

    private void cmdEscrita() {
        usaFmt = true;
        consume(TipoToken.PRINT);
        consume(TipoToken.AP);
        String args = conteudo();
        consume(TipoToken.FP);
        consume(TipoToken.CIF);
        traduz("fmt.Println(" + args + ")");
    }

    private String conteudo() {
        StringBuilder args = new StringBuilder();
        args.append(conteudoItem());
        while (check(TipoToken.HASH)) {
            consume(TipoToken.HASH);
            if (check(TipoToken.TEXT, TipoToken.ID)) {
                args.append(", ").append(conteudoItem());
            }
        }
        return args.toString();
    }

    private String conteudoItem() {
        if (check(TipoToken.TEXT)) {
            String txt = consume(TipoToken.TEXT).getLexema();
            return "\"" + txt + "\"";
        } else if (check(TipoToken.ID)) {
            return consume(TipoToken.ID).getLexema();
        } else {
            throw new RuntimeException(
                "[ERRO SINTÁTICO] Conteúdo inválido: \"" + peek().getLexema() + "\"");
        }
    }

    private void cmdSe() {
        consume(TipoToken.ALT);
        consume(TipoToken.AP);
        String cond = exprRel();
        consume(TipoToken.FP);
        consume(TipoToken.AC);
        traduz("if " + cond + " {");
        indent++;
        bloco();
        indent--;
        consume(TipoToken.FC);
        traduz("}");

        while (check(TipoToken.ALT_TAB)) {
            consume(TipoToken.ALT_TAB);
            consume(TipoToken.AP);
            String condElif = exprRel();
            consume(TipoToken.FP);
            consume(TipoToken.AC);
            removerUltimaLinha();
            traduz("} else if " + condElif + " {");
            indent++;
            bloco();
            indent--;
            consume(TipoToken.FC);
            traduz("}");
        }

        if (check(TipoToken.TAB)) {
            consume(TipoToken.TAB);
            consume(TipoToken.AC);
            removerUltimaLinha();
            traduz("} else {");
            indent++;
            bloco();
            indent--;
            consume(TipoToken.FC);
            traduz("}");
        }
    }

    private void cmdWhile() {
        consume(TipoToken.SHIFT);
        consume(TipoToken.AP);
        String cond = exprRel();
        consume(TipoToken.FP);
        consume(TipoToken.AC);
        traduz("for " + cond + " {");
        indent++;
        bloco();
        indent--;
        consume(TipoToken.FC);
        traduz("}");
    }

    private void cmdFor() {
        consume(TipoToken.ALTGR);
        consume(TipoToken.AP);

        // consome CAPS opcional
        if (check(TipoToken.CAPS)) consume(TipoToken.CAPS);
        consumeTipo();  // consome o tipo (descartado, Go infere com :=)
        String varFor = consume(TipoToken.ID).getLexema();
        consume(TipoToken.CIF);

        consume(TipoToken.TWOP);  

        // condição
        String cond = exprRel();

        consume(TipoToken.CIF);   
        consume(TipoToken.TWOP);  

        // incremento
        String incr = cmdRecurs();

        consume(TipoToken.FP);
        consume(TipoToken.AC);

       
        traduz("for " + varFor + " := 0; " + cond + "; " + incr + " {");
        indent++;
        bloco();
        indent--;
        consume(TipoToken.FC);
        traduz("}");
    }

    private String cmdRecurs() {
        String id  = consume(TipoToken.ID).getLexema();
        String op1 = consume(TipoToken.OP_ARIT).getLexema();
        consume(TipoToken.HASH);
        String op2 = consume(TipoToken.OP_ARIT).getLexema();
        consume(TipoToken.HASH);

        if (op1.equals("+") && op2.equals("+")) return id + "++";
        if (op1.equals("-") && op2.equals("-")) return id + "--";
        return id + " " + op1 + op2;
    }

    private String exprRel() {
        String esq = expr();
        String op  = consume(TipoToken.OP_REL).getLexema();
        String dir = expr();
        return esq + " " + op + " " + dir;
    }

    private String expr() {
        StringBuilder resultado = new StringBuilder(fator());
        while (check(TipoToken.OP_ARIT)) {
            String op = consume(TipoToken.OP_ARIT).getLexema();
            String f  = fator();
            resultado.append(" ").append(op).append(" ").append(f);
        }
        return resultado.toString();
    }

    private String fator() {
        if (check(TipoToken.NUM)) {
            return consume(TipoToken.NUM).getLexema();
        } else if (check(TipoToken.ID)) {
            return consume(TipoToken.ID).getLexema();
        } else if (check(TipoToken.AP)) {
            consume(TipoToken.AP);
            String e = expr();
            consume(TipoToken.FP);
            return "(" + e + ")";
        } else {
            throw new RuntimeException(
                "[ERRO SINTÁTICO] Fator esperado (NUM, ID ou expressão), encontrado: \""
                + peek().getLexema() + "\" na posição " + pos);
        }
    }

    private void consumeTipo() {
        if (check(TipoToken.NUMINT, TipoToken.NUMDEC, TipoToken.NUMSTR,
                  TipoToken.NUMBOOL, TipoToken.NUMFLOAT)) {
            pos++;
        } else {
            throw new RuntimeException(
                "[ERRO SINTÁTICO] Tipo esperado, encontrado: \"" + peek().getLexema() + "\"");
        }
    }

    private void removerUltimaLinha() {
        int len = saida.length();
        if (len == 0) return;
        if (saida.charAt(len - 1) == '\n') saida.deleteCharAt(--len);
        while (len > 0 && saida.charAt(len - 1) != '\n') {
            saida.deleteCharAt(--len);
        }
    }
}