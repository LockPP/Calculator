import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static String[] op = { "+", "-", "*", "/" };// Operation set
    public static void main(String[] args) throws IOException {
        int testNum;
        if(args.length == 0){
            System.out.println("请输入题目的数量");
            Scanner scanner = new Scanner(System.in);
            testNum = scanner.nextInt();
        }else {
            testNum = Integer.parseInt(args[0]);
        }

        FileWriter testsWriter = new FileWriter("subject.txt");
        for(int loop = 0; loop < testNum; ++loop) {
            String question = MakeFormula();
            System.out.println(question);
            String ret = Solve(question);
            System.out.println(ret);

            testsWriter.write(ret + "\n");
        }
        testsWriter.flush();
    }

    public static String MakeFormula(){
        StringBuilder build = new StringBuilder();
        Random random = new Random();
        int count = random.nextInt(2) + 2; // generate random count

        int[] numbers = new int[count];
        int[] operations = new int[count-1];

        for (int loop = 0; loop < numbers.length; ++loop){
            numbers[loop] = random.nextInt(99) + 1;
        }
        for (int loop = 0; loop < operations.length; ++loop){
            operations[loop] = random.nextInt(4);
            while (operations[loop] == 3 && numbers[loop] % numbers[loop+1] != 0){
                --numbers[loop+1];
            }
        }

        for (int loop = 0; loop < operations.length; ++loop){
            build.append(numbers[loop]);
            build.append(op[operations[loop]]);
        }
        build.append(numbers[numbers.length-1]);

        return build.toString();
    }

    public static String Solve(String formula){
        byte[] bytes = formula.getBytes(StandardCharsets.UTF_8);
        LinkedList<Integer> numbers = new LinkedList<>();
        LinkedList<Byte> operations = new LinkedList<Byte>();

        int index = 0;
        for(int loop = 0; loop < bytes.length; ++loop){
            if(bytes[loop] == '+' || bytes[loop] == '-' || bytes[loop] == '*' || bytes[loop] == '/'){
                numbers.add(new Integer(new String(bytes, index, loop-index)));
                operations.add(bytes[loop]);
                index = loop + 1;
            }
        }
        numbers.add(new Integer(new String(bytes, index, bytes.length-index)));

        for(int loop = 0; loop < operations.size(); ++loop){
            switch (operations.get(loop).byteValue()){
                case '*':
                    numbers.set(loop, numbers.get(loop)*numbers.get(loop+1));
                    numbers.remove(loop+1);
                    operations.remove(loop);
                    break;
                case '/':
                    numbers.set(loop, numbers.get(loop)/numbers.get(loop+1));
                    numbers.remove(loop+1);
                    operations.remove(loop);
                    break;
                default:
            }
        }
        for(int loop = 0; loop < operations.size(); ++loop){
            switch (operations.get(loop).byteValue()){
                case '+':
                    numbers.set(loop, numbers.get(loop)+numbers.get(loop+1));
                    numbers.remove(loop+1);
                    operations.remove(loop);
                    break;
                case '-':
                    numbers.set(loop, numbers.get(loop)-numbers.get(loop+1));
                    numbers.remove(loop+1);
                    operations.remove(loop);
                    break;
                default:
            }
        }
        return formula + "=" + numbers.get(0);
    }
}
