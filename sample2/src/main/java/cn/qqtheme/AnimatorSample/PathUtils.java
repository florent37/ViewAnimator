package cn.qqtheme.AnimatorSample;

import android.graphics.Path;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

/**
 * Create {@link Path} Utility
 * <p/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/1/24 0:07
 * Builder:Android Studio
 *
 * @see BezierPoint
 * @see SvgPathParser
 */
public final class PathUtils {

    private PathUtils() {
    }

    /**
     * Create bubble path.
     *
     * @param width  the width
     * @param height the height
     * @return the path
     */
    public static Path createBubble(int width, int height) {
        return createBubble(width, height, 10, 25, 0.3f);
    }

    /**
     * create bubble, Just the opposite with follower
     *
     * @param width       the width
     * @param height      the height
     * @param quadCount   the quad count
     * @param wobbleRange the wobble range
     * @param intensity   the intensity
     * @return the path
     */
    public static Path createBubble(int width, int height, int quadCount, int wobbleRange,
                                    @FloatRange(from = 0, to = 1) float intensity) {
        int max = (int) (width * 3 / 4f);
        int min = (int) (width / 4f);
        Random random = new Random();
        ArrayList<BezierPoint> points = new ArrayList<BezierPoint>();
        BezierPoint startPoint = new BezierPoint(random.nextInt(max) % (max - min + 1) + min, height);
        for (int i = quadCount; i > 0; i--) {
            if (i == quadCount) {
                points.add(startPoint);
            } else {
                BezierPoint otherPoint = new BezierPoint(0, 0);
                if (random.nextInt(100) % 2 == 0) {
                    otherPoint.x = startPoint.x + random.nextInt(wobbleRange);
                } else {
                    otherPoint.x = startPoint.x - random.nextInt(wobbleRange);
                }
                otherPoint.y = (int) (height / (float) quadCount * i);
                points.add(otherPoint);
            }
        }
        return createCubicSpline(points, intensity);
    }

    /**
     * create follower
     *
     * @param width  the width
     * @param height the height
     * @return the path
     */
    public static Path createFollower(int width, int height) {
        return createFollower(width, height, 10, 70, 0.3f);
    }

    /**
     * create follower
     *
     * @param width       the width
     * @param height      the height
     * @param quadCount   the quad count
     * @param wobbleRange the wobble range
     * @param intensity   the intensity
     * @return the path
     */
    public static Path createFollower(int width, int height, int quadCount, int wobbleRange,
                                      @FloatRange(from = 0, to = 1) float intensity) {
        int max = (int) (width * 3 / 4f);
        int min = (int) (width / 4f);
        Random random = new Random();
        ArrayList<BezierPoint> points = new ArrayList<BezierPoint>();
        BezierPoint startPoint = new BezierPoint(random.nextInt(max) % (max - min + 1) + min, 0);
        for (int i = 0; i < quadCount; i++) {
            if (i == 0) {
                points.add(startPoint);
            } else {
                BezierPoint otherPoint = new BezierPoint(0, 0);
                if (random.nextInt(100) % 2 == 0) {
                    otherPoint.x = startPoint.x + random.nextInt(wobbleRange);
                } else {
                    otherPoint.x = startPoint.x - random.nextInt(wobbleRange);
                }
                otherPoint.y = (int) (height / (float) quadCount * i);
                points.add(otherPoint);
            }
        }
        return createCubicSpline(points, intensity);
    }

    /**
     * Create cubic spline path.
     *
     * @param points    the points
     * @param intensity the intensity
     * @return the path
     */
    @NonNull
    public static Path createCubicSpline(ArrayList<BezierPoint> points, @FloatRange(from = 0, to = 1) float intensity) {
        Path path = new Path();
        for (int i = 0; i < points.size(); i++) {
            BezierPoint point = points.get(i);
            if (i == 0) {
                BezierPoint next = points.get(i + 1);
                point.dx = ((next.x - point.x) * intensity);
                point.dy = ((next.y - point.y) * intensity);
            } else if (i == points.size() - 1) {
                BezierPoint prev = points.get(i - 1);
                point.dx = ((point.x - prev.x) * intensity);
                point.dy = ((point.y - prev.y) * intensity);
            } else {
                BezierPoint next = points.get(i + 1);
                BezierPoint prev = points.get(i - 1);
                point.dx = ((next.x - prev.x) * intensity);
                point.dy = ((next.y - prev.y) * intensity);
            }
            // create the cubic-spline path
            if (i == 0) {
                path.moveTo(point.x, point.y);
            } else {
                BezierPoint prev = points.get(i - 1);
                path.cubicTo(prev.x + prev.dx, (prev.y + prev.dy),
                        point.x - point.dx, (point.y - point.dy),
                        point.x, point.y);
            }
        }
        return path;
    }

    /**
     * The type Bezier point.
     */
    public static class BezierPoint {
        /**
         * The X.
         */
        public float x = 0f;
        /**
         * The Y.
         */
        public float y = 0f;
        /**
         * The x-axis distance
         */
        public float dx = 0f;
        /**
         * The y-axis distance
         */
        public float dy = 0f;

        /**
         * Instantiates a new bezier point.
         *
         * @param x the x
         * @param y the y
         */
        public BezierPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }

    }

}
