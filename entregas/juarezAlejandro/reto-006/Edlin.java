import java.util.Scanner;

class Edlin {
    public static void main(String[] args) {
        int activeLine[] = { 1 };
        String document[] = {
                "Bienvenidos al editor EDLIN",
                "Utilice el menu inferior para editar el texto",
                "------",
                "[L] permite definir la linea activa",
                "[E] permite editar la linea activa",
                "[I] permite intercambiar dos lineas",
                "[B] borra el contenido de la linea activa",
                "[D] deshace la última acción de la activa",
                "[R] rehacer la última acción deshecha",
                "[C] copia el contenido de la linea activa",
                "[P] pega el contenido de la linea activa",
                "[S] sale del programa",
                "",
                ""
        };

        String lastContent[] = { "" };
        String redoContent[] = { "" };
        String clipboard[] = { "" };

        do {
            print(document, activeLine);
        } while (processActions(document, activeLine, lastContent, redoContent, clipboard));
    }

    static void print(String[] document, int[] activeLine) {
        clearScreen();
        printHorizontalLine();
        for (int line = 0; line < document.length; line++) {
            System.out.println(line + separator(line, activeLine[0]) + document[line]);
        }
        printHorizontalLine();
    }

    static String separator(int line, int activeLine) {
        return line == activeLine ? ":*| " : ": | ";
    }

    static void printHorizontalLine() {
        System.out.println("-".repeat(50));
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static boolean processActions(String[] document, int[] activeLine, String[] lastContent, String[] redoContent,
            String[] clipboard) {
        System.out.println(
                "Comandos: [L]inea activa | [E]ditar | [I]ntercambiar | [D]eshacer | [R]ehacer | [C]opiar | [P]egar | [B]orrar | [S]alir");

        switch (askChar()) {
            case 'S':
            case 's':
                return false;
            case 'L':
            case 'l':
                setActiveLine(document, activeLine);
                break;
            case 'E':
            case 'e':
                edit(document, activeLine, lastContent, redoContent);
                break;
            case 'I':
            case 'i':
                exchangeLines(document);
                break;
            case 'B':
            case 'b':
                delete(document, activeLine, lastContent, redoContent);
                break;
            case 'D':
            case 'd':
                undo(document, activeLine, lastContent, redoContent);
                break;
            case 'R':
            case 'r':
                redo(document, activeLine, lastContent, redoContent);
                break;
            case 'C':
            case 'c':
                copy(document, activeLine, clipboard);
                break;
            case 'P':
            case 'p':
                paste(document, activeLine, clipboard, lastContent, redoContent);
                break;
        }
        return true;
    }

    static char askChar() {
        Scanner input = new Scanner(System.in);
        return input.next().charAt(0);
    }

    static void delete(String[] document, int[] activeLine, String[] lastContent, String[] redoContent) {
        System.out.println("Esta acción es irreversible: indique el número de línea activa para confirmarlo ["
                + activeLine[0] + "]");
        if (askInt() == activeLine[0]) {
            redoContent[0] = "";
            lastContent[0] = document[activeLine[0]];
            document[activeLine[0]] = "";
        }
    }

    static void exchangeLines(String[] document) {
        int originLine, destinationLine;
        String temporaryLine;
        boolean validLine = true;

        do {
            System.out.print("Indique primera línea a intercambiar: ");
            originLine = askInt();
            validLine = originLine >= 0 && originLine < document.length;
        } while (!validLine);

        do {
            System.out.print("Indique segunda línea a intercambiar: ");
            destinationLine = askInt();
            validLine = destinationLine >= 0 && destinationLine < document.length;
        } while (!validLine);

        temporaryLine = document[destinationLine];
        document[destinationLine] = document[originLine];
        document[originLine] = temporaryLine;
    }

    static void edit(String[] document, int[] activeLine, String[] lastContent, String[] redoContent) {
        System.out.println("EDITANDO> " + document[activeLine[0]]);
        redoContent[0] = "";
        lastContent[0] = document[activeLine[0]];
        document[activeLine[0]] = askString();
    }

    static String askString() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    static void setActiveLine(String[] document, int[] activeLine) {
        boolean validLine = true;
        do {
            System.out.print("Indique la nueva línea activa: ");
            activeLine[0] = askInt();
            validLine = activeLine[0] >= 0 && activeLine[0] < document.length;
        } while (!validLine);
    }

    static int askInt() {
        Scanner input = new Scanner(System.in);
        return input.nextInt();
    }

    static void undo(String[] document, int[] activeLine, String[] lastContent, String[] redoContent) {
        System.out.println("DESHACIENDO> Línea activa: " + activeLine[0]);
        redoContent[0] = document[activeLine[0]];
        document[activeLine[0]] = lastContent[0];
        lastContent[0] = redoContent[0];
        System.out.println("La acción está deshecha");
    }

    static void redo(String[] document, int[] activeLine, String[] lastContent, String[] redoContent) {
        if (redoContent[0].isEmpty()) {
            System.out.println("No hay acción para rehacer.");
            return;
        }
        System.out.println("REHACIENDO> Línea activa: " + activeLine[0]);
        String temp = document[activeLine[0]];
        document[activeLine[0]] = redoContent[0];
        redoContent[0] = lastContent[0];
        lastContent[0] = temp;
        System.out.println("La acción ha sido rehecha");
    }

    static void copy(String[] document, int[] activeLine, String[] clipboard) {
        clipboard[0] = document[activeLine[0]];
        System.out.println("Contenido copiado: " + clipboard[0]);
    }

    static void paste(String[] document, int[] activeLine, String[] clipboard, String[] lastContent,
            String[] redoContent) {
        if (clipboard[0].isEmpty()) {
            System.out.println("El portapapeles está vacío.");
            return;
        }
        redoContent[0] = "";
        lastContent[0] = document[activeLine[0]];
        document[activeLine[0]] = clipboard[0];
        System.out.println("Contenido pegado: " + clipboard[0]);
    }
}