import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * 自定义ClassLoader
 *
 * @author Administrator
 * @create 2021/5/11
 * @since 1.0.0
 */
public class MyClassLoader extends ClassLoader{
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] in_b = null;
        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("Hello.xlass");
             ByteArrayOutputStream swapStream = new ByteArrayOutputStream()){
            byte[] buff = new byte[1024];
            int rc = 0;
            while ((rc = resourceAsStream.read(buff, 0, 1024)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in_b = swapStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] decodeBytes = decode(in_b);
        return defineClass(name, decodeBytes, 0, decodeBytes.length);
    }

    public byte[] decode(byte[] oBytes) {
        byte decodeBytes[] = new byte[oBytes.length];
        for(int i=0;i<oBytes.length;i++) {
            decodeBytes[i] = (byte) (255 - oBytes[i]);
        }
        return decodeBytes;
    }

    public static void main(String[] args) {
        MyClassLoader myClassLoader = new MyClassLoader();
        try {
            Class<?> clazz = myClassLoader.findClass("Hello");
            Object o = clazz.newInstance();
            Method hello = clazz.getMethod("hello");
            hello.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
