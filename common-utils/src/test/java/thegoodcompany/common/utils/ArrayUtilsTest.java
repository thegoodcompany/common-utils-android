package thegoodcompany.common.utils;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static thegoodcompany.common.utils.GeneralUnitTest.printDiff;
import static thegoodcompany.common.utils.GeneralUnitTest.printDuration;
import static thegoodcompany.common.utils.GeneralUnitTest.printTestInfo;

public class ArrayUtilsTest {
    @Test
    public void arrayCombine_isCorrect() {
        Integer[] sample1 = {1, 2, 3};
        Integer[] sample2 = {4, 5};
        Integer[] sample3 = {6, 7, 8, 9};

        assertThat(ArrayUtils.combine(sample1)).isEqualTo(new Integer[]{1, 2, 3});
        assertThat(ArrayUtils.combine(sample1, sample2)).isEqualTo(new Integer[]{1, 2, 3, 4, 5});
        assertThat(ArrayUtils.combine(sample1, sample2, sample3)).isEqualTo(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertThat(ArrayUtils.combine(sample1, new Object[]{}, sample3)).isEqualTo(new Object[]{1, 2, 3, 6, 7, 8, 9});
    }

    @Test
    public void upCasting_isCorrect() {
        List<Integer> originalList = new ArrayList<>();
        originalList.add(5);
        originalList.add(4);
        originalList.add(3);
        originalList.add(2);
        originalList.add(1);

        List<Number> notBackedNumbers = ArrayUtils.upCastList(originalList);
        notBackedNumbers.add(10);
        notBackedNumbers.add(1.5);

        assertThat(notBackedNumbers.size()).isEqualTo(7);
        assertThat(originalList.size()).isEqualTo(5);

        List<Number> roBackedNumbers = ArrayUtils.upCastListBacked(originalList);
        assertThat(roBackedNumbers.size()).isEqualTo(5);
        assertThat(roBackedNumbers.get(0)).isEqualTo(5);

        originalList.add(-1);
        assertThat(roBackedNumbers.size()).isEqualTo(originalList.size());
        assertThat(roBackedNumbers.get(5)).isEqualTo(originalList.get(5));

        assertThrows(UnsupportedOperationException.class, () -> roBackedNumbers.add(6));

        List<Number> backedNumbers = ArrayUtils.upCastListBacked(originalList, Integer.class);
        assertThat(backedNumbers.size()).isEqualTo(originalList.size());
        originalList.add(-2);
        assertThat(backedNumbers.get(backedNumbers.size() - 1)).isEqualTo(-2);
        backedNumbers.add(-3);
        assertThat(backedNumbers.size()).isEqualTo(originalList.size());
        assertThat(backedNumbers.get(backedNumbers.size() - 1)).isEqualTo(-3);

        assertThrows(UnsupportedOperationException.class, () -> backedNumbers.add(10.2));
    }

    @Test
    public void arrayTransform_isCorrect() {
        int[] is = new int[]{5, 4, 3, 2};
        List<Integer> iList = Arrays.asList(10, 9, 8, 7, 6);
        List<Integer> iList2 = new ArrayList<>();
        iList2.add(15);
        iList2.add(14);
        iList2.add(13);
        iList2.add(12);
        iList2.add(11);

        assertThat(ArrayUtils.transform(is, (t, index, src) -> t * 2)).isEqualTo(Arrays.asList(10, 8, 6, 4));
        assertThat(ArrayUtils.transform(iList, (t, index, src) -> t * 2)).isEqualTo(Arrays.asList(20, 18, 16, 14, 12));
        assertThat(ArrayUtils.transform(iList2, (t, index, src) -> t * 2)).isEqualTo(Arrays.asList(30, 28, 26, 24, 22));

        assertThat(ArrayUtils.transform(is, (t, index, src) -> String.valueOf(t * 2 + 2)))
                .isEqualTo(Arrays.asList("12", "10", "8", "6"));
        assertThat(ArrayUtils.transform(iList, (t, index, src) -> String.valueOf(t * 2 + 2)))
                .isEqualTo(Arrays.asList("22", "20", "18", "16", "14"));
        assertThat(ArrayUtils.transform(iList2, (t, index, src) -> String.valueOf(t * 2 + 2)))
                .isEqualTo(Arrays.asList("32", "30", "28", "26", "24"));
    }

    @Test
    public void listToArrayVsArrayToList_performance() {
        long testCount = 50;

        Object[] listStore = new Object[1];
        Object[] arrSample = new Object[]{"potato", "tomato"};

        long aTLStart = System.nanoTime();
        for (int i = 0; i < testCount; i++) {
            listStore[0] = performArrayToList(arrSample);
        }
        long aTLDuration = System.nanoTime() - aTLStart;

        Object[][] arrStore = new Object[1][1];
        List<Object> listSample = Arrays.asList("potato", "tomato");

        long lTAStart = System.nanoTime();
        for (int i = 0; i < testCount; i++) {
            arrStore[0][0] = performListToArray(listSample);
        }
        long lTADuration = System.nanoTime() - lTAStart;

        assertThat(listStore[0]).isNotEqualTo(arrStore[0]);

        printTestInfo("List to array vs array to list", testCount);
        printDuration("list to array", lTADuration, testCount);
        printDuration("array to list", aTLDuration, testCount);
        printDiff("list to array - array to list", lTADuration - aTLDuration);
    }

    @NonNull
    private Object[] performListToArray(@NonNull List<Object> list) {
        return list.toArray();
    }

    @NonNull
    private List<Object> performArrayToList(@NonNull Object[] arr) {
        return Arrays.asList(arr);
    }
}
