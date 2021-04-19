package thegoodcompany.common.utils;

import org.junit.Test;

import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;
import static thegoodcompany.common.utils.GeneralUnitTest.printDiff;
import static thegoodcompany.common.utils.GeneralUnitTest.printDuration;
import static thegoodcompany.common.utils.GeneralUnitTest.printTestInfo;

public class StringUtilsTest {
    @Test
    public void stringJoining_isCorrect() {
        assertThat(StringUtils.join("-", "this", "is", "a", "string"))
                .isEqualTo("this-is-a-string");
        assertThat(StringUtils.join("-", Arrays.asList("this", "is", "a", "string")))
                .isEqualTo("this-is-a-string");
        assertThat(StringUtils.join(Arrays.asList("this", "is", "a", "string")))
                .isEqualTo("this, is, a, string");

        assertThat(StringUtils.join("-", "this", "string", "contains", null))
                .isEqualTo("this-string-contains-null");
    }

    @Test
    public void appendUnless_isWorking() {
        assertThat(StringUtils.joinUnless(new int[]{4, 0, 2}, new String[]{" Four", " Zero", " Two"}, ", ", 0))
                .isEqualTo("4 Four, 2 Two");
        assertThat(StringUtils.joinUnless(new int[]{4, 0, 2}, new String[]{" Four", " Zero", " Two"}, ", ", 2))
                .isEqualTo("4 Four, 0 Zero");
        assertThat(StringUtils.joinUnless(new int[]{4, 0, 2}, new String[]{" Four", " Zero", " Two"}, ", ", 4))
                .isEqualTo("0 Zero, 2 Two");

        assertThat(StringUtils.joinUnless(new Long[]{0L, 5L, 0L, 4L, 2L},
                new String[]{" Oneth", " Twoth", " Threeth", " Fourth", " Fifth"}, ", ", 0L))
                .isEqualTo("5 Twoth, 4 Fourth, 2 Fifth");
    }

    @Test
    public void splitting_isCorrect() {
        assertThat(StringUtils.splitAfterEach(3, "abcdefgh")).asList().containsExactly("abc", "def", "gh");
        assertThat(StringUtils.halfReverseSplitAfterEach(6, "4785682")).asList().containsExactly("4", "785682");
        assertThat(StringUtils.reverseSplitAfterEach(3, "85682")).asList().containsExactly("682", "85");
    }

    @Test
    public void emailValidator_isCorrect() {
        assertThat(StringUtils.isValidEmail("a@b.c")).isTrue();
        assertThat(StringUtils.isValidEmail("abc@ijk.xyz")).isTrue();
        assertThat(StringUtils.isValidEmail("abc@ijh.mno.xyz")).isTrue();
        assertThat(StringUtils.isValidEmail("abc@ij.k.x")).isTrue();

        assertThat(StringUtils.isValidEmail("a@b")).isFalse();
        assertThat(StringUtils.isValidEmail("abc@x.")).isFalse();
        assertThat(StringUtils.isValidEmail("abc@xyz")).isFalse();
        assertThat(StringUtils.isValidEmail("abc@ijk.xy.")).isFalse();
        assertThat(StringUtils.isValidEmail("@ijk.xy")).isFalse();
        assertThat(StringUtils.isValidEmail("")).isFalse();
    }

    @Test
    public void stringConcat_performance() {
        long amount = 1000000;

        String[] a = new String[]{"ab"};
        String[] b = new String[]{"cd"};
        String[] c = new String[]{"eg"};

        String[] regularStr = new String[1];
        long regularStart = System.nanoTime();

        for (int i = 0; i < amount; i++) {
            regularStr[0] = a[0] + b[0] + c[0];
        }

        long regularDuration = System.nanoTime() - regularStart;

        String[] newBuilderStr = new String[1];
        long newBuilderStart = System.nanoTime();

        for (int i = 0; i < amount; i++) {
            StringBuilder builder = new StringBuilder();
            newBuilderStr[0] = builder.append(a[0])
                    .append(b[0])
                    .append(c[0])
                    .toString();
        }

        long newBuilderDuration = System.nanoTime() - newBuilderStart;

        String[] builderStr = new String[1];
        long builderStart = System.nanoTime();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            builder.delete(0, 100);
            builder.append(a[0])
                    .append(b[0])
                    .append(c[0]);
        }
        builderStr[0] = builder.toString();

        long builderDuration = System.nanoTime() - builderStart;

        assertThat(ObjectUtils.equals(regularStr[0], newBuilderStr[0], builderStr[0])).isTrue();

        printTestInfo("String concat performance", amount);
        printDuration("Regular", regularDuration, amount);
        printDuration("New builder", newBuilderDuration, amount);
        printDuration("Builder", builderDuration, amount);
        printDiff("regular - new builder", regularDuration - newBuilderDuration);
        printDiff("regular - builder", regularDuration - builderDuration);
    }
}
