# Compilación de las clases del proyecto

JAVAC=javac
CLASSPATH=class

# Directorios
SRC_DIR=class
BIN_DIR=class

# Archivos fuente
SRC=$(wildcard $(SRC_DIR)/**/*.java)
CLASSES=$(SRC:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

all: $(CLASSES)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	$(JAVAC) -d $(BIN_DIR) $<

clean:
	rm -f $(BIN_DIR)/**/*.class
