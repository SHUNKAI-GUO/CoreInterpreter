//Author: Shunkai Guo
//Pretty print the origianl program if needed
public class Print {
    private static Tokenizer t;
    static int numOfSpace = 0;

    public static void printProg(ParseTree p, String program) throws Exception {
        assert (p.currNTNo() == 1) : "Wrong NTNo";
        t = new Tokenizer(program);
        t.skipToken();//program
        System.out.println("program");
        numOfSpace += 4;
        p.goDownLB();
        printDecl_seq(p);
        p.goUP();
        t.skipToken();//begin
        System.out.println("begin");
        p.goDownMB();
        printStmt_seq(p);
        p.goUP();
        t.skipToken();//end
        System.out.println("end");
    }

    public static void printDecl_seq(ParseTree p) throws Exception {
        assert (p.currNTNo() == 2) : "Wrong NTNo";
        p.goDownLB();
        printDecl(p);
        p.goUP();
        if (p.currAlt() == 2) {
            p.goDownMB();
            printDecl_seq(p);
            p.goUP();
        }
    }

    public static void printStmt_seq(ParseTree p) throws Exception {
        assert (p.currNTNo() == 3) : "Wrong NTNo";
        p.goDownLB();
        printStmt(p);
        p.goUP();
        if (p.currAlt() == 2) {
            p.goDownMB();
            printStmt_seq(p);
            p.goUP();
        }
    }

    public static void printDecl(ParseTree p) throws Exception {
        assert (p.currNTNo() == 4) : "Wrong NTNo";
        for (int i = 0; i < numOfSpace; i++) {
            System.out.print(" ");
        }
        t.skipToken();//int
        System.out.print("int ");
        p.goDownLB();
        printId_list(p);
        p.goUP();
        t.skipToken();//;
        System.out.println(";");
    }

    public static void printId_list(ParseTree p) throws Exception {
        assert (p.currNTNo() == 5) : "Wrong NTNo";
        p.goDownLB();
        printId(p);
        p.goUP();
        if (p.currAlt() == 2) {
            t.skipToken();//,
            System.out.print(", ");
            p.goDownMB();
            printId_list(p);
            p.goUP();
        }
    }

    public static void printStmt(ParseTree p) throws Exception {
        assert (p.currNTNo() == 6) : "Wrong NTNo";
        for (int i = 0; i < numOfSpace; i++) {
            System.out.print(" ");
        }
        p.goDownLB();
        if (p.currNTNo() == 7) {
            printAssign(p);
        } else if (p.currNTNo() == 11) {
            printOut(p);
        } else if (p.currNTNo() == 10) {
            printIn(p);
        } else if (p.currNTNo() == 8) {
            printIf(p);
        } else if (p.currNTNo() == 9) {
            printLoop(p);
        }
        p.goUP();
    }

    public static void printAssign(ParseTree p) throws Exception {
        assert (p.currNTNo() == 7) : "Wrong NTNo";
        p.goDownLB();
        printId(p);
        p.goUP();
        t.skipToken();//=
        System.out.print(" = ");
        p.goDownMB();
        printExp(p);
        p.goUP();
        t.skipToken();//;
        System.out.println(";");
    }

    public static void printIf(ParseTree p) throws Exception {
        assert (p.currNTNo() == 8) : "Wrong NTNo";
        t.skipToken();//if
        System.out.print("if ");
        p.goDownLB();
        printCond(p);
        p.goUP();
        t.skipToken();//then
        System.out.println(" then ");
        numOfSpace += 4;
        p.goDownMB();
        printStmt_seq(p);
        p.goUP();
        if (t.getToken() != TokenKind.LOWER_END) {
            for (int i = 0; i < numOfSpace - 4; i++) {
                System.out.print(" ");
            }
            t.skipToken();//else
            System.out.println("else");
            p.goDownRB();
            printStmt_seq(p);
            p.goUP();
        }
        numOfSpace -= 4;
        t.skipToken();//end
        t.skipToken();//;
        for (int i = 0; i < numOfSpace; i++) {
            System.out.print(" ");
        }
        System.out.print("end");
        System.out.println(";");
    }

    public static void printLoop(ParseTree p) throws Exception {
        assert (p.currNTNo() == 9) : "Wrong NTNo";
        t.skipToken();//while
        System.out.print("while ");
        p.goDownLB();
        printCond(p);
        p.goUP();
        t.skipToken();//loop
        System.out.println(" loop ");
        numOfSpace += 4;
        p.goDownMB();
        printStmt_seq(p);
        p.goUP();
        numOfSpace -= 4;
        t.skipToken();//end
        t.skipToken();//;
        for (int i = 0; i < numOfSpace; i++) {
            System.out.print(" ");
        }
        System.out.print("end");
        System.out.println(";");
    }

