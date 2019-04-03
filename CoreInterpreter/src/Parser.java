//Author:Shunkai Guo
//Parse the original program to the parse tree structure
public class Parser {

    private static Tokenizer t;

    public static void print(ParseTree p, String program) throws Exception {
        //System.out.println(p.currNTNo());
        Print.printProg(p, program);
    }

    public static ParseTree parse(String program) throws Exception {
        t = new Tokenizer(program);
        ParseTree parseTree = new ParseTree();
        parseProg(parseTree);
        return parseTree;
    }

    static void matchKey(TokenKind key) throws Exception {
        if (t.getToken() != key) {
            throw new Exception("ERROR KEYWORD");
        }
    }

    static void parseProg(ParseTree p) throws Exception {
        matchKey(TokenKind.LOWER_PROGRAM);
        t.skipToken();//program
        p.setNTNo(1);
        p.setAlt(2);
        p.createLB();
        p.goDownLB();
        parseDecl_seq(p);
        p.goUP();
        matchKey(TokenKind.LOWER_BEGIN);
        t.skipToken();//begin
        p.createMB();
        p.goDownMB();
        parseStmt_seq(p);
        p.goUP();
        matchKey(TokenKind.LOWER_END);
        t.skipToken();//end
    }

    static void parseDecl_seq(ParseTree p) throws Exception {
        matchKey(TokenKind.LOWER_INT);
        p.setNTNo(2);
        p.setAlt(1);
        p.createLB();
        p.goDownLB();
        parseDecl(p);
        p.goUP();
        if (t.getToken() == TokenKind.LOWER_INT) {
            p.setAlt(2);
            p.createMB();
            p.goDownMB();
            parseDecl_seq(p);
            p.goUP();
        }
    }

    static void parseStmt_seq(ParseTree p) throws Exception {
        p.setNTNo(3);
        p.setAlt(1);
        p.createLB();
        p.goDownLB();
        parseStmt(p);
        p.goUP();
        if ((t.getToken() != TokenKind.LOWER_END)
                && (t.getToken() != TokenKind.LOWER_ELSE)) {
            p.setAlt(2);
            p.createMB();
            p.goDownMB();
            parseStmt_seq(p);
            p.goUP();
        }
    }

    static void parseDecl(ParseTree p) throws Exception {
        matchKey(TokenKind.LOWER_INT);
        t.skipToken();//int
        p.setNTNo(4);
        p.setAlt(1);
        p.createLB();
        p.goDownLB();
        parseId_list(p);
        p.goUP();
        t.skipToken();//;
    }

    static void parseId_list(ParseTree p) throws Exception {
        p.setNTNo(5);
        p.setAlt(1);
        p.createLB();
        p.goDownLB();
        parseId(p);
        p.goUP();
        if (t.getToken() == TokenKind.COMMA) {
            t.skipToken();//,
            p.setAlt(2);
            p.createMB();
            p.goDownMB();
            parseId_list(p);
            p.goUP();
        }
    }

    static void parseStmt(ParseTree p) throws Exception {
        p.setNTNo(6);
        if (t.getToken() == TokenKind.IDENTIFIER) {
            p.setAlt(1);
            p.createLB();
            p.goDownLB();
            parseAssign(p);
            p.goUP();
        } else if (t.getToken() == TokenKind.LOWER_WRITE) {
            p.setAlt(1);
            p.createLB();
            p.goDownLB();
            parseOut(p);
            p.goUP();
        } else if (t.getToken() == TokenKind.LOWER_READ) {
            p.setAlt(1);
            p.createLB();
            p.goDownLB();
            parseIn(p);
            p.goUP();
        } else if (t.getToken() == TokenKind.LOWER_IF) {
            p.setAlt(1);
            p.createLB();
            p.goDownLB();
            parseIf(p);
            p.goUP();
        } else if (t.getToken() == TokenKind.LOWER_WHILE) {
            p.setAlt(1);
            p.createLB();
            p.goDownLB();
            parseLoop(p);
            p.goUP();
        }
    }

    static void parseAssign(ParseTree p) throws Exception {
        p.setNTNo(7);
        p.setAlt(2);
        p.createLB();
        p.goDownLB();
        parseId(p);
        p.goUP();
        matchKey(TokenKind.ASSIGNMENT_OPERATOR);
        t.skipToken();//=
        p.createMB();
        p.goDownMB();
        parseExp(p);
        p.goUP();
        t.skipToken();//;
    }

