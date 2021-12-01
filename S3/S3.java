import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
public class DFA {
    private int stateCount;
    private int startState;
    private int acceptingStatesN;
    private HashMap<Integer, State> states;
    private List<ArrayList<ArrayList<Character>>> string_permutations;
    private Queue<Pair<ArrayList<ArrayList<Character>>, State>> stateQueue;
    private int[] visits;
    public DFA(int stateCount, int startState) {
        this.stateCount = stateCount;
        this.startState = startState;
        states = new HashMap<Integer, State>();
        string_permutations = new ArrayList<ArrayList<ArrayList<Character>>>();
        stateQueue = new LinkedList<Pair<ArrayList<ArrayList<Character>>, State>>();
        visits = new int[stateCount];
        Arrays.fill(visits, 0);
        for(int i = 0; i < stateCount; i++)
            states.put(i, new State(i));
    }


    public void addTransition(int from, int to, char sym) {
        states.get(from).addTransition(states.get(to), sym);
    }
    public void setAccepting(int state) {
        states.get(state).setAccepting(true);
    }
    public List<String> getAcceptingStrings(int maxCount) {
        Pair<ArrayList<ArrayList<Character>>, State> state;
        int permCount = 0;
        /**
         * Add first state to queue.
         *
         * The pair is a combination of the next state to visit together with a multidim array containing the path taken before getting to this state.
         *
         * For instance, assuming that the next state is state 4, thus we have visited 3 states before:
         *
         * State: state4
         * Path: [ [a,b,c], [x], [l, s] ]
         *
         * In this path there were 3 transitions from state 1 to 2, 1 transition from state 2 to 3 and 3 transitions from state 3 to 4.
         * By creating permutations of all the paths above, we get all the possible combinations to reach the accepting state, assuming that state 4 is an accepting state in this case.
         */
        stateQueue.add(new Pair<ArrayList<ArrayList<Character>>, State>(new ArrayList<ArrayList<Character>>(), states.get(startState)));
        /* Iterate each state and add their relatives to queue */
        //long startTime = System.currentTimeMillis();


        /**
         * Traverse the DFA using BFS until we have enough permutations / paths as requested
         */
        while(permCount < maxCount) {
            state = stateQueue.poll();
            if(state == null)
                break;
            visits[state.getSecond().getId()]++;
            /**
             * If we reach an accepting state, add the current path to the string_permutations
             */
            if(state.getSecond().isAccepting()) {
                string_permutations.add(state.getFirst());
                permCount += countPermutations(state.getFirst());
            }
            /**
             * This means we reached our requested amount of strings
             */
            if(permCount >= maxCount)
                break;
            /**
             * Add every neighboring state of the current state to the queue, so that we traverse every possible state until we have enough strings
             */
            for(Map.Entry<State, ArrayList<Character>> connectedState : state.getSecond().getTransitions().entrySet()) {
                if(visits[connectedState.getKey().getId()] >= 400)
                    continue;
                ArrayList<ArrayList<Character>> new_list = new ArrayList<ArrayList<Character>>(state.getFirst());
                new_list.add(connectedState.getValue());
                stateQueue.add(new Pair<ArrayList<ArrayList<Character>>, State>(new_list, connectedState.getKey()));
            }
        }
        return permutate(maxCount);
    }
    /**
     * Find the permutations of all the paths
     *
     * @param maxCount the amount of strings requested
     * @return the strings
     */
    private List<String> permutate(int maxCount) {
        List<String> strings;
        ArrayList<String> transition;
        strings = new ArrayList<String>();
        /**
         * Iterate all paths and generate their corresponding permutations
         */
        for(ArrayList<ArrayList<Character>> path : string_permutations) {
            /**
             * This means we reached an accepting state directly on the first state, thus there are no transitions but the empty string is still valid in the DFA
             */
            if(path.size() == 0) {
                strings.add("");
                continue;
            }
            /**
             * Do we have enough strings yet?
             */
            if(strings.size() >= maxCount)
                break;
            transition = new ArrayList<String>();
            generatePermutations(path, transition, 0, "", maxCount - strings.size());
            /**
             * Add the generated strings of the current path to all the other generated strings
             */
            strings.addAll(transition);
        }
        return strings;
    }
    /**
     * Used to generate permutations of a path of the form [ [x, y, z, ...], [a, b, c, ....], ....]
     *
     * @param lists the path multidimensional array
     * @param result this will store the resulting strings of the current path
     * @param depth the depth in the lowest dimension array
     * @param current the current string being generated
     * @param maxCount the amount of strings still needed to be generated
     */
    private void generatePermutations(ArrayList<ArrayList<Character>> lists, List<String> result, int depth, String current, int maxCount) {
        if(result.size() >= maxCount)
            return;
        /**
         * We reached the end of the path, thus add the string
         */
        if (depth == lists.size()) {
            result.add(current);
            return;
        }
        /**
         * This is for performance enchanement when a transition has many different ways, > 10
         */
        int size = lists.get(depth).size();
        for (int i = 0; i < size; i++) {
            if(result.size() >= maxCount)
                return;
            generatePermutations(lists, result, depth + 1, current + lists.get(depth).get(i), maxCount);
        }
    }
    /**
     * Count the amount of permutations that are in a path
     *
     * @param arr the path multidimensional array
     * @return the number of permutations
     */
    private long countPermutations(ArrayList<ArrayList<Character>> arr) {
        long tot = 1;
        for(ArrayList<Character> i : arr) {
            if(tot > 1000)
                return 1000;
            tot *= i.size();
        }
        return tot;
    }
}