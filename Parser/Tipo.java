package parser;

import lexer.TipoToken;

/**
 * Tipos semanticos da linguagem .kb.
 *
 * Mapeamento:
 *   numint   -> INT
 *   numdec   -> FLOAT (decimal)
 *   numfloat -> FLOAT
 *   numstr   -> STRING
 *   numbool  -> BOOL
 *
 * ERRO   -> tipo "veneno" usado para propagar falha sem disparar
 *           erros em cascata. Qualquer operacao com ERRO resulta em ERRO.
 * INDEF  -> ainda nao classificado (literal NUM sem contexto, por exemplo).
 */
public enum Tipo {
    INT, FLOAT, STRING, BOOL, ERRO, INDEF;

    /** Converte um TipoToken de declaracao (NUMINT, NUMDEC, ...) em Tipo semantico. */
    public static Tipo deToken(TipoToken t) {
        switch (t) {
            case NUMINT:   return INT;
            case NUMDEC:   return FLOAT;
            case NUMFLOAT: return FLOAT;
            case NUMSTR:   return STRING;
            case NUMBOOL:  return BOOL;
            default:       return ERRO;
        }
    }

    /** Tipos numericos podem participar de operacoes aritmeticas e relacionais ordenadas. */
    public boolean ehNumerico() {
        return this == INT || this == FLOAT;
    }

    /**
     * Compatibilidade para atribuicao: o tipo da expressao pode ser convertido para o tipo da variavel?
     * - Mesmos tipos sempre sao compativeis.
     * - INT pode ser atribuido a FLOAT (promocao numerica).
     * - INDEF (literais numericos) e compativel com qualquer tipo numerico.
     * - ERRO nunca propaga novos erros (silencia para evitar cascata).
     */
    public boolean compativelCom(Tipo destino) {
        if (this == ERRO || destino == ERRO) return true;
        if (this == destino) return true;
        if (this == INT && destino == FLOAT) return true;
        if (this == INDEF && destino.ehNumerico()) return true;
        return false;
    }
}
