import Lesson6.ArrayTask;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestTask1_RE {
    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList (new Object[][]{
                {new int[]{1, 2, 3, 3, 8, 9}},
                {new int[]{1, 5, 3, 7, 10, 9}},
                {new int[]{1, 25, 3, 45, 7}},
                {new int[]{1, 5, 3, 10, 9}}
        });
    }
    private int[] in;
    private ArrayTask arrayTask;

    public TestTask1_RE(int[] in){
        this.in = in;
    }

    @BeforeClass
    public static void out(){
        System.out.println("TestTask1_RE Тест на выброс исключения метода 1-ой задачи");
    }

    @Before
    public void init(){
        arrayTask = new ArrayTask();
    }

    @Test(expected = RuntimeException.class)
    public void testRuntimeExceptionArrayTask1(){
        arrayTask.toArrayAfterFour(in);
    }
}