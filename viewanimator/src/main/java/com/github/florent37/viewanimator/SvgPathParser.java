package com.github.florent37.viewanimator;

import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

/**
 * SVG Path Parser
 * &lt;path d="M250 150 L150 350 L350 350 Z" /&gt;
 * <p/>
 * Author:李玉江[QQ:1032694760]
 * Email:liyujiang_tk@yeah.net
 * DateTime:2016/1/24 0:07
 * Builder:Android Studio
 * Link:https://github.com/pents90/svg-android
 */
public class SvgPathParser {

    /**
     * Try parse path.
     *
     * @param dAttributeOfPath the d attribute of &lt;path&gt; from the XML
     * @return the path
     */
    @WorkerThread
    @Nullable
    public static Path tryParsePath(String dAttributeOfPath) {
        try {
            return parsePath(dAttributeOfPath);
        } catch (Exception e) {
            Log.e(null, "parse svg path error", e);
        }
        return null;
    }

    /**
     * This is where the hard-to-parse paths are handled.
     * Uppercase rules are absolute positions, lowercase are relative.
     * Types of path rules:
     * <p/>
     * <ol>
     * <li>M/m - (x y)+ - Move to (without drawing)
     * <li>Z/z - (no params) - Close path (back to starting point)
     * <li>L/l - (x y)+ - Line to
     * <li>H/h - x+ - Horizontal ine to
     * <li>V/v - y+ - Vertical line to
     * <li>C/c - (x1 y1 x2 y2 x y)+ - Cubic bezier to
     * <li>S/s - (x2 y2 x y)+ - Smooth cubic bezier to (shorthand that assumes the x2, y2 from previous C/S is the x1, y1 of this bezier)
     * <li>Q/q - (x1 y1 x y)+ - Quadratic bezier to
     * <li>T/t - (x y)+ - Smooth quadratic bezier to (assumes previous control point is "reflection" of last one w.r.t. to current point)
     * </ol>
     * <p/>
     * Numbers are separate by whitespace, comma or nothing at all (!) if they are self-delimiting, (ie. begin with a - sign)
     *
     * @param dAttributeOfPath the d attribute of &lt;path&gt; from the XML
     */
    @WorkerThread
    public static Path parsePath(String dAttributeOfPath) throws Exception {
        int n = dAttributeOfPath.length();
        ParserHelper helper = new ParserHelper(dAttributeOfPath, 0);
        helper.skipWhitespace();
        Path path = new Path();
        float lastX = 0;
        float lastY = 0;
        float lastX1 = 0;
        float lastY1 = 0;
        float subPathStartX = 0;
        float subPathStartY = 0;
        char prevCmd = 0;
        while (helper.getPosition() < n) {
            char cmd = dAttributeOfPath.charAt(helper.getPosition());
            switch (cmd) {
                case '-':
                case '+':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (prevCmd == 'm' || prevCmd == 'M') {
                        cmd = (char) (((int) prevCmd) - 1);
                        break;
                    } else if (prevCmd == 'c' || prevCmd == 'C') {
                        cmd = prevCmd;
                        break;
                    } else if (prevCmd == 'l' || prevCmd == 'L') {
                        cmd = prevCmd;
                        break;
                    }
                default: {
                    helper.advance();
                    prevCmd = cmd;
                }
            }

            boolean wasCurve = false;
            switch (cmd) {
                case 'M':
                case 'm': {
                    float x = helper.nextFloat();
                    float y = helper.nextFloat();
                    Log.d(null, String.format("move to: [%s,%s]", x, y));
                    if (cmd == 'm') {
                        subPathStartX += x;
                        subPathStartY += y;
                        path.rMoveTo(x, y);
                        lastX += x;
                        lastY += y;
                    } else {
                        subPathStartX = x;
                        subPathStartY = y;
                        path.moveTo(x, y);
                        lastX = x;
                        lastY = y;
                    }
                    break;
                }
                case 'Z':
                case 'z': {
                    Log.d(null, String.format("close, move to: [%s,%s]", subPathStartX, subPathStartY));
                    path.close();
                    path.moveTo(subPathStartX, subPathStartY);
                    lastX = subPathStartX;
                    lastY = subPathStartY;
                    lastX1 = subPathStartX;
                    lastY1 = subPathStartY;
                    wasCurve = true;
                    break;
                }
                case 'L':
                case 'l': {
                    float x = helper.nextFloat();
                    float y = helper.nextFloat();
                    Log.d(null, String.format("line to: [%s,%s]", x, y));
                    if (cmd == 'l') {
                        path.rLineTo(x, y);
                        lastX += x;
                        lastY += y;
                    } else {
                        path.lineTo(x, y);
                        lastX = x;
                        lastY = y;
                    }
                    break;
                }
                case 'H':
                case 'h': {
                    float x = helper.nextFloat();
                    Log.d(null, String.format("horizontal line to: [%s]", x));
                    if (cmd == 'h') {
                        path.rLineTo(x, 0);
                        lastX += x;
                    } else {
                        path.lineTo(x, lastY);
                        lastX = x;
                    }
                    break;
                }
                case 'V':
                case 'v': {
                    float y = helper.nextFloat();
                    Log.d(null, String.format("vertical line to: [%s]", y));
                    if (cmd == 'v') {
                        path.rLineTo(0, y);
                        lastY += y;
                    } else {
                        path.lineTo(lastX, y);
                        lastY = y;
                    }
                    break;
                }
                case 'C':
                case 'c': {
                    wasCurve = true;
                    float x1 = helper.nextFloat();
                    float y1 = helper.nextFloat();
                    float x2 = helper.nextFloat();
                    float y2 = helper.nextFloat();
                    float x = helper.nextFloat();
                    float y = helper.nextFloat();
                    Log.d(null, String.format("cubic to: [%s,%s][%s,%s][%s,%s]", x1, y1, x2, y2, x, y));
                    if (cmd == 'c') {
                        x1 += lastX;
                        x2 += lastX;
                        x += lastX;
                        y1 += lastY;
                        y2 += lastY;
                        y += lastY;
                    }
                    path.cubicTo(x1, y1, x2, y2, x, y);
                    lastX1 = x2;
                    lastY1 = y2;
                    lastX = x;
                    lastY = y;
                    break;
                }
                case 'S':
                case 's': {
                    wasCurve = true;
                    float x2 = helper.nextFloat();
                    float y2 = helper.nextFloat();
                    float x = helper.nextFloat();
                    float y = helper.nextFloat();
                    Log.d(null, String.format("cubic to: [%s,%s][%s,%s]", x2, y2, x, y));
                    if (cmd == 's') {
                        x2 += lastX;
                        x += lastX;
                        y2 += lastY;
                        y += lastY;
                    }
                    float x1 = 2 * lastX - lastX1;
                    float y1 = 2 * lastY - lastY1;
                    path.cubicTo(x1, y1, x2, y2, x, y);
                    lastX1 = x2;
                    lastY1 = y2;
                    lastX = x;
                    lastY = y;
                    break;
                }
                case 'A':
                case 'a': {
                    float rx = helper.nextFloat();
                    float ry = helper.nextFloat();
                    float theta = helper.nextFloat();
                    int largeArc = (int) helper.nextFloat();
                    int sweepArc = (int) helper.nextFloat();
                    float x = helper.nextFloat();
                    float y = helper.nextFloat();
                    Log.d(null, String.format("arc to: [%s,%s][%s][%s,%s][%s,%s]", rx, ry, theta, largeArc, sweepArc, x, y));
                    drawArc(path, lastX, lastY, x, y, rx, ry, theta, largeArc, sweepArc);
                    lastX = x;
                    lastY = y;
                    break;
                }
            }
            if (!wasCurve) {
                lastX1 = lastX;
                lastY1 = lastY;
            }
            helper.skipWhitespace();
        }
        return path;
    }