    static void parseIf(ParseTree p) throws Exception {
        matchKey(TokenKind.LOWER_IF);
        t.skipToken();//if
        p.setNTNo(8);
        p.setAlt(2);
        p.createLB();
        p.goDownLB();
        parseCond(p);
        p.goUP();
        matchKey(TokenKind.LOWER_THEN);
        t.skipToken();//then
        p.createMB();
        p.goDownMB();
        parseStmt_seq(p);
        p.goUP();
        if (t.getToken() != TokenKind.LOWER_END) {
            matchKey(TokenKind.LOWER_ELSE);
            t.skipToken();//else
            p.createRB();
            p.goDownRB();
            parseStmt_seq(p);
            p.goUP();
        }
        matchKey(TokenKind.LOWER_END);
        t.skipToken();//end
        t.skipToken();//;
    }

    static void parseLoop(ParseTree p) throws Exception {
        matchKey(TokenKind.LOWER_WHILE);
        p.setNTNo(9);
        t.skipToken();//while
        p.setAlt(2);
        p.createLB();
        p.goDownLB();
        parseCond(p);
        p.goUP();
        matchKey(TokenKind.LOWER_LOOP);
        t.skipToken();//loop
        p.createMB();
        p.goDownMB();
        parseStmt_seq(p);
        p.goUP();
        matchKey(TokenKind.LOWER_END);
        t.skipToken();//end
        t.skipToken();//;
    }

    static void parseIn(ParseTree p) throws Exception {
        matchKey(TokenKind.LOWER_READ);
        t.skipToken();//read
        p.setNTNo(10);
        p.setAlt(1);
        p.createLB();
        p.goDownLB();
        parseId_list(p);
        p.goUP();
        t.skipToken();//;
    }

    static void parseOut(ParseTree p) throws Exception {
        matchKey(TokenKind.LOWER_WRITE);
        t.skipToken();//write
        p.setNTNo(11);
        p.setAlt(1);
        p.createLB();
        p.goDownLB();
        parseId_list(p);
        p.goUP();
        t.skipToken();//;
    }

    static void parseCond(ParseTree p) throws Exception {
        p.setNTNo(12);
        if (t.getToken() == TokenKind.LOGICAL_NOT) {
            t.skipToken();//!
            p.setAlt(1);
            p.createLB();
            p.goDownLB();
            parseCond(p);
            p.goUP();
        } else if (t.getToken() == TokenKind.LEFT_SQUARE) {
            t.skipToken();//[
            p.setAlt(2);
            p.createLB();
            p.goDownLB();
            parseCond(p);
            p.goUP();
            t.skipToken();// || or &&
            p.createMB();
            p.goDownMB();
            parseCond(p);
            p.goUP();
            t.skipToken();//]
        } else {
            p.setAlt(1);
            p.createLB();
            p.goDownLB();
            parseComp(p);
            p.goUP();
        }
    }

    static void parseComp(ParseTree p) throws Exception {
        matchKey(TokenKind.LEFT_PARENTHESIS);
        p.setNTNo(13);
        t.skipToken();//(
        p.setAlt(2);
        p.createLB();
        p.goDownLB();
        parseOp(p);
        p.goUP();
        t.skipToken();//comp_op
        p.createMB();
        p.goDownMB();
        parseOp(p);
        p.goUP();
        t.skipToken();//)
    }

    static void parseExp(ParseTree p) throws Exception {
        p.setNTNo(14);
        p.setAlt(1);
        p.createLB();
        p.goDownLB();
        parseTrm(p);
        p.goUP();
        if ((t.getToken() == TokenKind.ADD_OPERATOR)
                || ((t.getToken() == TokenKind.MINUS_OPERATOR))) {
            t.skipToken();//+/-
            p.setAlt(2);
            p.createMB();
            p.goDownMB();
            parseExp(p);
            p.goUP();
        }
    }

    static void parseTrm(ParseTree p) throws Exception {
        p.setNTNo(15);
        p.setAlt(1);
        p.createLB();
        p.goDownLB();
        parseOp(p);
        p.goUP();
        if (t.getToken() == TokenKind.MULTIPLY_OPERATOR) {
            t.skipToken();//*
            p.setAlt(2);
            p.createMB();
            p.goDownMB();
            parseTrm(p);
            p.goUP();
        }
    }

    static void parseOp(ParseTree p) throws Exception {
        p.setNTNo(16);
        p.setAlt(1);
        p.createLB();
        p.goDownLB();
        if (t.getToken() == TokenKind.IDENTIFIER) {
            parseId(p);
        } else if (t.getToken() == TokenKind.INTEGER_CONSTANT) {
            parseNo(p);
        } else if (t.getToken() == TokenKind.LEFT_PARENTHESIS) {
            t.skipToken();//(
            parseExp(p);
            t.skipToken();//)
        } else {
            throw new Exception("ERROR");
        }
        p.goUP();
    }

    static void parseId(ParseTree p) throws Exception {
        //System.out.println(t.getIdentifier());
        p.setId(t.getIdentifier());
        t.skipToken();
    }

    static void parseNo(ParseTree p) throws Exception {
        //System.out.println(t.getInteger());
        p.setValue(t.getInteger());
        t.skipToken();
    }
}
