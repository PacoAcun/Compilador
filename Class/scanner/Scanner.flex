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
%cup
%unicode
%line
%column

%{
private int line = 1; // Para llevar la cuenta de las líneas

// Método para verificar si el identificador es conocido
private boolean isKnownIdentifier(String id) {
    // Puedes añadir aquí lógica para verificar si el identificador es conocido
    // Por ejemplo, revisa si es una palabra reservada o un identificador predefinido
    return id.matches("if|else|while|return|int|void|boolean|true|false"); // Ejemplo de palabras clave
}
%}

%%

// Ignorar espacios en blanco y saltos de línea
[ \t\r]+        { /* Ignorar espacios en blanco */ }
\n              { line++; }

// Comentarios de una línea
"//".*          { /* Ignorar comentarios de una línea */ }

// Comentarios multilínea
"/*"([^*]|(\*+[^/]))*"*/" { /* Ignorar comentarios multilínea */ }

// Palabras clave
"if"            { return new Symbol(sym.IF); }
"else"          { return new Symbol(sym.ELSE); }
"while"         { return new Symbol(sym.WHILE); }
"return"        { return new Symbol(sym.RETURN); }
"int"           { return new Symbol(sym.INT); }
"void"          { return new Symbol(sym.VOID); }
"boolean"       { return new Symbol(sym.BOOLEAN); }
"true"          { return new Symbol(sym.TRUE); }
"false"         { return new Symbol(sym.FALSE); }

// Identificadores
[a-zA-Z_][a-zA-Z0-9_]*  { 
    if (isKnownIdentifier(yytext())) {
        return new Symbol(sym.ID, yytext()); 
    } else {
        System.err.println("Error de sintaxis en la línea " + line + ": identificador inválido '" + yytext() + "'"); 
        return new Symbol(sym.error); // Puedes manejar esto como prefieras
    }
}

// Números enteros
[0-9]+           { return new Symbol(sym.INT_LITERAL, Integer.parseInt(yytext())); }

// Operadores
"+"              { return new Symbol(sym.PLUS); }
"-"              { return new Symbol(sym.MINUS); }
"*"              { return new Symbol(sym.TIMES); }
"/"              { return new Symbol(sym.DIVIDE); }
"=="             { return new Symbol(sym.EQUALS); }
"!="             { return new Symbol(sym.NOT_EQUALS); }
"<"              { return new Symbol(sym.LT); }
"<="             { return new Symbol(sym.LE); }
">"              { return new Symbol(sym.GT); }
">="             { return new Symbol(sym.GE); }

// Delimitadores
"("              { return new Symbol(sym.LPAREN); }
")"              { return new Symbol(sym.RPAREN); }
"{"              { return new Symbol(sym.LBRACE); }
"}"              { return new Symbol(sym.RBRACE); }
";"              { return new Symbol(sym.SEMICOLON); }
","              { return new Symbol(sym.COMMA); }

// Errores de sintaxis
.                { System.err.println("Error de sintaxis en la línea " + line + ": carácter inesperado '" + yytext() + "'"); }

// Fin del archivo
<<EOF>>          { return new Symbol(sym.EOF); }

// Método para manejar errores
void yyerror(String message) {
    System.err.println("Error: " + message);
}
