# Objetivo principal: limpiar, generar Parser, Scanner y compilar
compilar-todo: limpiar crear-bin generar-parser generar-scanner compilar-todo

# Limpia, genera y compila para el análisis de escaneo
escaneo-todo: limpiar crear-bin generar-scanner generar-parser compilar-escaneo 

# Limpia, genera y compila para el análisis de parsing
parsing-todo: limpiar crear-bin generar-scanner generar-parser compilar-parsing

# Ejecuta el análisis de escaneo y parsing del archivo input.txt
analisis-completo: limpiar crear-bin generar-scanner generar-parser compilar-escaneo compilar-parsing ejecutar-escaneo ejecutar-parsing

# Elimina archivos generados y limpia la carpeta bin
limpiar:
	rm -rf bin
	rm -f compiler/parser/Parser.java compiler/parser/sym.java compiler/scanner/Scanner.java output.txt

# Crea el directorio bin para almacenar los .class
crear-bin:
	mkdir -p bin

# Genera el archivo Parser.java y sym.java utilizando JavaCup
generar-parser:
	java -jar compiler/lib/java-cup-11b.jar -parser Parser -symbols sym -destdir compiler/parser compiler/parser/Parser.cup

# Genera el archivo Scanner.java utilizando JFlex
generar-scanner:
	java -jar compiler/lib/jflex-full-1.9.1.jar -d compiler/scanner/ compiler/scanner/Scanner.flex

# Compila el escáner y el archivo principal del compilador y coloca los .class en bin/
compilar-escaneo:
	javac -cp compiler/lib/java-cup-11b.jar:. -d bin compiler/scanner/Scanner.java compiler/Compiler.java

# Compila el escáner, el parser y los archivos relacionados y coloca los .class en bin/
compilar-parsing:
	javac -cp compiler/lib/java-cup-11b.jar:. -d bin compiler/scanner/Scanner.java compiler/parser/Parser.java compiler/parser/sym.java compiler/Compiler.java

# Compila todos los archivos y coloca los .class en bin/
compilar-todo:
	javac -cp compiler/lib/java-cup-11b.jar:. -d bin compiler/scanner/Scanner.java compiler/parser/Parser.java compiler/parser/sym.java compiler/Compiler.java

# Ejecuta el análisis de escaneo y guarda el resultado en output.txt
ejecutar-escaneo:
	java -cp compiler/lib/java-cup-11b.jar:bin compiler.Compiler -target scan input.txt -o output.txt

# Ejecuta el análisis de parsing y guarda el resultado en output.txt
ejecutar-parsing:
	java -cp compiler/lib/java-cup-11b.jar:bin compiler.Compiler -target parse input.txt -o output.txt

# Ejecuta todo el proceso: limpieza, generación y análisis para el escaneo
run-escaneo: compilar-todo ejecutar-escaneo

# Ejecuta todo el proceso: limpieza, generación y análisis para el parsing
run-parsing: compilar-todo ejecutar-parsing
