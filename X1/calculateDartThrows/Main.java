/*
  Written by Edvard All and David Ljunggren
*/
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    StringBuilder darts = new StringBuilder();
    Scanner scanner = new Scanner(System.in);
    int input = scanner.nextInt();

    throwDarts(input, darts);

    if (darts.length() != 0)
      System.out.print(darts);
    else
      System.out.println("impossible");
  }

  static void throwDarts(int p, StringBuilder darts) {
    for (int i = 1; i <= 3; i++)
      for (int j = 1; j <= 20; j++)
        for (int k = 0; k <= 3; k++)
          for (int l = 1; l <= 20; l++)
            for (int m = 0; m <= 3; m++)
              for (int n = 1; n <= 20; n++)
                if (i * j + k * l + m * n == p) {
                  darts.append(dartToString(i, j));
                  darts.append(dartToString(k, l));
                  darts.append(dartToString(m, n));
                  return;
                }
  }

  static String dartToString(int m, int d) {
    if (m == 0)
      return "";

    String mult = (m % 3 == 0 ? "triple " : m % 2 == 0 ? "double " : "single ");

    return mult + d + "\n";
  }
}
