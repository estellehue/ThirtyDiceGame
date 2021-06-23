package java.se.umu.mobileapp.eshu0006.thirtydicegame.model;

import java.util.ArrayList;

/**
 * Class calculating the possible score options of the game.
 * Static methods as the code in the methods are not dependent on
 * instance creation and are not using any instance variable.
 */
public class Score {

    /**
     * Method returning the dice values used for the final score of the round.
     * The resulting dice (red) are the maximum score for the specified requiredSum.
     *
     * @param diceValuesSorted The sorted list of the six dice values
     * @param requiredSum The required sum for the combinations to have
     * @return The dice values used for the score
     */
    public static ArrayList<Integer> getDiceUsedForScore (ArrayList diceValuesSorted, int requiredSum) {
        ArrayList<ArrayList<Integer>> allCombinations = getAllCombinations(diceValuesSorted, requiredSum);
        ArrayList<ArrayList<Integer>> bestCombination = getBestCombination(diceValuesSorted, allCombinations);
        ArrayList<Integer> finalScoreCombination = getFinalCombination(bestCombination);
        return finalScoreCombination;
    }

    /**
     * Method returning the sum of the score based on the number of
     * combinations with the requiredSum added together.
     * Hence, the final combination's number of sets multiplied by the
     * requiredSum, resulting the in the total score for the round with the
     * requiredSum.
     *
     * @param diceValuesSorted The sorted list of the six dice values
     * @param requiredSum The required sum for the combinations to have
     * @return The sum of the score for the round
     */
    public static Integer getSumOfScore (ArrayList diceValuesSorted, int requiredSum)  {
        ArrayList<ArrayList<Integer>> allCombinations = getAllCombinations(diceValuesSorted, requiredSum);
        ArrayList<ArrayList<Integer>> bestCombination = getBestCombination(diceValuesSorted, allCombinations);
        Integer disjointSets = numberOfDisjointSets(diceValuesSorted, bestCombination);
        Integer finalRoundScore = disjointSets * requiredSum;
        return finalRoundScore;
    }

    /**
     * Method returning the sum of the score for the required sum of 3, being low.
     * For low score, all the dice of value 3 and lower are counted.
     *
     * @param diceValuesSorted The sorted list of the six dice values
     * @return The sum of the low-score for the round
     */
    public static Integer getSumOfScoreForLow (ArrayList diceValuesSorted) {
        Integer finalRoundScoreLow = 0;
        for (int i = 0; i < diceValuesSorted.size(); i++) {
            int value = (int) diceValuesSorted.get(i);
            int maxLimitForLowScore = 4;
            if (value < maxLimitForLowScore)
                finalRoundScoreLow += value;
        }
        return finalRoundScoreLow;
    }

    /**
     * Method extracting the values of the integers from the array of arrays,
     * placing all values in one single array list.
     * ex. [[1, 2], [3, 4], [5, 6]] -> [1, 2, 3, 4, 5, 6]
     *
     * @param finalCombinationSet The final set of combinations
     * @return The values in the final set of combinations in an array list
     */
    private static ArrayList<Integer> getFinalCombination(ArrayList<ArrayList<Integer>> finalCombinationSet) {
        ArrayList<Integer> result = new ArrayList();
        for (ArrayList<Integer> set : finalCombinationSet)
            for (Integer i : set)
                result.add(i);
        return result;
    }

    /**
     * Method returning the permutations of the diceSet whose total sum
     * matches the requiredSum.
     * ex. getAllCombinations([1, 2, 3, 4, 5, 6], 7) -> [[1, 2, 4], [3, 4], [2, 5], [1, 6]]
     * ex. getAllCombinations([1, 2, 3, 4, 5, 6], 9) -> [[1, 2, 6], [1, 3, 5], [2, 3, 4], [3, 6], [4, 5]]
     *
     * @param diceSet The set in which to identify the combinations
     * @param requiredSum The required sum for the combinations to have
     * @return The array list of possible combinations of array lists
     */
    private static ArrayList<ArrayList<Integer>> getAllCombinations (ArrayList<Integer> diceSet, Integer requiredSum) {
        ArrayList<Integer> currentComboOptions = new ArrayList();
        ArrayList<ArrayList<Integer>> finalResult = new ArrayList();

        for (int i = 0; i < (1 << diceSet.size()); i++) {    // Loops through all the bits in diceSet
            for (int j = 0; j < diceSet.size(); j++) {       // Loops through the number of elements in the diceSet
                if ((i & (1 << j)) > 0)                      // Generates all possible permutations (of different lengths) of the elements in the diceSet
                    currentComboOptions.add(diceSet.get(j)); // Adds all permutations to an array list
            }
            if (getTotalDiceSetSum(currentComboOptions).equals(requiredSum)) { // If the sum of all elements in each array is the same as the wanted sum, save it. This will filter out all unwanted permutations
                ArrayList<Integer> temporary = new ArrayList(currentComboOptions);
                finalResult.add(temporary);                                    // Adds the possible combination set to an array list of combinations-array-lists
            }
            currentComboOptions.clear();
        }
        return finalResult;
    }

