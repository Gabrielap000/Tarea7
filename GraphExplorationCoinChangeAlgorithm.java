package uniandes.algorithms.coinchange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
/**
 * Graph exploration algorithm for the coin change problem
 * @author Jorge Duitama
 */
public class GraphExplorationCoinChangeAlgorithm implements CoinChangeAlgorithm {

	//Input data
	private int totalValue;
	private int [] denominations;
	
	@Override
	public int[] calculateOptimalChange(int totalValue, int[] denominations) {
		//Input data is saved as class attributes to avoid passing the parameters through the different methods
		this.totalValue = totalValue;
		this.denominations = denominations;
		//To limit the number of solutions that will be compared in the cycle below
		//the suboptimal solution provided the greedy algorithm would be used as upper bound 
		int [] greedySol = new GreedyCoinChangeAlgorithm().calculateOptimalChange(totalValue, denominations);
		CoinChangeState opt = new CoinChangeState(greedySol); 
		int maxCoins = opt.getTotalCoins()-1;
		int minCoins = 0;
		while(minCoins<maxCoins) {
			int boundNumberOfCoins = (minCoins+maxCoins)/2;
			//Call of the graph exploration algorithm to find feasible solutions
			CoinChangeState solution = findFeasibleSolution(boundNumberOfCoins);
			if(solution==null) {
				minCoins = boundNumberOfCoins+1;
			} else {
				opt = solution;
				maxCoins = boundNumberOfCoins-1;
			}
		}
		return opt.getCoins();
	}

	private CoinChangeState findFeasibleSolution(int boundNumberOfCoins) {
		CoinChangeState answer = null;
		//Initial state
		int [] coins = new int [denominations.length];
		Arrays.fill(coins, 0);
		CoinChangeState state = new CoinChangeState(coins);
		//Agenda
		Queue<CoinChangeState> agenda = new LinkedList<>();
		agenda.add(state);
		while(agenda.size()>0 && answer == null) {
			//Choose next state from the agenda
			state = agenda.poll();
			if(isViable(state)) {
				if(isSolution(state,boundNumberOfCoins)) {
					answer = state;
				} else {
					//Add successors to the agenda
					List<CoinChangeState> successors = getSuccessors (state);
					agenda.addAll(successors);
				}
			}
		}
		return answer;
	}
	/**
	 * Calculates the successors of the given state. Successors are all states formed adding
	 * one coin of each denomination
	 * @param state source state to define successors
	 * @return List<CoinChangeState> successors of the given state
	 */
	private List<CoinChangeState> getSuccessors(CoinChangeState state) {
		int [] coins = Arrays.copyOf(state.getCoins(), state.getCoins().length);
		List<CoinChangeState> successors = new ArrayList<>(coins.length);
		for(int i=0;i<coins.length;i++) {
			coins[i]++;
			CoinChangeState suc = new CoinChangeState(coins);
			successors.add(suc);
			coins[i]--;
		}
		return successors;
	}
	/**
	 * Determines if the given state could lead to a solution. This function implements the branch and
	 * bound strategy within the graph exploration algorithm
	 * @param state that will be checked for viability. 
	 * @return boolean true if the total value of the given state is less or equal than the value to be completed
	 */
	private boolean isViable(CoinChangeState state) {
		return getTotalValue(state) <= totalValue;
	}

	/**
	 * Determines if the given state is a solution. Implements the satisfiability predicate of the
	 * graph exploration algorithm
	 * @param state that will be checked
	 * @param boundNumberOfCoins Maximum number of coins allowed
	 * @return boolean true if the total value of the given state is equal to the value to be completed
	 */
	private boolean isSolution(CoinChangeState state, int boundNumberOfCoins) {
		return getTotalValue(state) == totalValue && state.getTotalCoins()<=boundNumberOfCoins;
	}
	/**
	 * Calculates the total value of the given state taking into account the denominations
	 * @param state 
	 * @return
	 */
	private int getTotalValue(CoinChangeState state) {
		int [] coins = state.getCoins();
		int total = 0;
		for(int i=0;i<coins.length;i++) {
			total += coins[i]*denominations[i];
		}
		return total;
	}
	
}
/**
 * An specific state for the graph exploration defined as a number of coins for each denomination
 * @author Jorge Duitama
 */
class CoinChangeState {
	int[] coins;

	/**
	 * Creates a new state with the given configuration of coins
	 * @param coins Number of coins of each denomination.
	 * This array is copied internally to allow posterior modifications
	 */
	public CoinChangeState(int[] coins) {
		this.coins = Arrays.copyOf(coins, coins.length);
	}
	/**
	 * Calculates the total number of coins in this state
	 * @return in Sum of the coins of each denomination
	 */
	public int getTotalCoins () {
		int sum = 0;
		for(int i=0;i<coins.length;i++) {
			sum+=coins[i];
		}
		return sum;
	}
	/**
	 * Returns the number of coins of each denomination
	 * @return int [] number of coins of each denomination. The array is returned as is
	 */
	public int[] getCoins() {
		return coins;
	}
}
