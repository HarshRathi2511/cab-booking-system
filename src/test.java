// public class test {
    
// }


// /**
//  * Made by:
//  * Parth Thakkar (2020A7PS0088P)
//  */
// import java.lang.reflect.Array;
// import java.util.*;

// class qTimer implements Runnable {
//     private int currTime;
//     CommonData commonData;
//     private boolean shouldStop = false;
//     private int id;

//     qTimer(int time, CommonData commonData, int id) {
//         currTime = time;
//         this.commonData = commonData;
//         this.id = id;
//     }

//     private void countDown() {
//         commonData.timerOver[id] = false;
//         while (currTime > 0) {
//             try {
//                 Thread.sleep(1000);
//                 currTime--;
//             } catch (InterruptedException e) {
//                 System.out.println("Timer misfunction");
//             }
//         }
//     }

//     public void stop() {
//         shouldStop = true;
//     }

//     public void run() {
//         countDown();
//         if (currTime == 0) {
//             if (!shouldStop)
//                 System.out.println("Time up!");
//             commonData.timerOver[id] = true;
//         }
//     }
// }

// class ClearScreen {
//     static void clear() {
//         System.out.print("\033[H\033[2J");
//         System.out.flush();
//     }
// }

// class Question {
//     private String quest;
//     private int time;

//     Question(String s, int t) {
//         quest = s;
//         time = t;
//     }

//     public int getTime() {
//         return time;
//     }

//     public String getQuestion() {
//         return quest;
//     }
// }

// abstract class Solution<T> {
//     protected T sol;

//     Solution(T s) {
//         this.sol = s;
//     }

//     boolean checkSolution(T s) {
//         if (s.equals(sol))
//             return true;
//         else
//             return false;
//     }

//     T getSolution() {
//         return sol;
//     }
// }

// class IntegerSolution extends Solution<Integer> {

//     IntegerSolution(int n) {
//         super(n);
//     }
// }

// class ChoiceSolution extends Solution<String> {

//     ChoiceSolution(String s) {
//         super(s);
//     }
// }

// class Student implements Runnable, Comparable<Student> {
//     CommonData commonData;
//     private String name;
//     private int rollNo;
//     private int score;
//     private int id;
//     private String answer;
//     private ArrayList<String> record;
//     qTimer time;
//     float accuracy;
//     int noOfQuestionsAttempted;

//     Student(CommonData commonData, String n, int r, int id) {
//         name = n;
//         rollNo = r;
//         this.id = id;
//         this.commonData = commonData;
//         record = new ArrayList<String>();
//         score = 0;
//         accuracy = 0;
//         noOfQuestionsAttempted = 0;
//     }

//     public int getRoll() {
//         return rollNo;
//     }

//     public void run() {
//         answerQuestion();
//     }

//     void answerQuestion() {
//         // System.out.println("Id:" + id + " started");
//         synchronized (commonData.lock) {
//             // System.out.println("Id:" + id + " started inside");
//             while (!commonData.questionsOver) {
//                 if (!commonData.answeredQuestions[id]) {
//                     time = new qTimer(commonData.currentQuestion.getTime(), commonData, id);
//                     Thread timer = new Thread(time, "timer " + id);
//                     timer.start();
//                     Scanner sc = new Scanner(System.in);
//                     ClearScreen.clear();
//                     System.out.println(" Roll no:" + id + "\tTime:" + commonData.currentQuestion.getTime() + " seconds"
//                             + "\nQ" + commonData.qNo + ")" + commonData.currentQuestion.getQuestion());
//                     answer = sc.nextLine();
//                     time.stop();
//                     if (!commonData.timerOver[id]) {
//                         record.add(answer);
//                         noOfQuestionsAttempted++;
//                         try {
//                             int intAnswer = Integer.parseInt(answer);
//                             if (commonData.currentSolution.checkSolution(intAnswer))
//                                 score += 5;
//                         } catch (NumberFormatException e) {
//                             if (commonData.currentSolution.checkSolution(answer))
//                                 score += 5;
//                         }
//                         accuracy = (float) score * 100 / (5 * noOfQuestionsAttempted);
//                         time.stop();
//                     } else {
//                         record.add("Time over");
//                     }
//                     commonData.answeredQuestions[id] = true;
//                     commonData.lock.notifyAll();
//                 } else {
//                     try {
//                         commonData.lock.wait();
//                     } catch (InterruptedException e) {
//                         System.out.println("DEED");
//                     }
//                 }
//             }
//         }
//     }

