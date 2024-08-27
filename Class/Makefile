JAVAC=javac
CUPJAR=/Users/isaacjuarez/Downloads/java-cup-bin-11b-20160615/java-cup-11b.jar
CUPRUNTIME=/Users/isaacjuarez/Downloads/java-cup-bin-11b-20160615/java-cup-runtime-11b.jar
CLASSPATH=.:$(CUPJAR):$(CUPRUNTIME)

SRC_DIR=.
BIN_DIR=.

# Archivos fuente
SRC=$(wildcard $(SRC_DIR)/**/*.java)
CLASSES=$(SRC:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

all: $(CLASSES)

$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	$(JAVAC) -cp $(CLASSPATH) -d $(BIN_DIR) $<

clean:
	find $(BIN_DIR) -name "*.class" -type f -delete
