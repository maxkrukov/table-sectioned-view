package hudson.plugins.sectioned_view;

import java.io.File;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class CsvToTable {

    public static String escapeChars(String lineIn) {
        StringBuilder sb = new StringBuilder();
        int lineLength = lineIn.length();
        for (int i = 0; i < lineLength; i++) {
            char c = lineIn.charAt(i);
            switch (c) {
                case '"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String tableHeader(String[] columns) {
        String out = "<tr>" + System.lineSeparator();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < columns.length; i++) {
            buf.append("<th>" + columns[i] + "</th>" + System.lineSeparator());
        }
        out = out + buf.toString() + "</tr>" + System.lineSeparator();
        return out;
    }

    public static String tableRow(String[] columns) {
        String out = "<tr>" + System.lineSeparator();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < columns.length; i++) {
            buf.append("<td>" + columns[i] + "</td>");
        }
        out = out + buf.toString() + "</tr>" + System.lineSeparator();
        return out;
    }

    public static String main(String[] args)  throws InterruptedException, IOException {
        boolean withTableHeader = (args.length != 0);
        String out = "<table>" + System.lineSeparator();
        String stdinLine;
        boolean firstLine = true;
        StringBuffer buf = new StringBuffer();
        for ( String line : args ) {
            String[] columns = line.replaceAll("\\,$","\\,\\-").replace(",,",",-,").split(",");
            if (withTableHeader == true && firstLine == true) {
                out = out + tableHeader(columns);
                firstLine = false;
            } else {
                buf.append(tableRow(columns));
            }
        }
        String result = out + buf.toString() + System.lineSeparator() +"</table>";
        return result;
    }
}