    /**
     * Method calculating the total sum of the six dice face values.
     * @param set The set of six dice
     * @return sum The sum of the values of the dice in the set
     */
    private static Integer getTotalDiceSetSum(ArrayList<Integer> set) {
        Integer sum = 0;
        for (int value : set)
            sum += value;
        return sum;
    }

    /**
     * Method returning the permutations of the diceSet whose inner set
     * combinations for the same value have the most disjoint sets.
     * The combination creating sets, all of a specified sum, with no
     * number recurring in any set, disjoint.
     * ex. (sum = 7) getBestCombinations([1, 2, 3, 4, 5, 6], [[1, 2, 4], [3, 4], [2, 5], [1, 6]]) -> [3, 4], [2, 5], [1, 6]
     * ex. (sum = 9) getBestCombinations([1, 2, 3, 4, 5, 6], [[1, 2, 6], [1, 3, 5], [2, 3, 4], [3, 6], [4, 5]]) -> [3, 6], [4, 5]
     *
     * @param diceSet The set in which to identify the combinations
     * @param subSets The set of permutations of the diceSet
     * @return The array list of possible combinations of array lists
     */
    private static ArrayList<ArrayList<Integer>> getBestCombination (ArrayList<Integer> diceSet, ArrayList<ArrayList<Integer>> subSets) {
        ArrayList currentComboOptions = new ArrayList();
        ArrayList<ArrayList<Integer>> finalResult = new ArrayList();

        for (int i = 0; i < (1 << subSets.size()); i++) {    // Loops through all the bits in subSets
            for (int j = 0; j < subSets.size(); j++) {       // Loops through the number of elements in subSets
                if ((i & (1 << j)) > 0) {                    // Generates all possible permutations (of different lengths) of the elements in subSets
                    currentComboOptions.add(subSets.get(j)); // Adds all permutations to an array list
                }
            }
            int disjointSets = numberOfDisjointSets(diceSet, currentComboOptions); // Find the number of non-overlapping sets
            if (disjointSets > 0)                                                  // If there are any disjoint sets, save to return
                finalResult = new ArrayList<ArrayList<Integer>>(currentComboOptions);

            currentComboOptions.clear();
        }
        return finalResult;
    }

    /**
     * Method counting the number of disjoint sets in the diceSetPermutations.
     * The number of sets that do not share the same number, no number in common.
     * ex. numberOfDisjointSets([[1, 3, 5], [3, 4]) -> 0
     * ex. numberOfDisjointSets([[1, 3], [2, 4]) -> 2
     * ex. numberOfDisjointSets([[3]]) -> 1
     *
     * @param diceSet The set in which to identify the combinations
     * @param diceSetPermutations The set of permutations of the diceSet
     * @return The number of disjoint sets
     */
    private static Integer numberOfDisjointSets (ArrayList<Integer> diceSet, ArrayList<ArrayList<Integer>> diceSetPermutations) {
        ArrayList<Integer> diceSetCopy = new ArrayList(diceSet);
        boolean arrayCounted = true;
        Integer count = 0;

        for (ArrayList<Integer> arrayList : diceSetPermutations) {
            for (Integer arrayListInteger : arrayList) {
                if (diceSetCopy.contains(arrayListInteger))
                    diceSetCopy.remove(arrayListInteger);
                else
                    arrayCounted = false;
            }
            count++;
        }
        if (arrayCounted)
            return count;
        else
            return 0;
    }

}