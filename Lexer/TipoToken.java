package lexer;

public enum TipoToken {
    // Programa
    INIT_PROG, TERMINATE_PROG,
    // Comandos
    CAPS, SET, SETCAPS, ALT, TAB, ALT_TAB, SHIFT, ALTGR, INSERT, PRINT,
    // Tipos
    NUMINT, NUMDEC, NUMSTR, NUMBOOL, NUMFLOAT,
    // Operadores
    SETA, OP_REL, OP_ARIT,
    // Delimitadores
    CIF, HASH, TWOP, AP, FP, AC, FC,
    // Literais
    NUM, ID, TEXT,
    // Fim
    EOF
}
