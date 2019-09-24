/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.andcreations.ae.image;

/**
 *
 * @author David
 */
public class Compositor {
   public static final int ALPHA_MASK = 0xff000000;
   public static final int RED_MASK = 0x00ff0000;
   public static final int GREEN_MASK = 0x0000ff00;
   public static final int BLUE_MASK = 0x000000ff;

   public static enum Mode {
      Normal,
      Add,
      Multiply,
      Erase,
      Average,
      Overlay,
      Screen,
      Glow,
      Reflect,
      Red,
      Green,
      Blue,
      ColorDodge,
      ColorBurn,
      HardLight,
   }

   public static int premultiply(int argb) {
      int a = argb >>> 24;

      if (a == 0) {
         return 0;
      } else if (a == 255) {
         return argb;
      } else {
         return (a << 24) | multRGB(argb, a);
      }
   }

   public static int multRGB(int src, int multiplier) {
      multiplier++;
      return ((src & RED_MASK) * multiplier) >> 8 & RED_MASK
            | ((src & GREEN_MASK) * multiplier) >> 8 & GREEN_MASK
            | ((src & BLUE_MASK) * multiplier) >> 8;
   }

   public static void blendPixelPremultiplied(int [] pixels, int offset, int srcPx, int alpha, Mode mode) {
      switch (mode) {
         case Normal:
            blendPixel_SrcOver(pixels, offset, srcPx, alpha); return;
         case Add:
            blendPixel_Add(pixels, offset, srcPx, alpha); return;
         case Multiply:
            blendPixel_Multiply(pixels, offset, srcPx, alpha); return;
         case Erase:
            blendPixel_DstOut(pixels, offset, srcPx, alpha); return;
         case Average:
            blendPixel_Average(pixels, offset, srcPx, alpha); return;
         case Overlay:
            blendPixel_Overlay(pixels, offset, srcPx, alpha); return;
         case Screen:
            blendPixel_Screen(pixels, offset, srcPx, alpha); return;
         case Glow:
            blendPixel_Glow(pixels, offset, srcPx, alpha); return;
         case Reflect:
            blendPixel_Reflect(pixels, offset, srcPx, alpha); return;
         case Red:
            blendPixel_Red(pixels, offset, srcPx, alpha); return;
         case Green:
            blendPixel_Green(pixels, offset, srcPx, alpha); return;
         case Blue:
            blendPixel_Blue(pixels, offset, srcPx, alpha); return;
         case ColorBurn:
            blendPixel_ColorBurn(pixels, offset, srcPx, alpha); return;
         case ColorDodge:
            blendPixel_ColorDodge(pixels, offset, srcPx, alpha); return;
         case HardLight:
            blendPixel_HardLight(pixels, offset, srcPx, alpha); return;
      }
      throw new BlendModeNotAvailableException(mode);
   }

   @SuppressWarnings("serial")
   public static class BlendModeNotAvailableException extends RuntimeException {
      public BlendModeNotAvailableException(Mode mode) {
         super(mode + " is not available");
      }
   }

   /**
    * blends two non premultiplied colors together
    * @param pixels
    * @param offset
    * @param srcPx
    * @param alpha
    * @param mode
    */
   public static void blendPixelUnpremultiplied(int[] pixels, int offset, int srcPx, int alpha, Mode mode) {
      pixels[offset] = premultiplyEx(pixels[offset]);
      blendPixelPremultiplied(pixels, offset, premultiplyEx(srcPx), alpha, mode);
      pixels[offset] = unpremultiply(pixels[offset]);
   }

   public static void blendPixel_SrcOver(int pixels[], int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      final int oneMinusSrcA = 0xff - srcA;

      pixels[offset] =
            (blendOneMinus(srcA, destA, oneMinusSrcA) << 24
            | blendOneMinus(srcR, destR, oneMinusSrcA) << 16
            | blendOneMinus(srcG, destG, oneMinusSrcA) << 8
            | blendOneMinus(srcB, destB, oneMinusSrcA));
   }

