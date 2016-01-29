package cn.qqtheme.AnimatorSample;

import android.graphics.Path;
import android.support.annotation.FloatRange;

import com.github.florent37.viewanimator.SvgPathParser;

import java.util.ArrayList;
import java.util.Random;

/**
 * Create {@link Path} Utility
 * <p>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/1/24 0:07
 * Builder:Android Studio
 *
 * @see InnerPoint
 * @see SvgPathParser
 */
public final class PathUtils {

    private PathUtils() {
    }

    /**
     * create bubble
     *
     * @param height the height
     * @param initX  the init x
     * @param initY  the init y
     * @return the path
     */
    public static Path createBubble(int height, int initX, int initY) {
        Random random = new Random();
        int factor = 2;
        int xRand = 10;
        int animLength = 150 + random.nextInt(height - 150);
        int animLengthRand = 100 + random.nextInt(height - 100);
        int bezierFactor = 6 + random.nextInt(15 - 6);
        int xPointFactor = 30 + random.nextInt(100 - 30);
        int x = random.nextInt(xRand);
        int x2 = random.nextInt(xRand);
        int y = initY;
        //int y = height - initY;
        int y2 = animLength * factor + random.nextInt(animLengthRand);
        factor = y2 / bezierFactor;
        x = xPointFactor + x;
        x2 = xPointFactor + x2;
        int y3 = y - y2;
        y2 = y - y2 / 2;
        Path path = new Path();
        path.moveTo(initX, y);
        path.cubicTo(initX, y - factor, x, y2 + factor, x, y2);
        path.moveTo(x, y2);
        path.cubicTo(x, y2 - factor, x2, y3 + factor, x2, y3);
        return path;
    }

    /**
     * create follower
     *
     * @param width  the width
     * @param height the height
     * @return the path
     * @see #createFollower(int, int, int, int, float) #createFollower(int, int, int, int, float)
     */
    public static Path createFollower(int width, int height) {
        return createFollower(width, height, 10, 70, 0.3f);
    }

    /**
     * create follower
     *
     * @param width     the width
     * @param height    the height
     * @param quadCount the quad count
     * @param range     the range
     * @param intensity the intensity
     * @return the path
     */
    public static Path createFollower(int width, int height, int quadCount, int range,
                                      @FloatRange(from = 0, to = 1) float intensity) {
        //生成曲线的各个坐标点
        int max = (int) (width * 3 / 4f);
        int min = (int) (width / 4f);
        Random random = new Random();
        ArrayList<InnerPoint> points = new ArrayList<InnerPoint>();
        InnerPoint startPoint = new InnerPoint(random.nextInt(max) % (max - min + 1) + min, 0);
        for (int i = 0; i < quadCount; i++) {
            if (i == 0) {
                points.add(startPoint);
            } else {
                InnerPoint otherPoint = new InnerPoint(0, 0);
                if (random.nextInt(100) % 2 == 0) {
                    otherPoint.x = startPoint.x + random.nextInt(range);
                } else {
                    otherPoint.x = startPoint.x - random.nextInt(range);
                }
                otherPoint.y = (int) (height / (float) quadCount * i);
                points.add(otherPoint);
            }
        }
        //生成贝塞尔曲线
        Path path = new Path();
        for (int i = 0; i < points.size(); i++) {
            InnerPoint point = points.get(i);
            if (i == 0) {
                InnerPoint next = points.get(i + 1);
                point.dx = ((next.x - point.x) * intensity);
                point.dy = ((next.y - point.y) * intensity);
            } else if (i == points.size() - 1) {
                InnerPoint prev = points.get(i - 1);
                point.dx = ((point.x - prev.x) * intensity);
                point.dy = ((point.y - prev.y) * intensity);
            } else {
                InnerPoint next = points.get(i + 1);
                InnerPoint prev = points.get(i - 1);
                point.dx = ((next.x - prev.x) * intensity);
                point.dy = ((next.y - prev.y) * intensity);
            }
            // create the cubic-spline path
            if (i == 0) {
                path.moveTo(point.x, point.y);
            } else {
                InnerPoint prev = points.get(i - 1);
                path.cubicTo(prev.x + prev.dx, (prev.y + prev.dy),
                        point.x - point.dx, (point.y - point.dy),
                        point.x, point.y);
            }
        }
        return path;
    }

    private static class InnerPoint {
        /**
         * The X.
         */
        public float x = 0f;
        /**
         * The Y.
         */
        public float y = 0f;
        /**
         * The Dx.
         */
        public float dx = 0f;//x-axis distance
        /**
         * The Dy.
         */
        public float dy = 0f;//y-axis distance

        /**
         * Instantiates a new Inner point.
         *
         * @param x the x
         * @param y the y
         */
        public InnerPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }

    }

}
