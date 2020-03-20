import Lesson6.ArrayTask;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestTask1_1 {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{4, 2, 3, 8, 4, 9}, new int[]{9}},
                {new int[]{1, 2, 3, 4, 8, 9}, new int[]{8, 9}},
                {new int[]{4, 1, 5, 3, 10, 9}, new int[]{1, 5, 3, 10, 9}},
                {new int[]{1, 4, 3, 4, 7}, new int[]{7}},
                {new int[]{4, 5, 4, 10, 4}, new int[]{}}
        });
    }

    private int[] in;
    private int[] out;

    public TestTask1_1(int[] in, int[] out) {
        this.in = in;
        this.out = out;
    }

    private ArrayTask arrayTask;

    @BeforeClass
    public static void out(){
        System.out.println("TestTask1_1 Тест на правильность результата метода 1-ой задачи");
    }

    @Before
    public void init() {
        arrayTask = new ArrayTask();
    }

    @Test
    public void testArrayAfterFour() {
        Assert.assertArrayEquals(out, arrayTask.toArrayAfterFour(in));
    }
}
