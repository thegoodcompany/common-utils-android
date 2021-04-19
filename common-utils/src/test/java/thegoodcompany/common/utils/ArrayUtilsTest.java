package thegoodcompany.common.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

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
}
