import java.io.IOException;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class df {

    private ArrayList<ArrayList<String>> df;
    private int greatestIndex = 0;
    private ArrayList<String> dTypeList = new ArrayList<String>();
    private ArrayList<String> titleList = new ArrayList<String>();

    public df() {
        df = new ArrayList<ArrayList<String>>();
    }

    public df(ArrayList<ArrayList<String>> df) {
        this.df = df;
        updateGreatestIndex();
    }

    public df(String fileName) throws IOException {
        df = new ArrayList<ArrayList<String>>();
        List<String> rows = Files.readAllLines(Paths.get(fileName));
        for (int i = 1; i < rows.size(); i++) {
            String[] rowArray = rows.get(i).split(",");
            ArrayList<String> rowList = new ArrayList<String>();
            for (String val : rowArray) {
                rowList.add(val.trim());
            }
            if (rowList.size() > 1)
                add(rowList);
        }
        String[] titleArray = rows.get(0).split(",");
        ArrayList<String> titleList = new ArrayList<String>();
        for (String val : titleArray) {
            titleList.add(val.trim());
        }
        setColTitles(titleList);
    }

    public void add(ArrayList<String> arr) {
        // Check for dtype mixing (throw an error if detected)
        if (catchTypeMixing(arr)) {
            System.out.println("Error: different data types in same column; cannot add row " + df.size());
        }

        else {
            df.add(arr);
            // Check if the row size is less than the previous rows
            if (df.size() > 1 && arr.size() < df.get(this.greatestIndex).size()) {
                System.out.println("Warning: row size is less than previous rows");
            }
            // Check if the row size is greater than the previous rows
            if (df.size() > 1 && arr.size() > df.get(this.greatestIndex).size()) {
                System.out.println("Warning: row size is greater than previous rows");
            }

            updateGreatestIndex();
        }
    }

    public void add(String[] array) { add(arrayToArrayList(array)); }

    public void addcol(ArrayList<String> arr) {
        if (arr.size() != df.size()) {
            System.out.println("Error: column size must match row number");
        }
        else {
            for (int i = 0; i < arr.size(); i++) {
                df.get(i).add(arr.get(i));
            }
            updateGreatestIndex();
        }
    }

    public void addcol(String[] array) { addcol(arrayToArrayList(array)); }

    public void set(int i, ArrayList<String> arr) {
        // Check for dtype mixing (throw an error if detected)
        if (catchTypeMixing(arr)) {
            System.out.println("Error: different data types in same column; cannot set row " + df.size());
            remove(i);
        }
        else {

            df.set(i, arr);

            // Check if the row size is less than the previous rows
            if (!df.isEmpty() && arr.size() < df.get(this.greatestIndex).size()) {
                System.out.println("Warning: row size is less than previous rows");
            }
            // Check if the row size is greater than the previous rows
            if (!df.isEmpty() && arr.size() > df.get(this.greatestIndex).size()) {
                System.out.println("Warning: row size is greater than previous rows");
            }

            updateGreatestIndex();
        }
    }

    public void set(int i, String[] array) { set(i, arrayToArrayList(array)); }

    private boolean catchTypeMixing(ArrayList<String> arr) {

        if (df.isEmpty())
            return false;

        for (int i = 0; i < arr.size(); i++) {
            String cVal = arr.get(i);

            if (isNum(cVal.trim())) {

                if (cVal.contains(".") && !Objects.equals(dTypeList.get(i), "Double"))
                    return true;
                else if (!cVal.contains(".") && !Objects.equals(dTypeList.get(i), "Integer"))
                    arr.set(i, arr.get(i) + ".0");
            }

            else if (!Objects.equals(dTypeList.get(i), "String"))
                return true;
        }
        return false;
    }

    public void setColTitles(String[] array) {
        ArrayList<String> titleList = arrayToArrayList(array);
        if (df.get(this.greatestIndex).size() > titleList.size()) {
            System.out.println("Warning: title size is less than row size; not all columns will be named");
            this.titleList = titleList;
        }
        else if (df.get(this.greatestIndex).size() < titleList.size()) {
            System.out.println("Error: title size must be less than or equal to row size");
        }
        else {
            this.titleList = titleList;
        }
    }

    public void setColTitles(ArrayList<String> array) { setColTitles(arrayListToArray(array)); }

    public void remove(int i) {
        df.remove(i);
        updateGreatestIndex();
    }

    public void remove(int i, int j) {
        df.get(i).remove(j);
        updateGreatestIndex();
    }

    private void updateGreatestIndex() {
        int greatestIndex = this.greatestIndex;
        for (int i = 1; i < df.size(); i++) {
            if (df.get(i).size() > df.get(greatestIndex).size()) {
                greatestIndex = i;
            }
        }

        if (this.greatestIndex != greatestIndex || df.size() == 1)
            updateKey(greatestIndex);

        this.greatestIndex = greatestIndex;
    }

    private void updateKey(int greatestIndex) {
        if (df.size() > 1) {
            for (int i = df.get(this.greatestIndex).size(); i < df.get(greatestIndex).size(); i++) {
                String cVal = df.get(greatestIndex).get(i);

                if (isNum(cVal)) {

                    if (cVal.contains("."))
                        dTypeList.add(i, "Double");
                    else
                        dTypeList.add(i, "Integer");
                }
                else
                    dTypeList.add(i, "String");
            }
        }
        else {
            for (int i = 0; i < df.get(0).size(); i++) {
                String cVal = df.get(0).get(i);
                if (isNum(cVal.trim())) {
                    if (cVal.contains("."))
                        dTypeList.add(i, "Double");
                    else
                        dTypeList.add(i, "Integer");
                }
                else
                    dTypeList.add(i, "String");
            }
        }
    }

    public boolean isNum(String val) {
        for (int i = 0; i < val.length(); i++) {
            String cVal = val.substring(i, i + 1);
            if (!(cVal.contains("0") || cVal.contains("1") || cVal.contains("2") ||
                    cVal.contains("3") || cVal.contains("4") || cVal.contains("5") ||
                    cVal.contains("6") || cVal.contains("7") || cVal.contains("8") ||
                    cVal.contains("9") || cVal.contains(".")))
                return false;
        }
        if (!val.isEmpty())
            return true;
        else
            return false;
    }

    /** Manually set the data type of each column
    (this will definitely cause errors if used wrong)
    int = "Integer", String = "String", double = "Double" **/
    public void setKey(ArrayList<String> arr) {
        dTypeList = arr;
    }

    // For debugging
    public ArrayList<String> getKey() {
        return dTypeList;
    }

    public df getCol(int col) {
        df result = new df();
        for (int i = 0; i < df.size(); i++) {
            result.add(new String[] {df.get(i).get(col)});
        }
        return result;
    }

    public df getCol(String col) { return getCol(arraySearch(titleList, col).get(0)); }

    public df getRow(int row) {
        df result = new df();
        result.add(df.get(row));
        result.setColTitles(this.titleList);
        return result;
    }

    public void print() {
        System.out.println(df);
    }

    public void display() {
        if (df.isEmpty()) {
            System.out.println("Warning: empty dataFrame");
            return;
        }
        ArrayList<Integer> longestString = longestString();

        for (int i = 0; i < Integer.toString(df.size()).length() + 4; i++)
            System.out.print(" ");

        for (int i = 0; i < df.get(this.greatestIndex).size(); i++) {
            if (i < titleList.size())
                System.out.print(titleList.get(i).trim());
            else
                System.out.print(i);
            for (int k = 0; k < longestString.get(i); k++) {
                System.out.print(" ");
            }
        }
        System.out.println();
        for (int i = 0; i < df.size(); i++) {
            System.out.print(i);
            for (int k = 0; k < 4 + Integer.toString(df.size()).length()
                    - Integer.toString(i).length(); k++)
                System.out.print(" ");
            for (int j = 0; j < df.get(i).size(); j++) {
                System.out.print(df.get(i).get(j).trim());
                if (j < titleList.size()) {
                    for (int k = 0; k < longestString.get(j) - df.get(i).get(j).trim().length()
                            + titleList.get(j).trim().length(); k++) {
                        System.out.print(" ");
                    }

                }
                else {
                    for (int k = 0; k < longestString.get(j) - df.get(i).get(j).trim().length()
                            + Integer.toString(j).trim().length(); k++) {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }

    // Finds the longest string in each column and returns an arraylist of the lengths
    // For use in the display method
    private ArrayList<Integer> longestString() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < df.get(this.greatestIndex).size(); i++) {
            int longest = 0;
            for (ArrayList<String> strings : df) {
                if (strings.get(i).trim().length() > longest)
                    longest = strings.get(i).trim().length();
            }
            result.add(i, longest);
        }
        return result;
    }

    public static ArrayList<String> range(int n) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            result.add(i, i);
        }
        return intToString(result);
    }

    public static ArrayList<String> arrayToArrayList(String[] arr) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++) {
            result.add(i, arr[i]);
        }
        return result;
    }

    public static String[] arrayListToArray(ArrayList<String> arr) {
        String[] result = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            result[i] = arr.get(i);
        }
        return result;
    }

    public static ArrayList<String> range(int s, int n) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = s; i - s < n; i++) {
            result.add(i - s, i);
        }
        return intToString(result);
    }

    public static ArrayList<String> intToString(ArrayList<Integer> arr) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < arr.size(); i++) {
            result.add(i, Integer.toString(arr.get(i)));
        }
        return result;
    }

    public static ArrayList<Integer> arraySearch(ArrayList<String> array, String input) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < array.size(); i++) {
            if (Objects.equals(array.get(i), input))
                result.add(i);
        }
        return result;
    }

    // Returns a df where the values in the column are equal to the input
    public df find(int col, String input) {
        df result = new df();
        for (int i = 0; i < df.size(); i++) {
            if (Objects.equals(df.get(i).get(col), input))
                result.add(df.get(i));
        }
        if (result.df.isEmpty()) {
            return new df();
        }
        else {
            result.setColTitles(titleList);
            return result;
        }
    }

    public df find(String col, String input) { return find(arraySearch(titleList, col).get(0), input); }

    public df find(String col, int input) { return find(arraySearch(titleList, col).get(0), Integer.toString(input)); }

    public df findLessThan(int col, int input) {
        df result = new df();
        for (int i = 0; i < df.size(); i++) {
            if (Integer.parseInt(df.get(i).get(col)) < input)
                result.add(df.get(i));
        }
        if (result.df.isEmpty()) {
            return new df();
        }
        else {
            result.setColTitles(titleList);
            return result;
        }
    }

    public df findLessThan(String col, int input) { return findLessThan(arraySearch(titleList, col).get(0), input); }

    public df findGreaterThan(int col, int input) {
        df result = new df();
        for (int i = 0; i < df.size(); i++) {
            if (Integer.parseInt(df.get(i).get(col)) > input)
                result.add(df.get(i));
        }
        if (result.df.isEmpty()) {
            return new df();
        }
        else {
            result.setColTitles(titleList);
            return result;
        }
    }

    public df findGreaterThan(String col, int input) { return findGreaterThan(arraySearch(titleList, col).get(0), input); }

    // Sorts a column in ascending order. Must be an Integer data type column
    public df sortBy(int col) {
        df result = new df(this.df);
        result.setColTitles(this.titleList);
        if (result.df.isEmpty()) {
            System.out.println("Error: empty dataFrame");
            return result;
        }
        else if (col >= result.df.get(0).size()) {
            System.out.println("Error: column index out of bounds");
            return result;
        }
        else
        if (!this.dTypeList.get(col).equals("Integer") && !this.dTypeList.get(col).equals("Double")) {
            System.out.println("Error: column must be of Integer or Double data type");
            return result;
        }

        df = result.df;
        for (int i = 0; i < result.df.size(); i++) {
            int minIndex = i;
            for (int j = minIndex + 1; j < df.size(); j++) {
                if (this.dTypeList.get(col).equals("Double")) {
                    if (Double.parseDouble(df.get(j).get(col)) < Double.parseDouble(df.get(minIndex).get(col)))
                        minIndex = j;
                }
                else {
                    if (Integer.parseInt(df.get(j).get(col)) < Integer.parseInt(df.get(minIndex).get(col)))
                        minIndex = j;
                }
            }
            ArrayList<String> temp = df.get(i);
            df.set(i, df.get(minIndex));
            df.set(minIndex, temp);
        }
        return result;
    }

    public df sortBy(String col) { return sortBy(arraySearch(titleList, col).get(0)); }

}
