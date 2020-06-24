package thegoodkid.common.utils;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class StringUtilsTest {
    @Test
    public void appendUnless_isWorking() {
        assertThat(StringUtils.joinUnless(new int[]{4, 0, 2}, new String[]{" Four", " Zero", " Two"}, ", ", 0))
                .isEqualTo("4 Four, 2 Two");

        assertThat(StringUtils.joinUnless(new Long[] {0L, 5L, 0L, 4L, 2L},
                new String[]{" Oneth", " Twoth", " Threeth", " Fourth", " Fifth"}, ", ", 0L))
                .isEqualTo("5 Twoth, 4 Fourth, 2 Fifth");
    }
}