    private static void drawArc(Path p, float lastX, float lastY, float x, float y, float rx, float ry, float theta, int largeArc, int sweepArc) {
        // TODO: 2016/1/24 not implemented yet, may be very hard to do using Android drawing facilities
    }

    /**
     * Parses numbers from SVG text. Based on the Batik Number Parser (Apache 2 License).
     *
     * @author Apache Software Foundation, Larva Labs LLC
     */
    private static class ParserHelper {
        private char current;
        private CharSequence s;
        private int position;
        private int length;

        public ParserHelper(CharSequence s, int position) {
            this.s = s;
            this.position = position;
            length = s.length();
            current = s.charAt(position);
        }

        private char read() {
            if (position < length) {
                position++;
            }
            if (position == length) {
                return '\0';
            } else {
                return s.charAt(position);
            }
        }

        public int getPosition() {
            return position;
        }

        public void skipWhitespace() {
            while (position < length) {
                if (Character.isWhitespace(s.charAt(position))) {
                    advance();
                } else {
                    break;
                }
            }
        }

        public void skipNumberSeparator() {
            while (position < length) {
                char c = s.charAt(position);
                switch (c) {
                    case ' ':
                    case ',':
                    case '\n':
                    case '\t':
                        advance();
                        break;
                    default:
                        return;
                }
            }
        }

        public void advance() {
            current = read();
        }

