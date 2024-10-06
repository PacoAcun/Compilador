package compiler.ast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ASTGenerator {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java ASTGenerator <ruta del Parser.cup> <directorio de salida>");
            return;
        }

        String parserPath = args[0];
        String outputDir = args[1];

        try {
            String content = Files.readString(Paths.get(parserPath));
            List<String> classNames = extractClassNames(content);

            for (String className : classNames) {
                createASTClass(className, outputDir);
            }

            System.out.println("Clases AST generadas correctamente.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo Parser.cup: " + e.getMessage());
        }
    }

    private static List<String> extractClassNames(String content) {
        Pattern pattern = Pattern.compile("non\\s+terminal\\s+(\\w+)\\s+(\\w+)");
        Matcher matcher = pattern.matcher(content);
        return matcher.results()
                      .map(m -> m.group(2))
                      .collect(Collectors.toList());
    }

    private static void createASTClass(String className, String outputDir) throws IOException {
        String template = String.format("""
            package compiler.ast;

            public class %s {
                // Campos y métodos de la clase
            }
            """, className);

        File file = new File(outputDir, className + ".java");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(template);
        }
    }
}
