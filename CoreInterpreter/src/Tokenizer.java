
public class Tokenizer {

    private String program = "";

    public Tokenizer(String program) {
        this.program = program;
    }

    public void copy(Tokenizer copy) {
        this.program = copy.program;
    }

    public String get() {
        return this.program;
    }

    public String getIdentifier() {
        int curCharNum = 0;
        char curChar = this.program.charAt(curCharNum);
        if (curChar >= 'A' && curChar <= 'Z') {
            if (curCharNum < this.program.length() - 1) {
                int nextCharNum = curCharNum + 1;
                char nextChar = this.program.charAt(nextCharNum);
                if (nextChar < 'A' || nextChar > 'Z') {
                    while (nextChar >= '0' && nextChar <= '9'
                            && (curCharNum < this.program.length() - 1)) {
                        curCharNum++;
                        nextCharNum++;
                        nextChar = this.program.charAt(nextCharNum);
                    }
                } else {
                    while (nextChar >= 'A' && nextChar <= 'Z'
                            && (curCharNum < this.program.length() - 1)) {
                        curCharNum++;
                        nextCharNum++;
                        nextChar = this.program.charAt(nextCharNum);
                    }
                    while (nextChar >= '0' && nextChar <= '9'
                            && (nextCharNum < this.program.length() - 1)) {
                        curCharNum++;
                        nextCharNum++;
                        nextChar = this.program.charAt(nextCharNum);
                    }
                }
            }
        }
        return this.program.substring(0, curCharNum + 1);
    }

    public int getInteger() {
        int curCharNum = 0;
        char curChar = this.program.charAt(curCharNum);
        if (curChar <= '9' && curChar >= '0') {
            while (curCharNum < this.program.length() - 1) {
                curCharNum++;
                char nextChar = this.program.charAt(curCharNum);
                if (nextChar > '9' || nextChar < '0') {
                    break;
                }
            }
        }
        return Integer.parseInt(this.program.substring(0, curCharNum));
    }

    public TokenKind getToken() {
        TokenKind tokenkind = TokenKind.ERROR;
        if (this.program.length() == 0) {
            return TokenKind.EOF;
        }
        int curCharNum = 0;
        char curChar = this.program.charAt(curCharNum);
        if (curChar == ';') {
            tokenkind = TokenKind.SEMICOLON;
        } else if (curChar <= 'z' && curChar >= 'a') {
            switch (curChar) {
                case 'p':
                    if (this.program.substring(curCharNum, curCharNum + 7)
                            .equals("program")) {
                        tokenkind = TokenKind.LOWER_PROGRAM;
                    } else {
                        tokenkind = TokenKind.ERROR;
                    }
                    break;
                case 'i':
                    if (this.program.substring(curCharNum, curCharNum + 3)
                            .equals("int")) {
                        tokenkind = TokenKind.LOWER_INT;
                    } else if (this.program
                            .substring(curCharNum, curCharNum + 2)
                            .equals("if")) {
                        tokenkind = TokenKind.LOWER_IF;
                    }
                    break;
                case 'b':
                    if (this.program.substring(curCharNum, curCharNum + 5)
                            .equals("begin")) {
                        tokenkind = TokenKind.LOWER_BEGIN;
                    } else {
                        tokenkind = TokenKind.ERROR;
                    }
                    break;
                case 'e':
                    if (this.program.substring(curCharNum, curCharNum + 3)
                            .equals("end")) {
                        tokenkind = TokenKind.LOWER_END;
                    } else if (this.program
                            .substring(curCharNum, curCharNum + 4)
                            .equals("else")) {
                        tokenkind = TokenKind.LOWER_ELSE;
                    }
                    break;
                case 't':
                    if (this.program.substring(curCharNum, curCharNum + 4)
                            .equals("then")) {
                        tokenkind = TokenKind.LOWER_THEN;
                    } else {
                        tokenkind = TokenKind.ERROR;
                    }
                    break;
                case 'w':
                    if (this.program.substring(curCharNum, curCharNum + 5)
                            .equals("while")) {
                        tokenkind = TokenKind.LOWER_WHILE;
                    } else if (this.program
                            .substring(curCharNum, curCharNum + 5)
                            .equals("write")) {
                        tokenkind = TokenKind.LOWER_WRITE;
                    }
                    break;
                case 'l':
                    if (this.program.substring(curCharNum, curCharNum + 4)
                            .equals("loop")) {
                        tokenkind = TokenKind.LOWER_LOOP;
                    } else {
                        tokenkind = TokenKind.ERROR;
                    }
                    break;
                case 'r':
                    if (this.program.substring(curCharNum, curCharNum + 4)
                            .equals("read")) {
                        tokenkind = TokenKind.LOWER_READ;
                    } else {
                        tokenkind = TokenKind.ERROR;
                    }
                    break;
                default:
                    tokenkind = TokenKind.ERROR;
            }
        } else if (curChar <= '9' && curChar >= '0') {
            tokenkind = TokenKind.INTEGER_CONSTANT;
            while (curCharNum < this.program.length() - 1) {
                curCharNum++;
                char nextChar = this.program.charAt(curCharNum);
                if (nextChar <= 'z' && nextChar >= 'a'
                        || nextChar >= 'A' && nextChar <= 'Z') {
                    tokenkind = TokenKind.ERROR;
                    break;
                } else if (nextChar > '9' || nextChar < '0') {
                    break;
                }
            }
        } else if (curChar == '=') {
            tokenkind = TokenKind.ASSIGNMENT_OPERATOR;
            if (this.program.charAt(curCharNum + 1) == '=') {
                tokenkind = TokenKind.EQUALITY_TEST;
            }
        } else if (curChar == '|') {
            if (this.program.charAt(curCharNum + 1) == '|') {
                tokenkind = TokenKind.OR_OPERATOR;
            }
        } else if (curChar >= 'A' && curChar <= 'Z') {
            tokenkind = TokenKind.IDENTIFIER;
            if (curCharNum < this.program.length() - 1) {
                curCharNum++;
                char nextChar = this.program.charAt(curCharNum);
                if (nextChar < 'A' || nextChar > 'Z') {
                    while (nextChar >= '0' && nextChar <= '9'
                            && (curCharNum < this.program.length() - 1)) {
                        nextChar = this.program.charAt(curCharNum++);
                    }
                } else {
                    while (nextChar >= 'A' && nextChar <= 'Z'
                            && (curCharNum < this.program.length() - 1)) {
                        nextChar = this.program.charAt(curCharNum++);
                    }
                    while (nextChar >= '0' && nextChar <= '9'
                            && (curCharNum < this.program.length() - 1)) {
                        nextChar = this.program.charAt(curCharNum++);
                    }
                }
                if (nextChar <= 'z' && nextChar >= 'a'
                        || nextChar >= 'A' && nextChar <= 'Z') {
                    tokenkind = TokenKind.ERROR;
                }
            }
        } else if (curChar == ',') {
            tokenkind = TokenKind.COMMA;
        } else if (curChar == '!') {
            tokenkind = TokenKind.LOGICAL_NOT;
            if (this.program.charAt(curCharNum + 1) == '=') {
                tokenkind = TokenKind.NOT_EQUALITY_TEST;
            }
        } else if (curChar == '[') {
            tokenkind = TokenKind.LEFT_SQUARE;
        } else if (curChar == ']') {
            tokenkind = TokenKind.RIGHT_SQUARE;
        } else if (curChar == '&') {
            if (this.program.charAt(curCharNum + 1) == '&') {
                tokenkind = TokenKind.AND_OPERATOR;
            }
        } else if (curChar == '(') {
            tokenkind = TokenKind.LEFT_PARENTHESIS;
        } else if (curChar == ')') {
            tokenkind = TokenKind.RIGHT_PARENTHESIS;
        } else if (curChar == '+') {
            tokenkind = TokenKind.ADD_OPERATOR;
        } else if (curChar == '-') {
            tokenkind = TokenKind.MINUS_OPERATOR;
        } else if (curChar == '*') {
            tokenkind = TokenKind.MULTIPLY_OPERATOR;
        } else if (curChar == '<') {
            tokenkind = TokenKind.LESS_TEST;
            if (this.program.charAt(curCharNum + 1) == '=') {
                tokenkind = TokenKind.LESS_EQUAL_TEST;
            }
        } else if (curChar == '>') {
            tokenkind = TokenKind.GREATER_TEST;
            if (this.program.charAt(curCharNum + 1) == '=') {
                tokenkind = TokenKind.GREATER_EQUAL_TEST;
            }
        }
        return tokenkind;
    }

