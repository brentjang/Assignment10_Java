public class Foothill
{
   public static void main (String[] args)
   {
      int k;
      Student student;
      
      Student[] myClass = { new Student("smith","fred", 95), 
         new Student("bauer","jack",123),
         new Student("jacobs","carrie", 195), 
         new Student("renquist","abe",148),
         new Student("3ackson","trevor", 108), 
         new Student("perry","fred",225),
         new Student("loceff","fred", 44), 
         new Student("stollings","pamela",452),
         new Student("charters","rodney", 295), 
         new Student("cassar","john",321),
      };
      
      // instantiate a StudArrUtilObject
      StudentArrayUtilities myStuds = new StudentArrayUtilities();
      
      // we can add stdunts manually and individually
      myStuds.addStudent( new Student("bartman", "petra", 102) );
      myStuds.addStudent( new Student("charters","rodney", 295));
      
      // if we happen to have an array available, we can add students in loop.
      for (k = 0; k < myClass.length; k++)
         myStuds.addStudent( myClass[k] );

      System.out.println( myStuds.toString("Before: "));
      
      myStuds.arraySort();
      System.out.println( myStuds.toString("Sorting by default: "));
 
      Student.setSortKey(Student.SORT_BY_FIRST);
      myStuds.arraySort();
      System.out.println( myStuds.toString("Sorting by first name: "));
      
      Student.setSortKey(Student.SORT_BY_POINTS);
      myStuds.arraySort();
      System.out.println( myStuds.toString("Sorting by total points: "));
      
      // test median
      System.out.println("Median of evenClass = "
         +  myStuds.getMedianDestructive() + "\n");
      
      // various tests of removing and adding too many students
      for (k = 0; k < 100; k++)
      {
         if ( (student = myStuds.removeStudent()) != null)
         {
            System.out.println("Removed " + student);
         }
         else
         {
            System.out.println("Empty after " + k + " removes.");
            break;
         }
      }

      for (k = 0; k < 100; k++)
      {
         if (!myStuds.addStudent(new Student("first", "last", 22)))
         {
            System.out.println("Full after " + k + " adds.");
            break;
         }
      }
   }
}

// contains student constructor and sorting methods
class Student
{
   // constants that can be changed by user
   public static final String DEFAULT_NAME = "zz-error";
   public static final int DEFAULT_POINTS = 0;
   public static final int MAX_POINTS = 1000;
   public static final int SORT_BY_FIRST = 88;
   public static final int SORT_BY_LAST = 98;
   public static final int SORT_BY_POINTS = 108;
   private static int sortKey = SORT_BY_LAST;
   
   // private variables
   private String lastName;
   private String firstName;
   private int totalPoints;

   // constructor requires parameters - no default supplied
   public Student(String last, String first, int points)
   {
      if (setLastName(last) == false)
      {
         lastName = DEFAULT_NAME;
      }
      if (setFirstName(first) == false)
      {
         firstName = DEFAULT_NAME;
      }
      if (setPoints(points) == false)
      {
         totalPoints = DEFAULT_POINTS;
      }
   }

   // sets last name
   public boolean setLastName(String last)
   {
      if (validString(last) == false)
      {
         return false;
      }
      lastName = last;
      return true;
   }

   // sets first name
   public boolean setFirstName(String first)
   {
      if (validString(first) == false)
      {
         return false;
      }
      firstName = first;
      return true;
   }

   // set points
   public boolean setPoints(int points)
   {
      if (validPoints(points) == false)
      {
         return false;
      }
      totalPoints = points;
      return true;
   }

   // sets the sorting key value
   public static boolean setSortKey(int key)
   {
      if ( !(key == SORT_BY_FIRST || key == SORT_BY_LAST
            || key == SORT_BY_POINTS) )
      {
         return false;
      }
      sortKey = key;
      return true;
   }
   
   // returns last name
   public String getLastName() 
   { 
      return lastName; 
   }
   
   // returns first name
   public String getFirstName() 
   { 
      return firstName; 
   }
   
   // return total points
   public int getTotalPoints() 
   { 
      return totalPoints; 
   }
   
   // return sorting key value
   public static int getSortKey() 
   { 
      return sortKey; 
   }

   // compares two students based on the sorting key
   public static int compareTwoStudents
      (Student firstStudent, Student secondStudent)
   {
      int result;
      // switch statement for each sort value
      switch (sortKey)
      {
      case SORT_BY_FIRST:
         result = firstStudent.firstName.compareToIgnoreCase
         (secondStudent.firstName);
         break;
      case SORT_BY_LAST:
         result = firstStudent.lastName.compareToIgnoreCase
         (secondStudent.lastName);
         break;
      default:
         result = firstStudent.totalPoints - secondStudent.totalPoints;
         break;
      }
      return result;
   }

   // formats output for each string
   public String toString()
   {
      String resultString;
      resultString = (" " + lastName
            + ", " + firstName
            + " points: " + totalPoints
            + "\n");
      return resultString;
   }

