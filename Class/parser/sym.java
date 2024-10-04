package parser;

/** CUP generated class containing symbol constants. */
public class sym {
  /* terminals */
  public static final int INT_LITERAL = 2;
  public static final int LT = 3;
  public static final int GT = 4;
  public static final int ID = 5;
  public static final int LE = 6;
  public static final int GE = 7;
  public static final int DIVIDE = 14;
  public static final int LBRACKET = 23;
  public static final int STRING_LITERAL = 5; // Añadido
  public static final int EQUALS = 28;
  public static final int LPAREN = 6;
  public static final int INT = 20;
  public static final int FOR = 34;
  public static final int MINUS = 12;
  public static final int GREATER_THAN = 27;
  public static final int RPAREN = 7;
  public static final int SEMICOLON = 10;
  public static final int INTLIT = 3;
  public static final int COMMA = 25;
  public static final int CLASS = 22;
  public static final int PLUS = 11;
  public static final int MULTIPLY = 33;
  public static final int IF = 15;
  public static final int BOOLEAN = 37;
  public static final int EOF = 0;
  public static final int RETURN = 17;
  public static final int RBRACKET = 24;
  public static final int TRUE = 35;
  public static final int error = 1;
  public static final int VOID = 19;
  public static final int FLOATLIT = 4;
  public static final int STRING = 38; // Añadido
  public static final int PUBLIC = 39; // Añadido
  public static final int STATIC = 40; // Añadido
  public static final int TIMES = 13;
  public static final int LBRACE = 8;
  public static final int ELSE = 16;
  public static final int WHILE = 18;
  public static final int NOT_EQUALS = 29;
  public static final int FLOAT = 21;
  public static final int RBRACE = 9;
  public static final int EQUALS_EQUALS = 32;
  public static final int FALSE = 36;
  public static final int GREATER_THAN_EQUALS = 31;
  public static final int LESS_THAN = 26;

  // Corrección de símbolos faltantes
  public static final int SEMI = SEMICOLON; // Para ";"
  public static final int ASSIGN = EQUALS;  // Para "="
  public static final int EQ = EQUALS_EQUALS; // Para "=="

  public static final int LESS_THAN_EQUALS = 30;
  public static final int IDENTIFIER = 2;

  public static final String[] terminalNames = new String[] {
          "EOF",
          "error",
          "IDENTIFIER",
          "INTLIT",
          "FLOATLIT",
          "STRING_LITERAL", // Modificado
          "LPAREN",
          "RPAREN",
          "LBRACE",
          "RBRACE",
          "SEMICOLON",
          "PLUS",
          "MINUS",
          "TIMES",
          "DIVIDE",
          "IF",
          "ELSE",
          "RETURN",
          "WHILE",
          "VOID",
          "INT",
          "FLOAT",
          "CLASS",
          "LBRACKET",
          "RBRACKET",
          "COMMA",
          "LESS_THAN",
          "GREATER_THAN",
          "EQUALS",
          "NOT_EQUALS",
          "LESS_THAN_EQUALS",
          "GREATER_THAN_EQUALS",
          "EQUALS_EQUALS",
          "MULTIPLY",
          "FOR",
          "TRUE",
          "FALSE",
          "BOOLEAN",
          "STRING", // Añadido
          "PUBLIC", // Añadido
          "STATIC"  // Añadido
  };
}
