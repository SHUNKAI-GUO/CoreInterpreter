//Author: Shunkai Guo
//Execution of parsed program
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Executor {
    static Scanner s;
    private static Tokenizer t;
    private static Map<String, Integer> idMap = new HashMap<String, Integer>();

    static void matchKey(TokenKind key) throws Exception {
        if (t.getToken() != key) {
            throw new Exception("ERROR");
        }
    }

    public static void exeProg(ParseTree p, String program, String fileName)
            throws Exception {
        assert (p.currNTNo() == 1) : "Wrong NTNo";
        File file = new File(fileName);
        if (file.exists()) {
            s = new Scanner(new BufferedReader(new FileReader(fileName)));
        }
        t = new Tokenizer(program);
        matchKey(TokenKind.LOWER_PROGRAM);
        t.skipToken();//program
        p.goDownLB();
        exeDecl_seq(p);
        p.goUP();
        matchKey(TokenKind.LOWER_BEGIN);
        t.skipToken();//begin
        p.goDownMB();
        exeStmt_seq(p);
        p.goUP();
        matchKey(TokenKind.LOWER_END);
        t.skipToken();//end
        if (file.exists()) {
            s.close();
        }
    }

    public static void exeDecl_seq(ParseTree p) throws Exception {
        assert (p.currNTNo() == 2) : "Wrong NTNo";
        p.goDownLB();
        exeDecl(p);
        p.goUP();
        if (p.currAlt() == 2) {
            p.goDownMB();
            exeDecl_seq(p);
            p.goUP();
        }
    }

    public static void exeStmt_seq(ParseTree p) throws Exception {
        assert (p.currNTNo() == 3) : "Wrong NTNo";
        p.goDownLB();
        exeStmt(p);
        p.goUP();
        if (p.currAlt() == 2) {
            p.goDownMB();
            exeStmt_seq(p);
            p.goUP();
        }
    }

    public static void exeDecl(ParseTree p) throws Exception {
        assert (p.currNTNo() == 4) : "Wrong NTNo";
        matchKey(TokenKind.LOWER_INT);
        t.skipToken();//int
        p.goDownLB();
        exeId_list(p);
        p.goUP();
        matchKey(TokenKind.SEMICOLON);
        t.skipToken();//;
    }

    public static String exeId_list(ParseTree p) throws Exception {
        assert (p.currNTNo() == 5) : "Wrong NTNo";
        String res = "";
        p.goDownLB();
        res += exeId(p);
        res += ",";
        p.goUP();
        if (p.currAlt() == 2) {
            matchKey(TokenKind.COMMA);
            t.skipToken();//,
            p.goDownMB();
            res += exeId_list(p);
            p.goUP();
        }
        //System.out.println(res);
        return res;
    }

    public static void exeStmt(ParseTree p) throws Exception {
        assert (p.currNTNo() == 6) : "Wrong NTNo";
        p.goDownLB();
        if (p.currNTNo() == 7) {
            exeAssign(p);
        } else if (p.currNTNo() == 11) {
            exeOut(p);
        } else if (p.currNTNo() == 10) {
            exeIn(p);
        } else if (p.currNTNo() == 8) {
            exeIf(p);
        } else if (p.currNTNo() == 9) {
            exeLoop(p);
        }
        p.goUP();
    }

    public static void exeAssign(ParseTree p) throws Exception {
        assert (p.currNTNo() == 7) : "Wrong NTNo";
        p.goDownLB();
        String new_id = exeId(p);
        p.goUP();
        matchKey(TokenKind.ASSIGNMENT_OPERATOR);
        t.skipToken();//=
        p.goDownMB();
        int new_value = exeExp(p);
        p.goUP();
        matchKey(TokenKind.SEMICOLON);
        t.skipToken();//;
        if (idMap.containsKey(new_id)) {
            idMap.replace(new_id, new_value);
        } else {
            idMap.put(new_id, new_value);
        }
    }

    public static void exeIf(ParseTree p) throws Exception {
        assert (p.currNTNo() == 8) : "Wrong NTNo";
        matchKey(TokenKind.LOWER_IF);
        t.skipToken();//if
        p.goDownLB();
        boolean agreed = exeCond(p);
        p.goUP();
        //System.out.println(agreed);
        matchKey(TokenKind.LOWER_THEN);
        t.skipToken();//then
        p.goDownMB();
        //System.out.println(agreed);
        if (agreed) {
            exeStmt_seq(p);
        } else {
            while (t.getToken() != TokenKind.LOWER_END
                    && t.getToken() != TokenKind.LOWER_ELSE) {
                int num = 0;
                if (t.getToken() != TokenKind.LOWER_IF
                        && t.getToken() != TokenKind.LOWER_WHILE) {
                    num += 1;
                }
                t.skipToken();
                if (t.getToken() == TokenKind.LOWER_END && num > 0) {
                    num -= 0;
                    t.skipToken();
                }
            }
        }
        //System.out.println(t.get());
        p.goUP();
        if (t.getToken() != TokenKind.LOWER_END) {
            matchKey(TokenKind.LOWER_ELSE);
            t.skipToken();//else
            //System.out.println(t.get());
            p.goDownRB();
            if (!agreed) {
                exeStmt_seq(p);
            } else {
                while (t.getToken() != TokenKind.LOWER_END) {
                    t.skipToken();
                }
            }
            p.goUP();
        }
        //System.out.println(t.get());
        matchKey(TokenKind.LOWER_END);
        t.skipToken();//end
        t.skipToken();//;
    }

    public static void exeLoop(ParseTree p) throws Exception {
        assert (p.currNTNo() == 9) : "Wrong NTNo";
        matchKey(TokenKind.LOWER_WHILE);
        t.skipToken();//while
        Tokenizer tcopy = new Tokenizer("");
        Tokenizer tfinish = new Tokenizer("");
        tcopy.copy(t);
        p.goDownLB();
        boolean agreed = exeCond(p);
        p.goUP();
        matchKey(TokenKind.LOWER_LOOP);
        t.skipToken();//loop
        while (agreed) {
            p.goDownMB();
            exeStmt_seq(p);
            p.goUP();
            tfinish.copy(t);
            t.copy(tcopy);
            p.goDownLB();
            agreed = exeCond(p);
            p.goUP();
            matchKey(TokenKind.LOWER_LOOP);
            t.skipToken();//loop
            if (!agreed) {
                t.copy(tfinish);
            }
        }
        //System.out.println(t.get());
        while (t.getToken() != TokenKind.LOWER_END) {
            t.skipToken();
        }
        matchKey(TokenKind.LOWER_END);
        t.skipToken();//end
        t.skipToken();//;
    }

    static void exeIn(ParseTree p) throws Exception {
        assert (p.currNTNo() == 10) : "Wrong NTNo";
        if (!s.hasNext()) {
            throw new IllegalStateException("No valid input");
        }
        matchKey(TokenKind.LOWER_READ);
        t.skipToken();//read
        p.goDownLB();
        String new_ids = exeId_list(p);
        //System.out.println(new_ids);
        List<String> ids = Arrays.asList(new_ids.split(","));
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i);
            int value = s.nextInt();
            idMap.put(id, value);
        }
        p.goUP();
        matchKey(TokenKind.SEMICOLON);
        t.skipToken();//;
    }

    static void exeOut(ParseTree p) throws Exception {
        assert (p.currNTNo() == 11) : "Wrong NTNo";
        matchKey(TokenKind.LOWER_WRITE);
        t.skipToken();//write
        p.goDownLB();
        String new_ids = exeId_list(p);
        List<String> ids = Arrays.asList(new_ids.split(","));
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i);
            System.out.println(id + " = " + idMap.get(id));
        }
        p.goUP();
        matchKey(TokenKind.SEMICOLON);
        t.skipToken();//;
    }

    public static boolean exeCond(ParseTree p) throws Exception {
        assert (p.currNTNo() == 12) : "Wrong NTNo";
        boolean res = false;
        if (t.getToken() == TokenKind.LOGICAL_NOT) {
            t.skipToken();//!
            p.goDownLB();
            res = (!exeCond(p));
            p.goUP();
        } else if (t.getToken() == TokenKind.LEFT_SQUARE) {
            t.skipToken();//[
            p.goDownLB();
            boolean first = exeCond(p);
            p.goUP();
            TokenKind token = t.getToken();
            t.skipToken();// || or &&
            p.goDownMB();
            boolean second = exeCond(p);
            p.goUP();
            if (token == TokenKind.AND_OPERATOR) {
                res = first && second;
            } else if (token == TokenKind.OR_OPERATOR) {
                res = first || second;
            }
            t.skipToken();//]
        } else {
            p.goDownLB();
            res = exeComp(p);
            p.goUP();
        }
        //System.out.println(res);
        return res;
    }

    public static boolean exeComp(ParseTree p) throws Exception {
        assert (p.currNTNo() == 13) : "Wrong NTNo";
        boolean res = false;
        matchKey(TokenKind.LEFT_PARENTHESIS);
        t.skipToken();//(
        p.goDownLB();
        int first = exeOp(p);
        p.goUP();
        TokenKind token = t.getToken();
        t.skipToken();//comp_op
        p.goDownMB();
        int second = exeOp(p);
        p.goUP();
        switch (token) {
            case NOT_EQUALITY_TEST:
                res = (first != second);
                break;
            case EQUALITY_TEST:
                res = (first == second);
                break;
            case LESS_TEST:
                res = (first < second);
                break;
            case GREATER_TEST:
                res = (first > second);
                break;
            case LESS_EQUAL_TEST:
                res = (first <= second);
                break;
            case GREATER_EQUAL_TEST:
                res = (first >= second);
                break;
            default:
                break;
        }
        //System.out.println(res);
        matchKey(TokenKind.RIGHT_PARENTHESIS);
        t.skipToken();//)
        return res;
    }

    public static int exeExp(ParseTree p) throws Exception {
        assert (p.currNTNo() == 14) : "Wrong NTNo";
        int res = 0;
        p.goDownLB();
        res = exeTrm(p);
        p.goUP();
        if (t.getToken() == TokenKind.ADD_OPERATOR) {
            t.skipToken();//+
            p.goDownMB();
            res += exeExp(p);
            p.goUP();
        } else if (t.getToken() == TokenKind.MINUS_OPERATOR) {
            t.skipToken();//-
            p.goDownMB();
            res -= exeExp(p);
            p.goUP();
        }
        return res;
    }

    public static int exeTrm(ParseTree p) throws Exception {
        assert (p.currNTNo() == 15) : "Wrong NTNo";
        int res = 0;
        p.goDownLB();
        res += exeOp(p);
        p.goUP();
        if (t.getToken() == TokenKind.MULTIPLY_OPERATOR) {
            t.skipToken();//*
            p.goDownMB();
            res *= exeTrm(p);
            p.goUP();
        }
        return res;
    }

    public static int exeOp(ParseTree p) throws Exception {
        assert (p.currNTNo() == 16) : "Wrong NTNo";
        int res = 0;
        p.goDownLB();
        if (t.getToken() == TokenKind.IDENTIFIER) {
            String new_id = exeId(p);
            if (!idMap.containsKey(new_id)) {
                throw new Exception("Error: Attempt to use variable " + new_id
                        + " without prior initialization.");
            }
            res += idMap.get(new_id);
        } else if (t.getToken() == TokenKind.INTEGER_CONSTANT) {
            res += exeNo(p);
        } else if (t.getToken() == TokenKind.LEFT_PARENTHESIS) {
            t.skipToken();//(
            res += exeExp(p);
            t.skipToken();//)
        } else {
            throw new Exception("ERROR");
        }
        p.goUP();
        return res;
    }

    static String exeId(ParseTree p) throws Exception {
        String res = t.getIdentifier();
        t.skipToken();
        return res;
    }

    static int exeNo(ParseTree p) throws Exception {
        int res = t.getInteger();
        t.skipToken();
        return res;
    }
}