//     public int compareTo(Student s) {
//         return s.score - this.score;
//     }

//     public void getDetails() {
//         System.out.printf("|%4d |%10s |%5d |%12s |%9.2f |%n", rollNo, name, score, noOfQuestionsAttempted, accuracy);
//     }

//     public void getStudentAnalysis() {
//         System.out.println("\n\nRoll no:" + rollNo + "\tName:" + name + "\nScore:" + score
//                 + "\t  No of questions attempted" + noOfQuestionsAttempted + "\tAccuracy:" + accuracy);
//         for (int i = 0; i < commonData.qNo; i++) {
//             System.out.println("\nQ" + (i + 1) + ")" + commonData.questionArray.get(i));
//             System.out.printf("%s%-15s %s%s%n", "Answer given:", record.get(i), "Correct solution:", Admin.getSol(i));
//         }
//     }
// }

// class Admin implements Runnable {
//     CommonData commonData;
//     private static HashMap<Question, Solution> questionBank = new HashMap<Question, Solution>();
//     private ArrayList<Thread> threadList = new ArrayList<Thread>();
//     private static ArrayList<String> solutionList = new ArrayList<String>();
//     private Iterator itr;
//     private Scanner sc1;

//     Admin(CommonData commonData) {
//         this.commonData = commonData;
//         sc1 = new Scanner(System.in);
//     }

//     void setStudents() {
//         String name;
//         int rollNo;
//         Student stud;
//         int id = 0;
//         Scanner sc = new Scanner(System.in);
//         System.out.println("Enter number of students");
//         commonData.noOfStudents = sc.nextInt();
//         for (int i = 0; i < commonData.noOfStudents; i++) {
//             System.out.println("Name:");
//             name = sc.next();
//             System.out.println("Roll no:");
//             rollNo = sc.nextInt();
//             stud = new Student(commonData, name, rollNo, id++);
//             commonData.studList.add(stud);
//             Thread t = new Thread(commonData.studThreads, stud, Integer.toString(rollNo));
//             threadList.add(t);
//         }
//         commonData.answeredQuestions = new boolean[commonData.noOfStudents];
//         commonData.timerOver = new boolean[commonData.noOfStudents];
//         // sc.close();
//     }

//     public void run() {
//         setStudents();
//         adminMenu();
//     }

//     private void adminMenu() {
//         ClearScreen.clear();
//         Scanner sc = new Scanner(System.in);
//         System.out.println("+--------Admin Menu--------+");
//         System.out.printf("| %-25s|%n| %-25s|%n| %-25s|%n| %-25s|%n| %-25s|%n", "1-Add question", "2-Start test",
//                 "3-View Leaderboard", "4-Get student analysis", "5-Exit");
//         System.out.println("+--------------------------+\nEnter your choice:");
//         int ch = sc.nextInt();
//         switch (ch) {
//         case 1:
//             makeQuestion();
//             break;
//         case 2:
//             startTest();
//             break;
//         case 3:
//             viewLeaderBoard();
//             break;
//         case 4:
//             System.out.println("Enter roll no of student for analysis");
//             getStudentReport(sc.nextInt());
//             break;
//         case 5:
//             System.out.println("Thank you for using. Goodbye!");
//             return;
//         default:
//             System.out.println("Invalid choice");
//         }
//         adminMenu();
//     }

//     private void getStudentReport(int roll) {
//         ClearScreen.clear();
//         boolean studentExists = false;
//         for (Student stud : commonData.studList) {
//             if (stud.getRoll() == roll) {
//                 stud.getStudentAnalysis();
//                 studentExists = true;
//                 break;
//             }
//         }
//         if (!studentExists) {
//             System.out.println("Roll number does not exist!");
//         }
//         sc1.nextLine();
//     }

