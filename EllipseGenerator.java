/*
 * MIT License
 *
 * Copyright (c) 2024 Mojtaba Amani
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.awt.geom.Point2D;
import java.util.*;

public class EllipseGenerator {

    private static final double TWO_PI = 2.0 * Math.PI;

    /**
     * Generates points on an ellipse and moves it to fit within a bounding box.
     *
     * @param n             Number of points on the ellipse
     * @param semiMajorAxis Semi-major axis length of the ellipse
     * @param semiMinorAxis Semi-minor axis length of the ellipse
     * @param rotateEllipse Whether to randomly rotate the ellipse
     * @param xMin          Minimum x-value of the bounding box
     * @param xMax          Maximum x-value of the bounding box
     * @param yMin          Minimum y-value of the bounding box
     * @param yMax          Maximum y-value of the bounding box
     * @return List of points representing the ellipse within the bounding box
     */
    public List<Point2D.Double> generatePoints(int n, double semiMajorAxis, double semiMinorAxis, boolean rotateEllipse, double xMin, double xMax, double yMin, double yMax) {
        List<Point2D.Double> points = new ArrayList<>(n);

        // Generate points on the ellipse
        for (int i = 0; i < n; i++) {
            double theta = TWO_PI * i / n;
            double x = semiMajorAxis * Math.cos(theta);
            double y = semiMinorAxis * Math.sin(theta);
            points.add(new Point2D.Double(x, y));
        }

        // Rotate the ellipse by a random angle if needed
        if (rotateEllipse) {
            rotateEllipse(points);
        }

        // Move the ellipse to fit within the bounding box
        double xScale = (xMax - xMin) / (2 * semiMajorAxis);
        double yScale = (yMax - yMin) / (2 * semiMinorAxis);
        double minScale = Math.min(xScale, yScale);

        // Scale and translate points to fit the bounding box
        points.forEach(point -> {
            point.setLocation(point.getX() * minScale, point.getY() * minScale);
            point.setLocation(point.getX() + (xMax + xMin) / 2, point.getY() + (yMax + yMin) / 2);
        });

        return points;
    }

    /**
     * Rotates the given ellipse points by a random angle between 0 and 2*pi.
     *
     * @param points List of points representing the ellipse
     */
    private void rotateEllipse(List<Point2D.Double> points) {
        Random random = new Random();
        double randomAngle = TWO_PI * random.nextDouble();

        // Precompute cos and sin of the random angle
        double cosTheta = Math.cos(randomAngle);
        double sinTheta = Math.sin(randomAngle);

        // Rotate each point
        points.forEach(point -> {
            double x = point.getX();
            double y = point.getY();
            point.setLocation(x * cosTheta - y * sinTheta, x * sinTheta + y * cosTheta);
        });
    }

    public static void main(String[] args) {
        EllipseGenerator ellipseGenerator = new EllipseGenerator();
        Random random = new Random();

        // Define the range for the semi-major and semi-minor axes
        double minSemiMajorAxis = 10.0;
        double maxSemiMajorAxis = 20.0;
        double minSemiMinorAxis = 1.0;
        double maxSemiMinorAxis = 3.0;

        // Define the bounding box
        double xMin = 0.0;
        double xMax = 1.0;
        double yMin = 0.0;
        double yMax = 1.0;

        // Number of points to generate
        int numberOfPoints = 100;

        // Randomly select true or false for rotation
        boolean rotateEllipse = random.nextBoolean();

        // Randomly select semi-major and semi-minor axes
        double semiMajorAxis = minSemiMajorAxis + (maxSemiMajorAxis - minSemiMajorAxis) * random.nextDouble();
        double semiMinorAxis = minSemiMinorAxis + (maxSemiMinorAxis - minSemiMinorAxis) * random.nextDouble();

        // Generate points and move the ellipse into the bounding box
        List<Point2D.Double> boundedEllipsePoints = ellipseGenerator.generatePoints(numberOfPoints, semiMajorAxis, semiMinorAxis, rotateEllipse, xMin, xMax, yMin, yMax);

        boundedEllipsePoints.forEach(point -> System.out.println("(" + point.getX() + ", " + point.getY() + ")"));
    }
}