   /**
    * Blends pixel using DstOut algorithm
    * @param pixels
    * @param offset
    * @param srcPx
    * @param alpha
    */
   public static void blendPixel_DstOut(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      if (alpha != 255) {
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

        final int oneMinusAsAe = 0xff - mult(alpha, srcA);

      pixels[offset] =
            ( mult(destA, oneMinusAsAe) << 24
            | mult(destR, oneMinusAsAe) << 16
            | mult(destG, oneMinusAsAe) << 8
            | mult(destB, oneMinusAsAe));

   }

   public static void blendPixel_Average(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] =
            ( (srcA + destA) << 23
            | (srcR + destR) << 15
            | (srcG + destG) << 7
            | (srcB + destB) >> 1);
   }

   public static void blendPixel_Multiply(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      final int oneMinusSrcA = 0xff - srcA,
              oneMinusDstA = 0xff - destA;
      pixels[offset] =
            blend(srcA, destA, srcA) << 24 |
            (mult(srcR, destR) + mult(destR, oneMinusSrcA) + mult(srcR, oneMinusDstA)) << 16 |
            (mult(srcG, destG) + mult(destG, oneMinusSrcA) + mult(srcG, oneMinusDstA)) << 8 |
            mult(srcB, destB) + mult(destB, oneMinusSrcA) + mult(srcB, oneMinusDstA);
   }

