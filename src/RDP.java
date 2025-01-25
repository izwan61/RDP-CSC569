import java.io.*;

class RDP { // Recursive Descent Parser (Pushdown Machine)

    String inp;
    int pos = 0;  // Position in the input string
    String[] tokens; // Tokens to be parsed

    public static void main(String[] args) throws IOException {
        RDP rdp = new RDP();
        rdp.parse();
    }

    void parse() {
        System.out.println("Enter input string (use '↵' as the end marker):");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String input = reader.readLine(); // Read the input line
            tokens = input.split("\\s+"); // Split the input into tokens
            inp = tokens[pos]; // Set the first token
            //System.out.println("Starting parse...");
            Expr(); // Start parsing with Expr
            if (inp.equals("↵")) {
                accept(); // End of string marker
            } else {
                reject(); // Reject if end marker is not found
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Expr → Term Elist
    void Expr() {
        //System.out.println("Entering Expr...");
        Term();  // First, parse the term (this could be a factor or nested expression)
        Elist(); // Then, parse the Elist (addition/subtraction)
    }

    // Elist → + Term {ADD} Elist | ε
    void Elist() {
        //System.out.println("Entering Elist...");
        if (inp.equals("+")) { // Handle addition
            inp = getInp();
            Term(); // Parse the next term after "+"
            System.out.println("ADD");
            Elist(); // Continue parsing more terms or end
        } else {
            // Epsilon transition (nothing happens), end of Elist
            return;
        }
    }

    // Term → Factor Tlist
    void Term() {
        //System.out.println("Entering Term...");
        Factor();  // First, parse the factor (this could be a variable or nested expression)
        Tlist();   // Then, parse the Tlist (multiplication)
    }

    // Tlist → * Factor {MUL} Tlist | ε
    void Tlist() {
        //System.out.println("Entering Tlist...");
        if (inp.equals("*")) { // Handle multiplication
            inp = getInp();
            Factor(); // Parse the next factor after "*"
            System.out.println("MUL");
            Tlist(); // Continue parsing more multiplication or end
        } else {
            // Epsilon transition (nothing happens), end of Tlist
            return;
        }
    }

    // Factor → ( Expr ) | var {PRINT}
    void Factor() {
        //System.out.println("Entering Factor...");
        if (inp.equals("(")) { // Handle opening parenthesis
            inp = getInp(); // Consume '('
            Expr();  // Parse the expression inside the parentheses
            if (inp.equals(")")) { // Expect closing parenthesis
                inp = getInp(); // Consume ')'
            } else {
                reject(); // Reject if no closing parenthesis found
            }
        } else if (inp.equals("var")) { // Handle variable (var)
            inp = getInp();
            System.out.println("PRINT");
        } else {
            reject(); // Reject if neither a '(' nor 'var' is encountered
        }
    }

    // Accept input
    void accept() {
        System.out.println("accept"); // Parsing successful
    }

    // Reject input
    void reject() {
        System.out.println("reject"); // Parsing failed
        System.exit(0); // Terminate the parser
    }

    // Get the next input token
    String getInp() {
        pos++;
        if (pos < tokens.length) {
            return tokens[pos];
        }
        return "↵"; // End marker
    }
}
