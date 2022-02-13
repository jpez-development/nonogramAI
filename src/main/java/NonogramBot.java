import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.sum;
import static java.util.Collections.reverse;

public class NonogramBot {
    private List<List<Integer>> columnConstraints;
    private List<List<Integer>> rowConstraints;
    private int[][] playingField;
    private int size;
    private int minOverlap;
    private WebsiteHandler website;

    public NonogramBot(int Size, WebElement ColumnConstraints, WebElement RowConstraints, WebsiteHandler websiteHandler) {
        init(Size, ColumnConstraints, RowConstraints);
        website = websiteHandler;
    }

    private void init(int Size, WebElement ColumnConstraints, WebElement RowConstraints) {
        size = Size;
        playingField = new int[size][size];
        minOverlap = (size+1) / 2;
        columnConstraints = new ArrayList<>();
        rowConstraints = new ArrayList<>();
        populateConstraints(ColumnConstraints, columnConstraints);
        populateConstraints(RowConstraints, rowConstraints);
    }

    private void populateConstraints(WebElement constraints, List<List<Integer>> constraintsVar) {
        for(WebElement constraintList: constraints.findElements(By.className("task-group"))) {
            List<Integer> temp = new ArrayList<>();
            for (WebElement constraint: constraintList.findElements(By.className("task-cell"))) {
                if (constraint.getText().equals("")) {
                    continue;
                }
                else if (Character.isDigit(constraint.getText().charAt(0))) {
                    temp.add(Character.getNumericValue(constraint.getText().charAt(0)));
                }
            }
            constraintsVar.add(temp);
        }
    }

    public void newPuzzle(int Size, WebElement ColumnConstraints, WebElement RowConstraints) {
        init(Size, ColumnConstraints, RowConstraints);
    }

    public void solvePuzzle() { //figure out solution
        //1 = pixel
        //-1 = cross
        List<int[]> temp = setupLogic(rowConstraints);
        for (int[] row: temp) {
            playingField[temp.indexOf(row)] = row;
        }



        temp = setupLogic(columnConstraints);
        for (int[] column : temp) {
                for (int j = 0; j < size; j++) {
                    if (playingField[j][temp.indexOf(column)] != 1 && playingField[j][temp.indexOf(column)] != -1) {
                        playingField[j][temp.indexOf(column)] = column[j];
                    }
                }
        }

        for(int[] row: playingField) {
            if (Arrays.stream(row).sum() == rowConstraints.get(ArrayUtils.indexOf(playingField, row)).stream().mapToInt(Integer::intValue).sum()) {
                for (int i = 0; i < size; i++) {
                    if (playingField[ArrayUtils.indexOf(playingField, row)][i] != 1) {
                        playingField[ArrayUtils.indexOf(playingField, row)][i] = -1;
                    }
                }
            }
        }

        for(int[] row: playingField) {
            System.out.println(Arrays.toString(row));
        }
        //TODO check to see if lines are complete
    }

    private int[][] getSolution() {
        return playingField;
    }

    private List<int[]> setupLogic(List<List<Integer>> constraintVar) {
        List<int[]> tempVar = new ArrayList<>();
        for (List<Integer> constraint: constraintVar) {
            int[] temp = new int[size];
            if (constraint.size() == 1) {
                if (constraint.get(0) == 0) {
                    for (Integer x: temp) {
                        x = -1;
                    }
                }
                if (constraint.get(0) == size) {
                    for(Integer x: temp) {
                        x = 1;
                    }
                }
                else if (constraint.get(0) >= minOverlap) {
                    if (size%2 == 0) {
                        int left = (int) Math.floor(minOverlap) -1;
                        int right = (int) Math.ceil(minOverlap) -1;
                        int excess = constraint.get(0)-right;
                        for (int i = 0; i < excess; i++) {
                            temp[left-i] = 1;
                            temp[right+i] = 1;
                        }
                    }
                    else {
                        int excess = constraint.get(0)-minOverlap;
                        if (excess == 0) {
                            temp[minOverlap-1] = 1;
                        }
                        else {
                            for (int i = 0; i <= excess; i++) {
                                temp[minOverlap-i-1] = 1;
                                temp[minOverlap+i-1] = 1;
                            }
                        }
                    }
                }
            }
            else if (constraint.size() > 1) {
                int total = 0;
                for(Integer part: constraint) {
                    total += part;
                    total++;
                }
                total--;

                if (total == size) {
                    int track = 0;
                    for (int i = 0; i < constraint.size(); i++) {
                        for (int j = 0; j < constraint.get(i); j++) {
                            temp[track+j] = 1;
                        }
                        track += constraint.get(0);
                        if (i != constraint.size()-1) {
                            temp[track] = -1;
                            track++;
                        }
                    }
                }
            }
            tempVar.add(temp);
        }
        return tempVar;
    }
}
