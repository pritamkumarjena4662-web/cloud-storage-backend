// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
   public Main() {
   }

   public static void main(String[] var0) {
      Scanner var1 = new Scanner(System.in);
      ArrayList var2 = new ArrayList();

      while(true) {
         System.out.println("\n===== Student Management System =====");
         System.out.println("1. Add Student");
         System.out.println("2. View Students");
         System.out.println("3. Exit");
         System.out.print("Enter your choice: ");
         int var3 = var1.nextInt();
         switch (var3) {
            case 1:
               System.out.print("Enter ID: ");
               int var4 = var1.nextInt();
               var1.nextLine();
               System.out.print("Enter Name: ");
               String var5 = var1.nextLine();
               System.out.print("Enter Age: ");
               int var6 = var1.nextInt();
               var2.add(new Student(var4, var5, var6));
               System.out.println("Student Added Successfully!");
               break;
            case 2:
               if (var2.isEmpty()) {
                  System.out.println("No Students Found.");
               } else {
                  for(Student var8 : var2) {
                     var8.display();
                  }
               }
               break;
            case 3:
               System.out.println("Thank You!");
               var1.close();
               return;
            default:
               System.out.println("Invalid Choice!");
         }
      }
   }
}
