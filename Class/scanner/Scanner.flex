package scanner;

import java.io.IOException;
import java.io.Reader;
import java_cup.runtime.Symbol;
import parser.sym;
import opt.Algebraic;
import opt.ConstantF;

%%
%public
%class Scanner
%unicode
%cup
%line
%column

%{
    // Variables para seguimiento de línea y columna
    private int line = 1;
    private int column = 1;

    // Método para actualizar la posición
    private void updatePosition(String text) {
        for (char c : text.toCharArray()) {
            if (c == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
    }

    // Métodos para obtener la línea y columna actual
    public int yyline() {
        return line;
    }

    public int yycolumn() {
        return column;
    }

    // Método para crear un símbolo con valor
    private Symbol createSymbol(int type, String value) {
        return new Symbol(type, yyline(), yycolumn(), value);
    }

    // Clase para manejar errores
    public class ErrorMsg {
        public static void printError(String message, int line, int column) {
            System.err.println("Error en la línea " + line + ", columna " + column + ": " + message);
        }
    }
%}

%%

// Whitespace (ignoring)
\s+ { updatePosition(yytext()); /* ignore whitespace */ }

// Reserved keywords
"if" { updatePosition(yytext()); return createSymbol(sym.IF, "if"); }
"else" { updatePosition(yytext()); return createSymbol(sym.ELSE, "else"); }
"while" { updatePosition(yytext()); return createSymbol(sym.WHILE, "while"); }
"for" { updatePosition(yytext()); return createSymbol(sym.FOR, "for"); }
"int" { updatePosition(yytext()); return createSymbol(sym.INT, "int"); }
"float" { updatePosition(yytext()); return createSymbol(sym.FLOAT, "float"); }
"boolean" { updatePosition(yytext()); return createSymbol(sym.BOOLEAN, "boolean"); }
"true" { updatePosition(yytext()); return createSymbol(sym.TRUE, "true"); }
"false" { updatePosition(yytext()); return createSymbol(sym.FALSE, "false"); }
"public" { updatePosition(yytext()); return createSymbol(sym.PUBLIC, "public"); }
"private" { updatePosition(yytext()); return createSymbol(sym.PRIVATE, "private"); }
"protected" { updatePosition(yytext()); return createSymbol(sym.PROTECTED, "protected"); }
"void" { updatePosition(yytext()); return createSymbol(sym.VOID, "void"); }
"return" { updatePosition(yytext()); return createSymbol(sym.RETURN, "return"); }

// Operators and Delimiters
"=" { updatePosition(yytext()); return createSymbol(sym.EQUALS, "="); }
"==" { updatePosition(yytext()); return createSymbol(sym.EQUALS_EQUALS, "=="); }
"!=" { updatePosition(yytext()); return createSymbol(sym.NOT_EQUALS, "!="); }
"<" { updatePosition(yytext()); return createSymbol(sym.LESS_THAN, "<"); }
"<=" { updatePosition(yytext()); return createSymbol(sym.LESS_THAN_EQUALS, "<="); }
">" { updatePosition(yytext()); return createSymbol(sym.GREATER_THAN, ">"); }
">=" { updatePosition(yytext()); return createSymbol(sym.GREATER_THAN_EQUALS, ">="); }
"(" { updatePosition(yytext()); return createSymbol(sym.LPAREN, "("); }
")" { updatePosition(yytext()); return createSymbol(sym.RPAREN, ")"); }
"{" { updatePosition(yytext()); return createSymbol(sym.LBRACE, "{"); }
"}" { updatePosition(yytext()); return createSymbol(sym.RBRACE, "}"); }
";" { updatePosition(yytext()); return createSymbol(sym.SEMICOLON, ";"); }
"," { updatePosition(yytext()); return createSymbol(sym.COMMA, ","); }
"[" { updatePosition(yytext()); return createSymbol(sym.LBRACKET, "["); }
"]" { updatePosition(yytext()); return createSymbol(sym.RBRACKET, "]"); }
"+" { updatePosition(yytext()); return createSymbol(sym.PLUS, "+"); }
"-" { updatePosition(yytext()); return createSymbol(sym.MINUS, "-"); }
"*" { updatePosition(yytext()); return createSymbol(sym.MULTIPLY, "*"); }
"/" { updatePosition(yytext()); return createSymbol(sym.DIVIDE, "/"); }

// Literals
[0-9]+ { 
    updatePosition(yytext()); 
    // Usando Algebraic para procesar el entero si es necesario
    String processedValue = Algebraic.processInteger(yytext());
    return createSymbol(sym.INTLIT, processedValue); 
}
[0-9]*"."[0-9]+ { 
    updatePosition(yytext()); 
    // Usando ConstantF para procesar el flotante si es necesario
    String processedValue = ConstantF.processFloat(yytext());
    return createSymbol(sym.FLOATLIT, processedValue); 
}

// Handle malformatted float literals (e.g., "123.")
[0-9]+\.[0-9]* { 
    updatePosition(yytext());
    ErrorMsg.printError("Malformatted float literal: " + yytext(), yyline(), yycolumn()); 
}

// Identifiers
[a-zA-Z_][a-zA-Z0-9_]* { updatePosition(yytext()); return createSymbol(sym.IDENTIFIER, yytext()); }

// String literals (assuming Decaf includes them)
\"([^\"]|\\.)*\" { updatePosition(yytext()); return createSymbol(sym.STRINGLIT, yytext()); }

// Error handling for illegal characters
. { 
    updatePosition(yytext());
    ErrorMsg.printError("Illegal character: " + yytext(), yyline(), yycolumn()); 
}