   public static void blendPixel_Add(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] = 
            min(srcA + destA, 0xff) << 24 |
            min(srcR + destR, 0xff) << 16 |
            min(srcG + destG, 0xff) << 8 |
            min(srcB + destB, 0xff);
   }

   public static void blendPixel_Overlay(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      /*
      if a < 128 then
        result := (a*b) SHR 7
      else
        result := 255 - ((255-a) * (255-b) SHR 7);
      */
      pixels[offset] =
            min(255, srcA + destA) << 24 |
            overlay(srcR, destR) << 16 |
            overlay(srcG, destG) << 8 |
            overlay(srcB, destB);
   }

   public static void blendPixel_Screen(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      /*
        result := 255 - ((255-a) * (255-b) SHR 7);
      */
      pixels[offset] =
            min(255, srcA + destA) << 24 |
            screen(srcR, destR) << 16 |
            screen(srcG, destG) << 8 |
            screen(srcB, destB);
   }

   public static void blendPixel_Reflect(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] =
            min(255, srcA + destA) << 24 |
            reflect(srcR, destR) << 16 |
            reflect(srcG, destG) << 8 |
            reflect(srcB, destB);
   }

   public static void blendPixel_Glow(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] = // inverse reflect
            min(255, srcA + destA) << 24 |
            reflect(destR, srcR) << 16 |
            reflect(destG, srcG) << 8 |
            reflect(destB, srcB);
   }

   public static void blendPixel_ColorDodge(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] =
            min(255, srcA + destA) << 24 |
            colordodge(srcR, destR) << 16 |
            colordodge(srcG, destG) << 8 |
            colordodge(srcB, destB);
   }

   public static void blendPixel_ColorBurn(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] =
            min(255, srcA + destA) << 24 |
            colorburn(srcR, destR) << 16 |
            colorburn(srcG, destG) << 8 |
            colorburn(srcB, destB);
   }

   public static void blendPixel_Darken(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] =
            min(255, srcA + destA) << 24 |
            min(srcR, destR) << 16 |
            min(srcG, destG) << 8 |
            min(srcB, destB);
   }

   public static void blendPixel_Lighten(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] =
            min(255, srcA + destA) << 24 |
            max(srcR, destR) << 16 |
            max(srcG, destG) << 8 |
            max(srcB, destB);
   }

   public static void blendPixel_Red(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] =
            min(255, srcA + destA) << 24 |
            srcR << 16 |
            destG << 8 |
            destB;
   }

   public static void blendPixel_Green(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      if (alpha != 255) {
         srcG = mult(srcG, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] =
            min(255, srcA + destA) << 24 |
            destR << 16 |
            srcG << 8 |
            destB;
   }

   public static void blendPixel_Blue(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;

      pixels[offset] =
            min(255, srcA + destA) << 24 |
            destR << 16 |
            destG << 8 |
            srcB;
   }

   public static void blendPixel_HardLight(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] =
            min(0xff, srcA + destA) << 24 |
            hardlight(srcR, destR) << 16 |
            hardlight(srcG, destG) << 8 |
            hardlight(srcB, destB);
   }

   public static void blendPixel_Difference(int [] pixels, int offset, int srcPx, int alpha) {
      int destPx = pixels[offset];
      int srcA = srcPx >>> 24;
      int srcR = (srcPx & RED_MASK) >>> 16;
      int srcG = (srcPx & GREEN_MASK) >>> 8;
      int srcB = (srcPx & BLUE_MASK);
      if (alpha != 255) {
         srcR = mult(srcR, alpha);
         srcG = mult(srcG, alpha);
         srcB = mult(srcB, alpha);
         srcA = mult(srcA, alpha);
      }
      int destA = (destPx) >>> 24;
      int destR = (destPx & RED_MASK) >>> 16;
      int destG = (destPx & GREEN_MASK) >>> 8;
      int destB = (destPx & BLUE_MASK);

      pixels[offset] =
            blend(srcA, destA, srcA) << 24 |
               (srcR + destR - (2 * min(mult(srcR, destA), mult(destR, srcA)))) << 16 |
               (srcG + destG - (2 * min(mult(srcG, destA), mult(destG, srcA)))) << 8 |
               (srcB + destB - (2 * min(mult(srcB, destA), mult(destB, srcA))));
   }

   public static int hardlight(int a, int b) {
      return a < 128 ? (a * b) >> 7 : 0xff - ((0xff-a) * (0xff-b) >> 7);
   }

   public static int reflect(int a, int b) {
      return b == 255 ? 255 : min(255, a * a / (255 - b));
   }
   
   public static int colorburn(int a, int b) {
      return b == 0 ? 0 : max(0, (0xff - ((0xff-a) << 8)/ b));
   }

   public static int colordodge(int a, int b) {
      return b == 0xff ? 0xff : min(0xff, ((a<<8) / (0xff-b)));
   }

   public static int overlay(int a, int b) {
      if (a < 128) {
         return (a * b) >> 7;
      } else {
         return 0xff - (((0xff-a) * (0xff-b)) >> 7);
      }
   }

   public static int screen(int a, int b) {
      return 0xff - mult(a, b);
   }

   public static int min(int a, int b) {
      return a < b ? a:b;
   }

   public static int max(int a, int b) {
      return a > b ? a:b;
   }

   public static int blendOneMinus(int src, int dest, int oneMinus) {
      return src + mult(dest, oneMinus);
   }

   public static int blend(int src, int dest, int alpha) {
      return src + mult(dest, 0xFF - alpha);
   }

   public static int mult2(int val, int multiplier) {
      return (val * (multiplier + 1)) >> 8;
   }

   public static int mult(int a, int b) {
      final int t = a * b + 128;
      return (t >> 8) + t >> 8;
   }

   public static int premultiplyEx(int rgb) {
      return premultiply(rgb, rgb>>>24);
   }

   public static int premultiply(int rgbColor, int alpha) {

      if (alpha <= 0) {
         return 0;
      } else if (alpha >= 255) {
         return 0xff000000 | rgbColor;
      } else {
         int r = (rgbColor >> 16) & 0xff;
         int g = (rgbColor >> 8) & 0xff;
         int b = rgbColor & 0xff;

         r = (alpha * r + 127) / 255;
         g = (alpha * g + 127) / 255;
         b = (alpha * b + 127) / 255;
         return (alpha << 24) | (r << 16) | (g << 8) | b;
      }
   }

   public static int unpremultiply(int preARGBColor) {
      int a = preARGBColor >>> 24;

      if (a == 0) {
         return 0;
      } else if (a == 255) {
         return preARGBColor;
      } else {
         int r = (preARGBColor >> 16) & 0xff;
         int g = (preARGBColor >> 8) & 0xff;
         int b = preARGBColor & 0xff;

         r = 255 * r / a;
         g = 255 * g / a;
         b = 255 * b / a;
         return (a << 24) | (r << 16) | (g << 8) | b;
      }
   }
}