    static void printIn(ParseTree p) throws Exception {
        assert (p.currNTNo() == 10) : "Wrong NTNo";
        t.skipToken();//write
        System.out.print("read ");
        p.goDownLB();
        printId_list(p);
        p.goUP();
        t.skipToken();//;
        System.out.println(";");
    }

    static void printOut(ParseTree p) throws Exception {
        assert (p.currNTNo() == 11) : "Wrong NTNo";
        t.skipToken();//write
        System.out.print("write ");
        p.goDownLB();
        printId_list(p);
        p.goUP();
        t.skipToken();//;
        System.out.println(";");
    }

    public static void printCond(ParseTree p) throws Exception {
        assert (p.currNTNo() == 12) : "Wrong NTNo";
        if (t.getToken() == TokenKind.LOGICAL_NOT) {
            t.skipToken();//!
            System.out.print("!");
            p.goDownLB();
            printCond(p);
            p.goUP();
        } else if (t.getToken() == TokenKind.LEFT_SQUARE) {
            t.skipToken();//[
            System.out.print("[");
            p.goDownLB();
            printCond(p);
            p.goUP();
            if (t.getToken() == TokenKind.AND_OPERATOR) {
                System.out.print("&&");
            } else if (t.getToken() == TokenKind.OR_OPERATOR) {
                System.out.print("||");
            }
            t.skipToken();// || or &&
            p.goDownMB();
            printCond(p);
            p.goUP();
            t.skipToken();//]
            System.out.print("]");
        } else {
            p.goDownLB();
            printComp(p);
            p.goUP();
        }
    }

    public static void printComp(ParseTree p) throws Exception {
        assert (p.currNTNo() == 13) : "Wrong NTNo";
        t.skipToken();//(
        System.out.print("(");
        p.goDownLB();
        printOp(p);
        p.goUP();
        switch (t.getToken()) {
            case NOT_EQUALITY_TEST:
                System.out.print(" != ");
                break;
            case EQUALITY_TEST:
                System.out.print(" == ");
                break;
            case LESS_TEST:
                System.out.print(" < ");
                break;
            case GREATER_TEST:
                System.out.print(" > ");
                break;
            case LESS_EQUAL_TEST:
                System.out.print(" <= ");
                break;
            case GREATER_EQUAL_TEST:
                System.out.print(" >= ");
                break;
            default:
                break;
        }
        t.skipToken();//comp_op
        p.goDownMB();
        printOp(p);
        p.goUP();
        t.skipToken();//)
        System.out.print(")");
    }

    public static void printExp(ParseTree p) throws Exception {
        assert (p.currNTNo() == 14) : "Wrong NTNo";
        p.goDownLB();
        printTrm(p);
        p.goUP();
        if (t.getToken() == TokenKind.ADD_OPERATOR) {
            t.skipToken();//+
            System.out.print(" + ");
            p.goDownMB();
            printExp(p);
            p.goUP();
        } else if (t.getToken() == TokenKind.MINUS_OPERATOR) {
            t.skipToken();//-
            System.out.print(" - ");
            p.goDownMB();
            printExp(p);
            p.goUP();
        }
    }

    public static void printTrm(ParseTree p) throws Exception {
        assert (p.currNTNo() == 15) : "Wrong NTNo";
        p.goDownLB();
        printOp(p);
        p.goUP();
        if (t.getToken() == TokenKind.MULTIPLY_OPERATOR) {
            t.skipToken();//*
            System.out.print(" * ");
            p.goDownMB();
            printTrm(p);
            p.goUP();
        }
    }

    public static void printOp(ParseTree p) throws Exception {
        assert (p.currNTNo() == 16) : "Wrong NTNo";
        p.goDownLB();
        if (t.getToken() == TokenKind.IDENTIFIER) {
            printId(p);
        } else if (t.getToken() == TokenKind.INTEGER_CONSTANT) {
            printNo(p);
        } else if (t.getToken() == TokenKind.LEFT_PARENTHESIS) {
            t.skipToken();//(
            System.out.print("(");
            printExp(p);
            t.skipToken();//)
            System.out.print(")");
        } else {
            throw new Exception("ERROR");
        }
        p.goUP();
    }

    static void printId(ParseTree p) throws Exception {
        System.out.print(t.getIdentifier());
        t.skipToken();
    }

    static void printNo(ParseTree p) throws Exception {
        System.out.print(t.getInteger());
        t.skipToken();
    }

}
