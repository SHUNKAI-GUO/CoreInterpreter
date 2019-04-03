//Author: Shunkai Guo
//Main program
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ProgramMain {
    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        String str;
        if (args.length != 3) {
            throw new Exception("Wrong arguments");
        }
        try {
            br = new BufferedReader(new FileReader(args[0]));
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\r\n");
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ParseTree parseTree;
        parseTree = Parser.parse(sb.toString().trim());
        if (args[2].equals("print")) {
            Parser.print(parseTree, sb.toString().trim());
        }
        Executor.exeProg(parseTree, sb.toString().trim(), args[1]);

    }

}
