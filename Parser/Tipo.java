package parser;

import lexer.TipoToken;

public enum Tipo {
    INT, FLOAT, STRING, BOOL, ERRO, INDEF;

    public static Tipo deToken(TipoToken t) {
        switch (t) {
            case NUMINT:
                return INT;
            case NUMDEC:
                return FLOAT;
            case NUMFLOAT:
                return FLOAT;
            case NUMSTR:
                return STRING;
            case NUMBOOL:
                return BOOL;
            default:
                return ERRO;
        }
    }

    public boolean ehNumerico() {
        return this == INT || this == FLOAT;
    }

    public boolean compativelCom(Tipo destino) {
        if (this == ERRO || destino == ERRO)
            return true;
        if (this == destino)
            return true;
        if (this == INT && destino == FLOAT)
            return true;
        if (this == INDEF && destino.ehNumerico())
            return true;
        return false;
    }
}
