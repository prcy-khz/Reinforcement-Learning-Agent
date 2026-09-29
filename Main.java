import java.util.Random;
import java.util.Scanner;

public class Main {

    // ===== STATES (energy levels) =====
    static final String[] STATES = {"Exhausted", "Low", "Medium", "High"};

    // ===== ACTIONS (study activities) =====
    static final String[] ACTIONS = {"Revise Notes", "Practice Problems", "Group Study", "Take a Break"};

    // ===== HYPERPARAMETERS =====
    static final double ALPHA = 0.5;    // learning rate
    static final double GAMMA = 0.9;    // discount factor
    static final double EPSILON = 0.2;  // exploration rate

    // ===== Q-TABLE: one row per state, one column per action =====
    static double[][] qTable = new double[STATES.length][ACTIONS.length];

    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initialiseQTable();

        int choice;
        do {
            displayMenu();
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    trainAgent();
                    break;
                case 2:
                    getRecommendation();
                    break;
                case 3:
                    displayQTable();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    // ------------------------------------------------------------
    // Menu
    // ------------------------------------------------------------
    static void displayMenu() {
        System.out.println();
        System.out.println("===== STUDY PLANNING AGENT =====");
        System.out.println();
        System.out.println("1. Train Agent");
        System.out.println("2. Get Recommendation");
        System.out.println("3. Display Q-Table");
        System.out.println("4. Exit");
    }

    // ------------------------------------------------------------
    // Q-table
    // ------------------------------------------------------------
    static void initialiseQTable() {
        // TODO: set every Q-value to 0
    	for(int i = 0; i < STATES.length; i++) {
    		for(int j = 0; j < ACTIONS.length; j++) {
    			qTable[i][j] = 0;
    		}
    	}
    }

    static void displayQTable() {
        // TODO: print a header row with the action names,
        //       then one row per state showing its 4 Q-values (2 decimal places)
    	System.out.print("\t");
    	for(int i = 0; i < ACTIONS.length; i++) {
    		System.out.print(ACTIONS[i] + "\t");
    	}
    	System.out.println();
    	
    	for(int i = 0; i < STATES.length; i++) {
    		System.out.print(STATES[i] + "\t");
    		for(int j = 0; j < ACTIONS.length; j++) {
    			System.out.printf("%.2f\t" , qTable[i][j]);
    		}
    		System.out.println();
    	}
    }

    // ------------------------------------------------------------
    // Training
    // ------------------------------------------------------------
    static void trainAgent() {
        // TODO: ask how many episodes to train for
        // TODO: for each episode:
        //   1. state      = readState("Enter current energy level")
        //   2. action     = chooseAction(state)
        //   3. display the selected activity
        //   4. reward     = readRating()            (1-5)
        //   5. nextState  = readState("Enter NEW energy level after the activity")
        //   6. updateQValue(state, action, reward, nextState)
    	
    	System.out.println("How many episode?");
    	int episodes = scanner.nextInt();
    	
    	for(int i = 0; i < episodes; i++) {
    		int state = readState("Enter current energy level");
    		
    		int action = chooseAction(state);
    		
    		System.out.println("Selected Activity: " + ACTIONS[action]);
    		
    		int reward = readRating();
    		
    		int nextState = readState("Enter new energy level");
    		
    		updateQValue(state, action, reward, nextState);
    	}
    }

    // epsilon-greedy action selection
    static int chooseAction(int state) {
        // TODO: if random number < EPSILON            -> explore (random action)
        // TODO: else if all Q-values in the row are 0 -> explore (random action)
        // TODO: else                                  -> exploit (bestAction(state))
    	
    	if(random.nextDouble() < EPSILON) {
    		return random.nextInt(ACTIONS.length);
    		
    	}else if (allZero(state) == true) {
    		return random.nextInt(ACTIONS.length);
    	}else {
    		return bestAction(state);
    	}
        
    }

    // index of the highest Q-value in a row
    static int bestAction(int state) {
        // TODO: loop through the row and return the index of the max value
    	int max = 0;
    	for(int i = 0; i < qTable[state].length; i++) {
    		if(qTable[state][i] > qTable[state][max]) {
    			max = i;
    		}
    	}
    	
        return max;
    }

    // highest Q-value in a row (used for maxQ(s'))
    static double maxQ(int state) {
        // TODO: return the largest value in qTable[state]
    	int maxQ = 0;
    	
    	for(int i = 0; i < qTable[state].length; i++) {
    		if(qTable[state][i] > qTable[state][maxQ]){
    			maxQ = i;
    		}
    	}
        return qTable[state][maxQ];
    }

    static boolean allZero(int state) {
        // TODO: return true if every Q-value in the row equals 0
    	for(int i = 0; i < qTable[state].length ; i++) {
    		if(qTable[state][i] != 0) {
    			return false;
    		}
    	}
    	return true;
      
    }

    static void updateQValue(int state, int action, int reward, int nextState) {
        // TODO: Q = Q + ALPHA * (reward + GAMMA * maxQ(nextState) - Q)
        // Hint: calculate maxQ(nextState) BEFORE changing qTable[state][action]
        // TODO: optionally print the new Q-value
    	double qValue = qTable[state][action];
    	
    	double newqValue = qValue + ALPHA * (reward + GAMMA * maxQ(nextState) - qValue);
    	
    	qTable[state][action] = newqValue;
    	
    	System.out.println("New Q Value: " + newqValue);
    	
    }

    // ------------------------------------------------------------
    // Recommendation (inference - must NOT update the Q-table)
    // ------------------------------------------------------------
    static void getRecommendation() {
        // TODO: state = readState("Enter current energy level")
        // TODO: find the action with the highest Q-value (bestAction)
        // TODO: display the recommended activity
    	
    	int state = readState("Enter current energy level");
    	
    	int action = bestAction(state);
    	
    	
    		System.out.println("Recommended activity: " + ACTIONS[action]);
    	
    	
    }

    // ------------------------------------------------------------
    // Input helpers
    // ------------------------------------------------------------
    static int readState(String prompt) {
        // TODO: list the 4 energy levels (1-4), keep asking until the
        //       user enters a valid number, return the index (0-3)
    	for(int i = 0;  i < STATES.length; i++) {
    		System.out.println((i + 1) + ")" + STATES[i]);
    	}
    	int state;
    	
    	do {
    		state = readInt("Enter Energy Level");
    	}while(state < 1 || state > 4);
    	
    	
        return state - 1;
    }

    static int readRating() {
        // TODO: keep asking until the user enters an integer from 1 to 5
    		int rating;
    		
    		do {
    			rating = readInt("Enter rating (1-5)");
    		}while(rating < 1 || rating > 5);
    		
    		return rating;
    }

    static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Please enter a number: ");
        }
        return scanner.nextInt();
    }
}
