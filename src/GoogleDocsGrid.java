import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleDocsGrid {

    static class Point {
        int x, y;
        char ch;

        Point(int x, int y, char ch) {
            this.x = x;
            this.y = y;
            this.ch = ch;
        }
    }


    public static void main(String[] args) throws IOException {
        String url = args[0];
        List<Point> points = fetchAndParse(url);

        if (points.isEmpty()) {
            System.out.println("No data found. Check that the document is published and contains the expected table.");
            return;
        }

        // Determine grid dimensions (assuming non‑negative coordinates)
        int maxX = 0, maxY = 0;
        for (Point p : points) {
            if (p.x > maxX) maxX = p.x;
            if (p.y > maxY) maxY = p.y;
        }

        // Create grid (rows = y, columns = x) filled with spaces
        char[][] grid = new char[maxY + 1][maxX + 1];
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                grid[y][x] = ' ';
            }
        }

        // Place the actual characters at their coordinates
        for (Point p : points) {
            grid[p.y][p.x] = p.ch;   // use the character from the table
        }

        // Print the grid (y from top to bottom, x left to right)
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }

    /**
     * Fetches the HTML, locates the table with columns for x‑coordinate, Character, y‑coordinate,
     * and returns a list of Point objects.
     */
    private static List<Point> fetchAndParse(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements tables = doc.select("table");
        for (Element table : tables) {
            Elements rows = table.select("tr");
            if (rows.isEmpty()) continue;

            // Parse header row
            Element header = rows.first();
            Elements headers = header.select("th, td");
            int xCol = -1, charCol = -1, yCol = -1;
            for (int i = 0; i < headers.size(); i++) {
                String text = headers.get(i).text().trim().toLowerCase();
                if (text.contains("x-coordinate") || text.contains("x")) {
                    xCol = i;
                } else if (text.contains("character")) {
                    charCol = i;
                } else if (text.contains("y-coordinate") || text.contains("y")) {
                    yCol = i;
                }
            }

            if (xCol >= 0 && charCol >= 0 && yCol >= 0) {
                List<Point> points = new ArrayList<>();
                for (int r = 1; r < rows.size(); r++) {
                    Element row = rows.get(r);
                    Elements cols = row.select("td");
                    if (cols.size() <= Math.max(xCol, Math.max(charCol, yCol))) continue;

                    try {
                        int x = Integer.parseInt(cols.get(xCol).text().trim());
                        int y = Integer.parseInt(cols.get(yCol).text().trim());
                        String chStr = cols.get(charCol).text().trim();
                        char ch = chStr.isEmpty() ? ' ' : chStr.charAt(0);
                        points.add(new Point(x, y, ch));
                    } catch (NumberFormatException ignored) {
                        // skip rows with invalid numbers
                    }
                }
                return points;
            }
        }
        return new ArrayList<>();
    }
}
