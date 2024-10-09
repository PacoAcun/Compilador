// DO NOT EDIT
// Generated by JFlex 1.9.1 http://jflex.de/
// source: compiler/scanner/Scanner.flex

package compiler.scanner;

import java_cup.runtime.Symbol;
import compiler.parser.sym;

/* Declaración del scanner */

@SuppressWarnings("fallthrough")
public class Scanner implements java_cup.runtime.Scanner {

  /** This character denotes the end of file. */
  public static final int YYEOF = -1;

  /** Initial size of the lookahead buffer. */
  private static final int ZZ_BUFFERSIZE = 16384;

  // Lexical states.
  public static final int YYINITIAL = 0;
  public static final int COMMENT = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = {
     0,  0,  1, 1
  };

  /**
   * Top-level table for translating characters to character classes
   */
  private static final int [] ZZ_CMAP_TOP = zzUnpackcmap_top();

  private static final String ZZ_CMAP_TOP_PACKED_0 =
    "\1\0\37\u0100\1\u0200\267\u0100\10\u0300\u1020\u0100";

  private static int [] zzUnpackcmap_top() {
    int [] result = new int[4352];
    int offset = 0;
    offset = zzUnpackcmap_top(ZZ_CMAP_TOP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackcmap_top(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Second-level tables for translating characters to character classes
   */
  private static final int [] ZZ_CMAP_BLOCKS = zzUnpackcmap_blocks();

  private static final String ZZ_CMAP_BLOCKS_PACKED_0 =
    "\11\0\1\1\1\2\1\3\1\4\1\5\22\0\1\1"+
    "\1\6\1\7\2\0\1\10\1\11\1\12\1\13\1\14"+
    "\1\15\1\16\1\17\1\20\1\0\1\21\1\22\11\23"+
    "\1\0\1\24\1\25\1\26\1\27\2\0\6\30\21\31"+
    "\1\32\2\31\1\33\1\34\1\35\3\0\1\36\1\37"+
    "\1\40\1\41\1\42\1\43\1\31\1\44\1\45\1\31"+
    "\1\46\1\47\1\31\1\50\1\51\2\31\1\52\1\53"+
    "\1\54\1\55\1\56\1\57\1\32\2\31\1\60\1\61"+
    "\1\62\7\0\1\3\u01a2\0\2\3\326\0\u0100\3";

  private static int [] zzUnpackcmap_blocks() {
    int [] result = new int[1024];
    int offset = 0;
    offset = zzUnpackcmap_blocks(ZZ_CMAP_BLOCKS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackcmap_blocks(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /**
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\1\2\2\3\1\4\1\1\1\5\2\1"+
    "\1\6\1\7\1\10\1\11\1\12\1\13\1\14\2\15"+
    "\1\16\1\17\1\20\1\21\1\22\1\23\1\24\12\22"+
    "\1\25\1\1\1\26\1\2\1\3\2\2\1\27\1\2"+
    "\1\30\1\0\1\31\1\0\1\32\2\0\1\33\1\34"+
    "\1\35\1\2\1\0\1\36\1\37\1\40\11\22\1\41"+
    "\6\22\1\42\1\2\1\43\1\2\1\44\1\15\10\22"+
    "\1\45\1\46\1\47\7\22\1\50\2\22\1\51\2\22"+
    "\1\52\1\53\2\22\1\54\1\22\1\55\1\22\1\56"+
    "\1\22\1\57\3\22\1\60\1\61\1\62\1\22\1\63";

  private static int [] zzUnpackAction() {
    int [] result = new int[126];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\63\0\146\0\231\0\146\0\314\0\377\0\u0132"+
    "\0\146\0\u0165\0\u0198\0\146\0\146\0\146\0\u01cb\0\146"+
    "\0\u01fe\0\u0231\0\u0264\0\u0297\0\146\0\u02ca\0\u02fd\0\u0330"+
    "\0\u0363\0\146\0\146\0\u0396\0\u03c9\0\u03fc\0\u042f\0\u0462"+
    "\0\u0495\0\u04c8\0\u04fb\0\u052e\0\u0561\0\146\0\u0594\0\146"+
    "\0\u05c7\0\u05fa\0\u062d\0\u0660\0\u0693\0\u06c6\0\146\0\u0132"+
    "\0\146\0\u06f9\0\146\0\u072c\0\u075f\0\146\0\146\0\146"+
    "\0\u0792\0\u07c5\0\146\0\146\0\146\0\u07f8\0\u082b\0\u085e"+
    "\0\u0891\0\u08c4\0\u08f7\0\u092a\0\u095d\0\u0990\0\u0363\0\u09c3"+
    "\0\u09f6\0\u0a29\0\u0a5c\0\u0a8f\0\u0ac2\0\146\0\u0af5\0\146"+
    "\0\u0b28\0\146\0\u07c5\0\u0b5b\0\u0b8e\0\u0bc1\0\u0bf4\0\u0c27"+
    "\0\u0c5a\0\u0c8d\0\u0cc0\0\u0363\0\u0363\0\u0363\0\u0cf3\0\u0d26"+
    "\0\u0d59\0\u0d8c\0\u0dbf\0\u0df2\0\u0e25\0\u0363\0\u0e58\0\u0e8b"+
    "\0\u0363\0\u0ebe\0\u0ef1\0\u0363\0\u0363\0\u0f24\0\u0f57\0\u0363"+
    "\0\u0f8a\0\u0363\0\u0fbd\0\u0363\0\u0ff0\0\u0363\0\u1023\0\u1056"+
    "\0\u1089\0\u0363\0\u0363\0\u0363\0\u10bc\0\u0363";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[126];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length() - 1;
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /**
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpacktrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\1\5\1\0\1\4\1\6\1\7\1\10"+
    "\1\11\1\12\1\13\1\14\1\15\1\16\1\17\1\20"+
    "\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30"+
    "\3\31\1\32\1\3\1\33\1\31\1\34\1\35\1\31"+
    "\1\36\1\37\1\31\1\40\2\31\1\41\1\31\1\42"+
    "\1\31\1\43\1\31\1\44\1\45\1\46\1\47\1\50"+
    "\2\51\1\5\2\51\1\52\1\51\1\53\2\51\1\54"+
    "\2\51\1\55\3\51\1\56\41\51\64\0\1\4\2\0"+
    "\1\4\60\0\1\5\106\0\1\57\34\0\2\60\1\0"+
    "\4\60\1\61\24\60\1\62\26\60\11\0\1\63\51\0"+
    "\2\64\1\0\7\64\1\0\21\64\1\65\26\64\26\0"+
    "\1\66\62\0\1\67\51\0\1\70\3\0\1\71\63\0"+
    "\2\24\6\0\1\72\52\0\2\24\65\0\1\73\62\0"+
    "\1\74\62\0\1\75\56\0\2\31\4\0\3\31\3\0"+
    "\22\31\25\0\2\31\4\0\3\31\3\0\13\31\1\76"+
    "\1\77\5\31\25\0\2\31\4\0\3\31\3\0\1\100"+
    "\5\31\1\101\2\31\1\102\1\31\1\103\6\31\25\0"+
    "\2\31\4\0\3\31\3\0\11\31\1\104\10\31\25\0"+
    "\2\31\4\0\3\31\3\0\1\105\12\31\1\106\6\31"+
    "\25\0\2\31\4\0\3\31\3\0\5\31\1\107\4\31"+
    "\1\110\7\31\25\0\2\31\4\0\3\31\3\0\4\31"+
    "\1\111\15\31\25\0\2\31\4\0\3\31\3\0\4\31"+
    "\1\112\15\31\25\0\2\31\4\0\3\31\3\0\14\31"+
    "\1\113\5\31\25\0\2\31\4\0\3\31\3\0\13\31"+
    "\1\114\6\31\25\0\2\31\4\0\3\31\3\0\6\31"+
    "\1\115\13\31\64\0\1\116\1\0\2\51\1\0\12\51"+
    "\1\0\47\51\1\5\12\51\1\0\45\51\2\53\1\0"+
    "\4\53\1\51\5\53\1\60\16\53\1\117\26\53\2\51"+
    "\1\0\12\51\1\64\45\51\21\0\1\120\41\0\2\51"+
    "\1\0\12\51\1\70\3\51\1\121\41\51\7\0\1\60"+
    "\2\0\1\60\7\0\1\60\11\0\1\60\2\0\1\60"+
    "\3\0\1\60\4\0\1\60\1\0\1\60\1\0\1\60"+
    "\20\0\1\122\57\0\1\64\2\0\1\64\7\0\1\64"+
    "\11\0\1\64\2\0\1\64\3\0\1\64\4\0\1\64"+
    "\1\0\1\64\1\0\1\64\6\0\2\71\4\0\55\71"+
    "\22\0\2\123\4\0\1\123\5\0\6\123\41\0\2\31"+
    "\4\0\3\31\3\0\13\31\1\124\6\31\25\0\2\31"+
    "\4\0\3\31\3\0\4\31\1\125\15\31\25\0\2\31"+
    "\4\0\3\31\3\0\11\31\1\126\10\31\25\0\2\31"+
    "\4\0\3\31\3\0\1\127\21\31\25\0\2\31\4\0"+
    "\3\31\3\0\1\130\21\31\25\0\2\31\4\0\3\31"+
    "\3\0\12\31\1\131\7\31\25\0\2\31\4\0\3\31"+
    "\3\0\15\31\1\132\4\31\25\0\2\31\4\0\3\31"+
    "\3\0\11\31\1\133\10\31\25\0\2\31\4\0\3\31"+
    "\3\0\14\31\1\134\5\31\25\0\2\31\4\0\3\31"+
    "\3\0\16\31\1\135\3\31\25\0\2\31\4\0\3\31"+
    "\3\0\21\31\1\136\25\0\2\31\4\0\3\31\3\0"+
    "\16\31\1\137\3\31\25\0\2\31\4\0\3\31\3\0"+
    "\17\31\1\140\2\31\25\0\2\31\4\0\3\31\3\0"+
    "\7\31\1\141\12\31\25\0\2\31\4\0\3\31\3\0"+
    "\7\31\1\142\12\31\3\0\2\51\1\0\4\51\1\53"+
    "\2\51\1\53\2\51\1\0\4\51\1\53\11\51\1\53"+
    "\2\51\1\53\3\51\1\53\4\51\1\53\1\51\1\53"+
    "\1\51\1\53\6\51\2\121\1\0\3\51\7\121\1\71"+
    "\45\121\22\0\2\31\4\0\3\31\3\0\11\31\1\143"+
    "\10\31\25\0\2\31\4\0\3\31\3\0\1\144\21\31"+
    "\25\0\2\31\4\0\3\31\3\0\11\31\1\145\10\31"+
    "\25\0\2\31\4\0\3\31\3\0\14\31\1\146\5\31"+
    "\25\0\2\31\4\0\3\31\3\0\15\31\1\147\4\31"+
    "\25\0\2\31\4\0\3\31\3\0\16\31\1\150\3\31"+
    "\25\0\2\31\4\0\3\31\3\0\4\31\1\151\15\31"+
    "\25\0\2\31\4\0\3\31\3\0\15\31\1\152\4\31"+
    "\25\0\2\31\4\0\3\31\3\0\17\31\1\153\2\31"+
    "\25\0\2\31\4\0\3\31\3\0\4\31\1\154\15\31"+
    "\25\0\2\31\4\0\3\31\3\0\3\31\1\155\16\31"+
    "\25\0\2\31\4\0\3\31\3\0\11\31\1\156\10\31"+
    "\25\0\2\31\4\0\3\31\3\0\4\31\1\157\15\31"+
    "\25\0\2\31\4\0\3\31\3\0\10\31\1\160\11\31"+
    "\25\0\2\31\4\0\3\31\3\0\13\31\1\161\6\31"+
    "\25\0\2\31\4\0\3\31\3\0\15\31\1\162\4\31"+
    "\25\0\2\31\4\0\3\31\3\0\7\31\1\163\12\31"+
    "\25\0\2\31\4\0\3\31\3\0\4\31\1\164\15\31"+
    "\25\0\2\31\4\0\3\31\3\0\14\31\1\165\5\31"+
    "\25\0\2\31\4\0\3\31\3\0\4\31\1\166\15\31"+
    "\25\0\2\31\4\0\3\31\3\0\1\167\21\31\25\0"+
    "\2\31\4\0\3\31\3\0\17\31\1\170\2\31\25\0"+
    "\2\31\4\0\3\31\3\0\12\31\1\171\7\31\25\0"+
    "\2\31\4\0\3\31\3\0\12\31\1\172\7\31\25\0"+
    "\2\31\4\0\3\31\3\0\12\31\1\173\7\31\25\0"+
    "\2\31\4\0\3\31\3\0\16\31\1\174\3\31\25\0"+
    "\2\31\4\0\3\31\3\0\17\31\1\175\2\31\25\0"+
    "\2\31\4\0\3\31\3\0\4\31\1\176\15\31\3\0";

  private static int [] zzUnpacktrans() {
    int [] result = new int[4335];
    int offset = 0;
    offset = zzUnpacktrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpacktrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** Error code for "Unknown internal scanner error". */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  /** Error code for "could not match input". */
  private static final int ZZ_NO_MATCH = 1;
  /** Error code for "pushback value was too large". */
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /**
   * Error messages for {@link #ZZ_UNKNOWN_ERROR}, {@link #ZZ_NO_MATCH}, and
   * {@link #ZZ_PUSHBACK_2BIG} respectively.
   */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state {@code aState}
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\1\1\1\11\3\1\1\11\2\1\3\11"+
    "\1\1\1\11\4\1\1\11\4\1\2\11\12\1\1\11"+
    "\1\1\1\11\6\1\1\11\1\0\1\11\1\0\1\11"+
    "\2\0\3\11\1\1\1\0\3\11\20\1\1\11\1\1"+
    "\1\11\1\1\1\11\54\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[126];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** Input device. */
  private java.io.Reader zzReader;

  /** Current state of the DFA. */
  private int zzState;

  /** Current lexical state. */
  private int zzLexicalState = YYINITIAL;

  /**
   * This buffer contains the current text to be matched and is the source of the {@link #yytext()}
   * string.
   */
  private char zzBuffer[] = new char[Math.min(ZZ_BUFFERSIZE, zzMaxBufferLen())];

  /** Text position at the last accepting state. */
  private int zzMarkedPos;

  /** Current text position in the buffer. */
  private int zzCurrentPos;

  /** Marks the beginning of the {@link #yytext()} string in the buffer. */
  private int zzStartRead;

  /** Marks the last character in the buffer, that has been read from input. */
  private int zzEndRead;

  /**
   * Whether the scanner is at the end of file.
   * @see #yyatEOF
   */
  private boolean zzAtEOF;

  /**
   * The number of occupied positions in {@link #zzBuffer} beyond {@link #zzEndRead}.
   *
   * <p>When a lead/high surrogate has been read from the input stream into the final
   * {@link #zzBuffer} position, this will have a value of 1; otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /** Number of newlines encountered up to the start of the matched text. */
  @SuppressWarnings("unused")
  private int yyline;

  /** Number of characters from the last newline up to the start of the matched text. */
  @SuppressWarnings("unused")
  private int yycolumn;

  /** Number of characters up to the start of the matched text. */
  @SuppressWarnings("unused")
  private long yychar;

  /** Whether the scanner is currently at the beginning of a line. */
  @SuppressWarnings("unused")
  private boolean zzAtBOL = true;

  /** Whether the user-EOF-code has already been executed. */
  private boolean zzEOFDone;

  /* user code: */

/* Código de usuario */

/* Variables para el seguimiento de líneas y columnas */
private int curr_line = 1;
private int curr_column = 1;

/* Actualizar la posición actual */
private void updatePosition() {
    yyline = curr_line - 1;
    yycolumn = curr_column - 1;
}

/* Métodos para crear símbolos */
private Symbol symbol(int type) {
    debug("Token encontrado: " + sym.terminalNames[type] + " en línea " + (yyline + 1) + ", columna " + (yycolumn + 1));
    return new Symbol(type, yyline + 1, yycolumn + 1);
}

private Symbol symbol(int type, Object value) {
    debug("Token encontrado: " + sym.terminalNames[type] + " con valor '" + value + "' en línea " + (yyline + 1) + ", columna " + (yycolumn + 1));
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
}

/* Método para depuración */
private void debug(String message) {
    System.out.println("[DEBUG] " + message);
}



  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Scanner(java.io.Reader in) {
    this.zzReader = in;
  }


  /** Returns the maximum size of the scanner buffer, which limits the size of tokens. */
  private int zzMaxBufferLen() {
    return Integer.MAX_VALUE;
  }

  /**  Whether the scanner buffer can grow to accommodate a larger token. */
  private boolean zzCanGrow() {
    return true;
  }

  /**
   * Translates raw input code points to DFA table row
   */
  private static int zzCMap(int input) {
    int offset = input & 255;
    return offset == input ? ZZ_CMAP_BLOCKS[offset] : ZZ_CMAP_BLOCKS[ZZ_CMAP_TOP[input >> 8] | offset];
  }

  /**
   * Refills the input buffer.
   *
   * @return {@code false} iff there was new input.
   * @exception java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead - zzStartRead);

      /* translate stored positions */
      zzEndRead -= zzStartRead;
      zzCurrentPos -= zzStartRead;
      zzMarkedPos -= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate && zzCanGrow()) {
      /* if not, and it can grow: blow it up */
      char newBuffer[] = new char[Math.min(zzBuffer.length * 2, zzMaxBufferLen())];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      if (requested == 0) {
        throw new java.io.EOFException("Scan buffer limit reached ["+zzBuffer.length+"]");
      }
      else {
        throw new java.io.IOException(
            "Reader returned 0 characters. See JFlex examples/zero-reader for a workaround.");
      }
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
        if (numRead == requested) { // We requested too few chars to encode a full Unicode character
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        } else {                    // There is room in the buffer for at least one more char
          int c = zzReader.read();  // Expecting to read a paired low surrogate char
          if (c == -1) {
            return true;
          } else {
            zzBuffer[zzEndRead++] = (char)c;
          }
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }


  /**
   * Closes the input reader.
   *
   * @throws java.io.IOException if the reader could not be closed.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true; // indicate end of file
    zzEndRead = zzStartRead; // invalidate buffer

    if (zzReader != null) {
      zzReader.close();
    }
  }


  /**
   * Resets the scanner to read from a new input stream.
   *
   * <p>Does not close the old reader.
   *
   * <p>All internal variables are reset, the old input stream <b>cannot</b> be reused (internal
   * buffer is discarded and lost). Lexical state is set to {@code ZZ_INITIAL}.
   *
   * <p>Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader The new input stream.
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzEOFDone = false;
    yyResetPosition();
    zzLexicalState = YYINITIAL;
    int initBufferSize = Math.min(ZZ_BUFFERSIZE, zzMaxBufferLen());
    if (zzBuffer.length > initBufferSize) {
      zzBuffer = new char[initBufferSize];
    }
  }

  /**
   * Resets the input position.
   */
  private final void yyResetPosition() {
      zzAtBOL  = true;
      zzAtEOF  = false;
      zzCurrentPos = 0;
      zzMarkedPos = 0;
      zzStartRead = 0;
      zzEndRead = 0;
      zzFinalHighSurrogate = 0;
      yyline = 0;
      yycolumn = 0;
      yychar = 0L;
  }


  /**
   * Returns whether the scanner has reached the end of the reader it reads from.
   *
   * @return whether the scanner has reached EOF.
   */
  public final boolean yyatEOF() {
    return zzAtEOF;
  }


  /**
   * Returns the current lexical state.
   *
   * @return the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state.
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   *
   * @return the matched text.
   */
  public final String yytext() {
    return new String(zzBuffer, zzStartRead, zzMarkedPos-zzStartRead);
  }


  /**
   * Returns the character at the given position from the matched text.
   *
   * <p>It is equivalent to {@code yytext().charAt(pos)}, but faster.
   *
   * @param position the position of the character to fetch. A value from 0 to {@code yylength()-1}.
   *
   * @return the character at {@code position}.
   */
  public final char yycharat(int position) {
    return zzBuffer[zzStartRead + position];
  }


  /**
   * How many characters were matched.
   *
   * @return the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * <p>In a well-formed scanner (no or only correct usage of {@code yypushback(int)} and a
   * match-all fallback rule) this method will only be called with things that
   * "Can't Possibly Happen".
   *
   * <p>If this method is called, something is seriously wrong (e.g. a JFlex bug producing a faulty
   * scanner etc.).
   *
   * <p>Usual syntax/scanner level error handling should be done in error fallback rules.
   *
   * @param errorCode the code of the error message to display.
   */
  private static void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    } catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * <p>They will be read again by then next call of the scanning method.
   *
   * @param number the number of characters to be read again. This number must not be greater than
   *     {@link #yylength()}.
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
  yyclose();    }
  }




  /**
   * Resumes scanning until the next regular expression is matched, the end of input is encountered
   * or an I/O-Error occurs.
   *
   * @return the next token.
   * @exception java.io.IOException if any I/O-Error occurs.
   */
  @Override  public Symbol next_token() throws java.io.IOException
  {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char[] zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMap(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
          { return new java_cup.runtime.Symbol(sym.EOF); }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1:
            { System.err.println("Caracter ilegal: " + yytext() + " en línea " + curr_line + ", columna " + curr_column);
    curr_column += yylength();
            }
          // fall through
          case 52: break;
          case 2:
            { curr_column += yylength();
            }
          // fall through
          case 53: break;
          case 3:
            { curr_line++; curr_column = 1;
            }
          // fall through
          case 54: break;
          case 4:
            { updatePosition(); curr_column += yylength(); return symbol(sym.NOT, yytext());
            }
          // fall through
          case 55: break;
          case 5:
            { updatePosition(); curr_column += yylength(); return symbol(sym.MOD, yytext());
            }
          // fall through
          case 56: break;
          case 6:
            { updatePosition(); curr_column += yylength(); return symbol(sym.LPAREN, yytext());
            }
          // fall through
          case 57: break;
          case 7:
            { updatePosition(); curr_column += yylength(); return symbol(sym.RPAREN, yytext());
            }
          // fall through
          case 58: break;
          case 8:
            { updatePosition(); curr_column += yylength(); return symbol(sym.TIMES, yytext());
            }
          // fall through
          case 59: break;
          case 9:
            { updatePosition(); curr_column += yylength(); return symbol(sym.PLUS, yytext());
            }
          // fall through
          case 60: break;
          case 10:
            { updatePosition(); curr_column += yylength(); return symbol(sym.COMMA, yytext());
            }
          // fall through
          case 61: break;
          case 11:
            { updatePosition(); curr_column += yylength(); return symbol(sym.MINUS, yytext());
            }
          // fall through
          case 62: break;
          case 12:
            { updatePosition(); curr_column += yylength(); return symbol(sym.DIVIDE, yytext());
            }
          // fall through
          case 63: break;
          case 13:
            { updatePosition(); curr_column += yylength(); return symbol(sym.INT_LITERAL, yytext());
            }
          // fall through
          case 64: break;
          case 14:
            { updatePosition(); curr_column += yylength(); return symbol(sym.SEMI, yytext());
            }
          // fall through
          case 65: break;
          case 15:
            { updatePosition(); curr_column += yylength(); return symbol(sym.LT, yytext());
            }
          // fall through
          case 66: break;
          case 16:
            { updatePosition(); curr_column += yylength(); return symbol(sym.ASSIGN, yytext());
            }
          // fall through
          case 67: break;
          case 17:
            { updatePosition(); curr_column += yylength(); return symbol(sym.GT, yytext());
            }
          // fall through
          case 68: break;
          case 18:
            { updatePosition(); curr_column += yylength(); return symbol(sym.ID, yytext());
            }
          // fall through
          case 69: break;
          case 19:
            { updatePosition(); curr_column += yylength(); return symbol(sym.LBRACKET, yytext());
            }
          // fall through
          case 70: break;
          case 20:
            { updatePosition(); curr_column += yylength(); return symbol(sym.RBRACKET, yytext());
            }
          // fall through
          case 71: break;
          case 21:
            { updatePosition(); curr_column += yylength(); return symbol(sym.LBRACE, yytext());
            }
          // fall through
          case 72: break;
          case 22:
            { updatePosition(); curr_column += yylength(); return symbol(sym.RBRACE, yytext());
            }
          // fall through
          case 73: break;
          case 23:
            { curr_column += 1;
            }
          // fall through
          case 74: break;
          case 24:
            { updatePosition(); curr_column += yylength(); return symbol(sym.NEQ, yytext());
            }
          // fall through
          case 75: break;
          case 25:
            { updatePosition(); curr_column += yylength(); return symbol(sym.STRING_LITERAL, yytext());
            }
          // fall through
          case 76: break;
          case 26:
            { updatePosition(); curr_column += yylength(); return symbol(sym.AND, yytext());
            }
          // fall through
          case 77: break;
          case 27:
            { updatePosition(); curr_column += yylength(); return symbol(sym.PLUS_ASSIGN, yytext());
            }
          // fall through
          case 78: break;
          case 28:
            { updatePosition(); curr_column += yylength(); return symbol(sym.MINUS_ASSIGN, yytext());
            }
          // fall through
          case 79: break;
          case 29:
            { curr_column += 2; yybegin(COMMENT);
            }
          // fall through
          case 80: break;
          case 30:
            { updatePosition(); curr_column += yylength(); return symbol(sym.LE, yytext());
            }
          // fall through
          case 81: break;
          case 31:
            { updatePosition(); curr_column += yylength(); return symbol(sym.EQ, yytext());
            }
          // fall through
          case 82: break;
          case 32:
            { updatePosition(); curr_column += yylength(); return symbol(sym.GE, yytext());
            }
          // fall through
          case 83: break;
          case 33:
            { updatePosition(); curr_column += yylength(); return symbol(sym.IF, yytext());
            }
          // fall through
          case 84: break;
          case 34:
            { updatePosition(); curr_column += yylength(); return symbol(sym.OR, yytext());
            }
          // fall through
          case 85: break;
          case 35:
            { curr_column += 2; yybegin(YYINITIAL);
            }
          // fall through
          case 86: break;
          case 36:
            { updatePosition(); curr_column += yylength(); return symbol(sym.CHAR_LITERAL, yytext());
            }
          // fall through
          case 87: break;
          case 37:
            { updatePosition(); curr_column += yylength(); return symbol(sym.FOR, yytext());
            }
          // fall through
          case 88: break;
          case 38:
            { updatePosition(); curr_column += yylength(); return symbol(sym.INT, yytext());
            }
          // fall through
          case 89: break;
          case 39:
            { updatePosition(); curr_column += yylength(); return symbol(sym.NEW, yytext());
            }
          // fall through
          case 90: break;
          case 40:
            { updatePosition(); curr_column += yylength(); return symbol(sym.CHAR, yytext());
            }
          // fall through
          case 91: break;
          case 41:
            { updatePosition(); curr_column += yylength(); return symbol(sym.ELSE, yytext());
            }
          // fall through
          case 92: break;
          case 42:
            { updatePosition(); curr_column += yylength(); return symbol(sym.TRUE, yytext());
            }
          // fall through
          case 93: break;
          case 43:
            { updatePosition(); curr_column += yylength(); return symbol(sym.VOID, yytext());
            }
          // fall through
          case 94: break;
          case 44:
            { updatePosition(); curr_column += yylength(); return symbol(sym.BREAK, yytext());
            }
          // fall through
          case 95: break;
          case 45:
            { updatePosition(); curr_column += yylength(); return symbol(sym.CLASS, yytext());
            }
          // fall through
          case 96: break;
          case 46:
            { updatePosition(); curr_column += yylength(); return symbol(sym.FALSE, yytext());
            }
          // fall through
          case 97: break;
          case 47:
            { updatePosition(); curr_column += yylength(); return symbol(sym.WHILE, yytext());
            }
          // fall through
          case 98: break;
          case 48:
            { updatePosition(); curr_column += yylength(); return symbol(sym.RETURN, yytext());
            }
          // fall through
          case 99: break;
          case 49:
            { updatePosition(); curr_column += yylength(); return symbol(sym.BOOLEAN, yytext());
            }
          // fall through
          case 100: break;
          case 50:
            { updatePosition(); curr_column += yylength(); return symbol(sym.CALLOUT, yytext());
            }
          // fall through
          case 101: break;
          case 51:
            { updatePosition(); curr_column += yylength(); return symbol(sym.CONTINUE, yytext());
            }
          // fall through
          case 102: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
