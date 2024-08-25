package seam_carvre;

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private int[][] colors;

    private double[][] energies;

    private static final double BORDER_ENERGY = 1000.0;

    // public SeamCarver(double[][] energ) {
    //
    //     energies = energ; // initial array is for finding vertical seam
    //     colors = new int[energ.length][energ[0].length];
    //
    // }

    //
    // public SeamCarver(Color[][] arrColor) {
    //     colors = new int[arrColor.length][arrColor[0].length];
    //     for (int i = 0; i < arrColor.length; i++) {
    //         for (int j = 0; j < arrColor[0].length; j++) {
    //             colors[i][j] = arrColor[i][j].getRGB();
    //         }
    //     }
    //
    //     energies = new double[arrColor.length][arrColor[0].length]; // initial array is for finding vertical seam
    //
    //     // count pixels' energies
    //     for (int y = 0; y < energies.length; y++) {
    //         for (int x = 0; x < energies[0].length; x++) {
    //             energies[y][x] = countEnergy(x, y);
    //         }
    //     }
    // }

    // create a seam carver object based on the given picture
    public SeamCarver(Picture pic) {
        if (pic == null) {
            throw new IllegalArgumentException();
        }

        colors = new int[pic.height()][pic.width()];
        for (int i = 0; i < pic.height(); i++) {
            for (int j = 0; j < pic.width(); j++) {
                colors[i][j] = pic.getRGB(j, i);
            }
        }

        energies = new double[pic.height()][pic.width()]; // initial array is for finding vertical seam

        // count pixels' energies
        for (int y = 0; y < energies.length; y++) {
            for (int x = 0; x < energies[0].length; x++) {
                energies[y][x] = countEnergy(x, y);
            }
        }

    }

    private boolean isPixelOnEdge(int x, int y) {
        return x == 0 || x == width() - 1 || y == 0 || y == height() - 1;
    }

    private double countEnergy(int x, int y) {
        if (isPixelOnEdge(x, y)) {
            return BORDER_ENERGY;
        }


        return Math.sqrt(diff(x - 1, y, x + 1, y) +
                diff(x, y - 1, x, y + 1));
    }

    private double diff(int x1, int y1, int x2, int y2) {
        Color color1 = new Color(colors[y1][x1]);
        Color color2 = new Color(colors[y2][x2]);
        return Math.pow(color1.getRed() - color2.getRed(), 2) +
                Math.pow(color1.getGreen() - color2.getGreen(), 2) +
                Math.pow(color1.getBlue() - color2.getBlue(), 2);
    }

    private int arrayIndex(int x, int y) {
        return width() * y + x;
    }

    private int getX(int index) {
        return index % width();
    }

    private int getY(int index) {
        return index / width();
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(width(), height());
        for (int i = 0; i < pic.height(); i++) {
            for (int j = 0; j < pic.width(); j++) {
                pic.set(j, i, new Color(colors[i][j]));
            }
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return colors[0].length;
    }

    // height of current picture
    public int height() {
        return colors.length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) { // coordinates is outside the prescribed range
            throw new IllegalArgumentException();
        }

        return energies[y][x];
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int vertMin = -1; // vertex from the bottom which is end of the shortest path
        double distMin = Double.POSITIVE_INFINITY;

        int[][] edgeTo = new int[height()][width()]; // i - vertex to, arr[i] - vertex from
        double[][] distTo = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int s = 0; s < height(); s++) {
            distTo[s][0] = BORDER_ENERGY;

            // relax vertices in order of distance from s
            topologicalRelaxHor(s, distTo, edgeTo);

            // find min path and end of min path in the current step
            int currVertMin = 0;
            double currDistMin = Double.POSITIVE_INFINITY;
            for (int i = 0; i < height(); i++) {
                if (distTo[i][width() - 1] != Double.POSITIVE_INFINITY && distTo[i][width() - 1] < currDistMin) { // ch
                    currVertMin = i;
                    currDistMin = distTo[i][width() - 1];
                }
            }

            // save edgeTo for min path if found a new shorter path
            if (currDistMin < distMin) {
                vertMin = currVertMin;
                distMin = currDistMin;
            }
        }

        // traverse the shortest path
        int currVert = vertMin;
        int[] shortestPath = new int[width()];
        int x;
        for (x = width() - 1; x >= 0; x--) {
            shortestPath[x] = currVert;
            currVert = getY(edgeTo[currVert][x]);
        }

        return shortestPath;
    }

    private void topologicalRelaxHor(int stY, double[][] distTo, int[][] edgeTo) {
        int st = stY;
        int end = stY;
        for (int x = 1; x < width(); x++) {
            for (int y = st; y <= end; y++) {
                if (y - 1 >= 0) relax(x - 1, y, x, y - 1, distTo, edgeTo);
                relax(x - 1, y, x, y, distTo, edgeTo);
                if (y + 1 < height()) relax(x - 1, y, x, y + 1, distTo, edgeTo);
            }

            if (st > 0) {
                st--;
            }
            if (end < height() - 1) {
                end++;
            }
        }

    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int vertMin = -1; // vertex from the bottom which is end of the shortest path
        double distMin = Double.POSITIVE_INFINITY;
        double[][] distTo = new double[height()][width()];
        int[][] edgeTo = new int[height()][width()]; // i - vertex to, arr[i] - vertex from

        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int s = 0; s < width(); s++) {
            distTo[0][s] = BORDER_ENERGY;

            // relax vertices in order of distance from s
            topologicalRelaxVert(s, distTo, edgeTo);

            // find min path and end of min path in the current step
            for (int i = 0; i < width(); i++) {
                if (distTo[height() - 1][i] != Double.POSITIVE_INFINITY && distTo[height() - 1][i] < distMin) {
                    vertMin = i;
                    distMin = distTo[height() - 1][i];
                }
            }
        }

        // traverse the shortest path
        int currVert = vertMin;
        int[] shortestPath = new int[height()];
        int y;
        for (y = height() - 1; y >= 0; y--) {
            shortestPath[y] = currVert;
            currVert = getX(edgeTo[y][currVert]);
        }

        return shortestPath;
    }

    // relax edges in topological order
    private void topologicalRelaxVert(int stX, double[][] distTo, int[][] edgeTo) {
        int st = stX;
        int end = stX;
        for (int y = 1; y < height(); y++) {
            for (int x = st; x <= end; x++) {
                if (x - 1 > 0) relax(x, y - 1, x - 1, y, distTo, edgeTo);
                relax(x, y - 1, x, y, distTo, edgeTo);
                if (x + 1 < width()) relax(x, y - 1, x + 1, y, distTo, edgeTo);
            }

            if (st > 0) {
                st--;
            }
            if (end < width() - 1) {
                end++;
            }
        }

    }

    private void relax(int sourceX, int sourceY, int adjX, int adjY, double[][] distTo, int[][] edgeTo) {
        if (distTo[adjY][adjX] > distTo[sourceY][sourceX] + energies[adjY][adjX]) {
            distTo[adjY][adjX] = distTo[sourceY][sourceX] + energies[adjY][adjX];
            edgeTo[adjY][adjX] = arrayIndex(sourceX, sourceY);
        }

    }

    private boolean isValidSeam(int[] seam) {
        int restr = seam.length == width() ? height() : width();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= restr) {
                return false;
            }
            if (i - 1 > 0 && Math.abs(seam[i - 1] - seam[i]) > 1) {
                return false;
            }
            if (i + 1 < seam.length && Math.abs(seam[i + 1] - seam[i]) > 1) {
                return false;
            }
        }

        return true;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (height() <= 1 || seam == null || seam.length != width() || !isValidSeam(seam))
            throw new IllegalArgumentException();


        int[][] col = new int[colors.length - 1][colors[0].length];
        double[][] energ = new double[energies.length - 1][energies[0].length];

        int y, add;
        for (int i = 0; i < width(); i++) {
            y = seam[i];
            add = 0;
            for (int j = 0; j < height() - 1; j++) {
                if (j == y) {
                    add = 1;
                }

                col[j][i] = colors[j + add][i];
                energ[j][i] = energies[j + add][i];
            }
        }

        colors = col;

        for (int x = 0; x < seam.length; x++) {
            y = seam[x];
            if (y - 1 >= 0) {
                energ[y - 1][x] = countEnergy(x, y - 1);
            }
            if (y < col.length) {
                energ[y][x] = countEnergy(x, y);
                if (x + 1 < col[0].length) {
                    energ[y][x + 1] = countEnergy(x + 1, y);
                }
            }

        }

        energies = energ;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width() <= 1 || seam == null || seam.length != height() || !isValidSeam(seam))
            throw new IllegalArgumentException();

        int[][] col = new int[colors.length][colors[0].length - 1];
        double[][] energ = new double[energies.length][energies[0].length - 1];

        int x;
        for (int i = 0; i < height(); i++) {
            x = seam[i];
            System.arraycopy(colors[i], 0, col[i], 0, x);
            System.arraycopy(colors[i], x + 1, col[i], x, col[i].length - x);
            System.arraycopy(energies[i], 0, energ[i], 0, x);
            System.arraycopy(energies[i], x + 1, energ[i], x, col[i].length - x);

        }

        colors = col;

        for (int y = 0; y < seam.length; y++) {
            x = seam[y];
            if (x - 1 >= 0) {
                energ[y][x - 1] = countEnergy(x - 1, y);
            }
            if (x < col[0].length) {
                energ[y][x] = countEnergy(x, y);
                if (y - 1 >= 0) {
                    energ[y - 1][x] = countEnergy(x, y - 1);
                }
            }

        }

        energies = energ;

    }

    //  unit testing (optional)
    public static void main(String[] args) {
        double[][] energies0 = {{1000, 1000, 1000, 1000, 1000, 1000},
                {1000, 237.35, 151.02, 234.09, 107.89, 1000},
                {1000, 138.69, 228.10, 133.07, 211.51, 1000},
                {1000, 153.88, 174.01, 284.01, 194.05, 1000},
                {1000, 1000, 1000, 1000, 1000, 1000}};

        double[][] energies1 = {{1000.00, 1000.00, 1000.00, 1000.00, 1000.00},
                {1000.00, 300.07, 265.33, 289.67, 1000.00},
                {1000.00, 311.94, 94.36, 309.61, 1000.00},
                {1000.00, 295.49, 312.36, 193.36, 1000.00},
                {1000.00, 264.36, 216.49, 299.43, 1000.00},
                {1000.00, 1000.00, 1000.00, 1000.00, 1000.00}};

        double[][] energies2 = {{1000.00, 1000.00, 1000.00, 1000.00, 1000.00, 1000.0},
                {1000.00, 300.07, 265.33, 289.67, 289.67, 1000.00},
                {1000.00, 311.94, 94.36, 309.61, 289.67,1000.00},
                {1000.00, 311.94, 94.36, 309.61, 289.67,1000.00},
                {1000.00, 311.94, 94.36, 309.61, 289.67,1000.00},
                {1000.00, 1000.00, 1000.00, 1000.00, 1000.00, 1000.0}};


        Color[][] arrColor = {
                {new Color(255, 101, 51), new Color(255, 101, 153), new Color(255, 101, 255)},
                {new Color(255, 153, 51), new Color(255, 153, 153), new Color(255, 153, 255)},
                {new Color(255, 203, 51), new Color(255, 204, 153), new Color(255, 205, 255)},
                {new Color(255, 255, 51), new Color(255, 255, 153), new Color(255, 255, 255)},
        };

        // SeamCarver seamCarver = new SeamCarver(new Picture("space.jpg"));


        // SeamCarver seamCarver = new SeamCarver(arrColor);
        // System.out.println(Arrays.toString(seamCarver.findHorizontalSeam()));
        // System.out.println(Arrays.toString(seamCarver.findVerticalSeam()));
        // seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        // seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());

        // SeamCarver seamCarver = new SeamCarver(energies2);
        // int[] test =  { 5, 4, 4, 3 };
        // seamCarver.removeVerticalSeam(test);
        // seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        // seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        //
        // seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
        // seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
        // seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());


    }
}