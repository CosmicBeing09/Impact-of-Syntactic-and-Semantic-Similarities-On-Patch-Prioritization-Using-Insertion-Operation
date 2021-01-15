package introclassJava;

class IntObj {
    public int value;
    public IntObj () {
    } public IntObj (int i) {
        value = i;
    }
}

class FloatObj {
    public float value;
    public FloatObj () {
    } public FloatObj (float i) {
        value = i;
    }
}

class LongObj {
    public long value;
    public LongObj () {
    } public LongObj (long i) {
        value = i;
    }
}

class DoubleObj {
    public double value;
    public DoubleObj () {
    } public DoubleObj (double i) {
        value = i;
    }
}

class CharObj {
    public char value;
    public CharObj () {
    } public CharObj (char i) {
        value = i;
    }
}

public class grade_9c9308d4_000 {
    public java.util.Scanner scanner;
    public String output = "";

    public static void main (String[]args) throws Exception {
        grade_9c9308d4_000 mainClass = new grade_9c9308d4_000 ();
        String output;
        if (args.length > 0) {
            mainClass.scanner = new java.util.Scanner (args[0]);
        } else {
            mainClass.scanner = new java.util.Scanner (System.in);
        }
        mainClass.exec ();
        System.out.println (mainClass.output);
    }

    public void exec () throws Exception {
        FloatObj A = new FloatObj (), B = new FloatObj (), C =
            new FloatObj (), D = new FloatObj (), studentscore = new FloatObj ();
        CharObj lettergrade = new CharObj ();
        output +=
            (String.format
             ("Enter thresholds for A, B, C, D in that order, decreasing percentages > Thank you. "));
        A.value = scanner.nextFloat ();
        B.value = scanner.nextFloat ();
        C.value = scanner.nextFloat ();
        D.value = scanner.nextFloat ();
        output += (String.format ("Now enter student score (perecnt) >"));
        studentscore.value = scanner.nextFloat ();
        if (studentscore.value >= A.value) {
            lettergrade.value = 'A';
        } else if (studentscore.value >= B.value) {
            lettergrade.value = 'B';
        } else if (studentscore.value >= C.value) {
            lettergrade.value = 'C';
        } else {
            lettergrade.value = 'D';
        }
        output +=
            (String.format ("Student has an %c grade\n", lettergrade.value));
        if (true)
            return;;
    }
}
