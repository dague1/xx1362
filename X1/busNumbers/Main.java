// Written by Edvard All and David Ljunggren
import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int num = sc.nextInt();
    int busStop[] = new int[num];

    for (int i = 0; i < num; i++)
      busStop[i] = sc.nextInt();
    Arrays.sort(busStop);

    StringBuilder lines = new StringBuilder(String.valueOf(busStop[0]));
    for (int i = 1; i < num; i++) {
      if (busStop[i] == busStop[i - 1])
        continue;
      if ((busStop[i] == busStop[i - 1] + 1) && (num > i + 1) &&
          (busStop[i + 1] == busStop[i] + 1)) {
        lines.append("-");
        ++i;
        while ((num > i + 1) && (busStop[i + 1] == busStop[i] + 1))
          i++;
        lines.append(busStop[i]);
      } else
        lines.append(" " + busStop[i]);
    }
    System.out.println(lines);
  }
}
