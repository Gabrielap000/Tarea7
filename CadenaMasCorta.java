import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class CadenaMasCorta{
	
	public static void main(String[] args) 
	{
		
		
	}

	//Input data
	private int totalValue;
	private int [] denominations;
	
	public String[] calculateOptimalChange(int totalValue, int[] denominations, String[] cadenas) {
		//Input data is saved as class attributes to avoid passing the parameters through the different methods
		this.totalValue = totalValue;
		this.denominations = denominations;
		//To limit the number of solutions that will be compared in the cycle below
		//the suboptimal solution provided the greedy algorithm would be used as upper bound 
		String [] sol = solution(cadenas);
		Cadenas opt = new Cadenas(sol); 
		int maxCoins = opt.getTotalDeCadenas() -1;
		int minCoins = 0;
		while(minCoins<maxCoins) {
			int boundNumberOfCoins = (minCoins+maxCoins)/2;
			//Call of the graph exploration algorithm to find feasible solutions
			Cadenas solution = findFeasibleSolution(boundNumberOfCoins);
			if(solution==null) {
				minCoins = boundNumberOfCoins+1;
			} else {
				opt = solution;
				maxCoins = boundNumberOfCoins-1;
			}
		}
		return opt.getCadenas();
	}

	private Cadenas findFeasibleSolution(int boundNumberOfCoins) {
		Cadenas answer = null;
		//Initial state
		String [] cadenas = new String [denominations.length];
		Arrays.fill(cadenas, 0);
		Cadenas state = new Cadenas(cadenas);
		//Agenda
		Queue<Cadenas> agenda = new LinkedList<>();
		agenda.add(state);
		while(agenda.size()>0 && answer == null) {
			//Choose next state from the agenda
			state = agenda.poll();
			if(isViable(state)) {
				if(isSolution(state,boundNumberOfCoins)) {
					answer = state;
				} else {
					//Add successors to the agenda
					List<Cadenas> successors = getSuccessors (state);
					agenda.addAll(successors);
				}
			}
		}
		return answer;
	}
	
	private String solution(String[] cadenas) {
		
	String solucion = "";
	int max = Integer.MAX_VALUE;
	String temp = "";
	for (int i=0; i<cadenas.length; i++)
	{
		int cont = 0;
		if(cadenas[i].length() < max)
		{
			max = cadenas[i].length();
			temp =cadenas[i];
			for (int j=0; i<cadenas.length; i++)
			{
				if (cadenas[i].equals(temp))
				{
					
				}
				else if (cadenas[i].contains(temp))
				{
					cont ++;
				}
			}
		}
		else if(cont == cadenas.length -1)
		{
			solucion = temp;
		}
	}
	return solucion;
		
	}
	
	/**
	 * Calculates the successors of the given state. Successors are all states formed adding
	 * one coin of each denomination
	 * @param state source state to define successors
	 * @return List<CoinChangeState> successors of the given state
	 */
	private List<Cadenas> getSuccessors(Cadenas state) {
		String[] coins = Arrays.copyOf(state.getCadenas(), state.getCadenas().length);
		List<Cadenas> successors = new ArrayList<>(coins.length);
		for(int i=0;i<coins.length;i++) {
		
			Cadenas suc = new Cadenas(coins);
			successors.add(suc);
		}
		return successors;
	}
	/**
	 * Determines if the given state could lead to a solution. This function implements the branch and
	 * bound strategy within the graph exploration algorithm
	 * @param state that will be checked for viability. 
	 * @return boolean true if the total value of the given state is less or equal than the value to be completed
	 */
	private boolean isViable(Cadenas state) {
		return getTotalValue(state) <= totalValue;
	}

	/**
	 * Determines if the given state is a solution. Implements the satisfiability predicate of the
	 * graph exploration algorithm
	 * @param state that will be checked
	 * @param boundNumberOfCoins Maximum number of coins allowed
	 * @return boolean true if the total value of the given state is equal to the value to be completed
	 */
	private boolean isSolution(Cadenas state, int boundNumberOfCoins) {
		return getTotalValue(state) == totalValue && state.getTotalDeCadenas()<=boundNumberOfCoins;
	}
	/**
	 * Calculates the total value of the given state taking into account the denominations
	 * @param state 
	 * @return
	 */
	private int getTotalValue(Cadenas state) {
		String [] coins = state.getCadenas();
		int total = 0;
		for(int i=0;i<coins.length;i++) {
			total += coins[i].length()*denominations[i];
		}
		return total;
	}
	
}


class Cadenas {
	
	String[] cadenas;
	
	/**
	 * Constructor de Cadenas
	 * @param cadenas Cadenas de String.
	 */
	public Cadenas (String[]  cadenas) {
		
		this.cadenas = Arrays.copyOf(cadenas, cadenas.length);
	}
	/**
	 * Calcula el n??mero total de cadens
	 * @return cont Contador que retorna el total de cadenas
	 */
	public int getTotalDeCadenas () {
		
		int cont = 0;
		
		for(int i=0; i<cadenas.length; i++) {
			
			cont ++;
		}
		return cont;
	}
	/**
	 * Retorna todas las cadenas
	 * @return String[] retorna todas las cadenas.
	 */
	public String[] getCadenas() {
		return cadenas;
	}
}
