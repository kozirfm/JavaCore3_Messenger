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
public class TestTask2_False {

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList (new Object[][]{
                {new int[]{4, 1, 5, 1, 4}},    //false
                {new int[]{4, 4, 4, 4}},       //false
                {new int[]{1, 1, 1, 1, 1}},    //false
                {new int[]{7, 9, 5, 2, 0}}     //false
        });
    }
    private int[] in;
    private ArrayTask arrayTask;

    public TestTask2_False(int[] in){
        this.in = in;
    }

    @BeforeClass
    public static void out(){
        System.out.println("TestTask2_False Тест на правильность результата FALSE метода 2-ой задачи");
    }

    @Before
    public void init(){
        arrayTask = new ArrayTask();
    }

    @Test
    public void  testUnitAndFourInArrayFalse(){
        Assert.assertFalse("в массиве есть только 1 и 4",arrayTask.isUnitAndFourInArray(in));
    }
}
