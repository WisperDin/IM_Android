package com.cst.im;

import com.cst.im.dataBase.Constant;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * DeEnCode local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void Acring(){
        int id = 123;
        String username = "Acring";
        String password = "27452275";
        String sql = String.format("INSERT INTO %s (%s,%s,%s)VALUES(%s,%s,%s)", Constant.Login.TABLE_NAME,
                Constant.Login.ID,Constant.Login.USERNAME,Constant.Login.PASSWORD,
                id,username,password);
        System.out.print(sql);
    }
}