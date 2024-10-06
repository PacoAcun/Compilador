javac -cp "lib/java-cup-11b.jar;lib/jflex-full-1.9.1.jar" -d bin ast/*.java ast/ASTGenerator.java codegen/*.java irt/*.java opt/*.java parser/*.java scanner/*.java semantic/*.java Compiler.javaJAVAC=javac
JAVA=java
CUPJAR=lib/java-cup-11b.jar
CUPRUNTIME=lib/java-cup-11b-runtime.jar
JFLEXJAR=lib/jflex-full-1.9.1.jar
CLASSPATH=.:$(CUPJAR):$(CUPRUNTIME):$(JFLEXJAR):$(BIN_DIR)
PARSER_DIR=parser

SRC_DIR=.
BIN_DIR=bin

# Archivos fuente, EXCLUYENDO IRT Y CODEGEN
SRC=$(wildcard $(SRC_DIR)/ast/*.java) $(wildcard $(SRC_DIR)/semantic/*.java) $(wildcard $(SRC_DIR)/scanner/*.java) $(wildcard $(SRC_DIR)/parser/*.java)
CLASSES=$(SRC:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Archivo cup, flex y archivo de prueba
CUP_FILE=parser/parser.cup
LEXER_FILE=scanner/Scanner.flex
SCANNER_OUTPUT=scanner/Scanner.java
INPUT=input.txt

all: cup scanner astgen $(CLASSES)

# Compilar los archivos .java
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -cp $(CLASSPATH) -d $(BIN_DIR) $<

# Generar los archivos de CUP (Parser.java y sym.java)
cup:
	java -jar $(CUPJAR) -parser Parser -symbols sym -destdir $(PARSER_DIR) $(CUP_FILE)

# Generar el scanner (Scanner.java) a partir del archivo de especificaciones JFlex
scanner: $(SCANNER_OUTPUT)

$(SCANNER_OUTPUT): $(LEXER_FILE)
	rm -f $(SCANNER_OUTPUT)
	java -jar $(JFLEXJAR) $(LEXER_FILE)
	@echo "Scanner generado: $(SCANNER_OUTPUT)"

# Generar clases AST
astgen:
	@echo "Generando clases AST..."
	$(JAVAC) -cp $(CLASSPATH) $(SRC_DIR)/ast/ASTGenerator.java -d $(BIN_DIR)
	$(JAVA) -cp $(CLASSPATH):$(BIN_DIR) compiler.ast.ASTGenerator $(CUP_FILE) $(SRC_DIR)/ast
	@echo "Clases AST generadas."

# Limpiar los archivos .class y generados
clean:
	find $(BIN_DIR) -name "*.class" -type f -delete
	rm -f parser/Parser.java parser/sym.java scanner/Scanner.java

# Ejecutar el scanner con un archivo de entrada
test-scanner: all
	$(JAVA) -cp $(CLASSPATH):$(BIN_DIR) scanner.Scanner $(INPUT)
