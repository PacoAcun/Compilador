# Objetivo principal: limpiar, generar Parser y Scanner, compilar todo y ejecutar
run-todo: limpiar crear-bin generar-parser generar-scanner compilar-todo ejecutar-todo

# Ejecuta todo el proceso: limpieza, generación, compilación y ejecución del escaneo
run-escaneo: limpiar crear-bin generar-scanner compilar-escaneo ejecutar-escaneo

# Ejecuta todo el proceso: limpieza, generación, compilación y ejecución del parsing
run-parsing: limpiar crear-bin generar-parser generar-scanner compilar-parsing ejecutar-parsing

# Limpia los archivos generados y la carpeta bin
limpiar:
	rm -rf bin
	rm -f compiler/parser/Parser.java compiler/parser/sym.java compiler/scanner/Scanner.java output.txt

# Crea el directorio bin para almacenar los archivos .class
crear-bin:
	mkdir -p bin

# Genera Parser.java y sym.java utilizando Java CUP
generar-parser:
	java -jar compiler/lib/java-cup-11b.jar -parser Parser -symbols sym -package compiler.parser -destdir compiler/parser compiler/parser/Parser.cup

# Genera Scanner.java utilizando JFlex
generar-scanner:
	java -jar compiler/lib/jflex-full-1.9.1.jar -d compiler/scanner/ compiler/scanner/Scanner.flex

# Compila todos los archivos y coloca los .class en bin/
compilar-todo:
	javac -cp bin:compiler/lib/java-cup-11b.jar:. -d bin \
		compiler/ast/*.java \
		compiler/scanner/Scanner.java \
		compiler/parser/Parser.java \
		compiler/Compiler.java

# Compila el escáner y coloca los .class en bin/
compilar-escaneo:
	javac -cp bin:compiler/lib/java-cup-11b.jar:. -d bin \
		compiler/ast/*.java \
		compiler/scanner/Scanner.java \
		compiler/Compiler.java

# Compila el escáner, el parser y coloca los .class en bin/
compilar-parsing:
	javac -cp bin:compiler/lib/java-cup-11b.jar:. -d bin \
		compiler/ast/*.java \
		compiler/scanner/Scanner.java \
		compiler/parser/Parser.java \
		compiler/Compiler.java

# Ejecuta todo el análisis y guarda el resultado en output.txt
ejecutar-todo:
	java -cp bin:compiler/lib/java-cup-11b.jar:. compiler.Compiler input.txt -o output.txt

# Ejecuta el análisis de escaneo y guarda el resultado en output.txt
ejecutar-escaneo:
	java -cp bin:compiler/lib/java-cup-11b.jar:. compiler.Compiler -target scan input.txt -o output.txt

# Ejecuta el análisis de parsing y guarda el resultado en output.txt
ejecutar-parsing:
	java -cp bin:compiler/lib/java-cup-11b.jar:. compiler.Compiler -target parse input.txt -o output.txt
	java -cp bin:compiler/lib/java-cup-11b.jar:. compiler.Compiler -target dot input.txt -o output.dot

generar-dot:
	dot -Tpdf output.dot -o output.pdf
