import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

        // It's easy to declare a new dataframe using the df class
        df df = new df();
        // You can add data from an arraylist or an array
        df.add(new String[] {"i95", "25", "11", "2007", "m", "w", "220.0", "2"});
        df.add(new String[] {"i67", "2", "6", "2012", "a", "w", "560.4", "3"});
        df.add(new String[] {"i84", "14", "4", "2022", "s", "w", "70.2", "2"});
        // You can also set column titles (optional)
        df.setColTitles(new String[] {"interstate", "day", "month", "year", "initialFirst", "initialLast", "charge", "amt"});

        // Display command similar to display() in Jupyter notebook
        df.display();
        // List-like key keeps track of data types in columns
        System.out.println(df.getKey());

        // You can also create a dataFrame from a csv file
        df df2 = new df("src/sample_data/cities.csv");
        df2.display();

        // You can use the find method to get a new data frame with certain conditions
        df df3 = df2.findGreaterThan("\"LonM\"", 55);
        // You can also sort the data frame by a the values in a column
        df3.sortBy("\"LonM\"").display();

        // Another example of sorting using a different dataset
        df df1 = new df("src/sample_data/lead_shot.csv");
        df1.sortBy("\"Ounce\"").display();
        // You can also display individual rows.
        df1.getRow(1).display();
    }
}