    public void skipToken() {
        int curCharNum = 0;
        switch (this.getToken()) {
            case SEMICOLON:
            case COMMA:
            case LOGICAL_NOT:
            case LEFT_SQUARE:
            case RIGHT_SQUARE:
            case LEFT_PARENTHESIS:
            case RIGHT_PARENTHESIS:
            case ADD_OPERATOR:
            case MINUS_OPERATOR:
            case MULTIPLY_OPERATOR:
            case LESS_TEST:
            case GREATER_TEST:
            case ASSIGNMENT_OPERATOR: {
                curCharNum = 1;
                break;
            }
            case OR_OPERATOR:
            case AND_OPERATOR:
            case EQUALITY_TEST:
            case NOT_EQUALITY_TEST:
            case LESS_EQUAL_TEST:
            case GREATER_EQUAL_TEST:
            case LOWER_IF: {
                curCharNum = 2;
                break;
            }
            case LOWER_INT:
            case LOWER_END: {
                curCharNum = 3;
                break;
            }
            case LOWER_LOOP:
            case LOWER_READ:
            case LOWER_THEN:
            case LOWER_ELSE: {
                curCharNum = 4;
                break;
            }
            case LOWER_BEGIN:
            case LOWER_WHILE:
            case LOWER_WRITE: {
                curCharNum = 5;
                break;
            }
            case LOWER_PROGRAM: {
                curCharNum = 7;
                break;
            }

            case INTEGER_CONSTANT: {
                while (this.program.charAt(curCharNum) >= '0'
                        && this.program.charAt(curCharNum) <= '9') {
                    curCharNum++;
                }
                break;
            }
            case IDENTIFIER: {
                while (this.program.charAt(curCharNum) >= '0'
                        && this.program.charAt(curCharNum) <= '9'
                        || this.program.charAt(curCharNum) >= 'A'
                                && this.program.charAt(curCharNum) <= 'Z') {
                    curCharNum++;
                }
                break;
            }
            default:
                break;
        }

        while (curCharNum < this.program.length()
                && (this.program.charAt(curCharNum) == ' '
                        || this.program.charAt(curCharNum) == '\n'
                        || this.program.charAt(curCharNum) == '\r'
                        || this.program.charAt(curCharNum) == '\t')) {
            curCharNum++;
        }
        this.program = this.program.substring(curCharNum);

    }

}