        /**
         * Parses the content of the buffer and converts it to a float.
         */
        public float parseFloat() {
            int mant = 0;
            int mantDig = 0;
            boolean mantPos = true;
            boolean mantRead = false;

            int exp = 0;
            int expDig = 0;
            int expAdj = 0;
            boolean expPos = true;

            switch (current) {
                case '-':
                    mantPos = false;
                    // fallthrough
                case '+':
                    current = read();
            }

            m1:
            switch (current) {
                default:
                    return Float.NaN;

                case '.':
                    break;

                case '0':
                    mantRead = true;
                    l:
                    for (; ; ) {
                        current = read();
                        switch (current) {
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                break l;
                            case '.':
                            case 'e':
                            case 'E':
                                break m1;
                            default:
                                return 0.0f;
                            case '0':
                        }
                    }

                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    mantRead = true;
                    l:
                    for (; ; ) {
                        if (mantDig < 9) {
                            mantDig++;
                            mant = mant * 10 + (current - '0');
                        } else {
                            expAdj++;
                        }
                        current = read();
                        switch (current) {
                            default:
                                break l;
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                        }
                    }
            }

            if (current == '.') {
                current = read();
                m2:
                switch (current) {
                    default:
                    case 'e':
                    case 'E':
                        if (!mantRead) {
                            reportUnexpectedCharacterError(current);
                            return 0.0f;
                        }
                        break;

                    case '0':
                        if (mantDig == 0) {
                            l:
                            for (; ; ) {
                                current = read();
                                expAdj--;
                                switch (current) {
                                    case '1':
                                    case '2':
                                    case '3':
                                    case '4':
                                    case '5':
                                    case '6':
                                    case '7':
                                    case '8':
                                    case '9':
                                        break l;
                                    default:
                                        if (!mantRead) {
                                            return 0.0f;
                                        }
                                        break m2;
                                    case '0':
                                }
                            }
                        }
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        l:
                        for (; ; ) {
                            if (mantDig < 9) {
                                mantDig++;
                                mant = mant * 10 + (current - '0');
                                expAdj--;
                            }
                            current = read();
                            switch (current) {
                                default:
                                    break l;
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                            }
                        }
                }
            }

            switch (current) {
                case 'e':
                case 'E':
                    current = read();
                    switch (current) {
                        default:
                            reportUnexpectedCharacterError(current);
                            return 0f;
                        case '-':
                            expPos = false;
                        case '+':
                            current = read();
                            switch (current) {
                                default:
                                    reportUnexpectedCharacterError(current);
                                    return 0f;
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                            }
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                    }

                    en:
                    switch (current) {
                        case '0':
                            l:
                            for (; ; ) {
                                current = read();
                                switch (current) {
                                    case '1':
                                    case '2':
                                    case '3':
                                    case '4':
                                    case '5':
                                    case '6':
                                    case '7':
                                    case '8':
                                    case '9':
                                        break l;
                                    default:
                                        break en;
                                    case '0':
                                }
                            }

                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            l:
                            for (; ; ) {
                                if (expDig < 3) {
                                    expDig++;
                                    exp = exp * 10 + (current - '0');
                                }
                                current = read();
                                switch (current) {
                                    default:
                                        break l;
                                    case '0':
                                    case '1':
                                    case '2':
                                    case '3':
                                    case '4':
                                    case '5':
                                    case '6':
                                    case '7':
                                    case '8':
                                    case '9':
                                }
                            }
                    }
                default:
            }

            if (!expPos) {
                exp = -exp;
            }
            exp += expAdj;
            if (!mantPos) {
                mant = -mant;
            }

            return buildFloat(mant, exp);
        }

        private void reportUnexpectedCharacterError(char c) {
            throw new RuntimeException("Unexpected char '" + c + "'.");
        }

        /**
         * Computes a float from mantissa and exponent.
         */
        public static float buildFloat(int mant, int exp) {
            if (exp < -125 || mant == 0) {
                return 0.0f;
            }

            if (exp >= 128) {
                return (mant > 0)
                        ? Float.POSITIVE_INFINITY
                        : Float.NEGATIVE_INFINITY;
            }

            if (exp == 0) {
                return mant;
            }

            if (mant >= (1 << 26)) {
                mant++;  // round up trailing bits if they will be dropped.
            }

            return (float) ((exp > 0) ? mant * pow10[exp] : mant / pow10[-exp]);
        }

        /**
         * Array of powers of ten. Using double instead of float gives a tiny bit more precision.
         */
        private static final double[] pow10 = new double[128];

        static {
            for (int i = 0; i < pow10.length; i++) {
                pow10[i] = Math.pow(10, i);
            }
        }

        public float nextFloat() {
            skipWhitespace();
            float f = parseFloat();
            skipNumberSeparator();
            return f;
        }

    }

}