   // checks for a not null, non number string
   private static boolean validString(String string)
   {
      if (!Character.isLetter(string.charAt(0)) || string == null)
      {
         return false;
      }
      return true;
   }

   // must be non-negative and less than max points
   private static boolean validPoints(int points)
   {
      if (points < 0 || points > MAX_POINTS)
      {
         return false;
      }
      return true;
   }
}

// contains sorting and computational methods
class StudentArrayUtilities
{
   public static final int MAX_STUDENTS = 20;
   private int numStudents = 0;
   private Student[] theArray = new Student[MAX_STUDENTS];
   
   // prints the roster in full
   public String toString(String title)
   {
      String result = title + "\n";

      // build the output string from the individual Students:
      for (int i = 0; i < numStudents; i++)
      {
         result += " "+ theArray[i].toString();
      }
      return result;
   }

   // returns true if a modification was made to the array
   private boolean floatLargestToTop(int top)
   {
      boolean flag = false;
      Student temp;
      // compare with client call to see where the loop stops
      for (int k = 0; k < top; k++)
      {        
         if (Student.compareTwoStudents(theArray[k], theArray[k+1]) > 0)
         {
            temp = theArray[k];
            theArray[k] = theArray[k+1];
            theArray[k+1] = temp;
            flag = true;
         }
      }
      return flag;
   }

   // callable arraySort() - assumes Student class has a compareTo()
   public void arraySort()
   {
      for (int k = 0; k < numStudents; k++)
      {
         // compare with method def to see where inner loop stops
         if ( !floatLargestToTop(numStudents - 1 - k) )
         {
            return;
         }
      }
   }
   
   // returns the median points for given array
   public double getMedianDestructive()
   {
      double med;

      // if length 0
      if (theArray.length == 0)
      {
         return 0;
      }
      
      // if length 1
      if (theArray.length == 1)
      {
         return theArray[0].getTotalPoints();
      }

      // changes to sort by points
      int key = Student.getSortKey();
      Student.setSortKey(Student.SORT_BY_POINTS);
      arraySort();

      // checks if an odd or even length array
      if (theArray.length % 2 == 0)
      {
         med = (theArray[theArray.length / 2 - 1].getTotalPoints() +
               theArray[theArray.length / 2].getTotalPoints()) / 2.0;
      }
      else
      {
         med = theArray[theArray.length / 2].getTotalPoints();
      }
      // sets back to last sort key value
      Student.setSortKey(key);
      return med;
   }
   
   public boolean addStudent(Student student)
   {
      if (student != null && numStudents < MAX_STUDENTS)
      {
         theArray[numStudents] = student;
         numStudents++;
         return true;
      }
      return false;
   }

   public Student removeStudent()
   {
      Student lastStudent;
      if (numStudents != 0)
      {
         lastStudent = theArray[numStudents - 1];
         numStudents--;
         return lastStudent;
      }
      return null;
   }
}
/*
\\\\\\\\\\\\\\\\\\\\\\\\\\ PASTED OUTPUT ////////////////////
Before: 
   bartman, petra points: 102
   charters, rodney points: 295
   smith, fred points: 95
   bauer, jack points: 123
   jacobs, carrie points: 195
   renquist, abe points: 148
   zz-error, trevor points: 108
   perry, fred points: 225
   loceff, fred points: 44
   stollings, pamela points: 452
   charters, rodney points: 295
   cassar, john points: 321

 Sorting by default: 
   bartman, petra points: 102
   bauer, jack points: 123
   cassar, john points: 321
   charters, rodney points: 295
   charters, rodney points: 295
   jacobs, carrie points: 195
   loceff, fred points: 44
   perry, fred points: 225
   renquist, abe points: 148
   smith, fred points: 95
   stollings, pamela points: 452
   zz-error, trevor points: 108

 Sorting by first name: 
   renquist, abe points: 148
   jacobs, carrie points: 195
   loceff, fred points: 44
   perry, fred points: 225
   smith, fred points: 95
   bauer, jack points: 123
   cassar, john points: 321
   stollings, pamela points: 452
   bartman, petra points: 102
   charters, rodney points: 295
   charters, rodney points: 295
   zz-error, trevor points: 108

 Sorting by total points: 
   loceff, fred points: 44
   smith, fred points: 95
   bartman, petra points: 102
   zz-error, trevor points: 108
   bauer, jack points: 123
   renquist, abe points: 148
   jacobs, carrie points: 195
   perry, fred points: 225
   charters, rodney points: 295
   charters, rodney points: 295
   cassar, john points: 321
   stollings, pamela points: 452

 Median of evenClass = 308.0

 Removed  stollings, pamela points: 452

 Removed  cassar, john points: 321

 Removed  charters, rodney points: 295

 Removed  charters, rodney points: 295

 Removed  perry, fred points: 225

 Removed  jacobs, carrie points: 195

 Removed  renquist, abe points: 148

 Removed  bauer, jack points: 123

 Removed  zz-error, trevor points: 108

 Removed  bartman, petra points: 102

 Removed  smith, fred points: 95

 Removed  loceff, fred points: 44

 Empty after 12 removes.
 Full after 20 adds.
 */