//     private void makeQuestion() {
//         Scanner sc = new Scanner(System.in);
//         ClearScreen.clear();
//         System.out.println("Choose type of question:\n1-Multiple Choice Type\n2-Integer Type");
//         int ch = sc.nextInt();
//         switch (ch) {
//         case 1:
//             sc.nextLine();
//             System.out.println("Question:");
//             String quest = sc.nextLine();
//             System.out.println("Enter four option in four lines:-");
//             String optionA = sc.nextLine();
//             String optionB = sc.nextLine();
//             String optionC = sc.nextLine();
//             String optionD = sc.nextLine();
//             String finalQ = quest + "\na)" + optionA + "\nb)" + optionB + "\nc)" + optionC + "\nd)" + optionD;
//             System.out.println("Correct answer:");
//             String correctOption = sc.nextLine();
//             System.out.println("Time alloted for Question(seconds):");
//             int qTime = sc.nextInt();
//             questionBank.put(new Question(finalQ, qTime), new ChoiceSolution(correctOption));
//             break;
//         case 2:
//             sc.nextLine();
//             System.out.println("Question:");
//             String ques = sc.nextLine();
//             System.out.println("Answer:");
//             int ans = sc.nextInt();
//             System.out.println("Time alloted for Question:");
//             int qIntTime = sc.nextInt();
//             questionBank.put(new Question(ques, qIntTime), new IntegerSolution(ans));
//             break;
//         default:
//             System.out.println("Invalid Input");
//         }
//         // questionBank.put(new Question("Choose a", 10), new ChoiceSolution("a"));
//         itr = questionBank.entrySet().iterator();
//     }

//     private void selectQuestion() {
//         if (itr.hasNext()) {
//             commonData.qNo++;
//             Map.Entry mapElement = (Map.Entry) itr.next();
//             commonData.currentQuestion = (Question) mapElement.getKey();
//             commonData.questionArray.add(commonData.currentQuestion.getQuestion());
//             commonData.currentSolution = (Solution) mapElement.getValue();
//             if (commonData.currentSolution.getSolution() instanceof Integer)
//                 solutionList.add(Integer.toString((int) commonData.currentSolution.getSolution()));
//             else
//                 solutionList.add((String) commonData.currentSolution.getSolution());

//         } else {
//             commonData.questionsOver = true;
//         }
//     }

//     static String getSol(int i) {
//         return solutionList.get(i);
//     }

//     private void startTest() {
//         synchronized (commonData.lock) {
//             selectQuestion();
//             for (int i = 0; i < commonData.noOfStudents; i++) {
//                 Array.setBoolean(commonData.answeredQuestions, i, false);
//             }
//             for (Thread thr : threadList) {
//                 thr.start();
//             }
//             while (!commonData.questionsOver) {
//                 boolean everyOneAnswered = true;
//                 for (int i = 0; i < commonData.noOfStudents; i++) {
//                     if (commonData.answeredQuestions[i] == false) {
//                         everyOneAnswered = false;
//                         break;
//                     }
//                 }
//                 if (everyOneAnswered) {
//                     selectQuestion();
//                     for (int i = 0; i < commonData.noOfStudents; i++) {
//                         Array.setBoolean(commonData.answeredQuestions, i, false);
//                     }
//                     commonData.lock.notifyAll();
//                 } else {
//                     try {
//                         commonData.lock.wait();
//                     } catch (InterruptedException e) {
//                         e.printStackTrace();
//                     }
//                 }
//             }
//         }
//         endOfTest();
//     }

//     private void viewLeaderBoard() {
//         ClearScreen.clear();
//         Collections.sort(commonData.studList);
//         System.out.println("Leaderboad:\n+-------------------------------------------------+");
//         System.out.printf("|%4s |%10s |%5s |%12s |%9s |%n", "Roll", "Name", "Score", "Qs attempted", "Accuracy");
//         System.out.println("+-----+-----------+------+-------------+----------+");
//         Iterator<Student> i = commonData.studList.iterator();
//         while (i.hasNext())
//             i.next().getDetails();
//         sc1.nextLine();
//         System.out.println("+-------------------------------------------------+");
//     }

//     private void endOfTest() {
//         System.out.println("Test ended!");
//         sc1.nextLine();
//     }
// }

// class CommonData {
//     public ArrayList<Student> studList = new ArrayList<Student>();
//     ArrayList<String> questionArray = new ArrayList<String>();
//     public Object lock = new Object();
//     int qNo;
//     int noOfStudents;
//     boolean answeredQuestions[];
//     boolean hasSelectedQuestion = false;
//     boolean questionsOver = false;
//     boolean timerOver[];
//     Question currentQuestion;
//     Solution currentSolution;
//     ThreadGroup studThreads = new ThreadGroup("Students");
// }

// public class Main {
//     public static void main(String[] args) {
//         CommonData commonData = new CommonData();
//         Admin admin = new Admin(commonData);
//         Thread adminThread = new Thread(admin);
//         ClearScreen.clear();
//         System.out.println("\n\nWelcome to Online Quiz System!\n");
//         adminThread.start();
//     }

// }