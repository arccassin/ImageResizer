import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private static int newWindth = 300;
    //AMD Ryzen 5 2600X Six-Core Processor
    private static int coreCount = 6;

    public static void main(String[] args) {
        String srcFolder = "E:\\in";
        String dstFolder = "E:\\out";

        File srcDir = new File(srcFolder);

        long start = System.currentTimeMillis();

        File[] files = srcDir.listFiles();

        ArrayList<ArrayList<File>> filesArrayList = new ArrayList<>();

        //отсортируем по размеру, чтобы в каждый поток попал примерно одинаковый общий размер файлов
        Arrays.sort(files, (o1, o2) -> Long.compare(o2.length(), o1.length()));

        for (int i = 0; i <coreCount; i++) {
            filesArrayList.add(new ArrayList<File>());
        }
 
        long[] sizeFilesOnThread = new long[coreCount];

        for (int i = 0; i < files.length; i++) {
            int itmp = i % coreCount;
            filesArrayList.get(itmp).add(files[i]);
            sizeFilesOnThread[itmp] += files[i].length();
        }

        for (int i = 0; i < filesArrayList.size(); i++) {
            ImageResizer resizer = new ImageResizer(filesArrayList.get(i),
                    newWindth, dstFolder, System.currentTimeMillis(), i);
            resizer.start();
        }

        for (int i = 0; i < sizeFilesOnThread.length; i++) {
            System.out.println("Size id = " + i + ": " + sizeFilesOnThread[i]);
        }


//        System.out.println("Duration: " + (System.currentTimeMillis() - start));
    }
}
