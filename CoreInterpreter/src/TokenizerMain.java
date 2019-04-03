
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TokenizerMain {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private TokenizerMain() {
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */

    public static void main(String[] args) {
        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        String str;
        try {
            br = new BufferedReader(new FileReader("test1.txt"));
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

        Tokenizer t = new Tokenizer(sb.toString());

        while (t.getToken() != TokenKind.EOF
                && t.getToken() != TokenKind.ERROR) {
            System.out.println(t.getToken().testDriverTokenNumber());
            t.skipToken();
        }
        if (t.getToken() == TokenKind.ERROR) {
            System.out.println("Error: Illegal token encountered.");
        }
        if (t.getToken() == TokenKind.EOF) {
            System.out.println(TokenKind.EOF.testDriverTokenNumber());
        }
    }

}
