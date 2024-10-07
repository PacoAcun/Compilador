package compiler.scanner;

import java_cup.runtime.*;
import compiler.parser.sym;

%%

/* Declaraciones y opciones de JFlex */
%public
%class Scanner
%cup
%unicode
%line
%column

%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline + 1, yycolumn + 1);
  }
  
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
  }
%}

/* Separador que indica el inicio de las reglas de expresión regular */
%%

/* Palabras clave */
"class"      { return symbol(sym.CLASS, yytext()); }
"int"        { return symbol(sym.INT, yytext()); }
"void"       { return symbol(sym.VOID, yytext()); }
"boolean"    { return symbol(sym.BOOLEAN, yytext()); }
"true"       { return symbol(sym.TRUE, yytext()); }
"false"      { return symbol(sym.FALSE, yytext()); }
"if"         { return symbol(sym.IF, yytext()); }
"else"       { return symbol(sym.ELSE, yytext()); }
"return"     { return symbol(sym.RETURN, yytext()); }
"while"      { return symbol(sym.WHILE, yytext()); }
"for"        { return symbol(sym.FOR, yytext()); }
"new"        { return symbol(sym.NEW, yytext()); }

/* Operadores y símbolos */
"=="         { return symbol(sym.EQ, yytext()); }
"!="         { return symbol(sym.NOT_EQUALS, yytext()); }
"="          { return symbol(sym.ASSIGN, yytext()); }
";"          { return symbol(sym.SEMI, yytext()); }
"{"          { return symbol(sym.LBRACE, "{"); }
"}"          { return symbol(sym.RBRACE, "}"); }
"("          { return symbol(sym.LPAREN, "("); }
")"          { return symbol(sym.RPAREN, ")"); }
"+"          { return symbol(sym.PLUS, yytext()); }
"-"          { return symbol(sym.MINUS, yytext()); }
"*"          { return symbol(sym.TIMES, yytext()); }
"/"          { return symbol(sym.DIVIDE, yytext()); }
","          { return symbol(sym.COMMA, yytext()); }
"&&"         { return symbol(sym.AND, yytext()); }
"||"         { return symbol(sym.OR, yytext()); }
"<="         { return symbol(sym.LESS_THAN_EQUALS, yytext()); }
">="         { return symbol(sym.GREATER_THAN_EQUALS, yytext()); }
"<"          { return symbol(sym.LESS_THAN, yytext()); }
">"          { return symbol(sym.GREATER_THAN, yytext()); }
"%"          { return symbol(sym.MOD, yytext()); }
"["          { return symbol(sym.LBRACKET, "["); }
"]"          { return symbol(sym.RBRACKET, "]"); }

/* Literales numéricos */
[0-9]+       { return symbol(sym.INT_LITERAL, Integer.parseInt(yytext())); }

/* Literales de carácter */
\'([^\'\\]|\\.)\' { return symbol(sym.CHAR_LITERAL, yytext().charAt(1)); }

/* Literales de cadena */
\"([^\"\\]|\\.)*\"  { return symbol(sym.STRING_LITERAL, yytext()); }

/* Identificadores */
[a-zA-Z_][a-zA-Z0-9_]* { return symbol(sym.ID, yytext()); }

/* Saltos de línea y espacios en blanco */
\n           { yyline++; yycolumn = 0; }
[ \t\r]+     { /* Ignorar espacios en blanco */ }

/* Comentarios de una línea */
"//".* { /* Ignorar comentarios de una línea */ }

/* Comentarios multilínea */
"/*"([^*]|[*][^/])*"*/" { /* Ignorar comentarios multilínea */ }

/* Manejo de errores (caracteres no reconocidos) */
.            { 
    throw new RuntimeException("Error: Carácter no reconocido '" + yytext() + "' en la línea " + (yyline + 1) + ", columna " + (yycolumn + 1));
}

/* Fin de archivo */
<<EOF>>     { return symbol(sym.EOF, "EOF"); }
